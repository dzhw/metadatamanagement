/**
 *
 */
package eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.citationmanagement.domain.Citation;
import eu.dzhw.fdz.metadatamanagement.citationmanagement.domain.builders.CitationBuilder;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImageType;
import eu.dzhw.fdz.metadatamanagement.common.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.common.domain.builders.PeriodBuilder;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.builders.DataSetBuilder;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.builders.InstrumentBuilder;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.builders.DataAcquisitionProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.builders.ReleaseBuilder;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionTypes;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.builders.QuestionBuilder;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.builders.StudyBuilder;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.builders.SurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.AccessWays;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Distribution;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.FilterDetails;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.FilterExpressionLanguages;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.GenerationDetails;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Histogram;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Missing;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RuleExpressionLanguages;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ScaleLevels;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Statistics;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ValidResponse;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders.DistributionBuilder;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders.FilterDetailsBuilder;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders.GenerationDetailsBuilder;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders.HistogramBuilder;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders.MissingBuilder;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders.StatisticsBuilder;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders.ValidResponseBuilder;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders.VariableBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class UnitTestCreateDomainObjectUtils {

  private UnitTestCreateDomainObjectUtils() {}

  public static DataAcquisitionProject buildDataAcquisitionProject() {

    String projectId = "testProject";

    return new DataAcquisitionProjectBuilder()
        .withId(projectId)
        .withStudyId(buildStudy(projectId).getId())
        .build();
  }
  
  public static Study buildStudy(String projectId) {
    
    List<Release> releases = new ArrayList<>();
    releases.add(buildRelease());
    
    return new StudyBuilder()
        .withId(projectId + "-TestStudie")
        .withAuthors("Test Author")
        .withCitationHint(new I18nStringBuilder()
            .withDe("Citation Hint De")
            .withEn("Citation Hint En")
            .build())
        .withDescripion(new I18nStringBuilder()
            .withDe("Description De")
            .withEn("Description En")
            .build())
        .withInstitution(new I18nStringBuilder()
            .withDe("Institution De")
            .withEn("Institution En")
            .build())
        .withSurveySeries(new I18nStringBuilder()
            .withDe("Survey Series De")
            .withEn("Survey Series En")
            .build())
        .withReleases(releases)
        .withSponsor(new I18nStringBuilder()
            .withDe("Sponsor De")
            .withEn("Sponsor En")
            .build())
        .withSurveySeries(new I18nStringBuilder()
            .withDe("Survey Series De")
            .withEn("Survey Series En")
            .build())
        .withTitle(new I18nStringBuilder()
            .withDe("Titel De")
            .withEn("Title En")
            .build())
        .build();    
  }

  public static Survey buildSurvey(String projectId) {
    return new SurveyBuilder().withId(projectId + "-sy1")
      .withDataAcquisitionProjectId(projectId)
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withTitle(new I18nStringBuilder().withDe("Titel")
        .withEn("title")
        .build())
      .withInstrumentId(projectId + "-Instrument")
      .withDataSetIds(Arrays.asList(projectId + "-ds1"))
      .build();
  }

  public static DataSet buildDataSet(String projectId, String surveyId) {
    List<String> variableIds = new ArrayList<>();
    variableIds.add("testProject-name1");
    variableIds.add("testProject-name2");
    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(surveyId);
    return new DataSetBuilder().withSurveyIds(surveyIds)
      .withDataAcquisitionProjectId(projectId)
      .withId(projectId + "-ds1")
      .withVariableIds(variableIds)
      .withSurveyIds(surveyIds)
      .withDescription(new I18nStringBuilder().withDe("De Beschreibung")
        .withEn("En Description")
        .build())
      .build();
  } 

  public static Variable buildVariable(String projectId, String surveyId) {

    // Prepare Variable
    List<String> accessWays = new ArrayList<>();
    accessWays.add(AccessWays.CUF);
    accessWays.add(AccessWays.REMOTE);
    accessWays.add(AccessWays.SUF);
    List<String> withSameVariablesInPanel = new ArrayList<>();
    withSameVariablesInPanel.add("testProject-name1");
    withSameVariablesInPanel.add("testProject-name2");
    String name = "name";

    // Create Variable
    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(surveyId);
    return new VariableBuilder().withId(projectId + "-" + name)
      .withDataType(DataTypes.NUMERIC)
      .withScaleLevel(ScaleLevels.CONTINOUS)
      .withDataAcquisitionProjectId(projectId)
      .withSurveyIds(surveyIds)
      .withDataSetIds(Arrays.asList(projectId + "-ds1"))
      .withLabel(new I18nStringBuilder().withDe("label")
        .withEn("label")
        .build())
      .withName(name)
      .withAccessWays(accessWays)
      .withDescription(new I18nStringBuilder().withDe("De Beschreibung")
        .withEn("En Description")
        .build())
      .withFilterDetails(new FilterDetailsBuilder()
        .withDescription(new I18nStringBuilder().withDe("De Filterbeschreibung")
            .withEn("En Filter Description")
            .build())
        .withExpressionLanguage(FilterExpressionLanguages.STATA)
        .withExpression("Filter Expression")
        .build())
      .withSameVariablesInPanel(withSameVariablesInPanel)
      .withDistribution(buildDistribution())
      .withGenerationDetails(buildGenerationDetails())
      .build();
  }
  
  public static Question buildQuestion(String projectId, String instrumentId, String variableId, String surveyId) {
    List<String> variableIds = new ArrayList<>();
    variableIds.add(variableId);
    
    return new QuestionBuilder().withDataAcquisitionProjectId(projectId)
      .withId(projectId + "-Question-1.1")
      .withAdditionalQuestionText(new I18nString("Zusätzlicher Fragetext", "Additional Question Text"))
      .withDataAcquisitionProjectId(projectId)
      .withImageType(ImageType.PNG)
      .withInstruction(new I18nString("Instruktionen", "Instruction"))
      .withInstrumentId(instrumentId)
      .withIntroduction(new I18nString("Einleitung", "Introduction"))
      .withNumber("123.12")
      .withPredecessor(new ArrayList<>())
      .withSuccessor(new ArrayList<>())
      .withQuestionText(new I18nString("Fragetext","Question text"))
      .withSurveyId(surveyId)
      .withTechnicalRepresentation("Technical representation")
      .withType(QuestionTypes.SINGLE_CHOICE)
      .withVariableIds(variableIds)
      .build();
  }
  
  public static Instrument buildInstrument(String projectId) {
    return new InstrumentBuilder().withDataAcquisitionProjectId(projectId)
      .withId(projectId + "-Instrument")
      .withSurveyId(projectId + "-sy1")
      .build();
  }

  public static Statistics buildStatistics() {
    return new StatisticsBuilder().withFirstQuartile(70.0)
      .withHighWhisker(130.0)
      .withKurtosis(234.0)
      .withLowWhisker(30.0)
      .withMaximum(140.0)
      .withMeanValue(87.5)
      .withMedian(90.0)
      .withMinimum(0.0)
      .withSkewness(123.0)
      .withStandardDeviation(40.0)
      .withThirdQuartile(110.0)
      .build();
  }

  public static GenerationDetails buildGenerationDetails() {
    return new GenerationDetailsBuilder()
      .withDescription(new I18nStringBuilder().withDe("De Beschreibung")
        .withEn("En Description")
        .build())
      .withRule("Rule 123 to 234")
      .withRuleExpressionLanguage(RuleExpressionLanguages.R)
      .build();
  }

  public static Missing buildMissing() {
    return new MissingBuilder().withAbsoluteFrequency(123)
      .withCode(1234)
      .withLabel(new I18nStringBuilder().withDe("De Label")
        .withEn("En Lable")
        .build())
      .withRelativeFrequency(43.78)
      .build();
  }

  public static ValidResponse buildValidResponse() {
    return new ValidResponseBuilder().withAbsoluteFrequency(123)
      .withLabel(new I18nStringBuilder().withDe("De Label")
        .withEn("En Lable")
        .build())
      .withValue("12.34")
      .withRelativeFrequency(43.78)
      .withValidRelativeFrequency(75.45)
      .build();
  }

  public static Histogram buildHistogram() {
    return new HistogramBuilder().withStart(0.0)
      .withEnd(10.0)
      .withNumberOfBins(10)
      .build();
  }

  public static Distribution buildDistribution() {
    List<Missing> missings = new ArrayList<>();
    missings.add(buildMissing());
    List<ValidResponse> validResponses = new ArrayList<>();
    validResponses.add(buildValidResponse());
    return new DistributionBuilder().withHistogram(buildHistogram())
      .withMissings(missings)
      .withValidResponses(validResponses)
      .withStatistics(buildStatistics())
      .withTotalAbsoluteFrequency(1234)
      .withTotalValidAbsoluteFrequency(1000)
      .withTotalValidRelativeFrequency(81.03)
      .build();
  }

  public static Release buildRelease() {
    return new ReleaseBuilder().withDoi("A Test Doi")
      .withNotes(new I18nStringBuilder().withDe("Eine Notiz für die Version 1.0")
        .withEn("A notice for the version 1.0.")
        .build())
      .withVersion("1.0")
      .withDate(LocalDateTime.now())
      .build();
  }

  public static Citation buildCitation() {
    return new CitationBuilder().withAuthor("Max Mustermann")
      .withBookTitle("Ein Testbuch fuer Testzwecke")
      .withChapter("Kapitel 1 - Der Test")
      .withEdition("Edition 1")
      .withEditor("Mara Mustermann")
      .withHowPublished("Gar nicht.")
      .withId("Reference001")
      .withInstitution("Institution Testbeispiel")
      .withJournal("Testjournal")
      .withNote("Eine Notiz")
      .withNumber("12 Number")
      .withOrganization("Testorganisation")
      .withPages("123")
      .withPublicationYear(2016)
      .withPublisher("Testverlag")
      .withSchool("Testschule")
      .withSeries("Eine Testserie")
      .withTitle("Der Titel vom Testbuch")
      .withType("Testtype")
      .withVolume("Volume 1")
      .withCitationString("Citation String")
      .withSourceReference("Source Reference")
      .build();
  }

  public static FilterDetails buildFilterDetails() {
    return new FilterDetailsBuilder()
      .withDescription(new I18nStringBuilder()
            .withDe("Eine Filterbeschreibung.")
            .withEn("A filter description.")
            .build())
      .withExpression("A Filter Expression")
      .withExpressionLanguage(FilterExpressionLanguages.STATA)
        .build();
  }
}
