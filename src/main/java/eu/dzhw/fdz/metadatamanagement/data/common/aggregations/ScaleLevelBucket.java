package eu.dzhw.fdz.metadatamanagement.data.common.aggregations;

import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;

/**
 * This is a special kind of buckets, because the sorting is not depending on the docCount. The
 * scale level filter needs his own sorting.
 * 
 * @author Daniel Katzberg
 *
 */
public class ScaleLevelBucket extends Bucket {

  /**
   * ScaleLevel provider is need for the correct comparing of the scale level buckets.
   */
  private ScaleLevelProvider scaleLevelProvider;

  /**
   * Constructor of the scale level bucket. It needs a scale level provider a language independent
   * comparing.
   * 
   * @param key The key of the bucket is a term if used in term aggregations.
   * @param docCount The number of documents having the buckets key.
   * @param scaleLevelProvider A scale level provider for an language independent comparing of the
   *        scale level order
   */
  public ScaleLevelBucket(String key, long docCount, ScaleLevelProvider scaleLevelProvider) {
    super(key, docCount);
    this.scaleLevelProvider = scaleLevelProvider;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.common.aggregations.Bucket#compareTo(eu.dzhw.fdz.
   * metadatamanagement.data.common.aggregations.Bucket)
   */
  @Override
  public int compareTo(Bucket other) {

    int thisScaleLevelOrder = this.scaleLevelProvider.getScaleLevelOrder(this.getKey());
    int otherScaleLevelOrder = this.scaleLevelProvider.getScaleLevelOrder(other.getKey());

    return thisScaleLevelOrder - otherScaleLevelOrder;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.common.aggregations.Bucket#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (object != null && getClass() == object.getClass()) {
      if (!super.equals(object)) {
        return false;
      }
      ScaleLevelBucket that = (ScaleLevelBucket) object;
      return Objects.equal(this.getKey(), that.getKey());
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.common.aggregations.Bucket#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(this.getKey());
  }

}
