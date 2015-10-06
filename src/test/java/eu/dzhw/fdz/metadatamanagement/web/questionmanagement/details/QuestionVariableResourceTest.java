package eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionVariable;

/**
 * 
 * @author Amine limouri
 *
 */
public class QuestionVariableResourceTest {
  
  @Test
  public void testHashCode() {
   
    QuestionVariable questionVariable1 = new QuestionVariable();
    QuestionVariable questionVariable2 = new QuestionVariable();
  
    questionVariable1.setId("id");
    questionVariable2.setId("id2");
    
    QuestionVariableResource questionVariableResource =
        new QuestionVariableResource(questionVariable1);
    
    QuestionVariableResource questionVariableResource2 =
        new QuestionVariableResource(questionVariable2);

    int hashCode = questionVariableResource.hashCode();
    int hashCode2 = questionVariableResource2.hashCode();

    assertThat(hashCode, not(0));
    assertThat(hashCode2, not(0));
    assertThat(hashCode, not(hashCode2));
    assertThat(questionVariableResource.getQuestionVariable().getId(), is("id"));
    assertThat(questionVariableResource2.getQuestionVariable().getId(), is("id2"));
  }

  @Test
  public void testEquals() {
    
    QuestionVariable questionVariable1 = new QuestionVariable();
    QuestionVariable questionVariable2 = new QuestionVariable();
  
    questionVariable1.setId("id");
    questionVariable2.setId("id2");
    
    QuestionVariableResource questionVariableResource =
        new QuestionVariableResource(questionVariable1);
    
    QuestionVariableResource questionVariableResource2 =
        new QuestionVariableResource(questionVariable2);
   
    boolean checkSame = questionVariableResource.equals(questionVariableResource);
    boolean checkNull = questionVariableResource.equals(null);
    boolean checkOtherClass = questionVariableResource.equals(new Object());
    boolean checkPageOther = questionVariableResource.equals(questionVariableResource2);

    
    assertEquals(true, checkSame);
    assertEquals(false, checkNull);
    assertEquals(false, checkOtherClass);
    assertEquals(false, checkPageOther);
  }
}
