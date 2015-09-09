package eu.dzhw.fdz.metadatamanagement.data.common.documents.filters;

import com.google.common.base.Objects;

/**
 * This abstract class handles a generic filter with the status for the correct view on the web
 * layer.
 * 
 * @author Daniel Katzberg
 *
 */
public class FilterBucket {

  /**
   * Represent the value of a filter. It should be by default the name of the bucket.getKey()
   */
  private String value;

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
  public FilterBucket(String value, long docCount) {
    this.value = value;
    this.docCount = docCount;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (object != null && getClass() == object.getClass()) {
      FilterBucket that = (FilterBucket) object;
      return Objects.equal(this.value, that.value);
    }
    return false;
  }

  /* GETTER / SETTER */
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public long getDocCount() {
    return docCount;
  }

  public void setDocCount(long docCount) {
    this.docCount = docCount;
  }
}
