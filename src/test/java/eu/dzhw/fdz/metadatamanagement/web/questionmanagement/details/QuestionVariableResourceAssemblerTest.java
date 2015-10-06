package eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionVariable;

/**
 * 
 * @author Amine limouri
 *
 */
public class QuestionVariableResourceAssemblerTest extends AbstractTest {

  @Autowired
  QuestionVariableResourceAssembler questionVariableResourceAssembler;

  @Test
  public void testToResource() {
   
    QuestionVariable questionVariable = new QuestionVariable();
    questionVariable.setId("id");
    
    QuestionVariableResource questionVariableResource = this.questionVariableResourceAssembler
        .toResource(questionVariable);

    assertThat(questionVariableResource, not(nullValue()));
    assertThat(questionVariableResource.getQuestionVariable().getId(), is("id"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToResourceException() {
    
    this.questionVariableResourceAssembler.toResource(new QuestionVariable());
    
  }
  
  @Test
  public void testInistantiateResource() {
  
    QuestionVariable questionVariable = new QuestionVariable();
    questionVariable.setId("id");
    
    QuestionVariableResource questionVariableResource = this.questionVariableResourceAssembler.instantiateResource(questionVariable);
    
    assertThat(questionVariableResource, not(nullValue()));
    assertThat(questionVariableResource.getQuestionVariable().getId(), is("id"));
  }

}
