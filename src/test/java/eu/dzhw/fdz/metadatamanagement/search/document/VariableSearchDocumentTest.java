/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.search.document;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Daniel Katzberg
 *
 */
public class VariableSearchDocumentTest {

  @Test
  public void testVariableSearchDocument() {

    // Arrange
    VariableSearchDocument document = new VariableSearchDocument();
    document.setDataType("numeric");
    document.setScaleLevel("nominal");
    document.setLabel("Label");
    document.setName("name");

    // Act

    // Assert
    assertThat(document.getDataType(), is("numeric"));
    assertThat(document.getScaleLevel(), is("nominal"));
    assertThat(document.getLabel(), is("Label"));
    assertThat(document.getName(), is("name"));
  }

}
