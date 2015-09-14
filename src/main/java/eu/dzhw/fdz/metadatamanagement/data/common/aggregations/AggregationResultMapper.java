package eu.dzhw.fdz.metadatamanagement.data.common.aggregations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.InternalNested;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
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
   * The default Constructor uses the JacksonDocumentMapper for the depending super call.
   */
  public AggregationResultMapper(JacksonDocumentMapper jacksonDocumentMapper) {
    super(jacksonDocumentMapper);
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
    FacetedPage<T> facetedPage = super.mapResults(response, clazz, pageable);

    // Build grouped aggregations / filter
    // iterate over names
    Map<String, HashSet<Bucket>> map = new HashMap<>();
    response.getAggregations().asMap().keySet().forEach(aggregationName -> {
      
        //Not nested Aggregations
        Aggregations aggregations = response.getAggregations();
      
        //Nested Aggregations
        if (response.getAggregations().get(aggregationName).getClass()
            .isAssignableFrom(InternalNested.class)) {
          InternalNested aggregation = response.getAggregations().get(aggregationName);
          aggregations = aggregation.getAggregations();
        } 
        
        //add buckets to the FacetedPage
        map.put(aggregationName, 
            this.getStringTerms(aggregations, aggregationName));
      }); 
    
    return new PageWithBuckets<T>(facetedPage, pageable, map);
  }

  /**
   * Creates a HashSet of Buckets of Aggregations and a given aggregation name. 
   * This method supports only StringTerms.
   * 
   * @param aggregations Many aggregations for by a given aggregation name.
   * @param aggregationName the name of the aggregation (unique)
   * @return A Hashset of Buckets with aggregation information.
   * 
   * @see StringTerms
   */
  private HashSet<Bucket> getStringTerms(Aggregations aggregations, String aggregationName) {
    StringTerms aggregation = aggregations.get(aggregationName);
    HashSet<Bucket> buckets = new HashSet<>();
    aggregation.getBuckets().forEach(bucket -> {
        buckets.add(new Bucket(bucket.getKey(), bucket.getDocCount()));
      });

    return buckets;
  }
}
