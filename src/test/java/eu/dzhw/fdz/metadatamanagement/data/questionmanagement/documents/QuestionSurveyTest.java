/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.DateRangeBuilder;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.builders.QuestionSurveyBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class QuestionSurveyTest {

  @Test
  public void testEquals() {
    // Arrange
    QuestionSurvey questionSurvey = new QuestionSurveyBuilder().withSurveyId("surveyid")
        .withTitle("title").withSurveyPeriod(new DateRangeBuilder().build()).build();
    QuestionSurvey questionSurvey2 = new QuestionSurveyBuilder().withSurveyId("surveyid2")
        .withTitle("title2").withSurveyPeriod(new DateRangeBuilder().build()).build();

    // Act
    boolean checkNull = questionSurvey.equals(null);
    boolean checkDifferentClass = questionSurvey.equals(new Object());
    boolean checkDifferentVariableDocument = questionSurvey.equals(questionSurvey2);
    boolean checkSame = questionSurvey.equals(questionSurvey);

    // Assert
    assertEquals(false, checkNull);
    assertEquals(false, checkDifferentClass);
    assertEquals(false, checkDifferentVariableDocument);
    assertEquals(true, checkSame);

  }

  @Test
  public void testToString() {
    // Arrange
    QuestionSurvey questionSurvey = new QuestionSurveyBuilder().withSurveyId("surveyid")
        .withTitle("title").withSurveyPeriod(new DateRangeBuilder().build()).build();

    // Act

    // Assert
    assertThat(questionSurvey.toString(), is("QuestionSurvey{surveyId=surveyid, title=title, "
        + "surveyPeriod=DateRange{startDate=null, endDate=null}}"));
  }

  @Test
  public void testHashCode() {
    // Arrange
    QuestionSurvey questionSurvey = new QuestionSurveyBuilder().withSurveyId("surveyid")
        .withTitle("title").withSurveyPeriod(new DateRangeBuilder().build()).build();
    QuestionSurvey questionSurvey2 = new QuestionSurveyBuilder().withSurveyId("surveyid2")
        .withTitle("title2").withSurveyPeriod(new DateRangeBuilder().build()).build();

    // Act

    // Assert
    assertThat(questionSurvey.hashCode(), not(0));
    assertThat(questionSurvey2.hashCode(), not(0));
    assertThat(questionSurvey2.hashCode(), not(questionSurvey.hashCode()));
  }

}
