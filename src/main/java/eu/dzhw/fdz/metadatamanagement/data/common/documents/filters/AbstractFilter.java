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
   * The default value is false. Represent the status if a filter is choosen.
   */
  private boolean choosen;

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
   * @param choosen is a filter choosen?
   * @param value the value of the filter.
   * @param docCount The number of found results by the filter
   * @param name The name is the name of the filter. e.g. VariableDocument.SCALE_LEVEL_FIELD
   */
  public AbstractFilter(boolean choosen, String value, long docCount, String name) {
    this.choosen = choosen;
    this.value = value;
    this.name = name;
    this.docCount = docCount;
  }

  /**
   * The check of this.choosen is very important! This check holds the scaleLevel request parameter
   * in the url and the checkbox in the filter menu, even if there are no results.
   * 
   * @return If there are more than zero results or is choosen return true.
   */
  public boolean foundResults() {
    return this.choosen || this.docCount > 0L ;
  }

  /* GETTER / SETTER */
  public boolean isChoosen() {
    return choosen;
  }

  public void setChoosen(boolean choosen) {
    this.choosen = choosen;
  }

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
