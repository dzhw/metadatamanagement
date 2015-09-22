package eu.dzhw.fdz.metadatamanagement.data.common.documents;

import java.util.HashMap;
import java.util.Map;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.AggregationAndHighlightingResultMapper;

/**
 * This util class supports and adds highlighting by elastic search for specific fields.
 * @author Daniel Katzberg
 *
 */
public class HighlightingUtils {
  
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
  
  public boolean isHighlighted(String absolutePath) {
    return this.getHighlightedFields().get(absolutePath) != null;
  }

  /* GETTER / SETTER */
  public Map<String, String> getHighlightedFields() {
    return highlightedFields;
  }

  public void setHighlightedFields(Map<String, String> highlightedFields) {
    this.highlightedFields = highlightedFields;
  }
}
