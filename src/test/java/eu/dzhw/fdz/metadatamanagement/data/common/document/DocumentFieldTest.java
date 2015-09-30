/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.common.document;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DocumentField;

/**
 * @author Daniel Katzberg
 *
 */
public class DocumentFieldTest {

  @Test
  public void testEquals() {
    //Arrange
    DocumentField documentField = new DocumentField("Path.to.this");
    DocumentField documentField2 = new DocumentField("Path.to.that");
    
    //Act
    boolean checkNull = documentField.equals(null);
    boolean checkDifferentClass = documentField.equals(new Object());
    boolean checkDifferentVariableDocument = documentField.equals(documentField2);
    boolean checkSame = documentField.equals(documentField);
    
    //Assert
    assertEquals(false, checkNull);
    assertEquals(false, checkDifferentClass);
    assertEquals(false, checkDifferentVariableDocument);
    assertEquals(true, checkSame);
  }

  @Test
  public void testHashCode() {
    // Arrange
    DocumentField documentField = new DocumentField("Path.to.this");
    DocumentField documentField2 = new DocumentField("Path.to.that");

    // Act

    // Assert
    assertThat(documentField.hashCode(), not(0));
    assertThat(documentField2.hashCode(), not(0));
    assertThat(documentField2.hashCode(), not(documentField.hashCode()));
  }

  @Test
  public void testToString() {
    // Arrange
    DocumentField documentField = new DocumentField("Path.to.this");

    // Act

    // Assert
    assertThat(documentField.toString(),
        is("DocumentField [absolutePath=Path.to.this, relativePath=this, parent=DocumentField "
            + "[absolutePath=Path.to, relativePath=to, parent=DocumentField "
            + "[absolutePath=Path, relativePath=Path, parent=null]]]"));
  }
}
