/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.RelatedVariable;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.DateRangeBuilder;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.RelatedVariableBuilder;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.builders.QuestionDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.builders.QuestionSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.validation.validators.QuestionDocumentCreateValidator;

/**
 * @author Daniel Katzberg
 *
 */
public class QuestionDocumentTest extends AbstractTest {

  @Autowired
  private QuestionDocumentCreateValidator questionDocumentCreateValidator;

  @Test
  public void testEmptyInvalidQuestionDocument() {
    // Arrange
    QuestionDocument questionDocument = new QuestionDocument();

    // Act
    Errors errors = new BeanPropertyBindingResult(questionDocument, "questionDocument");
    this.questionDocumentCreateValidator.validate(questionDocument, errors);

    // Assert
    assertEquals(7, errors.getErrorCount());
    assertThat(errors.getFieldError(QuestionDocument.NAME_FIELD.getAbsolutePath()).getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(errors.getFieldError(QuestionDocument.ID_FIELD.getAbsolutePath()).getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(errors.getFieldError(QuestionDocument.QUESTION_FIELD.getAbsolutePath()).getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(errors
        .getFieldError(
            QuestionDocument.NESTED_QUESTION_SURVEY_NESTED_PERIOD_END_DATE.getAbsolutePath())
        .getCode(), is(NotNull.class.getSimpleName()));
    assertThat(errors
        .getFieldError(
            QuestionDocument.NESTED_QUESTION_SURVEY_NESTED_PERIOD_START_DATE.getAbsolutePath())
        .getCode(), is(NotNull.class.getSimpleName()));
    assertThat(
        errors.getFieldError(QuestionDocument.NESTED_QUESTION_SURVEY_TITLE_FIELD.getAbsolutePath())
            .getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(
        errors.getFieldError(QuestionDocument.NESTED_QUESTION_SURVEY_ID_FIELD.getAbsolutePath())
            .getCode(),
        is(NotBlank.class.getSimpleName()));
  }

  @Test
  public void testMinimalValidQuestionDocument() {
    // Arrange
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    QuestionSurvey questionSurvey = new QuestionSurveyBuilder().withSurveyPeriod(dateRange)
        .withTitle("SurveyTitle").withSurveyId("SurveyID").build();
    QuestionDocument questionDocument = new QuestionDocumentBuilder().withId("TestID")
        .withName("Name").withQuestionSurvey(questionSurvey).withQuestion("Question").build();

    // Act
    Errors errors = new BeanPropertyBindingResult(questionDocument, "questionDocument");
    this.questionDocumentCreateValidator.validate(questionDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testInvalidName() {
    // Arrange
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    QuestionSurvey questionSurvey = new QuestionSurveyBuilder().withSurveyPeriod(dateRange)
        .withTitle("SurveyTitle").withSurveyId("SurveyID").build();
    QuestionDocument questionDocument = new QuestionDocumentBuilder().withId("TestID")
        .withName("ThisNameIsTooLongAndThrowAnException").withQuestionSurvey(questionSurvey)
        .withQuestion("Question").build();

    // Act
    Errors errors = new BeanPropertyBindingResult(questionDocument, "questionDocument");
    this.questionDocumentCreateValidator.validate(questionDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(QuestionDocument.NAME_FIELD.getAbsolutePath()).getCode(),
        is(Size.class.getSimpleName()));
  }

  @Test
  public void testInvalidId() {
    // Arrange
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    QuestionSurvey questionSurvey = new QuestionSurveyBuilder().withSurveyPeriod(dateRange)
        .withTitle("SurveyTitle").withSurveyId("SurveyID").build();
    QuestionDocument questionDocument = new QuestionDocumentBuilder().withId("This ID is not Okay")
        .withName("Name").withQuestion("Question").withQuestionSurvey(questionSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(questionDocument, "questionDocument");
    this.questionDocumentCreateValidator.validate(questionDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(QuestionDocument.ID_FIELD.getAbsolutePath()).getCode(),
        is(Pattern.class.getSimpleName()));
  }

  @Test
  public void testIdWithSigns() {
    // Arrange
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    QuestionSurvey questionSurvey = new QuestionSurveyBuilder().withSurveyPeriod(dateRange)
        .withTitle("SurveyTitle").withSurveyId("SurveyID").build();
    QuestionDocument questionDocument = new QuestionDocumentBuilder().withId("This-ID_is-okay")
        .withName("Name").withQuestionSurvey(questionSurvey).withQuestion("Question").build();

    // Act
    Errors errors = new BeanPropertyBindingResult(questionDocument, "questionDocument");
    this.questionDocumentCreateValidator.validate(questionDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testInvalidQuestion() {
    // Arrange
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    QuestionSurvey questionSurvey = new QuestionSurveyBuilder().withSurveyPeriod(dateRange)
        .withTitle("SurveyTitle").withSurveyId("SurveyID").build();
    QuestionDocument questionDocument = new QuestionDocumentBuilder().withId("Id").withName("Name")
        .withQuestionSurvey(questionSurvey)
        .withQuestion("Too Long Question. Too Long Question. Too Long Question. Too Long Question. "
            + "Too Long Question. Too Long Question. Too Long Question. Too Long Question. "
            + "Too Long Question. Too Long Question. Too Long Question. Too Long Question. "
            + "Too Long Question. Too Long Question. Too Long Question. Too Long Question. "
            + "Too Long Question. Too Long Question. Too Long Question. Too Long Question. ")
        .build();

    // Act
    Errors errors = new BeanPropertyBindingResult(questionDocument, "questionDocument");
    this.questionDocumentCreateValidator.validate(questionDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(QuestionDocument.QUESTION_FIELD.getAbsolutePath()).getCode(),
        is(Size.class.getSimpleName()));
  }

  @Test
  public void testInvalidRelatedVariablesList() {
    // Arrange
    RelatedVariable relatedVariable = new RelatedVariableBuilder().build();
    List<RelatedVariable> listRelatedVariable = new ArrayList<>();
    listRelatedVariable.add(relatedVariable);

    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    QuestionSurvey questionSurvey = new QuestionSurveyBuilder().withSurveyPeriod(dateRange)
        .withTitle("SurveyTitle").withSurveyId("SurveyID").build();
    QuestionDocument questionDocument = new QuestionDocumentBuilder().withId("TestID")
        .withName("Name").withQuestionSurvey(questionSurvey).withQuestion("Question")
        .withRelatedVariables(listRelatedVariable).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(questionDocument, "questionDocument");
    this.questionDocumentCreateValidator.validate(questionDocument, errors);

    // Assert
    assertEquals(3, errors.getErrorCount());
    assertThat(errors.getFieldError("relatedVariables[0].label").getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(errors.getFieldError("relatedVariables[0].id").getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(errors.getFieldError("relatedVariables[0].name").getCode(),
        is(NotBlank.class.getSimpleName()));
  }

  @Test
  public void testEquals() {
    // Arrange
    QuestionDocument questionDocument = new QuestionDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay").withQuestion("Question1")
        .withQuestionSurvey(new QuestionSurveyBuilder().build()).build();
    QuestionDocument questionDocument2 = new QuestionDocumentBuilder().build();

    // Act
    boolean checkNull = questionDocument.equals(null);
    boolean checkDifferentClass = questionDocument.equals(new Object());
    boolean checkDifferentVariableDocument = questionDocument.equals(questionDocument2);
    boolean checkSame = questionDocument.equals(questionDocument);
    questionDocument.setName("name");
    questionDocument2.setName("name");
    boolean checkDifferentVariableDocumentSameName = questionDocument.equals(questionDocument2);
    questionDocument.setQuestion("Question");
    questionDocument2.setQuestion("Question");
    boolean checkDifferentVariableDocumentSameQuestion = questionDocument.equals(questionDocument2);
    questionDocument.setRelatedVariables(new ArrayList<>());
    questionDocument2.setRelatedVariables(new ArrayList<>());
    boolean checkDifferentVariableDocumentSameVariableDocument =
        questionDocument.equals(questionDocument2);

    // Assert
    assertEquals(false, checkNull);
    assertEquals(false, checkDifferentClass);
    assertEquals(false, checkDifferentVariableDocument);
    assertEquals(true, checkSame);
    assertEquals(false, checkDifferentVariableDocumentSameName);
    assertEquals(false, checkDifferentVariableDocumentSameQuestion);
    assertEquals(false, checkDifferentVariableDocumentSameVariableDocument);
  }

  @Test
  public void testHashCode() {
    // Arrange
    QuestionDocument questionDocument = new QuestionDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay").withQuestion("Question1")
        .withQuestionSurvey(new QuestionSurveyBuilder().build()).build();
    QuestionDocument questionDocument2 = new QuestionDocumentBuilder().withId("ThisIDisOkay2")
        .withName("ThisNameIsOkay2").withQuestion("Question2").build();

    // Act

    // Assert
    assertThat(questionDocument.hashCode(), not(0));
    assertThat(questionDocument2.hashCode(), not(0));
    assertThat(questionDocument2.hashCode(), not(questionDocument.hashCode()));
  }

  @Test
  public void testToString() {
    // Arrange
    QuestionDocument questionDocument = new QuestionDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay").withQuestion("Question1")
        .withQuestionSurvey(new QuestionSurveyBuilder().build()).build();

    // Act

    // Assert
    assertThat(questionDocument.toString(),
        is("QuestionDocument{super=QuestionDocument{id=[ThisIDisOkay]}, "
            + "questionSurvey=QuestionSurvey{surveyId=null, title=null, surveyPeriod=DateRange{startDate=null, "
            + "endDate=null}}, question=Question1, name=ThisNameIsOkay, relatedVariables=[]}"));
  }

}
