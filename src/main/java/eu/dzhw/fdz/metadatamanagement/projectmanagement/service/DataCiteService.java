package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import eu.dzhw.fdz.metadatamanagement.common.domain.Elsst;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.domain.Sponsor;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.helper.DoiBuilder;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.GeographicCoverage;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service for registering project metadata at DataCite.
 */
@Service
@Slf4j
@AllArgsConstructor
public class DataCiteService {

  private ObjectMapper mapper = new ObjectMapper();
  private final String MDM_BASE_URL = "https://metadata.fdz.dzhw.eu/";
  private final String MDM_DATA_PACKAGE_TYPE = "data-packages";
  private final String MDM_ANALYSIS_PACKAGE_TYPE = "analysis-packages";

  private final DataPackageRepository dataPackageRepository;

  private final AnalysisPackageRepository analysisPackageRepository;

  private final SurveyRepository surveyRepository;

  private final DataAcquisitionProjectRepository projectRepository;

  @Autowired
  private final DoiBuilder doiBuilder;


  /**
   * Creates the DataCite metadata object for a project as a JsonNode.
   * The resulting object can be used as the payload for querying the DataCite API.
   * @param project the project dataset
   * @return the metadata as a JsonNode
   */
  public JsonNode getDataCiteMetadataforProject(DataAcquisitionProject project) {
    Map<String, Object> attrObj = null;
    if (project.getConfiguration().getRequirements().isDataPackagesRequired()) {
      final var dataPackages = this.dataPackageRepository.findByDataAcquisitionProjectId(project.getId());
      if (dataPackages.isEmpty()) {
        throw new RuntimeException("This project has no data package linked to it");
      } else if (dataPackages.size() > 1) {
        throw new RuntimeException("This project has more than one data package linked to it");
      }
      final DataPackage dataPackage = dataPackages.get(0);
      final List<Survey> surveys = this.surveyRepository.findByDataAcquisitionProjectId(project.getId());
      attrObj = this.createAttrObjectForDataPackage(project, dataPackage, surveys);
    } else if (project.getConfiguration().getRequirements().isAnalysisPackagesRequired()) {
      final var analysisPackages = this.analysisPackageRepository
        .findByDataAcquisitionProjectIdAndShadowIsTrue(project.getId()).collect(Collectors.toList());
      if (analysisPackages.isEmpty()) {
        throw new RuntimeException("This project has no analysis package linked to it");
      } else if (analysisPackages.size() > 1) {
        throw new RuntimeException("This project has more than one analysis package linked to it");
      }
      final AnalysisPackage analysisPackage = analysisPackages.get(0);
      attrObj = this.createAttrObjectForAnalysisPackage(project, analysisPackage);
    }

    if (attrObj == null) {
      throw new RuntimeException(String.format("Metadata for project with id '%s' could not be created.", project.getId()));
    }

    return this.createDataCitePayload(attrObj);
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

  /**
   * Maps MDM data to the Data Cite Metadata schema for data packages.
   * @param project the project dataset
   * @param dataPackage the dataPackage dataset
   * @param surveys a list of survey datasets
   * @return a map of key value pairs filled with metadata
   */
  private Map<String,Object> createAttrObjectForDataPackage(DataAcquisitionProject project, DataPackage dataPackage, List<Survey> surveys) {
    Map<String,Object> attrObj = new HashMap<>();
    this.addBasicInfo(attrObj, project, dataPackage.getMasterId(), true);
    attrObj.put("titles", this.createTitlesList(dataPackage.getTitle()));
    attrObj.put("publicationYear", project.getRelease().getFirstDate().getYear());
    attrObj.put("types", this.createTypesObject());
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
  private Map<String, Object> createAttrObjectForAnalysisPackage(DataAcquisitionProject project, AnalysisPackage analysisPackage) {
    Map<String,Object> attrObj = new HashMap<>();
    this.addBasicInfo(attrObj, project, analysisPackage.getMasterId(), true);
    attrObj.put("titles", this.createTitlesList(analysisPackage.getTitle()));
    attrObj.put("publicationYear", project.getRelease().getFirstDate().getYear());
    attrObj.put("types", this.createTypesObject());
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

  /************************************************************************
   * GENERIC METHODS
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
  public String createUrlValue(DataAcquisitionProject project, String lang, String packageMasterId, Boolean isAnalysisPackage) {
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
    // todo: check if previous version has DOI
    // todo: if so add relatedIdentifier
    List<DataAcquisitionProject> previousList = this.projectRepository.findByMasterIdAndShadowIsTrue(project.getMasterId()).filter(
      pp -> pp.getSuccessorId() == project.getId()
    ).collect(Collectors.toList());
    if (!previousList.isEmpty()) {
      Map<String,String> relatedIdentifier = new HashMap<>();
      relatedIdentifier.put("relatedIdentifier", this.doiBuilder.buildDataOrAnalysisPackageDoiForDataCite(
        previousList.get(0).getId(), previousList.get(0).getRelease())); // add DOI of previous version
      relatedIdentifier.put("relatedIdentifierType", "DOI");
      relatedIdentifier.put("relationType", "IsNewVersionOf");
      relatedIdentifiersList.add(relatedIdentifier);
    }

    // todo: update previous version with relation in a seperate process

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
        Locale localeDe = new Locale("de", geoCov.getCountry());
        Map<String, String> geoLocationObjDe = new HashMap<>();
        geoLocationObjDe.put("geoLocationPlace", localeDe.getDisplayCountry(localeDe));
        geoLocationsList.add(geoLocationObjDe);
        Locale localeEn = new Locale("en", geoCov.getCountry());
        Map<String, String> geoLocationObjEn = new HashMap<>();
        geoLocationObjEn.put("geoLocationPlace", localeEn.getDisplayCountry(localeEn));
        geoLocationsList.add(geoLocationObjEn);
      }
    }
    return geoLocationsList;
  }

  /**
   * Creates a list of dates including the embargo date if present and field period dates of all surveys.
   * @param project the project dataset
   * @param surveys a list of survey datasets
   * @return a list of date objects
   */
  private List<Map<String, String>> createDatesList(DataAcquisitionProject project, List<Survey> surveys) {
    List<Map<String, String>> datesList = new ArrayList<>();
    if (project.getRelease().getIsPreRelease() && project.getEmbargoDate() != null) {
      Map<String, String> dateObj = new HashMap<>();
      dateObj.put("date", project.getEmbargoDate().toString());
      dateObj.put("dateType", "Other");
      dateObj.put("dateInformation",
        String.format("This data package is currently not yet available for order as it is subject to an embargo until %s." +
          " Publication can only take place after this date. Please note that the embargo date does not necessarily correspond" +
          " to the expected release date. Please contact userservice@dzhw.eu if you wish to receive information regarding the" +
          " release date of the data package.", project.getEmbargoDate().toString()));
      datesList.add(dateObj);
    } else {
      Map<String, String> dateObj = new HashMap<>();
      String pattern = "yyyy-MM-dd";
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
      dateObj.put("date", project.getRelease().getFirstDate().format(formatter));
      dateObj.put("dateType", "Available");
      datesList.add(dateObj);
    }

    for (Survey survey : surveys) {
      Map<String, String> dateObj = new HashMap<>();
      String startDate = survey.getFieldPeriod().getStart().toString();
      String endDate = survey.getFieldPeriod().getEnd().toString();
      dateObj.put("date", String.format("%s/%s", startDate, endDate));
      dateObj.put("dateType", "Collected");
      // todo: add information to which study this date applies?
      datesList.add(dateObj);
    }

    return datesList;
  }

  /**
   * Creates the types object.
   * @return the types object as a map of key value pairs
   */
  private Map<String, String> createTypesObject() {
    Map<String, String> typesObj = new HashMap<>();
    typesObj.put("resourceTypeGeneral", "Dataset");
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
  public List<Map<String, Object>> createCreatorsList(List<Person> creators, List<I18nString> institutions) {
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
      creatorObject.put("affiliation", this.createCreatorAffiliationList(institutions));
      creatorsList.add(creatorObject);
    }
    for (I18nString institution : institutions) {
      Map<String, Object> creatorObject = new HashMap<>();
      creatorObject.put("name", institution.getEn() != null ? institution.getEn() : institution.getDe());
      creatorObject.put("nameType", "Organizational");
      creatorObject.put("nameIdentifiers", new ArrayList<>()); //todo identifier
      creatorObject.put("affiliation", new ArrayList<>());
      creatorsList.add(creatorObject);
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
      nameIdentifierObject.put("schemeURI", "https://orcid.org");
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
    for (I18nString institution : institutions) {
      if (institution.getEn() != null) {
        Map<String, String> affiliationObj = new HashMap<>();
        affiliationObj.put("name", institution.getEn());
        affiliationList.add(affiliationObj);
      }
      if (institution.getDe() != null) {
        Map<String, String> affiliationObj = new HashMap<>();
        affiliationObj.put("name", institution.getDe());
        affiliationList.add(affiliationObj);
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
  public List<Map<String, String>> createDescriptionsList(I18nString description, List<Survey> surveys) {
    List<Map<String, String>> descriptionList = new ArrayList<>();
    // add descriptions of type "abstract"
    if (description != null && description.getEn() != null) {
      Map<String,String> descriptionObj = new HashMap<>();
      descriptionObj.put("description", description.getEn());
      descriptionObj.put("lang", "en");
      descriptionObj.put("descriptionType", "Abstract");
      descriptionList.add(descriptionObj);
    }
    if (description != null && description.getDe() != null) {
      Map<String,String> descriptionObj = new HashMap<>();
      descriptionObj.put("description", description.getDe());
      descriptionObj.put("lang", "de");
      descriptionObj.put("descriptionType", "Abstract");
      descriptionList.add(descriptionObj);
    }

    // add survey population description of type "methods"
    for (Survey survey : surveys) {
      Map<String,String> descriptionObjEn = new HashMap<>();
      descriptionObjEn.put("description", survey.getPopulation().getDescription().getEn());
      descriptionObjEn.put("lang", "en");
      descriptionObjEn.put("descriptionType", "Methods");
      descriptionList.add(descriptionObjEn);

      Map<String,String> descriptionObjDe = new HashMap<>();
      descriptionObjDe.put("description", survey.getPopulation().getDescription().getDe());
      descriptionObjDe.put("lang", "de");
      descriptionObjDe.put("descriptionType", "Methods");
      descriptionList.add(descriptionObjDe);
    }

    // todo: add descriptions of survey methods???

    return descriptionList;
  }

  /**
   * Creates a list of contributors including a fixed reference to "FDZ-DZHW" as a distributor
   * and a list of data curators.
   * @param contributors a list of project contributors
   * @return a list of contributors
   */
  public List<Map<String, Object>> createContributorsList(List<Person> contributors) {
    List<Map<String, Object>> contributorList = new ArrayList<>();
    Map<String, Object> contributor = new HashMap<>();
    contributor.put("name", "FDZ-DZHW");
    contributor.put("nameType", "Organizational");
    contributor.put("contributorType", "Distributor");
    // todo: nameIdentifier ???
    contributor.put("nameIdentifiers", new ArrayList<>());
    contributorList.add(contributor);

    for (Person person : contributors) {
      Map<String, Object> contributorObject = new HashMap<>();
      contributorObject.put("name", person.getMiddleName() != null ? String.format(
        "%s, %s %s",  person.getLastName(), person.getFirstName(), person.getMiddleName()) :
        String.format("%s, %s",  person.getLastName(), person.getFirstName()));
      contributorObject.put("nameType", "Personal"); //todo controlled vocab
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
    Map<String, String> affiliateObjDe = new HashMap<>();
    affiliateObjDe.put("name", "Deutsches Zentrum für Hochschul- und Wissenschaftsforschung (DZHW)");
    affiliateObjDe.put("lang", "de");
    affiliateObjDe.put("affiliationIdentifier", "https://ror.org/01n8j6z65");
    affiliateObjDe.put("affiliationIdentifierScheme", "ROR");
    affiliateObjDe.put("schemeUri", "https://ror.org/");
    affiliationList.add(affiliateObjDe);

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
  public List<Map<String, String>> createFundingReferencesList(List<Sponsor> sponsors) {
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
  public List<Map<String, String>> createSubjectsList(Set<String> tagsDe, Set<String> tagsEn, Set<Elsst> elsstTagsDe, Set<Elsst> elsstTagsEn) {
    List<Map<String, String>> subjectsList = new ArrayList<>();
    // normal tags
    if (tagsEn != null) {
      for (String tag : tagsEn) {
        Map<String, String> subject = new HashMap<>();
        subject.put("subject", tag);
        subject.put("lang", "en");
        //todo: consolidation of further fields needed
        subjectsList.add(subject);
      }
    }
    if (tagsDe != null) {
      for (String tag : tagsDe) {
        Map<String, String> subject = new HashMap<>();
        subject.put("subject", tag);
        subject.put("lang", "de");
        //todo: consolidation of further fields needed
        subjectsList.add(subject);
      }
    }

    //elsst tags
    if (elsstTagsEn != null) {
      for (Elsst tag : elsstTagsEn) {
        Map<String, String> subject = new HashMap<>();
        subject.put("subject", tag.getPrefLabel());
        subject.put("subjectScheme", "CESSDA European Language Social Science Thesaurus (ELSST)"); //todo: name?
        subject.put("schemeURI", "https://thesauri.cessda.eu/elsst-4/en/"); //todo: constant/link?
        String elsstBaseUri = "https://thesauri.cessda.eu/elsst-4/en/page/";
        subject.put("valueURI", elsstBaseUri + tag.getLocalname());
        subject.put("lang", "en");
        subjectsList.add(subject);
      }
    }

    if (elsstTagsDe != null) {
      for (Elsst tag : elsstTagsDe) {
        Map<String, String> subject = new HashMap<>();
        subject.put("subject", tag.getPrefLabel());
        subject.put("subjectScheme", "CESSDA European Language Social Science Thesaurus (ELSST)"); //todo: name?
        subject.put("schemeURI", "https://thesauri.cessda.eu/elsst-4/en/"); //todo: constant/link?
        String elsstBaseUri = "https://thesauri.cessda.eu/elsst-4/en/page/";
        subject.put("valueURI", elsstBaseUri + tag.getLocalname());
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
  public List<Map<String, String>> createTitlesList(I18nString titles) {
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
  public List<Map<String, String>> createAlternateIdentifiersListForDp(DataPackage dataPackage, List<Survey> surveys) {
    List<Map<String, String>> alternateIdentifiersList = new ArrayList<>();
    if (dataPackage.getTransmissionViaVerbundFdb()) {
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
