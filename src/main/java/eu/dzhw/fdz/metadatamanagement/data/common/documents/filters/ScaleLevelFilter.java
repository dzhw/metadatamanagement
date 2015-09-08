package eu.dzhw.fdz.metadatamanagement.data.common.documents.filters;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.ValidScaleLevelValidator;

/**
 * The scale level filter represents a scale level button / checkbox at the web layer. It holds the
 * status and the value of the filter.
 * 
 * @author Daniel Katzberg
 *
 */
public class ScaleLevelFilter extends AbstractFilter {


  private ScaleLevelProvider scaleLevelProvider;

  /**
   * Default constructor, which initialized the filter by null values.
   */
  public ScaleLevelFilter(ScaleLevelProvider scaleLevelProvider) {
    super(false, null, 0L, VariableDocument.SCALE_LEVEL_FIELD);
    this.scaleLevelProvider = scaleLevelProvider;
  }

  /**
   * A constructor for setting all fields.
   * 
   * @param choosen is a filter choosen?
   * @param value the value of the filter.
   * @param name the name of the filter.
   */
  public ScaleLevelFilter(boolean choosen, String value, long docCount,
      ScaleLevelProvider scaleLevelProvider) {
    super(choosen, value, docCount, VariableDocument.SCALE_LEVEL_FIELD);
    this.scaleLevelProvider = scaleLevelProvider;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.common.documents.filter.AbstractFilter#isValid()
   */
  @Override
  public boolean isValid() {
    ValidScaleLevelValidator validScaleLevelValidator =
        new ValidScaleLevelValidator(this.scaleLevelProvider);
    return validScaleLevelValidator.isValid(this.getValue(), null);
  }

}
