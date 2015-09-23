package eu.dzhw.fdz.metadatamanagement.data.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.util.HtmlUtils;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.AggregationAndHighlightingResultMapper;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.DocumentField;

/**
 * This util class supports and adds highlighting by elastic search for specific fields.
 * 
 * @author Daniel Katzberg
 *
 */
public class HighlightingUtils {

  /**
   * The defined html open tag for an highlighted part.
   */
  public static final String HIGHLIGHTING_OPEN_TAG = "<em>";

  /**
   * The defined html close tag for an highlighted part.
   */
  public static final String HIGHLIGHTING_CLOSE_TAG = "</em>";

  /**
   * This map values are highlighted elements by elastic search. This maps will be set by the
   * Resultmapper ({@link AggregationAndHighlightingResultMapper}.
   */
  private Map<String, String> highlightedFields;

  /**
   * Basic Constructor.
   */
  public HighlightingUtils() {
    this.highlightedFields = new HashMap<>();
  }

  /**
   * @param unescapedHtml a string element with no escaped html.
   * @return The escaped html but the the highlighted tags
   */
  public static String escapeHtml(String unescapedHtml) {
    StringBuffer stringBuffer = new StringBuffer();

    while (unescapedHtml.length() > 0) {
      // check for highlight tags. No more Tags? -> finish
      if (!unescapedHtml.contains(HIGHLIGHTING_OPEN_TAG)) {
        stringBuffer.append(HtmlUtils.htmlEscape(unescapedHtml));
        break; 
      }

      // Open Tag, the returns string is a shorted unescaped html. the escaped part is excluded.
      unescapedHtml =
          HighlightingUtils.escapeHtmlHelper(unescapedHtml, HIGHLIGHTING_OPEN_TAG, stringBuffer);

      // Close Tag the returns string is a shorted unescaped html. the escaped part is excluded.
      unescapedHtml =
          HighlightingUtils.escapeHtmlHelper(unescapedHtml, HIGHLIGHTING_CLOSE_TAG, stringBuffer);
    }

    return stringBuffer.toString();
  }

  /**
   * @param unescapedHtml The complete unescaped html element.
   * @param tag check for the open or close tag. (given by this static constants in this class)
   * @param stringBuffer the actual used string buffer
   * @return the shorten and updated unescaped html element.
   */
  private static String escapeHtmlHelper(String unescapedHtml, String tag,
      StringBuffer stringBuffer) {
    int indexOpenTag = unescapedHtml.indexOf(tag);
    if (indexOpenTag > 0) {
      String unescapedHtmlPart = unescapedHtml.substring(0, indexOpenTag);
      stringBuffer.append(HtmlUtils.htmlEscape(unescapedHtmlPart));
    }
    stringBuffer.append(HighlightingUtils.replaceTags(HtmlUtils.htmlEscape(tag)));
    return unescapedHtml.substring(indexOpenTag + tag.length());
  }

  /**
   * This method is called only at the position wehre the open / close highlighting tag will be
   * insert in the escaped html.
   * 
   * @param escapedHtml the escaped html with the wrong em tags
   * @return Overwrites the &lt; and &gt; elements again the html tags signs.
   */
  private static String replaceTags(String escapedHtml) {
    escapedHtml = escapedHtml.replaceAll("&lt;", "<");
    return escapedHtml.replaceAll("&gt;", ">");
  }

  /**
   * @param absolutePath The absolute path of a document field
   * @return if there is a highlight variant of the value of a document field.
   * @see DocumentField
   */
  public boolean isFieldHighlighted(String absolutePath) {
    return this.getHighlightedFields().containsKey(absolutePath);
  }

  /* GETTER / SETTER */
  public Map<String, String> getHighlightedFields() {
    return highlightedFields;
  }

  public void setHighlightedFields(Map<String, String> highlightedFields) {
    this.highlightedFields = highlightedFields;
  }
}
