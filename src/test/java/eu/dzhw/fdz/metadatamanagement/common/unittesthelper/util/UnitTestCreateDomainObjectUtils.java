/**
 *
 */
package eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImageType;
import eu.dzhw.fdz.metadatamanagement.common.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.common.domain.builders.PeriodBuilder;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetTypes;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.SubDataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.builders.DataSetBuilder;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.builders.SubDataSetBuilder;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentTypes;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.builders.InstrumentAttachmentMetadataBuilder;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.builders.InstrumentBuilder;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.builders.DataAcquisitionProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.builders.ReleaseBuilder;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionTypes;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.TechnicalRepresentation;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.builders.QuestionBuilder;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.builders.TechnicalRepresentationBuilder;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.builders.RelatedPublicationBuilder;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.DataAvailabilities;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.SurveyDesigns;
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
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RelatedQuestion;
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
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders.RelatedQuestionBuilder;
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
        .build();
  }
  
  public static Study buildStudy(String projectId) {
    
    List<Release> releases = new ArrayList<>();
    releases.add(buildRelease());
    
    List<String> accessWays = new ArrayList<>();
    accessWays.add(AccessWays.DOWNLOAD_CUF);
    accessWays.add(AccessWays.REMOTE_DESKTOP);
    accessWays.add(AccessWays.DOWNLOAD_SUF);
    accessWays.add(AccessWays.ONSITE_SUF);
    
    return new StudyBuilder()
        .withId(projectId)
        .withAuthors("Test Author")
        .withCitationHint(new I18nStringBuilder()
            .withDe("Citation Hint De")
            .withEn("Citation Hint En")
            .build())
        .withDescription(new I18nStringBuilder()
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
        .withDataAvailability(DataAvailabilities.AVAILABLE)
        .withSurveyDesign(SurveyDesigns.PANEL)
        .withAccessWays(accessWays)
        .withDataAcquisitionProjectId(projectId)
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
      .withPopulation(new I18nStringBuilder().withDe("Population DE")
          .withEn("Population EN")
          .build())
      .withSample(new I18nStringBuilder().withDe("Sample DE")
        .withEn("Sample EN")
        .build())
      .withSurveyMethod(new I18nStringBuilder().withDe("Survey Method DE")
          .withEn("Survey Method EN")
          .build())
      .withGrossSampleSize(100)
      .withSampleSize(100)
      .withResponseRate(100.0)
      .withNumber(1)
      .build();
  }

  public static DataSet buildDataSet(String projectId, String surveyId, Integer surveyNumber) {
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(surveyNumber);
    
    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(surveyId);
        
    List<SubDataSet> subDataSets = new ArrayList<>(); 
    subDataSets.add(new SubDataSetBuilder().withName(projectId + "-ds1")
        .withNumberOfAnalyzedVariables(1)
        .withNumberOfObservations(1)
        .withAccessWay(AccessWays.DOWNLOAD_SUF) 
        .withDescription(new I18nStringBuilder().withDe("Description DE")
          .withEn("Description 3 EN")
          .build()).build());
    subDataSets.add(new SubDataSetBuilder().withName(projectId + "-ds2")
        .withNumberOfAnalyzedVariables(1)
        .withNumberOfObservations(1)
        .withAccessWay(AccessWays.REMOTE_DESKTOP) 
        .withDescription(new I18nStringBuilder().withDe("Description 2 DE")
          .withEn("Description 3 EN")
          .build()).build());
    subDataSets.add(new SubDataSetBuilder().withName(projectId + "-ds3")
        .withNumberOfAnalyzedVariables(1)
        .withNumberOfObservations(1)
        .withAccessWay(AccessWays.DOWNLOAD_CUF) 
        .withDescription(new I18nStringBuilder().withDe("Description 3 DE")
          .withEn("Description 3 EN")
          .build()).build());
    subDataSets.add(new SubDataSetBuilder().withName(projectId + "-ds4")
        .withNumberOfAnalyzedVariables(1)
        .withNumberOfObservations(1)
        .withAccessWay(AccessWays.ONSITE_SUF) 
        .withDescription(new I18nStringBuilder().withDe("Description 4 DE")
          .withEn("Description 4 EN")
          .build()).build());
    
    return new DataSetBuilder().withSurveyIds(surveyIds)
      .withDataAcquisitionProjectId(projectId)
      .withId(projectId + "-ds1")
      .withSurveyNumbers(surveyNumbers)
      .withSurveyIds(surveyIds)
      .withNumber(1)
      .withType(DataSetTypes.PERSONAL_RECORD)
      .withDescription(new I18nStringBuilder().withDe("De Beschreibung")
        .withEn("En Description")
        .build())
      .withSubDataSets(subDataSets)
      .build();
  } 

  public static Variable buildVariable(String projectId, Integer dataSetNumber, String name, Integer index, List<Integer> surveyNumbers) {

    // Prepare Variable
    List<String> accessWays = new ArrayList<>(); 
    accessWays.add(AccessWays.DOWNLOAD_CUF);
    accessWays.add(AccessWays.REMOTE_DESKTOP);
    accessWays.add(AccessWays.DOWNLOAD_SUF);
    accessWays.add(AccessWays.ONSITE_SUF);
    
    List<String> relatedVariables = new ArrayList<>();
    relatedVariables.add(projectId + "-ds" + dataSetNumber + "-name3");   
    
    List<RelatedQuestion> relatedQuestions = new ArrayList<>();
    relatedQuestions.add(buildRelatedQuestion(projectId, "1", "1"));
    return new VariableBuilder().withId(projectId + "-ds" + dataSetNumber + "-" + name)
      .withDataType(DataTypes.NUMERIC)
      .withScaleLevel(ScaleLevels.CONTINOUS)
      .withDataAcquisitionProjectId(projectId)
      .withName(name)
      .withDataSetId(projectId + "-ds" + dataSetNumber)
      .withLabel(new I18nStringBuilder().withDe("label")
          .withEn("label")
          .build())
      .withAccessWays(accessWays)
      .withSurveyNumbers(surveyNumbers)
      .withIndexInDataSet(index)
      .withDataSetNumber(dataSetNumber)
      .withRelatedQuestions(relatedQuestions)
      .withAnnotations(new I18nStringBuilder().withDe("De Beschreibung")
        .withEn("En Description")
        .build())
      .withFilterDetails(new FilterDetailsBuilder()
        .withDescription(new I18nStringBuilder().withDe("De Filterbeschreibung")
            .withEn("En Filter Description")
            .build())
        .withExpressionLanguage(FilterExpressionLanguages.STATA)
        .withExpression("Filter Expression")
        .build())
      .withDistribution(buildDistribution())
      .withGenerationDetails(buildGenerationDetails())
      .withRelatedVariables(relatedVariables)
      .build();
  }
  
  public static TechnicalRepresentation buildTechnicalRepresentation() {
    return new TechnicalRepresentationBuilder()
        .withLanguage("TR Language")
        .withType("Technical Representation Type")
        .withSource("Technical Representation Source")
        .build();
  }
  
  public static Question buildQuestion(String projectId, Integer instrumentNumber, 
      String instrumentId, String surveyId) {
    return new QuestionBuilder().withDataAcquisitionProjectId(projectId)
      .withId(projectId +"-ins" + instrumentNumber + "-123.12")
      .withAdditionalQuestionText(new I18nString("Zusätzlicher Fragetext", "Additional Question Text"))
      .withDataAcquisitionProjectId(projectId)
      .withImageType(ImageType.PNG)
      .withInstruction(new I18nString("Instruktionen", "Instruction"))
      .withInstrumentId(instrumentId)
      .withIntroduction(new I18nString("Einleitung", "Introduction"))
      .withNumber("123.12")
      .withSuccessors(new ArrayList<>())
      .withQuestionText(new I18nString("Fragetext","Question text"))
      .withTechnicalRepresentation(buildTechnicalRepresentation())
      .withType(QuestionTypes.SINGLE_CHOICE)
      .withTopic(new I18nString("Topic De", "Topic EN"))
      .withInstrumentNumber(instrumentNumber)
      .withSuccessorNumbers(new ArrayList<>())
      .build();
  }
  
  public static Instrument buildInstrument(String projectId) {
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(projectId + "-sy1");
    return new InstrumentBuilder().withDataAcquisitionProjectId(projectId)
      .withId(projectId + "-ins1")
      .withTitle(new I18nStringBuilder()
          .withDe("Instrument.de")
          .withEn("Instrument.en")
          .build())
      .withDescription(new I18nStringBuilder()
          .withDe("Instrument.de")
          .withEn("Instrument.en")
          .build())
      .withSurveyIds(surveyIds)
      .withDataAcquisitionProjectId(projectId)
      .withType("CAPI")
      .withNumber(1)
      .withSurveyNumbers(surveyNumbers)
      .build();
  }

  public static Statistics buildStatistics() {
    return new StatisticsBuilder().withFirstQuartile("70.0")
      .withHighWhisker(130.0)
      .withKurtosis(234.0)
      .withLowWhisker(30.0)
      .withMaximum("140.0")
      .withMeanValue(87.5)
      .withMedian("90.0")
      .withMinimum("0.0")
      .withSkewness(123.0)
      .withStandardDeviation(40.0)
      .withThirdQuartile("110.0")
      .withMode("Mode")
      .withDeviance(12.4)
      .withMeanDeviation(58.7)
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
  
  public static RelatedPublication buildRelatedPublication() {
    
    List<String> studyIds = new ArrayList<>();
    studyIds.add("Study-IdExample");
    
    return new RelatedPublicationBuilder()
        .withDoi("A DOI")
        .withId("HurzId123")
        .withPublicationAbstract("A publication Abstract")
        .withSourceLink("http://www.hurzexample.de/")
        .withSourceReference("A Source Reference")
        .withTitle("A Title of a Related Publication")
        .withStudyIds(studyIds)
        .withAuthors("Author")
        .withYear(2017)
        .withAbstractSource(new I18nStringBuilder()
            .withDe("Test")
            .withEn("Test")
            .build())
        .build();
    
  }

  public static Instrument buildInstrument(
      String projectId, String surveyId) {
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(surveyId);
    return new InstrumentBuilder()
        .withId(projectId + "-ins1")
        .withDataAcquisitionProjectId(projectId)
        .withSurveyIds(surveyIds)
        .withTitle(new I18nStringBuilder()
            .withDe("Hurz")
            .withEn("Hurz")
            .build())
        .withDescription(new I18nStringBuilder()
            .withDe("Hurz")
            .withEn("Hurz")
            .build())
        .withType("CAPI")
        .withNumber(1)
        .withSurveyNumbers(surveyNumbers)
        .build();
  }
  
  public static InstrumentAttachmentMetadata buildInstrumentAttachmentMetadata(String projectId, Integer instrumentNumber) {
    return new InstrumentAttachmentMetadataBuilder()
          .withDataAcquisitionProjectId(projectId)
          .withInstrumentId(projectId + "-ins" + instrumentNumber)
          .withInstrumentNumber(instrumentNumber)
          .withFileName("filename.txt")
          .withTitle(new I18nString("Titel", "Title"))
          .withType(InstrumentAttachmentTypes.QUESTION_FLOW)
          .build();
  }
  
  public static RelatedQuestion buildRelatedQuestion(String projectId, String questionNumber, String instrumentNumber) {
    return new RelatedQuestionBuilder()
        .withInstrumentNumber(instrumentNumber)
        .withQuestionNumber(questionNumber)
        .withInstrumentId(projectId + "-ins" + instrumentNumber)
        .withInstrumentNumber(projectId + "-ins" + instrumentNumber + "-" +questionNumber)
        .withRelatedQuestionStrings(new I18nStringBuilder()
            .withDe("Related Question String DE")
            .withEn("Related Question String EN")
            .build())
        .build();
  }
}
