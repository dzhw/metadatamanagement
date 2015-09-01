/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.common.exceptions;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class DocumentNotFoundResourceTest extends AbstractWebTest {

  private DocumentNotFoundResource documentNotFoundResource;
  private String unknownId;

  @Before
  public void beforeTest() {
    this.unknownId = "unknownId";
    this.documentNotFoundResource = new DocumentNotFoundResource(this.unknownId);
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
    boolean sameIdCheck =
        this.documentNotFoundResource.equals(new DocumentNotFoundResource(this.unknownId));
    boolean differentIdCheck = this.documentNotFoundResource
        .equals(new DocumentNotFoundResource(this.unknownId + "_Other"));

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
    String unknownId = this.documentNotFoundResource.getUnknownId();

    // Assert
    assertThat(unknownId, is(this.unknownId));
  }

}
