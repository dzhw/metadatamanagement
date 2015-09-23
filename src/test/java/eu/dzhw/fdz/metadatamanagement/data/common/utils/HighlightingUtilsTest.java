/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.common.utils;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Daniel Katzberg
 *
 */
public class HighlightingUtilsTest {
  
  @Test
  public void testEscapedHtml() {
    //Arrange
    HighlightingUtils highlightingUtils = new HighlightingUtils();
    
    //Act
    String escapedHtml = HighlightingUtils.escapeHtml("<html><body>Hello.</body></html>");
    
    //Assert
    assertThat(highlightingUtils, is(notNullValue()));
    assertThat(escapedHtml, is("&lt;html&gt;&lt;body&gt;Hello.&lt;/body&gt;&lt;/html&gt;"));
  }
}
