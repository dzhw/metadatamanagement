package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.common.domain.Elsst;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.domain.Sponsor;
import eu.dzhw.fdz.metadatamanagement.common.service.MarkdownHelper;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.helper.DoiBuilder;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.GeographicCoverage;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import freemarker.template.TemplateException;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.AutoTimer;
import org.springframework.boot.actuate.metrics.web.client.MetricsRestTemplateCustomizer;
import org.springframework.boot.actuate.metrics.web.client.RestTemplateExchangeTagsProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * A service for registering project metadata at DataCite.
 */
@Service
@Slf4j
public class DataCiteService {

  private ObjectMapper mapper = new ObjectMapper();
  private final String MDM_BASE_URL = "https://metadata.fdz.dzhw.eu/";
  private final String MDM_DATA_PACKAGE_TYPE = "data-packages";
  private final String MDM_ANALYSIS_PACKAGE_TYPE = "analysis-packages";
  private static final String IS_ALIVE_ENDPOINT = "/heartbeat";
  private static final String REGISTRATION_ENDPOINT = "/dois";

  @Autowired
  private DataPackageRepository dataPackageRepository;

  @Autowired
  private AnalysisPackageRepository analysisPackageRepository;

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private DataAcquisitionProjectRepository projectRepository;

  @Autowired
  private DataAcquisitionProjectVersionsService dataAcquisitionProjectVersionsService;

  @Autowired
  private DoiBuilder doiBuilder;

  @Autowired
  private MetadataManagementProperties metadataManagementProperties;

  @Autowired
  private MarkdownHelper markdownHelper;

  private RestTemplate restTemplate;


  /**
   * Constructor for DataCite Services. Set the Rest Template.
   */
  @Autowired
  public DataCiteService(MeterRegistry meterRegistry, RestTemplateExchangeTagsProvider tagProvider) {
    this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    this.restTemplate.getMessageConverters().add(0,
      new StringHttpMessageConverter(Charset.forName("UTF-8")));
    MetricsRestTemplateCustomizer customizer = new MetricsRestTemplateCustomizer(meterRegistry,
      tagProvider, "datacite.client.requests", AutoTimer.ENABLED);
    customizer.customize(this.restTemplate);
  }


  /**
   * Checks the DataCite heartbeat endpoint.
   * @return Returns the status of the DataCite Server
   */
  public Boolean isDataCiteHealthy() {
    final String daraHealthEndpoint = this.getApiEndpoint() + IS_ALIVE_ENDPOINT;

    ResponseEntity<String> result =
      this.restTemplate.getForEntity(daraHealthEndpoint, String.class);

    return result.getStatusCode().equals(HttpStatus.OK);
  }

  /**
   * Returns DataCite api endpoint.
   * @return the api endpoint given by the configuration.
   */
  public String getApiEndpoint() {
    return this.metadataManagementProperties.getDataCite().getEndpoint();
  }

  /**
   * Registers or updates a project with a given doi to DataCite.
   * @param project The Project.
   * @return The HttpStatus from DataCite.
   * @throws TemplateException Exception while collecting metadata.
   */
  public HttpStatus registerOrUpdateProjectToDataCite(DataAcquisitionProject project) throws DataCiteMetadataException {
    JsonNode payload = this.getDataCiteMetadataForProject(project);
    HttpStatus httpStatusFromDataCite = this.sendToDataCite(payload);
    return httpStatusFromDataCite;
  }

  /**
   * Registers or updates a project by ID with a given doi to DataCite.
   * @param projectId the id of the project
   * @return The HttpStatus from DataCite.
   * @throws DataCiteMetadataException Exception while collecting metadata.
   */
  public HttpStatus registerOrUpdateProjectToDataCite(String projectId) throws DataCiteMetadataException {
    DataAcquisitionProject project = this.projectRepository.findById(projectId).get();
    return this.registerOrUpdateProjectToDataCite(project);
  }

  /**
   * Sends PUT request with generated payload to DataCite to register or update DOIs.
   * @param payload the payload object
   * @return the HttpStatus from DataCite
   */
  private HttpStatus sendToDataCite(JsonNode payload) {
    try {
      log.debug(String.format("Sending metadata for DOI '%s' to DataCite:",
        payload.get("data").get("attributes").get("doi").asText()));
      log.debug(payload.toPrettyString());
      return this.putToDataCite(payload.get("data").get("attributes").get("doi").asText(), payload);
    } catch (HttpClientErrorException httpClientError) {
      log.error("HTTP Error during DataCite call", httpClientError);
      log.error("DataCite Response Body:\n" + httpClientError.getResponseBodyAsString());
      throw httpClientError;
    }
  }

  /**
   * Creates the DataCite metadata object for a project as a JsonNode.
   * The resulting object can be used as the payload for querying the DataCite API.
   * @param project the project dataset
   * @return the metadata as a JsonNode
   */
  public JsonNode getDataCiteMetadataForProject(DataAcquisitionProject project) throws DataCiteMetadataException {
    Map<String, Object> attrObj = null;
    if (project.getConfiguration().getRequirements().isDataPackagesRequired()) {
      final var dataPackage = this.dataPackageRepository.findOneByDataAcquisitionProjectId(project.getId());
      if (dataPackage == null) {
        throw new DataCiteMetadataException(
          String.format("The project with id '%s' has no data package linked to it.", project.getId()));
      }
      final List<Survey> surveys = this.surveyRepository.findByDataAcquisitionProjectId(project.getId());
      attrObj = this.createAttrObjectForDataPackage(project, dataPackage, surveys);
    } else if (project.getConfiguration().getRequirements().isAnalysisPackagesRequired()) {
      final var analysisPackage = this.analysisPackageRepository
        .findOneByDataAcquisitionProjectId(project.getId());
      if (analysisPackage == null) {
        throw new DataCiteMetadataException(
          String.format("The project with id '%s' has no analysis package linked to it.", project.getId()));
      }
      attrObj = this.createAttrObjectForAnalysisPackage(project, analysisPackage);
    }

    if (attrObj == null) {
      throw new DataCiteMetadataException(
        String.format("Metadata for project with id '%s' could not be created. Aborting DataCite registration.", project.getId()));
    }

    return this.createDataCitePayload(attrObj);
  }

  /**
   * Maps MDM data to the DataCite Metadata schema for data packages.
   * @param project the project dataset
   * @param dataPackage the dataPackage dataset
   * @param surveys a list of survey datasets
   * @return a map of key value pairs filled with metadata
   */
  public Map<String,Object> createAttrObjectForDataPackage(
    DataAcquisitionProject project,
    DataPackage dataPackage,
    List<Survey> surveys) throws DataCiteMetadataException {
    if (dataPackage.getTitle() == null ||
      dataPackage.getProjectContributors() == null || dataPackage.getProjectContributors().isEmpty() ||
      project.getRelease() == null) {
      throw new DataCiteMetadataException("Error creating DataCite metadata. Project or data package is missing relevant fields.");
    }
    Map<String,Object> attrObj = new HashMap<>();
    this.addBasicInfo(attrObj, project, dataPackage.getMasterId(), false);
    attrObj.put("titles", this.createTitlesList(dataPackage.getTitle()));
    attrObj.put("publicationYear", project.getRelease().getFirstDate() != null ?
      project.getRelease().getFirstDate().getYear() : LocalDate.now().getYear());
    attrObj.put("types", this.createTypesObject(true));
    attrObj.put("version", project.getRelease().getVersion());
    attrObj.put("creators", this.createCreatorsList(dataPackage.getProjectContributors(), dataPackage.getInstitutions()));
    attrObj.put("publisher", this.createPublisherObject());
    attrObj.put("descriptions", this.createDescriptionsList(dataPackage.getDescription(), surveys));
    attrObj.put("contributors", this.createContributorsList(dataPackage.getDataCurators()));
    attrObj.put("fundingReferences", this.createFundingReferencesList(dataPackage.getSponsors()));
    attrObj.put("subjects", this.createSubjectsList(
      dataPackage.getTags() != null ? dataPackage.getTags().getDe(): null,
      dataPackage.getTags() != null ? dataPackage.getTags().getEn() : null,
      dataPackage.getTagsElsst() != null ? dataPackage.getTagsElsst().getDe() : null,
      dataPackage.getTagsElsst() != null ? dataPackage.getTagsElsst().getEn() : null));
    attrObj.put("dates", this.createDatesList(project, surveys));
    attrObj.put("geoLocations", this.createGeoLocationsList(surveys));
    attrObj.put("alternateIdentifiers", this.createAlternateIdentifiersListForDp(dataPackage, surveys));
    attrObj.put("relatedIdentifiers", this.createRelatedIdentifiersList(project));
    attrObj.put("rightsList", this.createRightsList(project, dataPackage.getMasterId(), false));
    return attrObj;
  }

  /**
   * Maps MDM data to the Data Cite Metadata schema for analysis packages.
   * @param project the project dataset
   * @param analysisPackage the analysisPackage dataset
   * @return a map of key value pairs filled with metadata
   */
  public Map<String, Object> createAttrObjectForAnalysisPackage(
    DataAcquisitionProject project,
    AnalysisPackage analysisPackage) throws DataCiteMetadataException {
    if (analysisPackage.getTitle() == null ||
      analysisPackage.getAuthors() == null || analysisPackage.getAuthors().isEmpty() ||
      project.getRelease() == null) {
      throw new DataCiteMetadataException("Error creating DataCite metadata. Project or analysis package is missing relevant fields.");
    }
    Map<String,Object> attrObj = new HashMap<>();
    this.addBasicInfo(attrObj, project, analysisPackage.getMasterId(), true);
    attrObj.put("titles", this.createTitlesList(analysisPackage.getTitle()));
    attrObj.put("publicationYear", project.getRelease().getFirstDate() != null ?
      project.getRelease().getFirstDate().getYear() : LocalDate.now().getYear());
    attrObj.put("types", this.createTypesObject(false));
    attrObj.put("version", project.getRelease().getVersion());
    attrObj.put("creators", this.createCreatorsList(analysisPackage.getAuthors(), analysisPackage.getInstitutions()));
    attrObj.put("publisher", this.createPublisherObject());
    attrObj.put("descriptions", this.createDescriptionsList(analysisPackage.getDescription(), new ArrayList<>()));
    attrObj.put("contributors", this.createContributorsList(analysisPackage.getDataCurators()));
    attrObj.put("fundingReferences", this.createFundingReferencesList(analysisPackage.getSponsors()));
    attrObj.put("subjects", this.createSubjectsList(
      analysisPackage.getTags() != null ? analysisPackage.getTags().getDe() : null,
      analysisPackage.getTags() != null ? analysisPackage.getTags().getEn() : null,
      analysisPackage.getTagsElsst() != null ? analysisPackage.getTagsElsst().getDe() : null,
      analysisPackage.getTagsElsst() != null ? analysisPackage.getTagsElsst().getEn() : null));
    attrObj.put("dates", this.createDatesList(project, new ArrayList<>()));
    attrObj.put("geoLocations", new ArrayList<>());
    attrObj.put("alternateIdentifiers", new ArrayList<>());
    attrObj.put("relatedIdentifiers", this.createRelatedIdentifiersList(project));
    attrObj.put("rightsList", this.createRightsList(project, analysisPackage.getMasterId(), true));
    return attrObj;
  }

  /**
   * Calls the DataCite Api to create or update a DOI metadata dataset.
   * @param doi the DOI of the dataset to be created or updated
   * @param payload the paload object
   * @return the HttpStatus
   * @throws HttpClientErrorException
   */
  private HttpStatus putToDataCite(String doi, JsonNode payload) throws HttpClientErrorException {
    // Load DataCite Information
    final String dataCiteEndpoint =
      this.metadataManagementProperties.getDataCite().getEndpoint() + REGISTRATION_ENDPOINT;
    final String dataCiteUsername = this.metadataManagementProperties.getDataCite().getUsername();
    final String dataCitePassword = this.metadataManagementProperties.getDataCite().getPassword();

    // Build Header
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json;charset=UTF-8");
    headers.add("Accept", "application/vnd.api+json"); // from api doc
    String auth = dataCiteUsername + ":" + dataCitePassword;
    byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName(Charsets.UTF_8.name())));
    headers.add("Authorization", "Basic " + new String(encodedAuth, Charsets.UTF_8));

    UriComponentsBuilder builder =
      UriComponentsBuilder.fromUriString(dataCiteEndpoint);
    builder.pathSegment("{doi}");
    HttpEntity<String> request = new HttpEntity(payload, headers);
    try {
      this.restTemplate.put(builder.build(doi), request);
      return HttpStatus.OK;
    } catch (HttpClientErrorException httpClientError) {
      throw httpClientError;
    }
  }

  private ResponseEntity<JsonNode> getDoiFromDataCite(String doi) throws HttpClientErrorException {
    // Load DataCite Information
    final String dataCiteEndpoint =
      this.metadataManagementProperties.getDataCite().getEndpoint() + REGISTRATION_ENDPOINT;
    UriComponentsBuilder builder =
      UriComponentsBuilder.fromUriString(dataCiteEndpoint);
    builder.pathSegment("{doi}");
    try {
      return this.restTemplate.getForEntity(builder.build(doi), JsonNode.class);
    } catch (HttpClientErrorException httpClientError) {
      throw httpClientError;
    }
  }

  /**
   * Generates the payload node required for registering DOIs with the DataCite API.
   * @param attrObj a map filled with all metadata properties
   * @return the payload node
   */
  private JsonNode createDataCitePayload(Map<String, Object> attrObj) {
    Map<String,Object> dataObj = new HashMap<>();
    dataObj.put("attributes", attrObj);
    dataObj.put("type", "dois");
    Map<String, Object> baseObj = new HashMap<>();
    baseObj.put("data", dataObj);
    JsonNode node = mapper.valueToTree(baseObj);
    return node;
  }

  /************************************************************************
   * HELPER METHODS
   ***********************************************************************/

  /**
   * Adds basic information to the metadata map. Basic information include the following properties:
   *  - event: always set to 'publish' for registering and publishing DOIs in one step
   *  - doi: the DOI of the dataset matching the pattern [prefix]/DZHW:[projectId]:[version], e.g. '10.83079/DZHW:becobe:1.0.6'
   *  - prefix: the DOI prefix, e.g. '10.83079'
   *  - suffix: the DOI suffix, e.g. 'DZHW:becobe:1.0.6'
   *  - url: the url of the dataset within MDM
   * @param attrObj the metadata map object
   * @param project the project dataset
   * @param packageMasterId the package's masterId
   * @param isAnalysisPackage a flag indicating if the package is an analysis package or not
   */
  private void addBasicInfo(Map<String, Object> attrObj, DataAcquisitionProject project, String packageMasterId, Boolean isAnalysisPackage) {
    attrObj.put("event", "publish");
    attrObj.put("doi", doiBuilder.buildDataOrAnalysisPackageDoiForDataCite(project.getId(), project.getRelease()));
    attrObj.put("prefix", doiBuilder.getDoiPrefixForDataCite());
    attrObj.put("suffix", doiBuilder.getDoiSuffixForDataCite(project.getId(), project.getRelease()));
    String urlLanguage = project.getRelease().getDoiPageLanguage() != null ?
      project.getRelease().getDoiPageLanguage() : "en";
    attrObj.put("url", this.createUrlValue(project, urlLanguage, packageMasterId, isAnalysisPackage));
  }

  /**
   * Generates the MDM url of a data or analysis package.
   * @param project the project dataset
   * @param lang the url language determining the landing page language
   * @param packageMasterId the package's masterId
   * @param isAnalysisPackage a flag indicating if the package is an analysis package or not
   * @return the link to a data or analysis package within MDM
   */
  private String createUrlValue(DataAcquisitionProject project, String lang, String packageMasterId, Boolean isAnalysisPackage) {
    String packageType = isAnalysisPackage ? MDM_ANALYSIS_PACKAGE_TYPE : MDM_DATA_PACKAGE_TYPE;
    String url = MDM_BASE_URL + lang + "/" + packageType + "/" + packageMasterId +
      "?version=" + project.getRelease().getVersion();
    return url;
  }

  /**
   * Creates a list of rights objects
   * @return the list of rights objects
   */
  private List<Map<String, String>> createRightsList(DataAcquisitionProject project, String packageMasterId, Boolean isAnalysisPackage) {
    List<Map<String, String>> rightsList = new ArrayList<>();

    Map<String, String> rightObjDe = new HashMap<>();
    rightObjDe.put("rights", String.format("Beantragung notwendig unter %s",
      createUrlValue(project, "de", packageMasterId, isAnalysisPackage)));
    rightObjDe.put("lang", "de");
    rightsList.add(rightObjDe);

    Map<String, String> rightObjEn = new HashMap<>();
    rightObjEn.put("rights", String.format("Application necessary under %s",
      createUrlValue(project, "en", packageMasterId, isAnalysisPackage)));
    rightObjEn.put("lang", "en");
    rightsList.add(rightObjEn);

    return rightsList;
  }

  /**
   * Creates a list of relatedIdentifiers.
   * @param project the project dataset
   * @return the list of relatedIdentifier objects
   */
  private List<Map<String, String>> createRelatedIdentifiersList(DataAcquisitionProject project) {
    List<Map<String, String>> relatedIdentifiersList = new ArrayList<>();
    Map<String,String> relatedIdentifier = new HashMap<>();

    // check if project has a previous version and add "isNewVersionOf" attribute if so
    Release previousRelease = dataAcquisitionProjectVersionsService.findPreviousRelease(project.getMasterId(), project.getRelease());
    if (previousRelease != null) {
      String previousDoi = doiBuilder.buildDataOrAnalysisPackageDoiForDataCite(project.getId(), previousRelease);
      try {
        ResponseEntity<JsonNode> previousDoiMetadata = this.getDoiFromDataCite(previousDoi);
        if (previousDoiMetadata.getStatusCode().is2xxSuccessful()) {
          relatedIdentifier.put("relatedIdentifier", previousDoi);
          relatedIdentifier.put("relatedIdentifierType", "DOI");
          relatedIdentifier.put("relationType", "IsNewVersionOf");
          relatedIdentifiersList.add(relatedIdentifier);

          List<Map<String, String>> previousRelatedIdentifiersList = new ArrayList<>();
          Map<String,String> previousRelatedIdentifier = new HashMap<>();
          previousRelatedIdentifier.put("relatedIdentifier",
            doiBuilder.buildDataOrAnalysisPackageDoiForDataCite(project.getId(), project.getRelease()));
          previousRelatedIdentifier.put("relatedIdentifierType", "DOI");
          previousRelatedIdentifier.put("relationType", "IsPreviousVersionOf");
          previousRelatedIdentifiersList.add(previousRelatedIdentifier);

          // if the previous release has a version before itself we need to re-add the
          // 'isNewVersionOf' identifier to prevent it from being remove during the update
          Release secondPreviousRelease = dataAcquisitionProjectVersionsService.findPreviousRelease(project.getMasterId(), previousRelease);
          if (secondPreviousRelease != null) {
            String secondPreviousDoi = doiBuilder.buildDataOrAnalysisPackageDoiForDataCite(project.getId(), secondPreviousRelease);
            try {
              ResponseEntity<JsonNode> secondPreviousDoiMetadata = this.getDoiFromDataCite(secondPreviousDoi);
              if (secondPreviousDoiMetadata.getStatusCode().is2xxSuccessful()) {
                Map<String,String> previousIsNewVersionIdentifier = new HashMap<>();
                previousIsNewVersionIdentifier.put("relatedIdentifier", secondPreviousDoi);
                previousIsNewVersionIdentifier.put("relatedIdentifierType", "DOI");
                previousIsNewVersionIdentifier.put("relationType", "IsNewVersionOf");
                previousRelatedIdentifiersList.add(previousIsNewVersionIdentifier);
              }
            } catch (HttpClientErrorException e) {
              log.info("Could not find second previous version.");
            }
          }

          Map<String, Object> updateRelatedIdentifierObj = new HashMap<>();
          updateRelatedIdentifierObj.put("relatedIdentifiers", previousRelatedIdentifiersList);
          Map<String, Object> previousDataObj = new HashMap<>();
          previousDataObj.put("attributes", updateRelatedIdentifierObj);
          previousDataObj.put("type", "dois");
          Map<String, Object> previousPayload = new HashMap<>();
          previousPayload.put("data", previousDataObj);
          JsonNode updatePayload = mapper.valueToTree(previousPayload);

          try {
            log.debug(String.format("Updating related identifiers of previous version's DOI '%s':", previousDoi));
            log.debug(updatePayload.toPrettyString());
            this.putToDataCite(previousDoi, updatePayload);
            log.debug(String.format("Successfully updated related identifiers of previous version's DOI '%s'", previousDoi));
          } catch (HttpClientErrorException httpClientError) {
            log.error("HTTP Error during DataCite call to update DOI metadata of previous version", httpClientError);
            log.error("DataCite Response Body:\n" + httpClientError.getResponseBodyAsString());
            throw httpClientError;
          }
        }
      } catch (HttpClientErrorException e) {
        log.info("Could not find previous version");
      }
    }

    return relatedIdentifiersList;
  }

  /**
   * Creates a list of geographic references.
   * @param surveys a list of survey datasets
   * @return a list of geoLocation objects
   */
  private List<Map<String, String>> createGeoLocationsList(List<Survey> surveys) {
    List<Map<String, String>> geoLocationsList = new ArrayList<>();
    for (Survey survey : surveys) {
      for (GeographicCoverage geoCov : survey.getPopulation().getGeographicCoverages()) {
        Locale localeEn = new Locale("en", geoCov.getCountry());
        Map<String, String> geoLocationObjEn = new HashMap<>();
        geoLocationObjEn.put("geoLocationPlace", localeEn.getDisplayCountry(localeEn));
        geoLocationsList.add(geoLocationObjEn);
      }
    }
    return geoLocationsList;
  }

  /**
   * Creates a list of relevant dates including field period dates of all surveys and relevant release dates.
   * Release related dates may appear in the following options:
   *      - regular released project: release date is added with dateType 'available'
   *      - pre-released project: embargo date is added with dateType 'available' and information on the date,
   *        additionally the first release date is added with dateType 'accepted'
   *      - hidden project: the current date is added with dateType 'withdrawn' and information on the
   *        hidden status of the project
   * @param project the project dataset
   * @param surveys a list of survey datasets
   * @return a list of date objects
   */
  private List<Map<String, String>> createDatesList(DataAcquisitionProject project, List<Survey> surveys) {
    List<Map<String, String>> datesList = new ArrayList<>();
    String pattern = "yyyy-MM-dd";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    if (project.isHidden()) {
      Map<String, String> dateObj = new HashMap<>();
      dateObj.put("date", LocalDate.now().format(formatter));
      dateObj.put("dateType", "Withdrawn");
      dateObj.put("dateInformation", "The publication of the project was withdrawn.");
      datesList.add(dateObj);
    } else {
      if (project.getRelease().getIsPreRelease() && project.getEmbargoDate() != null) {
        // Pre-Release (accepted = firstDate or current date, available = Embargo)
        Map<String, String> dateObjEmbargo = new HashMap<>();
        dateObjEmbargo.put("date", project.getEmbargoDate().toString());
        dateObjEmbargo.put("dateType", "Available");
        dateObjEmbargo.put("dateInformation",
          String.format("This dataset is currently not yet available for order as it is subject to an embargo until %s." +
            " Publication can only take place after this date. Please note that the embargo date does not necessarily correspond" +
            " to the expected release date. Please contact userservice@dzhw.eu if you wish to receive information regarding the" +
            " release date of the dataset.", project.getEmbargoDate().toString()));
        datesList.add(dateObjEmbargo);

        Map<String, String> dateObjFirst = new HashMap<>();
        dateObjFirst.put("date", project.getRelease().getFirstDate() != null ?
          project.getRelease().getFirstDate().format(formatter) : LocalDate.now().format(formatter));
        dateObjFirst.put("dateType", "Accepted");
        datesList.add(dateObjFirst);
      } else {
        // Normal Release (available = Release)
        Map<String, String> dateObj = new HashMap<>();
        dateObj.put("date", project.getRelease().getFirstDate() != null ?
          project.getRelease().getFirstDate().format(formatter) : LocalDate.now().format(formatter));
        dateObj.put("dateType", "Available");
        datesList.add(dateObj);
      }
    }

    // add field periods for all surveys and add survey title
    for (Survey survey : surveys) {
      Map<String, String> dateObjSur = new HashMap<>();
      String startDate = survey.getFieldPeriod().getStart().toString();
      String endDate = survey.getFieldPeriod().getEnd().toString();
      dateObjSur.put("date", String.format("%s/%s", startDate, endDate));
      dateObjSur.put("dateType", "Collected");
      dateObjSur.put("dateInformation", survey.getTitle().getEn());
      datesList.add(dateObjSur);
    }

    return datesList;
  }

  /**
   * Creates the types object.
   * @return the types object as a map of key value pairs
   */
  private Map<String, String> createTypesObject(Boolean isDataPackage) {
    Map<String, String> typesObj = new HashMap<>();
    typesObj.put("resourceTypeGeneral", isDataPackage ? "Dataset" : "Collection");
    return typesObj;
  }

  /**
   * Creates the publisher object.
   * @return a map of key value pairs filled with metadata of the publisher
   */
  private Map<String, String> createPublisherObject() {
    Map<String, String> publisherObj = new HashMap<>();
    publisherObj.put("name", "German Centre for Higher Education Research and Science Studies (DZHW)");
    publisherObj.put("lang", "en");
    publisherObj.put("publisherIdentifier", "https://ror.org/01n8j6z65");
    publisherObj.put("publisherIdentifierScheme", "ROR");
    publisherObj.put("schemeUri", "https://ror.org/");
    return publisherObj;
  }

  /**
   * Creates a list of creator objects including names, institutional references and
   * identifiers.
   * @param creators list of creators
   * @param institutions list of institutions
   * @return a list of maps of key value pairs filled with metadata of the creators
   */
  private List<Map<String, Object>> createCreatorsList(List<Person> creators, List<I18nString> institutions) {
    List<Map<String, Object>> creatorsList = new ArrayList<>();
    for (Person person : creators) {
      Map<String, Object> creatorObject = new HashMap<>();
      creatorObject.put("name", person.getMiddleName() != null ? String.format(
        "%s, %s %s",  person.getLastName(), person.getFirstName(), person.getMiddleName()) :
        String.format("%s, %s",  person.getLastName(), person.getFirstName()));
      creatorObject.put("nameType", "Personal");
      creatorObject.put("givenName", person.getFirstName() + (person.getMiddleName() != null ?
        " " + person.getMiddleName() : ""));
      creatorObject.put("familyName", person.getLastName());
      creatorObject.put("nameIdentifiers", this.createCreatorNameIdentifierList(person));
      creatorObject.put("affiliation", institutions != null && institutions.size() == 1 ? this.createCreatorAffiliationList(institutions) : new ArrayList<>());
      creatorsList.add(creatorObject);
    }
    if (institutions != null) {
      for (I18nString institution : institutions) {
        Map<String, Object> creatorObject = new HashMap<>();
        creatorObject.put("name", institution.getEn() != null ? institution.getEn() : institution.getDe());
        creatorObject.put("nameType", "Organizational");
        creatorObject.put("nameIdentifiers", new ArrayList<>());
        creatorObject.put("affiliation", new ArrayList<>());
        creatorsList.add(creatorObject);
      }
    }
    return creatorsList;
  }

  /**
   * Creates a list of name identifiers for a creator. At the moment only ORCIDs are
   * represented in the list.
   * @param person the person dataset
   * @return the list of name identifier objects
   */
  private List<Map<String, String>> createCreatorNameIdentifierList(Person person) {
    List<Map<String, String>> nameIdentifierList = new ArrayList<>();
    if (person.getOrcid() != null) {
      Map<String, String> nameIdentifierObject = new HashMap<>();
      nameIdentifierObject.put("nameIdentifier", person.getOrcid());
      nameIdentifierObject.put("nameIdentifierScheme", "ORCID");
      nameIdentifierObject.put("schemeUri", "https://orcid.org");
      nameIdentifierList.add(nameIdentifierObject);
    }
    return nameIdentifierList;
  }

  /**
   * Creates a list of  organizational or institutional affiliations of a person.
   * @param institutions
   * @return a list of affiliation objects
   */
  private List<Map<String, String>>  createCreatorAffiliationList(List<I18nString> institutions) {
    List<Map<String, String>> affiliationList = new ArrayList<>();
    if (institutions != null) {
      for (I18nString institution : institutions) {
        if (institution.getEn() != null) {
          Map<String, String> affiliationObj = new HashMap<>();
          affiliationObj.put("name", institution.getEn());
          affiliationList.add(affiliationObj);
        }
      }
    }
    // todo: include identifiers when they are available in MDM
    return affiliationList;
  }

  /**
   * Creates a list of description objects.
   * @param description the description entry from a data or analysis package
   * @param surveys a list of surveys
   * @return
   */
  private List<Map<String, String>> createDescriptionsList(I18nString description, List<Survey> surveys) {
    List<Map<String, String>> descriptionList = new ArrayList<>();
    // add descriptions of type "abstract"
    if (description != null && description.getEn() != null) {
      Map<String,String> descriptionObj = new HashMap<>();
      descriptionObj.put("description", markdownHelper.getPlainText(description.getEn()));
      descriptionObj.put("lang", "en");
      descriptionObj.put("descriptionType", "Abstract");
      descriptionList.add(descriptionObj);
    }
    if (description != null && description.getDe() != null) {
      Map<String,String> descriptionObj = new HashMap<>();
      descriptionObj.put("description", markdownHelper.getPlainText(description.getDe()));
      descriptionObj.put("lang", "de");
      descriptionObj.put("descriptionType", "Abstract");
      descriptionList.add(descriptionObj);
    }

    // add survey population description of type "methods"
    for (Survey survey : surveys) {
      Map<String,String> descriptionObjEn = new HashMap<>();
      descriptionObjEn.put("description", markdownHelper.getPlainText(
        survey.getPopulation().getDescription().getEn()));
      descriptionObjEn.put("lang", "en");
      descriptionObjEn.put("descriptionType", "Methods");
      descriptionList.add(descriptionObjEn);

      Map<String,String> descriptionObjDe = new HashMap<>();
      descriptionObjDe.put("description", markdownHelper.getPlainText(
        survey.getPopulation().getDescription().getDe()));
      descriptionObjDe.put("lang", "de");
      descriptionObjDe.put("descriptionType", "Methods");
      descriptionList.add(descriptionObjDe);
    }

    return descriptionList;
  }

  /**
   * Creates a list of contributors including a fixed reference to "FDZ-DZHW" as a distributor
   * and a list of data curators.
   * @param contributors a list of project contributors
   * @return a list of contributors
   */
  private List<Map<String, Object>> createContributorsList(List<Person> contributors) {
    List<Map<String, Object>> contributorList = new ArrayList<>();
    Map<String, Object> contributor = new HashMap<>();
    contributor.put("name", "FDZ-DZHW");
    contributor.put("nameType", "Organizational");
    contributor.put("contributorType", "Distributor");

    List<Map<String, String>> nameIdentifiers = new ArrayList<>();
    Map<String, String> identifierObj = new HashMap<>();
    identifierObj.put("schemeUri", "https://ror.org/");
    identifierObj.put("nameIdentifier", "https://ror.org/01n8j6z65");
    identifierObj.put("nameIdentifierScheme", "ROR");
    nameIdentifiers.add(identifierObj);
    contributor.put("nameIdentifiers", nameIdentifiers);

    contributorList.add(contributor);

    for (Person person : contributors) {
      Map<String, Object> contributorObject = new HashMap<>();
      contributorObject.put("name", person.getMiddleName() != null ? String.format(
        "%s, %s %s",  person.getLastName(), person.getFirstName(), person.getMiddleName()) :
        String.format("%s, %s",  person.getLastName(), person.getFirstName()));
      contributorObject.put("nameType", "Personal");
      contributorObject.put("givenName", person.getFirstName() + (person.getMiddleName() != null ?
        " " + person.getMiddleName() : ""));
      contributorObject.put("familyName", person.getLastName());
      contributorObject.put("contributorType", "DataCurator");
      contributorObject.put("nameIdentifiers", this.createCreatorNameIdentifierList(person));
      contributorObject.put("affiliation", this.createContributorAffiliationList());
      contributorList.add(contributorObject);
    }
    return contributorList;
  }

  /**
   * Creates a list of affiliation entries.
   * @return list of affiliation entries
   */
  private List<Map<String, String>> createContributorAffiliationList() {
    List<Map<String, String>> affiliationList = new ArrayList<>();

    Map<String, String> affiliateObjEn = new HashMap<>();
    affiliateObjEn.put("name", "German Centre for Higher Education Research and Science Studies (DZHW)");
    affiliateObjEn.put("lang", "en");
    affiliateObjEn.put("affiliationIdentifier", "https://ror.org/01n8j6z65");
    affiliateObjEn.put("affiliationIdentifierScheme", "ROR");
    affiliateObjEn.put("schemeUri", "https://ror.org/");
    affiliationList.add(affiliateObjEn);

    return affiliationList;
  }

  /**
   * Creates the fundingReferences list.
   * @param sponsors the dataPackage dataset
   * @return the list of dfunding references.
   */
  private List<Map<String, String>> createFundingReferencesList(List<Sponsor> sponsors) {
    List<Map<String, String>> fundingReferenceList = new ArrayList<>();
    for (Sponsor sponsor : sponsors) {
      if (sponsor.getName() != null && sponsor.getName().getDe() != null) {
        Map<String, String> fundingRefDe = new HashMap<>();
        fundingRefDe.put("funderName", sponsor.getName().getDe());
        //todo: add identifiers when they are available in MDM
        fundingReferenceList.add(fundingRefDe);
      }
      if (sponsor.getName() != null && sponsor.getName().getEn() != null) {
        Map<String, String> fundingRefEn = new HashMap<>();
        fundingRefEn.put("funderName", sponsor.getName().getEn());
        //todo: add identifiers when they are available in MDM
        fundingReferenceList.add(fundingRefEn);
      }
    }
    return fundingReferenceList;
  }

  /**
   * Creates a list of subjects from the tags and elsst tags of a datapackage.
   * @param tagsDe German tags of a data or analysis package
   * @param tagsEn English tags of a data or analysis package
   * @param elsstTagsDe German elsst tags of a data or analysis package
   * @param elsstTagsEn German elsst tags of a data or analysis package
   * @return the list of subject objects
   */
  private List<Map<String, String>> createSubjectsList(Set<String> tagsDe, Set<String> tagsEn, Set<Elsst> elsstTagsDe, Set<Elsst> elsstTagsEn) {
    List<Map<String, String>> subjectsList = new ArrayList<>();
    // normal tags
    if (tagsEn != null) {
      for (String tag : tagsEn) {
        Map<String, String> subject = new HashMap<>();
        subject.put("subject", tag);
        subject.put("lang", "en");
        subjectsList.add(subject);
      }
    }
    if (tagsDe != null) {
      for (String tag : tagsDe) {
        Map<String, String> subject = new HashMap<>();
        subject.put("subject", tag);
        subject.put("lang", "de");
        subjectsList.add(subject);
      }
    }

    //elsst tags
    if (elsstTagsEn != null) {
      for (Elsst tag : elsstTagsEn) {
        Map<String, String> subject = new HashMap<>();
        subject.put("subject", tag.getPrefLabel());
        subject.put("subjectScheme", "CESSDA European Language Social Science Thesaurus (ELSST)");
        subject.put("schemeUri", "https://thesauri.cessda.eu/elsst-4/en/");
        String elsstBaseUri = "https://thesauri.cessda.eu/elsst-4/en/page/";
        subject.put("valueUri", elsstBaseUri + tag.getLocalname());
        subject.put("lang", "en");
        subjectsList.add(subject);
      }
    }

    if (elsstTagsDe != null) {
      for (Elsst tag : elsstTagsDe) {
        Map<String, String> subject = new HashMap<>();
        subject.put("subject", tag.getPrefLabel());
        subject.put("subjectScheme", "CESSDA European Language Social Science Thesaurus (ELSST)");
        subject.put("schemeUri", "https://thesauri.cessda.eu/elsst-4/en/");
        String elsstBaseUri = "https://thesauri.cessda.eu/elsst-4/en/page/";
        subject.put("valueUri", elsstBaseUri + tag.getLocalname());
        subject.put("lang", "de");
        subjectsList.add(subject);
      }
    }

    return subjectsList;
  }

  /**
   * Creates a list of title objects. Each entry represents one title option including the language.
   * @param titles the title object of a data or analysis package
   * @return the list of title objects
   */
  private List<Map<String, String>> createTitlesList(I18nString titles) {
    List<Map<String, String>> titleList = new ArrayList<>();

    if (titles != null && titles.getDe() != null) {
      Map<String,String> titleObj = new HashMap<>();
      titleObj.put("title", titles.getDe());
      titleObj.put("lang", "de");
      titleList.add(titleObj);
    }
    if (titles != null && titles.getEn() != null) {
      Map<String,String> titleObj = new HashMap<>();
      titleObj.put("title", titles.getEn());
      titleObj.put("lang", "en");
      titleList.add(titleObj);
    }
    return titleList;
  }

  /**********************************************************************
   * DataPackage Methods
   *********************************************************************/

  /**
   * Creates a list of alternateIdentifiers for VerbundFDB and QND.
   * @param dataPackage the dataPackage dataset
   * @param surveys the list of survey datasets
   * @return a list of alternateIdentifier objects
   */
  private List<Map<String, String>> createAlternateIdentifiersListForDp(DataPackage dataPackage, List<Survey> surveys) {
    List<Map<String, String>> alternateIdentifiersList = new ArrayList<>();
    if (dataPackage.getTransmissionViaVerbundFdb() != null && dataPackage.getTransmissionViaVerbundFdb() != false) {
      Map<String, String> identifierObj = new HashMap<>();
      identifierObj.put("alternateIdentifierType", "VerbundFDB");
      identifierObj.put("alternateIdentifier", "1");
      alternateIdentifiersList.add(identifierObj);
    }

    // Add flag for qualitative data in surveys
    boolean hasQualitativeSurvey = false;
    for (Survey survey : surveys) {
      if (survey.getDataType().equals(DataTypes.QUALITATIVE_DATA)) {
        hasQualitativeSurvey = true;
        break;
      }
    }
    if (hasQualitativeSurvey) {
      Map<String, String> identifierObj = new HashMap<>();
      identifierObj.put("alternateIdentifierType", "QDN");
      identifierObj.put("alternateIdentifier", "2");
      alternateIdentifiersList.add(identifierObj);
    }
    return alternateIdentifiersList;
  }
}
