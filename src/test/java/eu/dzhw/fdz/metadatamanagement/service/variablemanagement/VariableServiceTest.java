package eu.dzhw.fdz.metadatamanagement.service.variablemanagement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.DateRangeBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.AnswerOption;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.AnswerOptionBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.datatype.PageWithBuckets;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;


/**
 * @author Daniel Katzberg
 */
public class VariableServiceTest extends AbstractWebTest {

  @Autowired
  private VariableService variableService;

  @Autowired
  private DataTypesProvider dataTypesProvider;

  @Autowired
  private ScaleLevelProvider scaleLevelProvider;

  @Test
  public void testSearchWithQueryGerman() {

    // Arrange
    Pageable pageable = new PageRequest(0, 10);
    LocaleContextHolder.setLocale(Locale.GERMAN);
    for (int i = 1; i <= 9; i++) {
      VariableSurvey variableSurvey = new VariableSurveyBuilder()
          .withSurveyId("SearchUnitTest_Survey_ID").withTitle("SearchUnitTestTitle 0" + i)
          .withVariableAlias("SearchUnitTestVariableAlias 0" + i).build();

      VariableDocument variableDocument = new VariableDocumentBuilder()
          .withId("SearchUnitTest_ID0" + i).withName("SearchUnitTestName 0" + i)
          .withLabel("SearchUnitTestLabel 0" + i).withQuestion("SearchUnitTestQuestion 0" + i)
          .withDataType(this.dataTypesProvider.getNumericValueByLocale())
          .withScaleLevel(this.scaleLevelProvider.getMetricByLocal())
          .withVariableSurvey(variableSurvey).build();
      this.variableService.save(variableDocument);
    }

    // Act
    PageWithBuckets<VariableDocument> result =
        this.variableService.search("SearchUnitTestName", null, pageable);

    // Assert
    assertThat(result.getNumberOfElements(), is(9));

    // Delete
    for (int i = 1; i <= 9; i++) {
      this.variableService.delete("SearchUnitTest_ID0" + i);
    }
  }

  @Test
  public void testSearchWithoutQueryEnglish() {
    // Arrange
    Pageable pageable = new PageRequest(0, 10);
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    for (int i = 1; i <= 9; i++) {
      VariableSurvey variableSurvey =
          new VariableSurveyBuilder().withSurveyId("SearchNullUnitTest_Survey_ID" + i)
              .withTitle("SearchNullUnitTestTitle 0" + i)
              .withVariableAlias("SearchNullUnitTestVariableAlias 0" + i).build();

      VariableDocument variableDocument =
          new VariableDocumentBuilder().withId("SearchNullUnitTest_ID0" + i)
              .withName("SearchNullUnitTestName 0" + i).withLabel("SearchNullUnitTestLabel 0" + i)
              .withQuestion("SearchNullUnitTestQuestion 0" + i)
              .withDataType(this.dataTypesProvider.getNumericValueByLocale())
              .withScaleLevel(this.scaleLevelProvider.getMetricByLocal())
              .withVariableSurvey(variableSurvey).build();
      this.variableService.save(variableDocument);
    }

    // Act
    PageWithBuckets<VariableDocument> result = variableService.search(null, null, pageable);

    // Assert
    assertThat(result.getNumberOfElements(), greaterThanOrEqualTo(9));

    // Delete
    for (int i = 1; i <= 9; i++) {
      this.variableService.delete("SearchNullUnitTest_ID0" + i);
    }
  }

  @Test
  public void testSaveAndDelete() {
    String idVariableDocument = "FDZ_ID0000001";

    // Arrange
    LocaleContextHolder.setLocale(Locale.GERMAN);

    // DateRange
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(3)).build();

    // Variable Survey
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID_1").withTitle("Title 1")
            .withVariableAlias("A name for a Document").withSurveyPeriod(dateRange).build();

    // Answer Options
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions.add(new AnswerOptionBuilder().withCode(1).withLabel("Label 1").build());
    answerOptions.add(new AnswerOptionBuilder().withCode(2).withLabel("Label 2").build());

    // Variable Document
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId(idVariableDocument).withName("A name for a Document")
            .withLabel("A label for a Document").withScaleLevel(ScaleLevelProvider.GERMAN_NOMINAL)
            .withDataType(DataTypesProvider.GERMAN_NUMERIC).withAnswerOptions(answerOptions)
            .withVariableSurvey(variableSurvey).build();

    // Act
    VariableDocument savedVariableDocument = this.variableService.save(variableDocument);
    this.variableService.delete(idVariableDocument);
    Pageable pageable = new PageRequest(0, 10);
    PageWithBuckets<VariableDocument> results =
        this.variableService.search(idVariableDocument, null, pageable);

    // Assert
    assertThat(savedVariableDocument, is(variableDocument));
    assertThat(savedVariableDocument, not(nullValue()));
    assertThat(results.getNumberOfElements(), is(0));

  }

  @Test
  public void testSearchWithQueryGermanAndFilterScaleLevel() {

    // Arrange
    Pageable pageable = new PageRequest(0, 10);
    LocaleContextHolder.setLocale(Locale.GERMAN);
    for (int i = 1; i <= 9; i++) {
      VariableSurvey variableSurvey = new VariableSurveyBuilder()
          .withSurveyId("SearchUnitTest_Survey_ID").withTitle("SearchUnitTestTitle 0" + i)
          .withVariableAlias("SearchUnitTestVariableAlias 0" + i).build();

      VariableDocument variableDocument = new VariableDocumentBuilder()
          .withId("SearchUnitTest_ID0" + i).withName("SearchUnitTestName 0" + i)
          .withLabel("SearchUnitTestLabel 0" + i).withQuestion("SearchUnitTestQuestion 0" + i)
          .withDataType(this.dataTypesProvider.getNumericValueByLocale())
          .withScaleLevel(this.scaleLevelProvider.getMetricByLocal())
          .withVariableSurvey(variableSurvey).build();
      this.variableService.save(variableDocument);
    }

    // Act
    PageWithBuckets<VariableDocument> resultOkay = this.variableService
        .search("SearchUnitTestName", ScaleLevelProvider.GERMAN_METRIC, pageable);
    
    // Assert
    assertThat(resultOkay.getNumberOfElements(), is(9));
    assertThat(resultOkay.getBuckets().get(0).getKey(), is(ScaleLevelProvider.GERMAN_METRIC));
    assertThat(resultOkay.getBuckets().get(0).getDocCount(), is(9L));

    // Delete
    for (int i = 1; i <= 9; i++) {
      this.variableService.delete("SearchUnitTest_ID0" + i);
    }
  }

  @Test
  public void testSearchWithoutQueryGermanButWithFilterScaleLevel() {

    // Arrange
    Pageable pageable = new PageRequest(0, 10);
    LocaleContextHolder.setLocale(Locale.GERMAN);
    for (int i = 1; i <= 9; i++) {
      VariableSurvey variableSurvey = new VariableSurveyBuilder()
          .withSurveyId("SearchUnitTest_Survey_ID").withTitle("SearchUnitTestTitle 0" + i)
          .withVariableAlias("SearchUnitTestVariableAlias 0" + i).build();

      VariableDocument variableDocument = new VariableDocumentBuilder()
          .withId("SearchUnitTest_ID0" + i).withName("SearchUnitTestName 0" + i)
          .withLabel("SearchUnitTestLabel 0" + i).withQuestion("SearchUnitTestQuestion 0" + i)
          .withDataType(this.dataTypesProvider.getNumericValueByLocale())
          .withScaleLevel(this.scaleLevelProvider.getOrdinalByLocal())
          .withVariableSurvey(variableSurvey).build();
      this.variableService.save(variableDocument);
    }

    // Act
    PageWithBuckets<VariableDocument> resultOkay =
        this.variableService.search(null, ScaleLevelProvider.GERMAN_ORDINAL, pageable);

    // Assert
    assertThat(resultOkay.getNumberOfElements(), is(9));
    assertThat(resultOkay.getBuckets().get(0).getKey(), is(ScaleLevelProvider.GERMAN_ORDINAL));
    assertThat(resultOkay.getBuckets().get(0).getDocCount(), is(9L));

    // Delete
    for (int i = 1; i <= 9; i++) {
      this.variableService.delete("SearchUnitTest_ID0" + i);
    }
  }


  @Test
  public void testSearchWithQueryGermanAndWrongFilterScaleLevel() {

    // Arrange
    Pageable pageable = new PageRequest(0, 10);
    LocaleContextHolder.setLocale(Locale.GERMAN);
    for (int i = 1; i <= 9; i++) {
      VariableSurvey variableSurvey = new VariableSurveyBuilder()
          .withSurveyId("SearchUnitTest_Survey_ID").withTitle("SearchUnitTestTitle 0" + i)
          .withVariableAlias("SearchUnitTestVariableAlias 0" + i).build();

      VariableDocument variableDocument = new VariableDocumentBuilder()
          .withId("SearchUnitTest_ID0" + i).withName("SearchUnitTestName 0" + i)
          .withLabel("SearchUnitTestLabel 0" + i).withQuestion("SearchUnitTestQuestion 0" + i)
          .withDataType(this.dataTypesProvider.getNumericValueByLocale())
          .withScaleLevel(this.scaleLevelProvider.getMetricByLocal())
          .withVariableSurvey(variableSurvey).build();
      this.variableService.save(variableDocument);
    }

    // Act
    PageWithBuckets<VariableDocument> resultNotOkay = this.variableService
        .search("SearchUnitTestName", ScaleLevelProvider.GERMAN_ORDINAL, pageable);

    // Assert
    assertThat(resultNotOkay.getNumberOfElements(), is(0));

    // Delete
    for (int i = 1; i <= 9; i++) {
      this.variableService.delete("SearchUnitTest_ID0" + i);
    }
  }
}
