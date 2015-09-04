package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.datatype;

import org.elasticsearch.search.aggregations.Aggregations;
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
public class PageWithAggregations<T> extends FacetedPageImpl<T> {

  private static final long serialVersionUID = -5002782339546000833L;
  private transient Aggregations aggregations = null;

  /**
   * Constructs a PageWithAggregations from a given facetedPage.
   * 
   * @param facetedPage A previously constructed {@link FacetedPage}
   * @param pageable A {@link Pageable}
   * @param aggregations Aggregations from elastic search
   */
  public PageWithAggregations(FacetedPage<T> facetedPage, Pageable pageable,
      Aggregations aggregations) {
    super(facetedPage.getContent(), pageable, facetedPage.getTotalElements(),
        facetedPage.getFacets());
    this.aggregations = aggregations;
  }

  public Aggregations getAggregations() {
    return aggregations;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), aggregations);
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof PageWithAggregations) {
      if (!super.equals(object)) {
        return false;
      }
      PageWithAggregations<?> that = (PageWithAggregations<?>) object;
      return Objects.equal(this.aggregations, that.aggregations);
    }
    return false;
  }
}
