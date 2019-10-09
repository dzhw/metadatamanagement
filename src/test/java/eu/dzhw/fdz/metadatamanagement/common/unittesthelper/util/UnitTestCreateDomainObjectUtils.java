/**
 *
 */
package eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.javers.common.collections.Sets;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Period;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.domain.Resolution;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.ConceptAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.ConceptAttachmentTypes;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetTypes;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.Format;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.SubDataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentTypes;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.AssigneeGroup;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Configuration;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.ImageType;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionImageMetadata;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionTypes;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.TechnicalRepresentation;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.DataAvailabilities;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudyAttachmentTypes;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.SurveyDesigns;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Tags;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.GeographicCoverage;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Population;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.AccessWays;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Distribution;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.FilterDetails;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.FilterExpressionLanguages;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.GenerationDetails;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Missing;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RelatedQuestion;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RuleExpressionLanguages;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ScaleLevels;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Statistics;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.StorageTypes;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ValidResponse;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * @author Daniel Katzberg
 *
 */
public class UnitTestCreateDomainObjectUtils {

  private UnitTestCreateDomainObjectUtils() {}

  public static DataAcquisitionProject buildDataAcquisitionProject() {

    String projectId = "testproject";

    Configuration configuration = buildDataAcquisitionProjectConfiguration(
        Collections.singletonList("defaultPublisher"), null);

    DataAcquisitionProject project =
        DataAcquisitionProject.builder().id(projectId).hasBeenReleasedBefore(false)
            .configuration(configuration).assigneeGroup(AssigneeGroup.PUBLISHER).build();

    project.setMasterId(projectId);
    return project;
  }

  public static Configuration buildDataAcquisitionProjectConfiguration(List<String> publishers,
      List<String> dataProviders) {
    Configuration configuration = new Configuration();
    configuration.setDataProviders(dataProviders);
    configuration.setPublishers(publishers);
    return configuration;
  }

  public static Study buildStudy(String projectId) {
    Tags tags = new Tags();
    tags.setDe(new HashSet<String>(Arrays.asList("Test-Tag")));
    List<Person> authors = new ArrayList<>();
    authors.add(buildPerson("Test", null, "Authors"));
    List<I18nString> institutions =
        Arrays.asList(I18nString.builder().de("Institution De").en("Institution En").build());

    String studyId = UnitTestCreateValidIds.buildStudyId(projectId);
    Study study = Study.builder().id(studyId).authors(authors)
        .description(I18nString.builder().de("Description De").en("Description En").build())
        .institutions(institutions)
        .studySeries(I18nString.builder().de("Study Series De").en("Study Series En").build())
        .sponsor(I18nString.builder().de("Sponsor De").en("Sponsor En").build())
        .title(I18nString.builder().de("Titel De").en("Title En").build())
        .annotations(I18nString.builder().de("De Anmerkungen").en("En Annotations").build())
        .tags(tags).dataAvailability(DataAvailabilities.AVAILABLE).surveyDesign(SurveyDesigns.PANEL)
        .dataAcquisitionProjectId(projectId).build();
    study.setMasterId(studyId);
    return study;
  }

  public static Survey buildSurvey(String projectId) {
    GeographicCoverage geographicCoverage = GeographicCoverage.builder().country("DE").build();

    Population population = Population.builder()
        .unit(I18nString.builder().de("Hochschulabsolventen").en("College Graduates").build())
        .description(
            I18nString.builder().de("Population Beschreibung").en("Population Description").build())
        .geographicCoverages(Collections.singletonList(geographicCoverage)).build();

    String surveyId = UnitTestCreateValidIds.buildSurveyId(projectId, 1);

    Survey survey = Survey.builder().id(surveyId).dataAcquisitionProjectId(projectId)
        .studyId(UnitTestCreateValidIds.buildStudyId(projectId))
        .fieldPeriod(Period.builder().start(LocalDate.now()).end(LocalDate.now()).build())
        .title(I18nString.builder().de("Titel").en("title").build()).population(population)
        .sample(I18nString.builder().de("Sonstige").en("Other").build())
        .surveyMethod(I18nString.builder().de("Survey Method DE").en("Survey Method EN").build())
        .annotations(I18nString.builder().de("De Anmerkungen").en("En Annotations").build())
        .dataType(eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.DataTypes.QUALITATIVE_DATA)
        .grossSampleSize(100).sampleSize(100).responseRate(100.0).number(1).wave(1).build();

    survey.setMasterId(surveyId);
    return survey;
  }

  public static DataSet buildDataSet(String projectId, String surveyId, Integer surveyNumber) {
    String dataSetId = UnitTestCreateValidIds.buildDataSetId(projectId, 1);
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(surveyNumber);

    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(surveyId);

    List<SubDataSet> subDataSets = new ArrayList<>();
    subDataSets.add(SubDataSet.builder().name(UnitTestCreateValidIds.buildDataSetId(projectId, 1))
        .numberOfObservations(1).accessWay(AccessWays.DOWNLOAD_SUF)
        .citationHint(I18nString.builder().de("Citation Hint De").en("Citation Hint En").build())
        .description(I18nString.builder().de("Description DE").en("Description 3 EN").build())
        .build());
    subDataSets.add(SubDataSet.builder().name(UnitTestCreateValidIds.buildDataSetId(projectId, 2))
        .numberOfObservations(1).accessWay(AccessWays.REMOTE_DESKTOP)
        .citationHint(I18nString.builder().de("Citation Hint De").build())
        .description(I18nString.builder().de("Description 2 DE").en("Description 3 EN").build())
        .build());
    subDataSets.add(SubDataSet.builder().name(UnitTestCreateValidIds.buildDataSetId(projectId, 3))
        .numberOfObservations(1).accessWay(AccessWays.DOWNLOAD_CUF)
        .citationHint(I18nString.builder().en("Citation Hint En").build())
        .description(I18nString.builder().de("Description 3 DE").en("Description 3 EN").build())
        .build());
    subDataSets.add(SubDataSet.builder().name(UnitTestCreateValidIds.buildDataSetId(projectId, 4))
        .numberOfObservations(1).accessWay(AccessWays.ONSITE_SUF)
        .citationHint(I18nString.builder().de("Citation Hint De").en("Citation Hint En").build())
        .description(I18nString.builder().de("Description 4 DE").en("Description 4 EN").build())
        .build());

    DataSet dataSet = DataSet.builder().surveyIds(surveyIds).dataAcquisitionProjectId(projectId)
        .id(dataSetId).surveyNumbers(surveyNumbers).surveyIds(surveyIds).number(1)
        .format(Format.WIDE).type(DataSetTypes.PERSONAL_RECORD)
        .description(I18nString.builder().de("De Beschreibung").en("En Description").build())
        .annotations(I18nString.builder().de("De Anmerkungen").en("En Annotations").build())
        .studyId(UnitTestCreateValidIds.buildStudyId(projectId)).subDataSets(subDataSets).build();
    dataSet.setMasterId(dataSetId);
    return dataSet;
  }

  public static Variable buildVariable(String projectId, Integer dataSetNumber, String name,
      Integer index, List<Integer> surveyNumbers) {

    String variableId = UnitTestCreateValidIds.buildVariableId(projectId, dataSetNumber, name);

    // Prepare Variable
    List<String> accessWays = new ArrayList<>();
    accessWays.add(AccessWays.DOWNLOAD_CUF);
    accessWays.add(AccessWays.REMOTE_DESKTOP);
    accessWays.add(AccessWays.DOWNLOAD_SUF);
    accessWays.add(AccessWays.ONSITE_SUF);

    List<String> surveyIds = new ArrayList<>();
    for (Integer surveyNumber : surveyNumbers) {
      surveyIds.add(UnitTestCreateValidIds.buildSurveyId(projectId, surveyNumber));
    }

    List<String> relatedVariables = new ArrayList<>();
    relatedVariables.add(UnitTestCreateValidIds.buildVariableId(projectId, dataSetNumber, "name3"));

    List<RelatedQuestion> relatedQuestions = new ArrayList<>();
    relatedQuestions.add(buildRelatedQuestion(projectId, "1", "1"));
    Variable variable = Variable.builder().id(variableId).dataType(DataTypes.NUMERIC)
        .scaleLevel(ScaleLevels.RATIO).dataAcquisitionProjectId(projectId)
        .studyId(UnitTestCreateValidIds.buildStudyId(projectId)).name(name)
        .dataSetId(UnitTestCreateValidIds.buildDataSetId(projectId, dataSetNumber))
        .label(I18nString.builder().de("label").en("label").build()).accessWays(accessWays)
        .surveyNumbers(surveyNumbers).surveyIds(surveyIds).indexInDataSet(index)
        .dataSetNumber(dataSetNumber).relatedQuestions(relatedQuestions)
        .annotations(I18nString.builder().de("De Beschreibung").en("En Description").build())
        .filterDetails(FilterDetails.builder()
            .description(I18nString.builder().de("De Filterbeschreibung")
                .en("En Filter Description").build())
            .expressionLanguage(FilterExpressionLanguages.STATA).expression("Filter Expression")
            .build())
        .distribution(buildDistribution()).generationDetails(buildGenerationDetails())
        .relatedVariables(relatedVariables)
        .panelIdentifier(projectId + "-ds" + dataSetNumber + "-" + name)
        .derivedVariablesIdentifier(projectId + "-ds" + dataSetNumber + "-" + name)
        .storageType(StorageTypes.DOUBLE).doNotDisplayThousandsSeparator(true).build();
    variable.setMasterId(variableId);
    return variable;
  }

  public static TechnicalRepresentation buildTechnicalRepresentation() {
    return TechnicalRepresentation.builder().language("TR Language")
        .type("Technical Representation Type").source("Technical Representation Source").build();
  }

  public static Question buildQuestion(String projectId, Integer instrumentNumber,
      String instrumentId) {

    String questionId =
        UnitTestCreateValidIds.buildQuestionId(projectId, instrumentNumber, "123.12");

    Question question = Question.builder().dataAcquisitionProjectId(projectId).id(questionId)
        .additionalQuestionText(
            new I18nString("Zusätzlicher Fragetext", "Additional Question Text"))
        .dataAcquisitionProjectId(projectId)
        .instruction(new I18nString("Instruktionen", "Instruction")).instrumentId(instrumentId)
        .introduction(new I18nString("Einleitung", "Introduction")).number("123.12")
        .indexInInstrument(1).successors(new ArrayList<>())
        .questionText(new I18nString("Fragetext", "Question text"))
        .technicalRepresentation(buildTechnicalRepresentation()).type(QuestionTypes.SINGLE_CHOICE)
        .topic(new I18nString("Topic De", "Topic EN")).instrumentNumber(instrumentNumber)
        .successorNumbers(new ArrayList<>())
        .annotations(I18nString.builder().de("De Anmerkungen").en("En Annotations").build())
        .studyId(UnitTestCreateValidIds.buildStudyId(projectId)).build();
    question.setMasterId(questionId);
    return question;
  }

  public static QuestionImageMetadata buildQuestionImageMetadata(String projectId,
      String questionId) {

    return QuestionImageMetadata.builder().fileName("TestFileName.PNG").language("de")
        .resolution(Resolution.builder().widthX(800).heightY(600).build()).imageType(ImageType.PNG)
        .containsAnnotations(false).dataAcquisitionProjectId(projectId).questionId(questionId)
        .indexInQuestion(1).build();
  }

  public static Instrument buildInstrument(String projectId) {
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(UnitTestCreateValidIds.buildSurveyId(projectId, 1));
    String instrumentId = UnitTestCreateValidIds.buildInstrumentId(projectId, 1);
    Instrument instrument =
        Instrument.builder().dataAcquisitionProjectId(projectId).id(instrumentId)
            .title(I18nString.builder().de("Instrument.de").en("Instrument.en").build())
            .subtitle(I18nString.builder().de("Instrument Untertitel de")
                .en("Instrument Subtitle en").build())
            .description(I18nString.builder().de("Instrument.de").en("Instrument.en").build())
            .annotations(I18nString.builder().de("De Anmerkungen").en("En Annotations").build())
            .surveyIds(surveyIds).studyId(UnitTestCreateValidIds.buildStudyId(projectId))
            .dataAcquisitionProjectId(projectId).type("CAPI").number(1).surveyNumbers(surveyNumbers)
            .build();
    instrument.setMasterId(instrumentId);
    return instrument;
  }

  public static Statistics buildStatistics() {
    return Statistics.builder().firstQuartile("70.0").highWhisker(130.0).kurtosis(234.0)
        .lowWhisker(30.0).maximum("140.0").meanValue(87.5).median("90.0").minimum("0.0")
        .skewness(123.0).standardDeviation(40.0).thirdQuartile("110.0").mode("Mode").deviance(12.4)
        .meanDeviation(58.7).build();
  }

  public static GenerationDetails buildGenerationDetails() {
    return GenerationDetails.builder()
        .description(I18nString.builder().de("De Beschreibung").en("En Description").build())
        .rule("Rule 123 to 234").ruleExpressionLanguage(RuleExpressionLanguages.R).build();
  }

  public static Missing buildMissing() {
    return Missing.builder().absoluteFrequency(123).code("1234")
        .label(I18nString.builder().de("De Label").en("En Lable").build()).relativeFrequency(43.78)
        .build();
  }

  public static ValidResponse buildValidResponse() {
    return ValidResponse.builder().absoluteFrequency(123)
        .label(I18nString.builder().de("De Label").en("En Lable").build()).value("12.34")
        .relativeFrequency(43.78).validRelativeFrequency(75.45).build();
  }

  public static Distribution buildDistribution() {
    List<Missing> missings = new ArrayList<>();
    missings.add(buildMissing());
    List<ValidResponse> validResponses = new ArrayList<>();
    validResponses.add(buildValidResponse());
    return Distribution.builder().missings(missings).validResponses(validResponses)
        .statistics(buildStatistics()).totalAbsoluteFrequency(1234)
        .totalValidAbsoluteFrequency(1000).totalValidRelativeFrequency(81.03).build();
  }

  public static Release buildRelease() {
    return Release.builder().version("1.0.0").lastDate(LocalDateTime.now()).build();
  }

  public static FilterDetails buildFilterDetails() {
    return FilterDetails.builder()
        .description(
            I18nString.builder().de("Eine Filterbeschreibung.").en("A filter description.").build())
        .expression("A Filter Expression").expressionLanguage(FilterExpressionLanguages.STATA)
        .build();
  }

  public static RelatedPublication buildRelatedPublication() {

    List<String> studyIds = new ArrayList<>();
    studyIds.add(UnitTestCreateValidIds.buildStudyId("testproject"));

    return RelatedPublication.builder().doi("A DOI")
        .id(UnitTestCreateValidIds.buildRelatedPublicationId("HurzId123"))
        .publicationAbstract("A publication Abstract").sourceLink("http://www.hurzexample.de/")
        .sourceReference("A Source Reference").title("A Title of a Related Publication")
        .studyIds(studyIds).authors("Author").year(2017)
        .abstractSource(I18nString.builder().de("Test").en("Test").build()).language("de").build();

  }

  public static RelatedPublication buildRelatedPublication(String authorsString,
      int publicationYear) {

    List<String> studyIds = new ArrayList<>();
    studyIds.add(UnitTestCreateValidIds.buildStudyId("testproject"));

    return RelatedPublication.builder().doi("A DOI")
        .id(UnitTestCreateValidIds.buildRelatedPublicationId("HurzId123"))
        .publicationAbstract("A publication Abstract").sourceLink("http://www.hurzexample.de/")
        .sourceReference("A Source Reference").title("A Title of a Related Publication")
        .studyIds(studyIds).authors(authorsString).year(publicationYear)
        .abstractSource(I18nString.builder().de("Test").en("Test").build()).language("de").build();

  }

  public static Instrument buildInstrument(String projectId, String surveyId) {
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(surveyId);
    String instrumentId = UnitTestCreateValidIds.buildInstrumentId(projectId, 1);
    Instrument instrument = Instrument.builder().id(instrumentId)
        .dataAcquisitionProjectId(projectId).studyId(UnitTestCreateValidIds.buildStudyId(projectId))
        .surveyIds(surveyIds).title(I18nString.builder().de("Hurz").en("Hurz").build())
        .subtitle(I18nString.builder().de("Instrument Untertitel de").en("Instrument Subtitle en")
            .build())
        .description(I18nString.builder().de("Hurz").en("Hurz").build())
        .annotations(I18nString.builder().de("De Anmerkungen").en("En Annotations").build())
        .type("CAPI").number(1).surveyNumbers(surveyNumbers).build();
    instrument.setMasterId(instrumentId);
    return instrument;
  }

  public static InstrumentAttachmentMetadata buildInstrumentAttachmentMetadata(String projectId,
      Integer instrumentNumber) {
    return InstrumentAttachmentMetadata.builder().dataAcquisitionProjectId(projectId)
        .instrumentId(UnitTestCreateValidIds.buildInstrumentId(projectId, instrumentNumber))
        .instrumentNumber(instrumentNumber).fileName("filename.txt")
        .description(new I18nString("Beschreibung", "Description")).language("de")
        .type(InstrumentAttachmentTypes.QUESTION_FLOW).indexInInstrument(1).build();
  }

  public static SurveyAttachmentMetadata buildSurveyAttachmentMetadata(String projectId,
      Integer surveyNumber) {
    return SurveyAttachmentMetadata.builder().dataAcquisitionProjectId(projectId)
        .surveyId(UnitTestCreateValidIds.buildSurveyId(projectId, surveyNumber))
        .surveyNumber(surveyNumber).fileName("filename.txt")
        .description(new I18nString("Beschreibung", "Description")).title("Title").language("de")
        .indexInSurvey(1).build();
  }

  public static DataSetAttachmentMetadata buildDataSetAttachmentMetadata(String projectId,
      Integer dataSetNumber) {
    return DataSetAttachmentMetadata.builder().dataAcquisitionProjectId(projectId)
        .dataSetId(UnitTestCreateValidIds.buildDataSetId(projectId, dataSetNumber))
        .dataSetNumber(dataSetNumber).fileName("filename.txt")
        .description(new I18nString("Beschreibung", "Description")).title("Title").language("de")
        .indexInDataSet(1).build();
  }

  public static StudyAttachmentMetadata buildStudyAttachmentMetadata(String projectId) {
    return StudyAttachmentMetadata.builder().dataAcquisitionProjectId(projectId)
        .studyId(UnitTestCreateValidIds.buildStudyId(projectId)).fileName("filename.txt")
        .description(new I18nString("Beschreibung", "Description")).title("Title").language("de")
        .type(StudyAttachmentTypes.METHOD_REPORT).indexInStudy(1).build();
  }

  public static ConceptAttachmentMetadata buildConceptAttachmentMetadata(String conceptId) {
    return ConceptAttachmentMetadata.builder().conceptId(conceptId).fileName("filename.txt")
        .description(new I18nString("Beschreibung", "Description")).title("Title").language("de")
        .type(ConceptAttachmentTypes.DOCUMENTATION).indexInConcept(1).build();
  }

  public static RelatedQuestion buildRelatedQuestion(String projectId, String questionNumber,
      String instrumentNumber) {
    return RelatedQuestion.builder().instrumentNumber(instrumentNumber)
        .questionNumber(questionNumber)
        .instrumentId(
            UnitTestCreateValidIds.buildInstrumentId(projectId, Integer.parseInt(instrumentNumber)))
        .relatedQuestionStrings(I18nString.builder().de("Related Question String DE")
            .en("Related Question String EN").build())
        .build();
  }

  public static Person buildPerson(String firstName, String middleName, String lastName) {
    return Person.builder().firstName(firstName).middleName(middleName).lastName(lastName).build();
  }

  public static Concept buildConcept() {
    eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Tags tags =
        new eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Tags();
    tags.setDe(new HashSet<String>(Arrays.asList("Test-Tag")));
    tags.setEn(new HashSet<String>(Arrays.asList("Test-Tag")));
    List<Person> authors = new ArrayList<>();
    authors.add(buildPerson("Test", null, "Authors"));
    Set<String> originalLanguages = Sets.asSet("de", "en");

    return Concept.builder().id("con-conceptid$")
        .description(I18nString.builder().de("Beschreibung").en("Description").build())
        .title(I18nString.builder().de("Titel").en("Title").build()).tags(tags)
        .originalLanguages(originalLanguages).authors(authors).citationHint("Hurz").build();
  }
}
