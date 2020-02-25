
package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.web.client.MetricsRestTemplateCustomizer;
import org.springframework.boot.actuate.metrics.web.client.RestTemplateExchangeTagsProvider;
import org.springframework.core.io.Resource;
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

import com.google.common.base.Charsets;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.CollectionModes;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentTypes;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.FreeResourceTypes;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.helper.DoiBuilder;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.DataAvailabilities;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.SurveyDesigns;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.TimeMethods;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.GeographicCoverage;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import freemarker.core.XMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

/**
 * Access component for getting health information or registration or updates for dara and the doi.
 *
 * @author Daniel Katzberg
 */
@Service
@Slf4j
public class DaraService {

  public static final String IS_ALiVE_ENDPOINT = "api/isAlive";
  public static final String REGISTRATION_ENDPOINT = "study/importXML";

  @Autowired
  private MetadataManagementProperties metadataManagementProperties;

  @Autowired
  private DataAcquisitionProjectRepository projectRepository;

  @Autowired
  private StudyRepository studyRepository;

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private DataSetRepository dataSetRepository;

  @Autowired
  private VariableRepository variableRepository;

  @Autowired
  private InstrumentRepository instrumentRepository;

  @Autowired
  private RelatedPublicationRepository relatedPublicationRepository;

  @Value(value = "classpath:templates/dara/register.xml.tmpl")
  private Resource registerXml;

  @Autowired
  private DoiBuilder doiBuilder;

  @Autowired
  private DataAcquisitionProjectVersionsService dataAcquisitionProjectVersionsService;

  private RestTemplate restTemplate;

  // Key for Register XML Template
  private static final String KEY_REGISTER_XML_TMPL = "register.xml.tmpl";

  // Availability Controlled
  private static final String AVAILABILITY_CONTROLLED_DELIVERY = "Delivery";
  private static final String AVAILABILITY_CONTROLLED_NOT_AVAILABLE = "NotAvailable";

  /**
   * Constructor for Dara Services. Set the Rest Template.
   */
  @Autowired
  public DaraService(MeterRegistry meterRegistry, RestTemplateExchangeTagsProvider tagProvider) {
    this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    this.restTemplate.getMessageConverters().add(0,
        new StringHttpMessageConverter(Charset.forName("UTF-8")));
    MetricsRestTemplateCustomizer customizer =
        new MetricsRestTemplateCustomizer(meterRegistry, tagProvider, "dara.client.requests");
    customizer.customize(this.restTemplate);
  }

  /**
   * Check the dara health endpoint.
   *
   * @return Returns the status of the dara server.
   */
  public boolean isDaraHealthy() {

    final String daraHealthEndpoint = this.getApiEndpoint() + IS_ALiVE_ENDPOINT;

    ResponseEntity<String> result =
        this.restTemplate.getForEntity(daraHealthEndpoint, String.class);

    return result.getStatusCode().equals(HttpStatus.OK);
  }

  /**
   * Registers or updates a dataset with a given doi to dara.
   *
   * @param project The Project.
   * @return The HttpStatus from Dara Returns a false, if something gone wrong.
   * @throws IOException the io exception for non readable xml file.
   * @throws TemplateException Exception for filling the template.
   */
  public HttpStatus registerOrUpdateProjectToDara(DataAcquisitionProject project)
      throws IOException, TemplateException {

    // Read register xml
    String registerXmlStr = IOUtils.toString(this.registerXml.getInputStream(), Charsets.UTF_8);

    // Fill template
    String filledTemplate = this.fillTemplate(registerXmlStr, this.getTemplateConfiguration(),
        this.getDataForTemplate(project), KEY_REGISTER_XML_TMPL);

    // Send Rest Call for Registration
    HttpStatus httpStatusFromDara = this.postToDaraImportXml(filledTemplate);
    return httpStatusFromDara;
  }

  /**
   * Registers or updates a dataset with a given doi to dara.
   *
   * @param projectId The id of the Project.
   * @return The HttpStatus from Dara Returns a false, if something gone wrong.
   * @throws IOException the io exception for non readable xml file.
   * @throws TemplateException Exception for filling the template.
   */
  public HttpStatus registerOrUpdateProjectToDara(String projectId)
      throws IOException, TemplateException {

    // Load Project
    DataAcquisitionProject project = this.projectRepository.findById(projectId).get();
    return this.registerOrUpdateProjectToDara(project);
  }

  /**
   * This is the kernel method for registration, update and unregister of a doi element.
   *
   * @param filledTemplate The filled and used template.
   * @return the HttpStatus from Dara.
   */
  private HttpStatus postToDaraImportXml(String filledTemplate) {
    log.debug("The filled Template for dara:");
    log.debug(filledTemplate);

    // Load Dara Information
    final String daraEndpoint =
        this.metadataManagementProperties.getDara().getEndpoint() + REGISTRATION_ENDPOINT;
    final String daraUsername = this.metadataManagementProperties.getDara().getUsername();
    final String daraPassword = this.metadataManagementProperties.getDara().getPassword();

    // Build Header
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/xml;charset=UTF-8");
    String auth = daraUsername + ":" + daraPassword;
    byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName(Charsets.UTF_8.name())));
    headers.add("Authorization", "Basic " + new String(encodedAuth, Charsets.UTF_8));

    // Build
    // It is always true, because every new release will have
    // a new doi based on the new release version.
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromUriString(daraEndpoint).queryParam("registration", "true");

    // Build Request
    HttpEntity<String> request = new HttpEntity<>(filledTemplate, headers);

    // Send Post
    // Info: result.getBody() has the registered DOI
    try {
      ResponseEntity<String> result =
          this.restTemplate.postForEntity(builder.build().toUri(), request, String.class);
      log.debug("Response code from Dara: {}", result.getStatusCode());
      log.debug("Response body from Dara: {}", result.getBody());
      return result.getStatusCode();
    } catch (HttpClientErrorException httpClientError) {
      log.error("HTTP Error durind Dara call", httpClientError);
      log.error("Dara Response Body:\n" + httpClientError.getResponseBodyAsString());
      // Has been released is false? Something went wrong at the local save?
      // Catch the second try for registring
      // Idempotent Method!
      String responseBody = httpClientError.getResponseBodyAsString();
      if (httpClientError.getStatusCode().is4xxClientError()
          && (responseBody.equals("A resource with the given doiProposal exists in the system.")
              || responseBody.contains("remint failed"))) {
        return HttpStatus.CREATED;
      } else {
        throw httpClientError;
      }
    }
  }

  /**
   * Load all needed Data for the XML Templates. The data is callable in freemarker by: study
   * releaseDate availabilityControlled resourceType
   *
   * @param project The project to find the study.
   * @return Returns a Map of names and the depending objects. If the key is 'study' so the study
   *         object is the value. Study is the name for the object use in freemarker.
   */
  private Map<String, Object> getDataForTemplate(DataAcquisitionProject project) {

    Map<String, Object> dataForTemplate = new HashMap<>();

    // Get Project Information
    dataForTemplate.put("dataAcquisitionProject", project);

    // Get Study Information
    Study study = this.studyRepository.findOneByDataAcquisitionProjectId(project.getId());
    dataForTemplate.put("study", study);

    String availabilityControlled = AVAILABILITY_CONTROLLED_NOT_AVAILABLE;
    if (!study.isHidden() && study.getDataAvailability().equals(DataAvailabilities.AVAILABLE)) {
      availabilityControlled = AVAILABILITY_CONTROLLED_DELIVERY;
    }

    Release release = project.getRelease();
    if (release == null) {
      release = dataAcquisitionProjectVersionsService.findLastRelease(project.getMasterId());
    }
    if (release.getFirstDate() == null) {
      Optional<DataAcquisitionProject> previousRelease = projectRepository
          .findById(project.getMasterId() + "-" + release.getVersion());
      if (previousRelease.isPresent()) {
        release.setFirstDate(previousRelease.get().getRelease().getFirstDate());
      } else {
        release.setFirstDate(LocalDateTime.now());
      }
    }

    String doi = doiBuilder.buildStudyDoi(study, release);
    dataForTemplate.put("doi", doi);

    String previousDoi = doiBuilder.buildStudyDoi(study,
        dataAcquisitionProjectVersionsService.findPreviousRelease(project.getMasterId(), release));
    dataForTemplate.put("previousDoi", previousDoi);

    // Get Surveys Information
    List<Survey> surveys =
        this.surveyRepository.findByDataAcquisitionProjectIdOrderByNumber(project.getId());
    dataForTemplate.put("surveys", surveys);

    dataForTemplate.put("surveyUnits", concatenateUnits(surveys));

    dataForTemplate.put("geographicCoverages", deduplicateGeographicCoverages(surveys));

    dataForTemplate.put("surveySamplesMap", concatenateSurveySamplesByLanguage(surveys));

    // Get Datasets Information
    List<DataSet> dataSets = this.dataSetRepository.findByDataAcquisitionProjectId(project.getId());
    dataForTemplate.put("dataSets", dataSets);
    HashMap<String, Long> dataSetNumberOfVariablesMap = new HashMap<>();

    for (DataSet dataSet : dataSets) {
      long numberVariables = this.variableRepository.countByDataSetId(dataSet.getId());
      dataSetNumberOfVariablesMap.put(dataSet.getId(), numberVariables);
    }
    dataForTemplate.put("numberOfVariablesMap", dataSetNumberOfVariablesMap);

    // Get Related Publications
    List<RelatedPublication> relatedPublications =
        this.relatedPublicationRepository.findByStudyIdsContaining(study.getMasterId());
    dataForTemplate.put("relatedPublications", relatedPublications);

    // Add Date
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    dataForTemplate.put("releaseDate", formatter.format(release.getFirstDate()));

    // Add Availability Controlled
    dataForTemplate.put("availabilityControlled", availabilityControlled);

    // Add Resource Type
    dataForTemplate.put("resourceTypeFree", computeResourceTypeFree(surveys));

    // Add Time Dimension
    dataForTemplate.put("timeDimension", computeTimeDimension(study));

    // Add data for collection mode
    dataForTemplate.put("surveyToCollectionModesMap", computeSurveyToCollectionModesMap(surveys));

    return dataForTemplate;
  }


  private String concatenateUnits(List<Survey> surveys) {
    return String.join("; ", surveys.stream()
        .map(survey -> survey.getPopulation().getUnit().getDe()).collect(Collectors.toSet()));
  }

  private Map<String, List<String>> computeSurveyToCollectionModesMap(List<Survey> surveys) {
    Map<String, List<String>> surveyToCollectionModesMap = new HashMap<>();
    for (Survey survey : surveys) {
      List<InstrumentSubDocumentProjection> instruments =
          instrumentRepository.findSubDocumentsBySurveyIdsContaining(survey.getId());
      List<String> collectionModes = new ArrayList<String>(instruments.size());
      for (InstrumentSubDocumentProjection instrument : instruments) {
        switch (instrument.getType()) {
          case InstrumentTypes.CAPI:
            collectionModes.add(CollectionModes.INTERVIEW_FACETOFACE_CAPICAMI);
            break;
          case InstrumentTypes.CATI:
            collectionModes.add(CollectionModes.INTERVIEW_TELEPHONE_CATI);
            break;
          case InstrumentTypes.CAWI:
            collectionModes.add(CollectionModes.SELFADMINISTEREDQUESTIONNAIRE_WEBBASED);
            break;
          case InstrumentTypes.PAPI:
            collectionModes.add(CollectionModes.SELFADMINISTEREDQUESTIONNAIRE_PAPER);
            break;
          case InstrumentTypes.INTERVIEW:
            collectionModes.add(CollectionModes.INTERVIEW_FACETOFACE);
            break;
          default:
            throw new NotImplementedException(
                "There is no mapping to DARAs collectionMode for the instrument type "
                    + instrument.getType());
        }
      }
      surveyToCollectionModesMap.put(survey.getId(), collectionModes);
    }
    return surveyToCollectionModesMap;
  }

  private Set<GeographicCoverage> deduplicateGeographicCoverages(List<Survey> surveys) {
    return surveys.stream()
        .flatMap(survey -> survey.getPopulation().getGeographicCoverages().stream())
        .collect(Collectors.toSet());

  }

  private Map<String, String> concatenateSurveySamplesByLanguage(List<Survey> surveys) {
    Set<I18nString> samples = surveys.stream().map(Survey::getSample).collect(Collectors.toSet());

    Map<String, String> samplesGroupedByLanguage = new HashMap<>();
    String german = samples.stream().map(I18nString::getDe).collect(Collectors.joining("; "));
    samplesGroupedByLanguage.put("de", german);
    String english = samples.stream().map(I18nString::getEn).collect(Collectors.joining("; "));
    samplesGroupedByLanguage.put("en", english);
    return samplesGroupedByLanguage;
  }

  private String computeTimeDimension(Study study) {
    if (study.getSurveyDesign().equals(SurveyDesigns.CROSS_SECTION)) {
      return TimeMethods.CROSSSECTION;
    }
    if (study.getSurveyDesign().equals(SurveyDesigns.PANEL)) {
      return TimeMethods.LONGITUDINAL_PANEL;
    }
    throw new NotImplementedException(
        "There is no mapping to DARAs timeDimension for the survey design "
            + study.getSurveyDesign());
  }

  private I18nString computeResourceTypeFree(List<Survey> surveys) {
    I18nString resourceTypeFree = null;
    for (Survey survey : surveys) {
      if (survey.getDataType().equals(DataTypes.QUANTITATIVE_DATA) && resourceTypeFree == null) {
        resourceTypeFree = FreeResourceTypes.SURVEY_DATA;
      } else if (survey.getDataType().equals(DataTypes.QUALITATIVE_DATA)
          && resourceTypeFree == null) {
        resourceTypeFree = FreeResourceTypes.QUALITATIVE_DATA;
      } else if (survey.getDataType().equals(DataTypes.QUALITATIVE_DATA)
          && resourceTypeFree == FreeResourceTypes.SURVEY_DATA) {
        resourceTypeFree = FreeResourceTypes.MIXED_DATA;
      } else if (survey.getDataType().equals(DataTypes.QUANTITATIVE_DATA)
          && resourceTypeFree == FreeResourceTypes.QUALITATIVE_DATA) {
        resourceTypeFree = FreeResourceTypes.MIXED_DATA;
      }
    }
    return resourceTypeFree;
  }

  /**
   * Get the configuration for freemarker.
   *
   * @return a configratution object for the registration.
   */
  private Configuration getTemplateConfiguration() {
    // Configuration, based on Freemarker Version 2.3.23
    Configuration templateConfiguration = new Configuration(Configuration.VERSION_2_3_23);
    templateConfiguration.setDefaultEncoding(StandardCharsets.UTF_8.toString());
    templateConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    templateConfiguration.setNumberFormat("0.######");
    templateConfiguration.setOutputFormat(XMLOutputFormat.INSTANCE);

    return templateConfiguration;
  }

  /**
   * This method fills the xml templates.
   *
   * @param templateContent The content of a xml template.
   * @param templateConfiguration The configuration for freemarker.
   * @param fileName filename of the script which will be filled in this method.
   * @return The filled xml templates as byte array.
   * @throws IOException Handles IO Exception.
   * @throws TemplateException Handles template Exceptions.
   */
  private String fillTemplate(String templateContent, Configuration templateConfiguration,
      Map<String, Object> dataForTemplate, String fileName) throws IOException, TemplateException {
    // Read Template and escape elements
    Template texTemplate = new Template(fileName, templateContent, templateConfiguration);
    try (Writer stringWriter = new StringWriter()) {
      texTemplate.process(dataForTemplate, stringWriter);

      stringWriter.flush();
      return stringWriter.toString();
    }
  }

  /**
   * Returns dara api endpont.
   *
   * @return the api endpoint given by the configuration.
   */
  public String getApiEndpoint() {
    return this.metadataManagementProperties.getDara().getEndpoint();
  }

  public RestTemplate getRestTemplate() {
    return this.restTemplate;
  }
}
