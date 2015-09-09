package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.datatypes;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.FacetedPageImpl;

import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.filters.FilterBucket;

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
  private Map<String, List<FilterBucket>> filterBuckets;

  /**
   * Constructs a PageWithAggregations from a given facetedPage.
   * 
   * @param facetedPage A previously constructed {@link FacetedPage}
   * @param pageable A {@link Pageable}
   * @param filterBuckets filter object. The name of the aggregation is the key of the map. the list
   *        has the information of the docCount und the bucket key
   */
  public PageWithBuckets(FacetedPage<T> facetedPage, Pageable pageable,
      Map<String, List<FilterBucket>> filterBuckets) {
    super(facetedPage.getContent(), pageable, facetedPage.getTotalElements(),
        facetedPage.getFacets());
    this.filterBuckets = filterBuckets;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.domain.PageImpl#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), filterBuckets);
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
      return Objects.equal(this.filterBuckets, that.filterBuckets);
    }
    return false;
  }

  /* GETTER / SETTER */
  public Map<String, List<FilterBucket>> getFilterBuckets() {
    return filterBuckets;
  }
}
