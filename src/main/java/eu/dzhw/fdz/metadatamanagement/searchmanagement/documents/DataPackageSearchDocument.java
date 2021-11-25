package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.projection.AnalysisPackageSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.common.domain.Country;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Period;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.projections.ConceptSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.SubDataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Configuration;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.GeographicCoverage;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.helper.CountryCodeProvider;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representation of a dataPackage which is stored in elasticsearch.
 *
 * @author René Reitmann
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class DataPackageSearchDocument extends DataPackage implements SearchDocumentInterface {

  private static final long serialVersionUID = -4015837106569277626L;

  static final String[] FIELDS_TO_EXCLUDE_ON_DESERIALIZATION = new String[] {"nested*", "variables",
      "questions", "configuration", "guiLabels", "*Publications"};

  private List<DataSetSubDocument> dataSets = new ArrayList<>();
  private List<DataSetNestedDocument> nestedDataSets = new ArrayList<>();

  private List<VariableSubDocument> variables = new ArrayList<>();
  private List<VariableNestedDocument> nestedVariables = new ArrayList<>();

  private List<RelatedPublicationSubDocument> relatedPublications = new ArrayList<>();

  private List<RelatedPublicationNestedDocument> nestedRelatedPublications = new ArrayList<>();

  private List<SurveySubDocument> surveys = new ArrayList<>();
  private List<SurveyNestedDocument> nestedSurveys = new ArrayList<>();

  private List<QuestionSubDocument> questions = new ArrayList<>();
  private List<QuestionNestedDocument> nestedQuestions = new ArrayList<>();

  private List<InstrumentSubDocument> instruments = new ArrayList<>();
  private List<InstrumentNestedDocument> nestedInstruments = new ArrayList<>();

  private List<ConceptSubDocument> concepts = new ArrayList<>();
  private List<ConceptNestedDocument> nestedConcepts = new ArrayList<>();

  private List<AnalysisPackageSubDocument> analysisPackages = new ArrayList<>();
  private List<AnalysisPackageNestedDocument> nestedAnalysisPackages = new ArrayList<>();

  private List<I18nString> nestedInstitutions = new ArrayList<>();
  private List<I18nString> nestedSponsors = new ArrayList<>();

  private Release release = null;

  private Configuration configuration = null;

  private List<I18nString> surveyDataTypes;

  private Period surveyPeriod;

  private List<I18nString> surveyCountries;

  private Integer numberOfWaves;

  private String doi;

  private I18nString guiLabels = DataPackageDetailsGuiLabels.GUI_LABELS;

  private I18nString completeTitle;

  private List<String> accessWays;

  private List<String> dataLanguages;

  /**
   * Construct the search document with all related subdocuments.
   *
   * @param dataPackage The dataPackage to be searched for
   * @param dataSets all data sets available in this dataPackage
   * @param variables all variables available in this dataPackage
   * @param relatedPublications all related publications using this dataPackage
   * @param surveys all surveys available in this dataPackage
   * @param questions all questions available in this dataPackage
   * @param instruments all instruments available in this dataPackage
   * @param configuration the project configuration
   */
  @SuppressWarnings("CPD-START")
  public DataPackageSearchDocument(DataPackage dataPackage,
      List<DataSetSubDocumentProjection> dataSets, List<VariableSubDocumentProjection> variables,
      List<RelatedPublicationSubDocumentProjection> relatedPublications,
      List<SurveySubDocumentProjection> surveys, List<QuestionSubDocumentProjection> questions,
      List<InstrumentSubDocumentProjection> instruments,
      List<ConceptSubDocumentProjection> concepts,
      List<AnalysisPackageSubDocumentProjection> analysisPackages, Release release, String doi,
      Configuration configuration) {
    super(dataPackage);
    if (dataSets != null && !dataSets.isEmpty()) {
      this.dataSets = dataSets.stream().map(DataSetSubDocument::new).collect(Collectors.toList());
      this.nestedDataSets =
          dataSets.stream().map(DataSetNestedDocument::new).collect(Collectors.toList());
      this.accessWays = generateAccessWays(dataSets);
      this.dataLanguages = generateDataLanguages(dataSets);
    }
    if (variables != null && !variables.isEmpty()) {
      this.variables =
          variables.stream().map(VariableSubDocument::new).collect(Collectors.toList());
      this.nestedVariables =
          variables.stream().map(VariableNestedDocument::new).collect(Collectors.toList());
    }
    if (relatedPublications != null && !relatedPublications.isEmpty()) {
      this.relatedPublications = relatedPublications.stream()
          .map(RelatedPublicationSubDocument::new).collect(Collectors.toList());
      this.nestedRelatedPublications = relatedPublications.stream()
          .map(RelatedPublicationNestedDocument::new).collect(Collectors.toList());
    }
    if (surveys != null && !surveys.isEmpty()) {
      this.surveys = surveys.stream().map(SurveySubDocument::new).collect(Collectors.toList());
      this.nestedSurveys =
          surveys.stream().map(SurveyNestedDocument::new).collect(Collectors.toList());
      this.surveyDataTypes = generateSurveyDataTypes(surveys);
      this.numberOfWaves = generateNumberOfWaves(surveys);
      this.surveyPeriod = generateSurveyPeriod(surveys);
      this.surveyCountries = generateSurveyCountryNames(surveys);
    }
    if (questions != null && !questions.isEmpty()) {
      this.questions = questions.stream().map(question -> new QuestionSubDocument(question))
          .collect(Collectors.toList());
      this.nestedQuestions = questions.stream()
          .map(question -> new QuestionNestedDocument(question)).collect(Collectors.toList());
    }
    if (instruments != null && !instruments.isEmpty()) {
      this.instruments =
          instruments.stream().map(InstrumentSubDocument::new).collect(Collectors.toList());
      this.nestedInstruments =
          instruments.stream().map(InstrumentNestedDocument::new).collect(Collectors.toList());
    }
    if (concepts != null && !concepts.isEmpty()) {
      this.concepts = concepts.stream().map(concept -> new ConceptSubDocument(concept))
          .collect(Collectors.toList());
      this.nestedConcepts = concepts.stream().map(concept -> new ConceptNestedDocument(concept))
          .collect(Collectors.toList());
    }
    if (analysisPackages != null && !analysisPackages.isEmpty()) {
      this.analysisPackages = analysisPackages.stream()
          .map(analysisPackage -> new AnalysisPackageSubDocument(analysisPackage, doi))
          .collect(Collectors.toList());
      this.nestedAnalysisPackages = analysisPackages.stream()
          .map(analysisPackage -> new AnalysisPackageNestedDocument(analysisPackage))
          .collect(Collectors.toList());
    }
    this.nestedInstitutions = dataPackage.getInstitutions();
    this.nestedSponsors = dataPackage.getSponsors();
    this.release = release;
    this.configuration = configuration;
    this.doi = doi;
    this.completeTitle = I18nString.builder()
        .de((dataPackage.getTitle().getDe() != null ? dataPackage.getTitle().getDe()
            : dataPackage.getTitle().getEn()) + " (" + dataPackage.getId() + ")")
        .en((dataPackage.getTitle().getEn() != null ? dataPackage.getTitle().getEn()
            : dataPackage.getTitle().getDe()) + " (" + dataPackage.getId() + ")")
        .build();
  }

  private Period generateSurveyPeriod(List<SurveySubDocumentProjection> surveys) {
    Period surveyPeriod = new Period();
    surveyPeriod.setStart(surveys.stream().map(SurveySubDocumentProjection::getFieldPeriod)
        .map(Period::getStart).min(LocalDate::compareTo).orElse(null));
    surveyPeriod.setEnd(surveys.stream().map(SurveySubDocumentProjection::getFieldPeriod)
        .map(Period::getEnd).max(LocalDate::compareTo).orElse(null));
    return surveyPeriod;
  }

  /**
   * For the moment the number of waves is equal to the number of surveys within a data package.
   *
   * @param surveys All Survey Sub Document Projections.
   * @return The number of surveys.
   */
  private Integer generateNumberOfWaves(List<SurveySubDocumentProjection> surveys) {
    Integer numberOfWaves = 0;

    if (surveys != null) {
      numberOfWaves = surveys.size();
    }

    return numberOfWaves;
  }

  /**
   * Create an aggregated list of the data types of all {@link Survey}s.
   *
   * @param surveys All Survey Sub Document Projections.
   * @return aggregated List of survey data types
   */
  private List<I18nString> generateSurveyDataTypes(List<SurveySubDocumentProjection> surveys) {
    return surveys.stream().map(SurveySubDocumentProjection::getDataType).distinct()
        .collect(Collectors.toList());
  }

  /**
   * Create an aggregated list of the access ways of all {@link DataSet}s.
   *
   * @param dataSets All DataSet Sub Document Projections.
   * @return aggregated List of access ways
   */
  private List<String> generateAccessWays(List<DataSetSubDocumentProjection> dataSets) {
    return dataSets.stream().map(DataSetSubDocumentProjection::getSubDataSets)
        .flatMap(Collection::stream).map(SubDataSet::getAccessWay).distinct()
        .collect(Collectors.toList());
  }

  /**
   * Create an aggregated list of the languages of all {@link DataSet}s.
   *
   * @param dataSets All DataSet Sub Document Projections.
   * @return aggregated List of languages
   */
  private List<String> generateDataLanguages(List<DataSetSubDocumentProjection> dataSets) {
    return dataSets.stream().map(DataSetSubDocumentProjection::getLanguages)
        .filter(list -> list != null).flatMap(Collection::stream).distinct()
        .collect(Collectors.toList());
  }

  /**
   * Create an aggregated list of the country names of all {@link GeographicCoverage}s.
   *
   * @param surveys All Survey Sub Document Projections.
   * @return aggregated List of country names
   */
  private List<I18nString> generateSurveyCountryNames(List<SurveySubDocumentProjection> surveys) {
    Set<String> countryCodes =
        surveys.stream().flatMap(survey -> survey.getPopulation().getGeographicCoverages().stream())
            .map(GeographicCoverage::getCountry).collect(Collectors.toSet());
    List<I18nString> surveyCountryNames = new ArrayList<>(countryCodes.size());
    for (String countryCode : countryCodes) {
      Country selectedCountry = CountryCodeProvider.COUNTRY_CODES.stream()
          .filter(country -> country.getCode().equals(countryCode)).findFirst().orElse(null);
      if (selectedCountry != null) {
        surveyCountryNames.add(new I18nString(selectedCountry.getDe(), selectedCountry.getEn()));
      }
    }
    return surveyCountryNames;
  }
}
