package eu.dzhw.fdz.metadatamanagement.data.common.aggregations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.nested.InternalNested;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
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

    // Build grouped aggrogations / filter
    // iterate over names
    Map<String, HashSet<Bucket>> map = new HashMap<>();
    response.getAggregations().asMap().keySet().forEach(aggregationName -> {

        // StringTerms or non nested aggregation
        if (response.getAggregations().get(aggregationName).getClass()
            .isAssignableFrom(StringTerms.class)) {
          // TODO dkatzberg refactor duplicate code
          StringTerms aggregation = response.getAggregations().get(aggregationName);
          HashSet<Bucket> buckets = new HashSet<>();
          aggregation.getBuckets().forEach(bucket -> {
              buckets.add(new Bucket(bucket.getKey(), bucket.getDocCount()));
            });
          map.put(aggregationName, buckets);
          // InternalNested means nested aggrogrations
        } else if (response.getAggregations().get(aggregationName).getClass()
            .isAssignableFrom(InternalTerms.class)) {
          // TODO dkatzberg refactor duplicate code
          InternalTerms aggregation = response.getAggregations().get(aggregationName);
          HashSet<Bucket> buckets = new HashSet<>();
          aggregation.getBuckets().forEach(bucket -> {
              buckets.add(new Bucket(bucket.getKey(), bucket.getDocCount()));
            });
          map.put(aggregationName, buckets);
          // InternalNested means nested aggrogrations
        } else if (response.getAggregations().get(aggregationName).getClass()
            .isAssignableFrom(InternalNested.class)) {
          // Get aggregation
          InternalNested aggregation = response.getAggregations().get(aggregationName);
          HashSet<Bucket> buckets = new HashSet<>();
  
          // get entry for nested entries for the aggregation
          for (Entry<String, Aggregation> entry : 
              aggregation.getAggregations().asMap().entrySet()) {
            String nestedAggregationName = entry.getKey();
            // TODO dkatzberg refactor duplicate code
            StringTerms subAggregation = (StringTerms) entry.getValue();
            // get buckets from nested aggrogation
            subAggregation.getBuckets().forEach(bucket -> {
                buckets.add(new Bucket(bucket.getKey(), bucket.getDocCount()));
              });
            map.put(nestedAggregationName, buckets);
          }
        }
      });
    return new PageWithBuckets<T>(facetedPage, pageable, map);
  }
}
