/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class VariableDetailsResourceTest extends AbstractWebTest {

  @Autowired
  private ControllerLinkBuilderFactory controllerLinkBuilderFactory;
  
  @Test
  public void testHashCode() {
    // Arrange
    VariableDetailsResource variableDetailResource =
        new VariableDetailsResource(this.controllerLinkBuilderFactory,
            new VariableResource(new VariableDocumentBuilder().withId("ID").build()));

    // Act
    int hashCodeWithVariableDocument = variableDetailResource.hashCode();

    // Assert
    assertThat(hashCodeWithVariableDocument, not(0));
  }

  @Test
  public void testEquals() {
    //Arrange
    VariableDetailsResource variableDetailResource =
        new VariableDetailsResource(this.controllerLinkBuilderFactory,
            new VariableResource(new VariableDocumentBuilder().withId("ID").build()));
    VariableDetailsResource variableDetailResource2 =
        new VariableDetailsResource(this.controllerLinkBuilderFactory,
            new VariableResource(new VariableDocumentBuilder().withId("ID2").build()));
    variableDetailResource2.setVariableResource(null);
    
    
    //Act
    boolean checkSame = variableDetailResource.equals(variableDetailResource);
    boolean checkNull = variableDetailResource.equals(null);
    boolean checkOtherClass = variableDetailResource.equals(new ResourceSupport());
    boolean checkVariableDocumentOther = variableDetailResource.equals(variableDetailResource2);
    
    //Assert
    assertEquals(true, checkSame);
    assertEquals(false, checkNull);
    assertEquals(false, checkOtherClass);    
    assertEquals(false, checkVariableDocumentOther);
  }

}
