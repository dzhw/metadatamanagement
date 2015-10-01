/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.builders.QuestionDocumentBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class QuestionDetailsResourceTest extends AbstractTest {
  
  @Autowired
  private ControllerLinkBuilderFactory controllerLinkBuilderFactory;
  

  @Test
  public void testHashCode() {
    // Arrange
    QuestionDetailsResource questionDetailsResource =
        new QuestionDetailsResource(this.controllerLinkBuilderFactory,
            new QuestionResource(new QuestionDocumentBuilder().withId("ID").build()));
    QuestionDetailsResource questionDetailsResource2 =
        new QuestionDetailsResource(this.controllerLinkBuilderFactory,
            new QuestionResource(new QuestionDocumentBuilder().withId("ID2").build()));;
    questionDetailsResource2.setQuestionResource(null);

    // Act
    int hashCodeWithVariableDocument = questionDetailsResource.hashCode();
    int hashCodeWithVariableDocument2 = questionDetailsResource2.hashCode();

    // Assert
    assertThat(hashCodeWithVariableDocument, not(0));
    assertThat(hashCodeWithVariableDocument2, not(0));
    assertThat(hashCodeWithVariableDocument, not(hashCodeWithVariableDocument2));
  }

  @Test
  public void testEquals() {
    //Arrange
    QuestionDetailsResource questionDetailsResource =
        new QuestionDetailsResource(this.controllerLinkBuilderFactory,
            new QuestionResource(new QuestionDocumentBuilder().withId("ID").build()));
    QuestionDetailsResource questionDetailsResource2 =
        new QuestionDetailsResource(this.controllerLinkBuilderFactory,
            new QuestionResource(new QuestionDocumentBuilder().withId("ID2").build()));
    questionDetailsResource2.setQuestionResource(null);
    
    
    //Act
    boolean checkSame = questionDetailsResource.equals(questionDetailsResource);
    boolean checkNull = questionDetailsResource.equals(null);
    boolean checkOtherClass = questionDetailsResource.equals(new ResourceSupport());
    boolean checkVariableDocumentOther = questionDetailsResource.equals(questionDetailsResource2);
    boolean checkVariableDocumentOtherNullResource = questionDetailsResource2.equals(questionDetailsResource);
    questionDetailsResource2.setQuestionResource(questionDetailsResource.getQuestionResource());
    boolean checkVariableDocumentOtherSameResource = questionDetailsResource.equals(questionDetailsResource2);
    questionDetailsResource2.setQuestionResource(null);
    questionDetailsResource.setQuestionResource(null);
    boolean checkVariableDocumentOtherBothNullResource = questionDetailsResource.equals(questionDetailsResource2);
    
    //Assert
    assertEquals(true, checkSame);
    assertEquals(false, checkNull);
    assertEquals(false, checkOtherClass);    
    assertEquals(false, checkVariableDocumentOther);
    assertEquals(false, checkVariableDocumentOtherNullResource);
    assertEquals(false, checkVariableDocumentOtherSameResource);
    assertEquals(false, checkVariableDocumentOtherBothNullResource);
  }

}
