package eu.dzhw.fdz.metadatamanagement.data.common.documents.filters;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * The scale level filter represents a scale level button / checkbox at the web layer. It holds the
 * status and the value of the filter.
 * 
 * @author Daniel Katzberg
 *
 */
public class ScaleLevelFilter extends AbstractFilter {

  /**
   * Default constructor, which initialized the filter by null values.
   */
  public ScaleLevelFilter() {
    super(null, 0L, VariableDocument.SCALE_LEVEL_FIELD);
  }

  /**
   * A constructor for setting all fields.
   * 
   * @param value the value of the filter.
   * @param name the name of the filter.
   */
  public ScaleLevelFilter(String value, long docCount) {
    super(value, docCount, VariableDocument.SCALE_LEVEL_FIELD);
  }
}
