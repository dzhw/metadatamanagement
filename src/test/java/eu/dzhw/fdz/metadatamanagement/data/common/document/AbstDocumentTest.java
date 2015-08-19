/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.common.document;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument;

/**
 * @author Daniel Katzberg
 *
 */
public class AbstDocumentTest {

  @Test
  public void testHashCode() {
    // Arrange
    AbstractDocument abstractDocument = new AbstractDocument();

    // Act
    int emptyIdHashCode = abstractDocument.hashCode();
    abstractDocument.setId("1");
    int withIdHashCode = abstractDocument.hashCode();

    // Assert
    assertEquals(31, emptyIdHashCode);
    assertEquals(80, withIdHashCode);
  }

  @Test
  public void testEquals() {
    // Arrange
    AbstractDocument abstractDocument = new AbstractDocument();
    AbstractDocument abstractDocument2 = new AbstractDocument();

    // Act
    boolean checkSame = abstractDocument.equals(abstractDocument);
    boolean checkNull = abstractDocument.equals(null);
    boolean checkOtherClass = abstractDocument.equals(new Object());
    boolean checkIdNone = abstractDocument.equals(abstractDocument2);
    abstractDocument2.setId("2");
    boolean checkIdOther = abstractDocument.equals(abstractDocument2);
    abstractDocument.setId("1");
    boolean checkIdBoth = abstractDocument.equals(abstractDocument2);
    abstractDocument.setId("2");
    boolean checkIdBothSame = abstractDocument.equals(abstractDocument2);

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
