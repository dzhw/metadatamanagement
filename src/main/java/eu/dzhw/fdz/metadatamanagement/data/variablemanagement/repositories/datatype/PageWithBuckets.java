package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.datatype;

import java.util.List;

import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
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
  private List<Bucket> buckets;

  /**
   * Constructs a PageWithAggregations from a given facetedPage.
   * 
   * @param facetedPage A previously constructed {@link FacetedPage}
   * @param pageable A {@link Pageable}
   * @param buckets Buckets from elastic search
   */
  public PageWithBuckets(FacetedPage<T> facetedPage, Pageable pageable, List<Bucket> buckets) {
    super(facetedPage.getContent(), pageable, facetedPage.getTotalElements(),
        facetedPage.getFacets());
    this.buckets = buckets;
  }

  public List<Bucket> getBuckets() {
    return buckets;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.domain.PageImpl#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), buckets);
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
      return Objects.equal(this.buckets, that.buckets);
    }
    return false;
  }
}
