/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.common.exceptions;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.web.welcome.WelcomeController;

/**
 * @author Daniel Katzberg
 *
 */
public class DocumentNotFoundResourceTest extends AbstractTest {

  private ExceptionPageResource documentNotFoundResource;
  private String unknownId;
  private DocumentNotFoundException documentNotFoundException;

  @Before
  public void beforeTest() {
    this.unknownId = "unknownId";
    this.documentNotFoundException = new DocumentNotFoundException(this.unknownId, Locale.GERMAN,
        VariableDocument.class);
    this.documentNotFoundResource = new ExceptionPageResource(documentNotFoundException,
        WelcomeController.class, new ControllerLinkBuilderFactory());
  }

  @Test
  public void testHashCode() {
    // Arrange

    // Act
    int hashCode = this.documentNotFoundResource.hashCode();

    // Assert
    assertThat(hashCode, not(0));
  }

  @Test
  public void testEquals() {
    // Arrange

    // Act
    boolean nullCheck = this.documentNotFoundResource.equals(null);
    boolean otherClassCheck = this.documentNotFoundResource.equals(new Object());
    boolean sameCheck = this.documentNotFoundResource.equals(this.documentNotFoundResource);
    boolean sameIdCheck = this.documentNotFoundResource
        .equals(new ExceptionPageResource(this.documentNotFoundException,
            WelcomeController.class, new ControllerLinkBuilderFactory()));

    DocumentNotFoundException documentNotFoundException2 = new DocumentNotFoundException(
        this.unknownId + "_other", Locale.GERMAN, VariableDocument.class);
    boolean differentIdCheck = this.documentNotFoundResource
        .equals(new ExceptionPageResource(documentNotFoundException2,
            WelcomeController.class, new ControllerLinkBuilderFactory()));

    // Assert
    assertThat(nullCheck, is(false));
    assertThat(otherClassCheck, is(false));
    assertThat(sameCheck, is(true));
    assertThat(sameIdCheck, is(true));
    assertThat(differentIdCheck, is(false));
  }

  @Test
  public void testResourceUnknownId() {
    // Arrange

    // Act
    String unknownId = this.documentNotFoundResource.getException().getUnknownId();

    // Assert
    assertThat(unknownId, is(this.unknownId));
  }

}
