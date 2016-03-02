/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.unittest.util;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.domain.AtomicQuestion;
import eu.dzhw.fdz.metadatamanagement.domain.BibliographicalReference;
import eu.dzhw.fdz.metadatamanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.domain.GenerationDetails;
import eu.dzhw.fdz.metadatamanagement.domain.Questionnaire;
import eu.dzhw.fdz.metadatamanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.domain.Statistics;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.domain.Value;
import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.domain.builders.AtomicQuestionBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.BibliographicalReferenceBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.ConceptBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.DataAcquisitionProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.DataSetBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.GenerationDetailsBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.PeriodBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.QuestionnaireBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.ReleaseBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.StatisticsBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.SurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.ValueBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.VariableBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.AccessWay;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.AtomicQuestionType;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.DataType;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.FilterExpressionLanguage;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.RuleExpressionLanguage;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.ScaleLevel;

/**
 * @author Daniel Katzberg
 *
 */
public class UnitTestCreateDomainObjectUtils {

  private UnitTestCreateDomainObjectUtils() {}

  public static DataAcquisitionProject buildDataAcquisitionProject() {

    List<Release> releases = new ArrayList<>();
    releases.add(buildRelease());

    return new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .withReleases(releases)
      .build();
  }

  public static Survey buildSurvey(String projectId) {
    return new SurveyBuilder().withId("testSurvey")
      .withDataAcquisitionProjectId(projectId)
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withTitle(new I18nStringBuilder().withDe("Titel")
        .withEn("title")
        .build())
      .withQuestionnaireId("QuestionnaireId")
      .build();
  }

  public static DataSet buildDataSet(String projectId, String surveyId) {
    List<String> variableIds = new ArrayList<>();
    variableIds.add("Id001");
    variableIds.add("Id002");
    variableIds.add("Id003");
    List<String> surveyIds = new ArrayList<>();
    variableIds.add(surveyId);
    return new DataSetBuilder().withSurveyIds(surveyIds)
      .withDataAcquisitionProjectId(projectId)
      .withId("DataSet_001")
      .withVariableIds(variableIds)
      .withDescription(new I18nStringBuilder().withDe("De Beschreibung")
        .withEn("En Description")
        .build())
      .build();
  }

  public static AtomicQuestion buildAtomicQuestion(String projectId, String questionnaireId,
      String variableId) {
    return new AtomicQuestionBuilder().withCompositeQuestionName("CompositeQuestioName")
      .withDataAcquisitionProjectId(projectId)
      .withFootnote(new I18nStringBuilder().withDe("De Fussnote")
        .withEn("En Fussnote")
        .build())
      .withId("testAtomicQuestion")
      .withInstruction(new I18nStringBuilder().withDe("De Instruktion")
        .withEn("En Instruction")
        .build())
      .withIntroduction(new I18nStringBuilder().withDe("De Einführung")
        .withEn("En Introduction")
        .build())
      .withName("Name")
      .withQuestionnaireId(questionnaireId)
      .withQuestionText(new I18nStringBuilder().withDe("De Frage")
        .withEn("En Question")
        .build())
      .withSectionHeader(new I18nStringBuilder().withDe("De Kapitelüberschrift")
        .withEn("En Section header")
        .build())
      .withType(AtomicQuestionType.open)
      .withVariableId(variableId)
      .build();
  }

  public static Variable buildVariable(String projectId, String surveyId) {

    // Prepare Variable
    List<AccessWay> accessWays = new ArrayList<>();
    accessWays.add(AccessWay.cuf);
    accessWays.add(AccessWay.remote);
    accessWays.add(AccessWay.suf);
    List<Variable> withSameVariablesInPanel = new ArrayList<>();
    List<Value> withValues = new ArrayList<>();
    withValues.add(buildValueBuilder());

    // Create Variable
    return new VariableBuilder().withId("testVariable")
      .withDataType(DataType.numeric)
      .withScaleLevel(ScaleLevel.continous)
      .withDataAcquisitionProjectId(projectId)
      .withSurveyId(surveyId)
      .withLabel(new I18nStringBuilder().withDe("label").withEn("label").build())
      .withName("name")
      .withAccessWays(accessWays)
      .withDescription(new I18nStringBuilder().withDe("De Beschreibung")
        .withEn("En Description")
        .build())
      .withFilterDescription(new I18nStringBuilder().withDe("De Filterbeschreibung")
        .withEn("En Filter Description")
        .build())
      .withFilterExpressionLanguage(FilterExpressionLanguage.stata)
      .withFilterExpression("Filter Expression")
      .withSameVariablesInPanel(withSameVariablesInPanel)
      .withValues(withValues)
      .withConceptId("ConceptId001")
      .withStatistics(buildStatistics())
      .withGenerationDetails(buildGenerationDetails())
      .build();
  }

  public static Concept buildConcept() {
    return new ConceptBuilder().withDescription(new I18nStringBuilder().withDe("De Beschreibung")
      .withEn("En Description")
      .build())
      .withId("ConceptId001")
      .withName(new I18nStringBuilder().withDe("Deutscher Name")
        .withEn("English name")
        .build())
      .build();
  }

  public static Questionnaire buildQuestionnaire(String projectId) {
    return new QuestionnaireBuilder().withDataAcquisitionProjectId(projectId)
      .withId("testQuestionnaire")
      .withSurveyId("testSurveyId")
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
      .withRuleExpressionLanguage(RuleExpressionLanguage.r)
      .build();
  }

  public static Value buildValueBuilder() {
    return new ValueBuilder().withAbsoluteFrequency(123)
      .withCode("Code 123.45")
      .withIsAMissing(false)
      .withLabel(new I18nStringBuilder().withDe("De Label")
        .withEn("En Lable")
        .build())
      .withRelativeFrequency(43.78)
      .build();
  }

  public static Release buildRelease() {
    return new ReleaseBuilder().withDoi("A Test Doi")
      .withNotes(new I18nStringBuilder().withDe("Eine Notiz für die Version 1.0")
        .withEn("A notice for the version 1.0.")
        .build())
      .withVersion("1.0")
      .withDate(ZonedDateTime.now())
      .build();
  }

  public static BibliographicalReference buildBibliographicalReference() {
    return new BibliographicalReferenceBuilder().withAuthor("Max Mustermann")
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
      .build();
  }

}
