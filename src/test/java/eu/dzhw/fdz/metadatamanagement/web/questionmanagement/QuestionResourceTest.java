/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.questionmanagement;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.builders.QuestionDocumentBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class QuestionResourceTest {

  @Test
  public void testHashCode() {
    // Arrange
    QuestionResource questionResource =
        new QuestionResource(new QuestionDocumentBuilder().withId("id").build());
    QuestionResource questionResource2 =
        new QuestionResource(new QuestionDocumentBuilder().withId("id2").build());

    // Act
    int hashCode = questionResource.hashCode();
    int hashCode2 = questionResource2.hashCode();

    // Assert
    assertThat(hashCode, not(0));
    assertThat(hashCode2, not(0));
    assertThat(hashCode, not(hashCode2));
    assertThat(questionResource.getQuestionDocument().getId(), is("id"));
    assertThat(questionResource2.getQuestionDocument().getId(), is("id2"));
  }

  @Test
  public void testEquals() {
    // Arrange
    QuestionResource questionResource =
        new QuestionResource(new QuestionDocumentBuilder().withId("id").build());
    QuestionResource questionResource2 =
        new QuestionResource(new QuestionDocumentBuilder().withId("id2").build());

    // Act
    boolean checkSame = questionResource.equals(questionResource);
    boolean checkNull = questionResource.equals(null);
    boolean checkOtherClass = questionResource.equals(new Object());
    boolean checkPageOther = questionResource.equals(questionResource2);

    // Assert
    assertEquals(true, checkSame);
    assertEquals(false, checkNull);
    assertEquals(false, checkOtherClass);
    assertEquals(false, checkPageOther);
  }

}
