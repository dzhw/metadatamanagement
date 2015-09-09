package eu.dzhw.fdz.metadatamanagement.config.elasticsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.FacetedPage;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.filters.FilterBucket;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.datatypes.PageWithBuckets;

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
    
    //Build grouped aggrogations / filter
    //iterate over names
    Map<String, List<FilterBucket>> map = new HashMap<>();    
    response.getAggregations().asMap().keySet().forEach(name -> {
        StringTerms aggrogationsScaleLevel =
            response.getAggregations().get(name);
        
        //create list
        List<FilterBucket> listFilterBucket = new ArrayList<>();
        aggrogationsScaleLevel.getBuckets().forEach(bucket -> {
            listFilterBucket.add(new FilterBucket(bucket.getKey(), bucket.getDocCount()));
          });
        map.put(name, listFilterBucket);
      });
    
    return new PageWithBuckets<T>(facetedPage, pageable, map);
  }
}
