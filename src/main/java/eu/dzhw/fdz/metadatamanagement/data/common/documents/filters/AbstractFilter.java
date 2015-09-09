package eu.dzhw.fdz.metadatamanagement.data.common.documents.filters;

/**
 * This abstract class handles a generic filter with the status for the correct view on the web
 * layer.
 *  
 * @author Daniel Katzberg
 *
 */
public abstract class AbstractFilter {

  /**
   * Represent the value of a filter. It should be by default the name of the bucket.getKey()
   */
  private String value;

  /**
   * This is the name of the filter. For example VariableDocument.SCALE_LEVEL_FIELD
   */
  private String name;

  /**
   * The field for saving the number of found elements by the filter.
   */
  private long docCount;

  /**
   * A constructor for setting all fields.
   * 
   * @param value the value of the filter.
   * @param docCount The number of found results by the filter
   * @param name The name is the name of the filter. e.g. VariableDocument.SCALE_LEVEL_FIELD
   */
  public AbstractFilter(String value, long docCount, String name) {
    this.value = value;
    this.name = name;
    this.docCount = docCount;
  }

  /* GETTER / SETTER */
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getDocCount() {
    return docCount;
  }

  public void setDocCount(long docCount) {
    this.docCount = docCount;
  }
}
