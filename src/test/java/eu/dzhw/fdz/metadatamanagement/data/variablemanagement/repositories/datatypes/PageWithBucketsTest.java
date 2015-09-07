/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.datatypes;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mockito.Mockito;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * @author Daniel Katzberg
 *
 */
public class PageWithBucketsTest {

  @Test
  public void testHashCode() {

    // Arrange
    @SuppressWarnings("unchecked")
    PageWithBuckets<VariableDocument> pageWithBuckets = Mockito.mock(PageWithBuckets.class);

    // Act

    // Assert
    assertThat(pageWithBuckets.hashCode(), not(0));
  }

  @Test
  public void testEquals() {

    // Arrange
    @SuppressWarnings("unchecked")
    PageWithBuckets<VariableDocument> pageWithBuckets = Mockito.mock(PageWithBuckets.class);
    @SuppressWarnings("unchecked")
    PageWithBuckets<VariableDocument> pageWithBuckets2 = Mockito.mock(PageWithBuckets.class);

    // Act
    boolean checkNullObject = pageWithBuckets.equals(null);
    boolean checkSame = pageWithBuckets.equals(pageWithBuckets);
    boolean checkDifferentClass = pageWithBuckets.equals(new Object());
    boolean checkSurveyOther = pageWithBuckets.equals(pageWithBuckets2);

    // Assert
    assertEquals(false, checkNullObject);
    assertEquals(true, checkSame);
    assertEquals(false, checkDifferentClass);
    assertEquals(false, checkSurveyOther);
  }
}
