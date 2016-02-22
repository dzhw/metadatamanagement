/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.unittest.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.domain.AtomicQuestion;
import eu.dzhw.fdz.metadatamanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.domain.Questionnaire;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.domain.Value;
import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.domain.builders.AtomicQuestionBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.DataAcquisitionProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.DataSetBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.PeriodBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.QuestionnaireBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.SurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.ValueBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.VariableBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.AccessWay;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.AtomicQuestionType;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.DataType;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.FilterExpressionLanguage;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.ScaleLevel;

/**
 * @author Daniel Katzberg
 *
 */
public class UnitTestCreateDomainObjectUtils {

  private UnitTestCreateDomainObjectUtils() {}

  public static DataAcquisitionProject buildDataAcquisitionProject() {
    return new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
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
    return new DataSetBuilder().withSurveyId(surveyId)
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
    withValues.add(new ValueBuilder().withAbsoluteFrequency(123)
      .withCode("Code 123.45")
      .withIsAMissing(false)
      .withLabel(new I18nStringBuilder().withDe("De Label")
        .withEn("En Lable")
        .build())
      .withRelativeFrequency(43.78)
      .build());

    // Create Variable
    return new VariableBuilder().withId("testVariable")
      .withDataType(DataType.numeric)
      .withScaleLevel(ScaleLevel.continous)
      .withDataAcquisitionProjectId(projectId)
      .withSurveyId(surveyId)
      .withLabel("label")
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
      .build();
  }

  public static Questionnaire buildQuestionnaire(String projectId) {
    return new QuestionnaireBuilder().withDataAcquisitionProjectId(projectId)
      .withId("testQuestionnaire")
      .build();
  }

}
