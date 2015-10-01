/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.questionmanagement;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionDocument;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.builders.QuestionDocumentBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class QuestionResourceAssemblerTest extends AbstractTest {

  @Autowired
  QuestionResourceAssembler questionResourceAssembler;

  @Test
  public void testToResource() {
    // Arrange

    // Act
    QuestionResource questionResource = this.questionResourceAssembler
        .toResource(new QuestionDocumentBuilder().withId("id").build());

    // Arrange
    assertThat(questionResource, not(nullValue()));
    assertThat(questionResource.getQuestionDocument().getId(), is("id"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToResourceException() {
    // Arrange

    // Act
    // It should throw a exception
    this.questionResourceAssembler.toResource(new QuestionDocumentBuilder().build());

    // Arrange
  }
  
  @Test
  public void testInistantiateResource() {
    //Arrange
    QuestionDocument questionDocument = new QuestionDocumentBuilder().withId("id").build();
    
    //Act
    QuestionResource questionResource = this.questionResourceAssembler.instantiateResource(questionDocument);
    
    //Assert
    assertThat(questionResource, not(nullValue()));
    assertThat(questionResource.getQuestionDocument().getId(), is("id"));
  }

}
