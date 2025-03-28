package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  private final DataPackageRepository dataPackageRepository;

  private final SurveyRepository surveyRepository;

  private final DataAcquisitionProjectRepository projectRepository;

  @Autowired
  private DoiBuilder doiBuilder;


  /**
   * Creates the DataCite Metadata object for a project as a JsonNode.
   * The resulting object can be used as the payload for querying the DataCite API.
   * @param project the project dataset
   * @return the metadata as a JsonNode
   */
  public JsonNode getDataCiteMetadataforProject(DataAcquisitionProject project) {
    final var dataPackages = this.dataPackageRepository.findByDataAcquisitionProjectId(project.getId());
    if (dataPackages.isEmpty()) {
      throw new RuntimeException("This project has no data package linked to it");
    } else if (dataPackages.size() > 1) {
      throw new RuntimeException("This project has more than one data package linked to it");
    }
    final var dataPackage = dataPackages.get(0);

    final var surveys = this.surveyRepository.findByDataAcquisitionProjectId(project.getId());

    Map<String,Object> dataObj = new HashMap<>();
    dataObj.put("attributes", this.createAttrObject(project, dataPackage, surveys));
    dataObj.put("type", "dois");
    Map<String, Object> baseObj = new HashMap<>();
    baseObj.put("data", dataObj);
    JsonNode node = mapper.valueToTree(baseObj);
    return node;
  }

  /**
   * Creates the attr object mapping MDM data to the Data Cite Metadata schema.
   * @param project the project dataset
   * @param dataPackage the dataPackage dataset
   * @param surveys a list of survey datasets
   * @return a map of key value pairs filled with metadata
   */
  Map<String,Object> createAttrObject(DataAcquisitionProject project, DataPackage dataPackage, List<Survey> surveys) {
    Map<String,Object> attrObj = new HashMap<>();
    attrObj.put("event", "publish");
    attrObj.put("doi", doiBuilder.buildDataOrAnalysisPackageDoiForDataCite(project.getId(), project.getRelease()));
    attrObj.put("prefix", doiBuilder.getDoiPrefixForDataCite());
    attrObj.put("suffix", doiBuilder.getDoiSuffixForDataCite(project.getId(), project.getRelease()));
    String urlLanguage = project.getRelease().getDoiPageLanguage() != null ?
      project.getRelease().getDoiPageLanguage() : "en";
    String packageType = "data-packages";
    attrObj.put("url", MDM_BASE_URL + urlLanguage + "/" + packageType + "/" + dataPackage.getMasterId() +
      "?version=" + project.getRelease().getVersion());
    attrObj.put("titles", this.createTitlesList(dataPackage));
    attrObj.put("publicationYear", project.getRelease().getFirstDate().getYear());
    attrObj.put("types", this.createTypesObject());
    attrObj.put("version", project.getRelease().getVersion());
    attrObj.put("creators", this.createCreatorsList(dataPackage));
    attrObj.put("publisher", this.createPublisherObject(project));
    attrObj.put("descriptions", this.createDescriptionsList(dataPackage, surveys));
    attrObj.put("contributors", this.createContributorsList(dataPackage));
    attrObj.put("fundingReferences", this.createFundingReferencesList(dataPackage));
    attrObj.put("subjects", this.createSubjectsList(dataPackage));
    attrObj.put("dates", this.createDatesList(project, surveys));
    attrObj.put("geoLocations", this.createGeoLocationsList(surveys));
    attrObj.put("alternateIdentifiers", this.createAlternateIdentifiersList(dataPackage, surveys));
    attrObj.put("relatedIdentifiers", this.createRelatedIdentifiersList(project));
    return attrObj;
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
   * Creates a list of alternateIdentifiers for VerbundFDB and QND.
   * @param dataPackage the dataPackage dataset
   * @param surveys the list of survey datasets
   * @return a list of alternateIdentifier objects
   */
  private List<Map<String, String>> createAlternateIdentifiersList(DataPackage dataPackage, List<Survey> surveys) {
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

  /**
   * Creates a list of geographic references.
   * @param surveys a list of survey datasets
   * @return a list of geoLocation objects
   */
  private List<Map<String, String>> createGeoLocationsList(List<Survey> surveys) {
    //todo: check if this is correct
    List<Map<String, String>> geoLocationsList = new ArrayList<>();
    for (Survey survey : surveys) {
      for (GeographicCoverage geoCov : survey.getPopulation().getGeographicCoverages()) {
        Map<String, String> geoLocationObj = new HashMap<>();
        geoLocationObj.put("geoLocationPlace", geoCov.getCountry());
        geoLocationsList.add(geoLocationObj);
      }
    }
    return geoLocationsList;
  }

  /**
   * Creates a list of dates includign the embargo date if present and field period dates of all surveys.
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
   * Creates a list of subjects from the tags and elsst tags of a datapackage.
   * @param dataPackage the dataPackage dataset
   * @return the list of subject objects
   */
  private List<Map<String, String>> createSubjectsList(DataPackage dataPackage) {
    List<Map<String, String>> subjectsList = new ArrayList<>();
    // normal tags
    if (dataPackage.getTags() != null && dataPackage.getTags().getEn() != null) {
      for (String tag : dataPackage.getTags().getEn()) {
        Map<String, String> subject = new HashMap<>();
        subject.put("subject", tag);
        subject.put("lang", "en");
        //todo: consolidation of further fields needed
        subjectsList.add(subject);
      }
    }
    if (dataPackage.getTags() != null && dataPackage.getTags().getDe() != null) {
      for (String tag : dataPackage.getTags().getDe()) {
        Map<String, String> subject = new HashMap<>();
        subject.put("subject", tag);
        subject.put("lang", "de");
        //todo: consolidation of further fields needed
        subjectsList.add(subject);
      }
    }

    //elsst tags
    if (dataPackage.getTagsElsst() != null && dataPackage.getTagsElsst().getEn() != null) {
      for (Elsst tag : dataPackage.getTagsElsst().getEn()) {
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

    if (dataPackage.getTagsElsst() != null && dataPackage.getTagsElsst().getDe() != null) {
      for (Elsst tag : dataPackage.getTagsElsst().getDe()) {
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
   * Creates the fundingReferences list.
   * @param dataPackage the dataPackage dataset
   * @return the list of dfunding references.
   */
  private List<Map<String, String>> createFundingReferencesList(DataPackage dataPackage) {
    List<Map<String, String>> fundingReferenceList = new ArrayList<>();
    for (Sponsor sponsor : dataPackage.getSponsors()) {
      Map<String, String> fundingRef = new HashMap<>();
      fundingRef.put("funderName",
        sponsor.getName() != null && sponsor.getName().getDe() != null ? sponsor.getName().getDe() : sponsor.getName().getEn());
      //todo: consolidation of further fields needed
      fundingReferenceList.add(fundingRef);
    }
    return fundingReferenceList;
  }

  /**
   * Creates a list of contributors including a fixed reference to "FDZ-DZHW" as a distributor
   * and a list of data curators.
   * @param dataPackage the dataPackage dataset
   * @return a list of contributors
   */
  private List<Map<String, Object>> createContributorsList(DataPackage dataPackage) {
    List<Map<String, Object>> contributorList = new ArrayList<>();
    Map<String, Object> contributor = new HashMap<>();
    contributor.put("name", "FDZ-DZHW");
    contributor.put("nameType", "Organizational");
    contributor.put("contributorType", "Distributor");
    // todo: nameIdentifier ???
    contributor.put("nameIdentifiers", new ArrayList<>());
    contributorList.add(contributor);

    for (Person person : dataPackage.getDataCurators()) {
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
      //todo: do we add affiliations here?
      contributorObject.put("affiliation", this.createCreatorAffiliationList(dataPackage));
      contributorList.add(contributorObject);
    }
    return contributorList;
  }

  /**
   * Creates a list of description objects.
   * @param dataPackage
   * @return
   */
  private List<Map<String, String>> createDescriptionsList(DataPackage dataPackage, List<Survey> surveys) {
    List<Map<String, String>> descriptionList = new ArrayList<>();
    // add descriptions of type "abstract"
    if (dataPackage.getDescription() != null && dataPackage.getDescription().getEn() != null) {
      Map<String,String> descriptionObj = new HashMap<>();
      descriptionObj.put("description", dataPackage.getDescription().getEn());
      descriptionObj.put("lang", "en");
      descriptionObj.put("descriptionType", "Abstract");
      descriptionList.add(descriptionObj);
    }
    if (dataPackage.getDescription() != null && dataPackage.getDescription().getDe() != null) {
      Map<String,String> descriptionObj = new HashMap<>();
      descriptionObj.put("description", dataPackage.getDescription().getDe());
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
   * Creates a list of title objects. Each entry represents one title option including the language.
   * @param dataPackage the dataPackage dataset
   * @return the list of title objects
   */
  private List<Map<String, String>> createTitlesList(DataPackage dataPackage) {
    List<Map<String, String>> titleList = new ArrayList<>();

    if (dataPackage.getTitle() != null && dataPackage.getTitle().getDe() != null) {
      Map<String,String> titleObj = new HashMap<>();
      titleObj.put("title", dataPackage.getTitle().getDe());
      titleObj.put("lang", "de");
      titleList.add(titleObj);
    }
    if (dataPackage.getTitle() != null && dataPackage.getTitle().getEn() != null) {
      Map<String,String> titleObj = new HashMap<>();
      titleObj.put("title", dataPackage.getTitle().getEn());
      titleObj.put("lang", "en");
      titleList.add(titleObj);
    }
    return titleList;
  }

  /**
   * Creates the types object.
   * @return the types object as a map of key value pairs
   */
  private Map<String, String> createTypesObject() {
    Map<String, String> typesObj = new HashMap<>();
    typesObj.put("resourceTypeGeneral", "Dataset");
    //todo: add further types
    return typesObj;
  }

  /**
   * Creates a list of creator objects including names, institutional references and
   * identiefiers.
   * @param dataPackage the datapackage dataset
   * @return a list of maps of key value pairs filled with metadata of the creators
   */
  private List<Map<String, Object>> createCreatorsList(DataPackage dataPackage) {
    List<Map<String, Object>> creatorsList = new ArrayList<>();
    for (Person person : dataPackage.getProjectContributors()) {
      Map<String, Object> creatorObject = new HashMap<>();
      creatorObject.put("name", person.getMiddleName() != null ? String.format(
        "%s, %s %s",  person.getLastName(), person.getFirstName(), person.getMiddleName()) :
        String.format("%s, %s",  person.getLastName(), person.getFirstName()));
      creatorObject.put("nameType", "Personal");
      creatorObject.put("givenName", person.getFirstName() + (person.getMiddleName() != null ?
        " " + person.getMiddleName() : ""));
      creatorObject.put("familyName", person.getLastName());
      creatorObject.put("nameIdentifiers", this.createCreatorNameIdentifierList(person));
      creatorObject.put("affiliation", this.createCreatorAffiliationList(dataPackage));
      creatorsList.add(creatorObject);
    }
    for (I18nString institution : dataPackage.getInstitutions()) {
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
   * @param dataPackage the dataPackage dataset
   * @return a list of affiliation objects
   */
  private List<Map<String, String>>  createCreatorAffiliationList(DataPackage dataPackage) {
    List<Map<String, String>> affiliationList = new ArrayList<>();
    for (I18nString institution : dataPackage.getInstitutions()) {
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
   * Creates the publisher object including attributes "name", "identifier",
   * "identifierScheme", "schemeUri", and "lang".
   * @param project the project dataset
   * @return a map of key value pairs filled with metadata of the publisher
   */
  private Map<String, String> createPublisherObject(DataAcquisitionProject project) {
    Map<String, String> publisherObj = new HashMap<>();
    //todo: name, identifier, identifierScheme, schemeUri, lang
    return publisherObj;
  }

}
