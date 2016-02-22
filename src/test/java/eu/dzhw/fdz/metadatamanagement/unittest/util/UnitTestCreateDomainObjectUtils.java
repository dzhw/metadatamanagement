/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.unittest.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import eu.dzhw.fdz.metadatamanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.domain.builders.DataAcquisitionProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.DataSetBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.PeriodBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.SurveyBuilder;

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

}
