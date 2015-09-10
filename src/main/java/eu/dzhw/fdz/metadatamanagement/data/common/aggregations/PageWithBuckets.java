package eu.dzhw.fdz.metadatamanagement.data.common.aggregations;

import java.util.HashSet;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.FacetedPageImpl;

import com.google.common.base.Objects;

/**
 * This class holds only for possible return values a pageable object with the depending
 * aggregations.
 * 
 * @author Daniel Katzberg
 *
 * @param <T> The param T means classes from the domain.
 */
public class PageWithBuckets<T> extends FacetedPageImpl<T> {

  private static final long serialVersionUID = -5002782339546000833L;

  @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "SE_BAD_FIELD")
  private Map<String, HashSet<Bucket>> bucketMap;

  /**
   * Constructs a PageWithAggregations from a given facetedPage.
   * 
   * @param facetedPage A previously constructed {@link FacetedPage}
   * @param pageable A {@link Pageable}
   * @param bucketMap A map aggreagation name -> list of buckets
   */
  public PageWithBuckets(FacetedPage<T> facetedPage, Pageable pageable,
      Map<String, HashSet<Bucket>> bucketMap) {
    super(facetedPage.getContent(), pageable, facetedPage.getTotalElements(),
        facetedPage.getFacets());
    this.bucketMap = bucketMap;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.domain.PageImpl#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), bucketMap);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.domain.PageImpl#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (object instanceof PageWithBuckets) {
      if (!super.equals(object)) {
        return false;
      }
      PageWithBuckets<?> that = (PageWithBuckets<?>) object;
      return Objects.equal(this.bucketMap, that.bucketMap);
    }
    return false;
  }

  /**
   * Get the map of buckets.
   * 
   * @return A map aggregation name -> list of buckets
   */
  public Map<String, HashSet<Bucket>> getBucketMap() {
    return bucketMap;
  }
}
