/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.provider.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.provider.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class VariablesRepositoryTest extends AbstractWebTest {

  @Autowired
  private VariableRepository variablesRepository;

  @Autowired
  private DataTypesProvider dataTypesProvider;

  @Autowired
  private ScaleLevelProvider scaleLevelProvider;
  
  @Test
  public void testMatchQueryInAllFieldEnglish() {

    // Arrange
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    for (int i = 1; i <= 9; i++) {
      VariableSurvey variableSurvey = new VariableSurveyBuilder()
          .withSurveyId("MatchQUnitTest_Survey_ID").withTitle("MatchQUnitTestTitle 0" + i)
          .withVariableAlias("MatchQUnitTestVariableAlias 0" + i).build();

      VariableDocument variableDocument = new VariableDocumentBuilder()
          .withId("MatchQUnitTest_ID0" + i).withName("MatchQUnitTestName 0" + i)
          .withLabel("MatchQUnitTestLabel 0" + i).withQuestion("MatchQUnitTestQuestion 0" + i)
          .withDataType(this.dataTypesProvider.getNumericValueByLocale())
          .withScaleLevel(this.scaleLevelProvider.getMetricByLocal())
          .withVariableSurvey(variableSurvey).build();
      this.variablesRepository.save(variableDocument);
    }

    // Act
    Page<VariableDocument> resultName =
        this.variablesRepository.matchQueryInAllField("MatchQUnitTestName", new PageRequest(0, 10));
    Page<VariableDocument> resultQuestion = this.variablesRepository
        .matchQueryInAllField("MatchQUnitTestQuestion", new PageRequest(0, 10));
    Page<VariableDocument> resultTitle = this.variablesRepository
        .matchQueryInAllField("MatchQUnitTestTitle", new PageRequest(0, 10));
    Page<VariableDocument> resultVariableAlias = this.variablesRepository
        .matchQueryInAllField("MatchQUnitTestVariableAlias", new PageRequest(0, 10));

    // AssertEquals
    assertThat(resultName.getNumberOfElements(), is(9));
    assertThat(resultQuestion.getNumberOfElements(), is(9));
    assertThat(resultTitle.getNumberOfElements(), is(9));
    assertThat(resultVariableAlias.getNumberOfElements(), is(9));

    // Delete
    for (int i = 1; i <= 9; i++) {
      this.variablesRepository.delete("MatchQUnitTest_ID0" + i);
    }
  }

  @Test
  public void testMatchPhrasePrefixQueryGerman() {

    // Arrange
    LocaleContextHolder.setLocale(Locale.GERMAN);
    for (int i = 1; i <= 9; i++) {
      VariableSurvey variableSurvey = new VariableSurveyBuilder()
          .withSurveyId("PrefixUnitTest_Survey_ID").withTitle("PrefixUnitTestTitle 0" + i)
          .withVariableAlias("PrefixUnitTestVariableAlias 0" + i).build();

      VariableDocument variableDocument = new VariableDocumentBuilder()
          .withId("PrefixUnitTest_ID0" + i).withName("PrefixUnitTestName 0" + i)
          .withLabel("PrefixUnitTestLabel 0" + i).withQuestion("PrefixUnitTestQuestion 0" + i)
          .withDataType(this.dataTypesProvider.getNumericValueByLocale())
          .withScaleLevel(this.scaleLevelProvider.getMetricByLocal())
          .withVariableSurvey(variableSurvey).build();
      this.variablesRepository.save(variableDocument);
    }

    // Act
    Page<VariableDocument> result = this.variablesRepository
        .matchPhrasePrefixQuery("PrefixUnitTest_ID01", new PageRequest(0, 20));

    // Assert
    assertThat(result.getNumberOfElements(), is(9));

    // Delete
    for (int i = 1; i <= 9; i++) {
      this.variablesRepository.delete("PrefixUnitTest_ID0" + i);
    }
  }

  @Test
  public void testMatchFilterBySurveyIdGerman() {

    // Arrange
    LocaleContextHolder.setLocale(Locale.GERMAN);
    for (int i = 1; i <= 9; i++) {
      VariableSurvey variableSurvey =
          new VariableSurveyBuilder().withSurveyId("SurveyFilterUnitTest_Survey_ID")
              .withTitle("SurveyFilterUnitTestTitle 0" + i)
              .withVariableAlias("SurveyFilterUnitTestVariableAlias 0" + i).build();

      VariableDocument variableDocument = new VariableDocumentBuilder()
          .withId("SurveyFilterUnitTest_ID0" + i).withName("SurveyFilterUnitTestName 0" + i)
          .withLabel("SurveyFilterUnitTestLabel 0" + i)
          .withQuestion("SurveyFilterUnitTestQuestion 0" + i)
          .withDataType(this.dataTypesProvider.getNumericValueByLocale())
          .withScaleLevel(this.scaleLevelProvider.getMetricByLocal())
          .withVariableSurvey(variableSurvey).build();
      this.variablesRepository.save(variableDocument);
    }

    // Act
    Page<VariableDocument> result =
        this.variablesRepository
        .filterBySurveyIdAndVariableAlias("SurveyFilterUnitTest_Survey_ID", 
            "SurveyFilterUnitTestVariableAlias 08");

    // Assert
    assertThat(result.getNumberOfElements(), is(1));

    // Delete
    for (int i = 1; i <= 9; i++) {
      this.variablesRepository.delete("SurveyFilterUnitTest_ID0" + i);
    }
  }

}
