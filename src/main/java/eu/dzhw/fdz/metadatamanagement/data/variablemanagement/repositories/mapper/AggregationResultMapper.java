package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.mapper;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregations;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.FacetedPage;

import eu.dzhw.fdz.metadatamanagement.config.elasticsearch.JacksonDocumentMapper;

/**
 * The aggregation result mapper is a sub class from the {@link DefaultResultMapper}. It extends the
 * MapResuts method for saving the aggregations. It uses the JacksonDocumentMapper for mapping e.g.
 * LocalDate Object from Strings.
 * 
 * @author Daniel Katzberg
 *
 */
public class AggregationResultMapper extends DefaultResultMapper {

  /**
   * The actual aggregations of a query result. It
   */
  private Aggregations aggregations;

  /**
   * The default Constructor uses the JacksonDocumentMapper for the depending super call.
   */
  public AggregationResultMapper() {
    super(new JacksonDocumentMapper());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.data.elasticsearch.core.DefaultResultMapper#mapResults(org.elasticsearch.
   * action.search.SearchResponse, java.lang.Class, org.springframework.data.domain.Pageable)
   */
  @Override
  public <T> FacetedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {

    this.aggregations = response.getAggregations();
    return super.mapResults(response, clazz, pageable);
  }

  /* GETTER / SETTER */
  public Aggregations getAggregations() {
    return aggregations;
  }
}
