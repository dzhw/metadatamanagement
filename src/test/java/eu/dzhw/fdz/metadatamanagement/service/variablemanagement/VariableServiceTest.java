package eu.dzhw.fdz.metadatamanagement.service.variablemanagement;

import static org.hamcrest.MatcherAssert.assertThat;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import eu.dzhw.fdz.metadatamanagement.MetaDataManagementApplicationSmokeTest;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.AnswerOption;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.provider.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.provider.ScaleLevelProvider;


/**
 * @author Daniel Katzberg
 */
public class VariableServiceTest extends MetaDataManagementApplicationSmokeTest {

  // TODO Depending on json test files
  @Autowired
  private VariableService variableService;

  @Test
  public void testSearchWithQuery() {
    // Arrange
    Pageable pageable = new PageRequest(0, 10);

    // Act

    // Assert
    assertThat(variableService.search("name", pageable).getSize(), is(10));

  }

  @Test
  public void testSearchWithoutQuery() {
    // Arrange
    Pageable pageable = new PageRequest(0, 10);

    // Act

    // Assert
    assertThat(variableService.search(null, pageable).getSize(), is(10));

  }

  @Test
  public void testSaveAndDelete() {
    String id = "FDZ_ID0000001";

    // Arrange
    // Variable Document
    LocaleContextHolder.setLocale(Locale.GERMAN);
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setId(id);
    variableDocument.setName("A name for a Document");
    variableDocument.setLabel("A label for a Document");
    variableDocument.setScaleLevel(ScaleLevelProvider.GERMAN_NOMINAL);
    variableDocument.setDataType(DataTypesProvider.GERMAN_NUMERIC);

    // Answer Options
    List<AnswerOption> answerOptions = new ArrayList<>();
    AnswerOption answerOption1 = new AnswerOption();
    answerOption1.setCode(1);
    answerOption1.setLabel("Label 1");
    AnswerOption answerOption2 = new AnswerOption();
    answerOption2.setCode(2);
    answerOption2.setLabel("Label 2");
    answerOptions.add(answerOption1);
    answerOptions.add(answerOption2);
    variableDocument.setAnswerOptions(answerOptions);

    // DateRange
    DateRange dateRange = new DateRange();
    dateRange.setStartDate(LocalDate.now());
    dateRange.setEndDate(LocalDate.now().plusDays(3));

    // Variable Survey
    VariableSurvey variableSurvey = new VariableSurvey();
    variableSurvey.setSurveyId("Survey_ID_1");
    variableSurvey.setTitle("Title 1");
    variableSurvey.setVariableAlias(variableDocument.getName());
    variableSurvey.setSurveyPeriod(dateRange);
    variableDocument.setVariableSurvey(variableSurvey);

    // Act
    VariableDocument savedVariableDocument = this.variableService.save(variableDocument);
    this.variableService.delete(id);

    Pageable pageable = new PageRequest(0, 10);
    Page<VariableDocument> results = this.variableService.search(id, pageable);
    int numberElements = results.getNumberOfElements();

    // Assert
    assertThat(savedVariableDocument, is(variableDocument));
    assertThat(savedVariableDocument, not(nullValue()));
    assertThat(numberElements, is(0));

  }
}
