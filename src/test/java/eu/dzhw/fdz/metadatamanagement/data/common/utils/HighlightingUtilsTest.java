/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.common.utils;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author Daniel Katzberg
 *
 */
public class HighlightingUtilsTest {
  
  @Test
  public void testEscapedHtml() {
    //Arrange
    
    //Act
    String escapedHtml = HighlightingUtils.escapeHtml("<html><body>Hello.</body></html>");
    
    //Assert
    assertThat(escapedHtml, is("&lt;html&gt;&lt;body&gt;Hello.&lt;/body&gt;&lt;/html&gt;"));
  }
  
  @Test
  public void testisHighlighted() {
    //Arrange
    HighlightingUtils highlightingUtils = new HighlightingUtils();
    Map<String, String> map = new HashMap<>();
    
    //Act
    map.put("Highlight1", "Value1");
    highlightingUtils.setHighlightedFields(map);
    
    //Assert
    assertThat(highlightingUtils.getHighlightedFields().size(), is(1));
    assertThat(highlightingUtils.getHighlightedFields().get("Highlight1"), is("Value1"));
    assertThat(highlightingUtils.getHighlightedFields().get("Highlight2"), is(nullValue()));
    assertThat(highlightingUtils.isFieldHighlighted("Highlight1"), is(true));
    assertThat(highlightingUtils.isFieldHighlighted("Highlight2"), is(false));
  }

}
