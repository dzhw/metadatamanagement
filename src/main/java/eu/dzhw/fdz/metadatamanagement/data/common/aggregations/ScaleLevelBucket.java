package eu.dzhw.fdz.metadatamanagement.data.common.aggregations;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;

/**
 * This is a special kind of buckets, because the sorting is not depending on the docCount. The
 * scalelevel filter needs his own sorting.
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
    
    int thisScaleLevelOrder = this.scaleLevelProvider.castScaleLevelToInt(this.getKey());
    int otherScaleLevelOrder = this.scaleLevelProvider.castScaleLevelToInt(other.getKey());
        
    return thisScaleLevelOrder - otherScaleLevelOrder;
  }
  
  

}
