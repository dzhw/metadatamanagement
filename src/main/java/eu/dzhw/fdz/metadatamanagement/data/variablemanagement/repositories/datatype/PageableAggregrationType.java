package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.datatype;

import org.elasticsearch.search.aggregations.Aggregations;
import org.springframework.data.domain.Page;

/**
 * This class holds only for possible return values a pageable object with the depending
 * aggregations.
 * 
 * @author Daniel Katzberg
 *
 * @param <T> The param T means classes from the domain.
 */
public class PageableAggregrationType<T> {

  private Page<T> page;
  private Aggregations aggregations;

  /**
   * Every pageable aggregation type need a pageable object and depending aggregations.
   * 
   * @param facetedPage A pageable object of a domain representation class
   * @param aggregations An aggregation which are from the same query like the pageable object.
   */
  public PageableAggregrationType(Page<T> page, Aggregations aggregations) {
    this.page = page;
    this.aggregations = aggregations;
  }

  /* GETTER */
  public Page<T> getPage() {
    return page;
  }

  public Aggregations getAggregations() {
    return aggregations;
  }
}
