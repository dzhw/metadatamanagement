/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.hateoas.ResourceSupport;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * @author Daniel Katzberg
 *
 */
public class VariableResourceTest {
  
  @Test
  public void testHashCode(){
    //Arrange
    VariableResource variableResource = new VariableResource(null);
    
    //Act
    int hashCodeWithoutVariableDocument = variableResource.hashCode();
    variableResource = new VariableResource(new VariableDocument());
    int hashCodeWithVariableDocument = variableResource.hashCode();
        
    //Assert
    assertThat(hashCodeWithoutVariableDocument, not(0));
    assertThat(hashCodeWithVariableDocument, not(0));
  }
  
  @Test
  public void testEquals(){
    
    //Arrange
    VariableResource variableResource = new VariableResource(new VariableDocument());
    variableResource.getVariableDocument().setName("1");
    VariableResource variableResource2 = new VariableResource(new VariableDocument());
    variableResource2.getVariableDocument().setName("2");
        
    //Act
    boolean checkSame = variableResource.equals(variableResource);
    boolean checkNull = variableResource.equals(null);
    boolean checkOtherClass = variableResource.equals(new ResourceSupport());
    boolean checkVariableDocumentOther = variableResource.equals(variableResource2);
    variableResource2.getVariableDocument().setName("1");
    boolean checkVariableDocumentBothSame = variableResource.equals(variableResource2);
    
    //Assert
    assertEquals(true, checkSame);
    assertEquals(false, checkNull);
    assertEquals(false, checkOtherClass);    
    assertEquals(false, checkVariableDocumentOther);
    assertEquals(true, checkVariableDocumentBothSame);
    
  }

}
