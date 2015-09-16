package eu.dzhw.fdz.metadatamanagement.data.common.aggregations;

import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.data.common.CastTypesUtils;

/**
 * This class holds the result of an elasticsearch query with a term aggregation.
 * 
 * @author Daniel Katzberg
 *
 */
public class Bucket implements Comparable<Bucket> {

  /**
   * The key of the bucket is a term if used in term aggregations.
   */
  private String key;

  /**
   * The number of documents having the buckets key.
   */
  private long docCount;

  /**
   * A constructor for setting all fields.
   * 
   * @param key The key of the bucket is a term if used in term aggregations.
   * @param docCount The number of documents having the buckets key.
   */
  public Bucket(String key, long docCount) {
    this.key = key;
    this.docCount = docCount;
  }

  /**
   * Only the key is relevant for equality.
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(key);
  }

  /**
   * Only the key is relevant for equality.
   */
  @Override
  public boolean equals(Object object) {
    if (object instanceof Bucket) {
      Bucket that = (Bucket) object;
      return Objects.equal(this.key, that.key);
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(Bucket other) {

    // check again itself. TreeSet need a return value for equal items at the insertion.
    // This value is 0!
    // Ignore the same item.
    if (this.equals(other)) {
      return 0;
    }

    // check different buckets
    int compareValue = CastTypesUtils.castLongToInt(other.getDocCount() - this.docCount);

    // by the same docCount (return value = 0) on different buckets, it ignores the 'other' bucket
    // it is necessary to increase the value by one to get a definite order and do not ignoring
    // buckets with the same docCount.
    if (compareValue == 0) {
      return 1;
    }

    return compareValue;
  }

  /* GETTER / SETTER */
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public long getDocCount() {
    return docCount;
  }

  public void setDocCount(long docCount) {
    this.docCount = docCount;
  }
}
