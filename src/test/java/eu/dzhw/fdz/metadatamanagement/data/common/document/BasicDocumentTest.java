/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.common.document;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.BasicDocument;

/**
 * @author Daniel Katzberg
 *
 */
public class BasicDocumentTest {

  @Test
  public void testHashCode() {
    // Arrange
    BasicDocument basicDocument = new BasicDocument();

    // Act
    int emptyIdHashCode = basicDocument.hashCode();
    basicDocument.setId("1");
    int withIdHashCode = basicDocument.hashCode();

    // Assert
    assertThat(emptyIdHashCode, not(0));
    assertThat(withIdHashCode, not(0));
  }

  @Test
  public void testEquals() {
    // Arrange
    BasicDocument basicDocument = new BasicDocument();
    BasicDocument abstractDocument2 = new BasicDocument();

    // Act
    boolean checkSame = basicDocument.equals(basicDocument);
    boolean checkNull = basicDocument.equals(null);
    boolean checkOtherClass = basicDocument.equals(new Object());
    boolean checkIdNone = basicDocument.equals(abstractDocument2);
    abstractDocument2.setId("2");
    boolean checkIdOther = basicDocument.equals(abstractDocument2);
    basicDocument.setId("1");
    boolean checkIdBoth = basicDocument.equals(abstractDocument2);
    basicDocument.setId("2");
    boolean checkIdBothSame = basicDocument.equals(abstractDocument2);

    // Assert
    assertEquals(true, checkSame);
    assertEquals(false, checkNull);
    assertEquals(false, checkOtherClass);
    assertEquals(true, checkIdNone);
    assertEquals(false, checkIdOther);
    assertEquals(false, checkIdBoth);
    assertEquals(true, checkIdBothSame);
  }
}
