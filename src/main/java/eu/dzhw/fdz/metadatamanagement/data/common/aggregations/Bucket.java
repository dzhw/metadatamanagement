package eu.dzhw.fdz.metadatamanagement.data.common.aggregations;

import com.google.common.base.Objects;

/**
 * This class holds the result of an elasticsearch query with a term aggregation.
 * 
 * @author Daniel Katzberg
 *
 */
public class Bucket {

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

  @Override
  // TODO docCount not in equals and hashCode?
  public int hashCode() {
    return Objects.hashCode(key);
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof Bucket) {
      Bucket that = (Bucket) object;
      return Objects.equal(this.key, that.key);
    }
    return false;
  }
}
