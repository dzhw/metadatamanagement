/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.builders.QuestionDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.builders.QuestionSurveyBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class QuestionDocumentTest extends AbstractTest {

  @Test
  public void testEquals() {
    //Arrange
    QuestionDocument questionDocument = new QuestionDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay").withQuestion("Question1")
        .withQuestionSurvey(new QuestionSurveyBuilder().build()).build();
    QuestionDocument questionDocument2 = new QuestionDocumentBuilder().build();
    
    //Act
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
    questionDocument.setQuestionVariables(new ArrayList<>());
    questionDocument2.setQuestionVariables(new ArrayList<>());
    boolean checkDifferentVariableDocumentSameVariableDocument = questionDocument.equals(questionDocument2);
    
    //Assert
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
            + "endDate=null}}, question=Question1, name=ThisNameIsOkay, questionVariables=[]}"));
  }

}
