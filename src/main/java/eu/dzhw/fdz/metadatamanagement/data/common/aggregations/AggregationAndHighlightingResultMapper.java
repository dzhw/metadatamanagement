package eu.dzhw.fdz.metadatamanagement.data.common.aggregations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.InternalNested;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.highlight.HighlightField;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.FacetedPage;

import eu.dzhw.fdz.metadatamanagement.config.elasticsearch.JacksonDocumentMapper;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.DocumentField;
import eu.dzhw.fdz.metadatamanagement.data.common.utils.HighlightingUtils;

/**
 * The aggregation result mapper is a sub class from the {@link DefaultResultMapper}. It extends the
 * MapResults method for saving the aggregations. It uses the JacksonDocumentMapper for mapping e.g.
 * LocalDate Object from Strings.
 * 
 * @author Daniel Katzberg
 *
 */
public class AggregationAndHighlightingResultMapper extends DefaultResultMapper {

  /**
   * The default Constructor uses the JacksonDocumentMapper for the depending super call.
   */
  public AggregationAndHighlightingResultMapper(JacksonDocumentMapper jacksonDocumentMapper) {
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

    HashMap<String, String> highlightedMap = new HashMap<>();

    // both array and list should be of same size and order
    SearchHit[] searchHits = response.getHits().hits();
    List<T> pageContent = facetedPage.getContent();

    for (int i = 0; i < searchHits.length; i++) {
      SearchHit searchHit = searchHits[i];

      Map<String, HighlightField> highlightedFields = searchHit.getHighlightFields();
      if (!highlightedFields.isEmpty()) {
        T mappedHit = pageContent.get(i);
        
        for (Entry<String, HighlightField> entry : highlightedFields.entrySet()) {
          // add
          String unescapedHtml = getAllFragmentsConcatenated(entry.getValue().getFragments());
          highlightedMap.put(entry.getValue().getName().replace(".highlight", ""),
              HighlightingUtils.escapeHtml(unescapedHtml)); //escape html
        }

        //set Map
        BeanWrapper bean = new BeanWrapperImpl(mappedHit);
        bean.setPropertyValue("highlightingUtils.highlightedFields", highlightedMap);

      }
    }

    // extract buckets
    Map<DocumentField, Set<Bucket>> map = new HashMap<>();

    extractStringTermAggregations(map, response.getAggregations());

    return new PageWithBuckets<T>(facetedPage, pageable, map);
  }

  private String getAllFragmentsConcatenated(Text[] fragments) {
    StringBuilder stringBuilder = new StringBuilder();
    for (Text text : fragments) {
      if (stringBuilder.length() > 0) {
        stringBuilder.append(" ");
      }
      stringBuilder.append(text.toString());
    }
    return stringBuilder.toString();
  }

  /**
   * Recursively search for {@link StringTerms} aggregations and add their buckets to the given map.
   * 
   * @param map The resulting map which holds all buckets for the aggregations.
   * @param aggregations The aggregations of the response or nested aggregations.
   */
  private void extractStringTermAggregations(Map<DocumentField, Set<Bucket>> map,
      Aggregations aggregations) {
    for (Entry<String, Aggregation> entry : aggregations.asMap().entrySet()) {

      // STRINGTERM
      if (entry.getValue().getClass().isAssignableFrom(StringTerms.class)) {
        DocumentField field = new DocumentField(entry.getKey());
        map.put(field, getStringTermBuckets(field, (StringTerms) entry.getValue()));

        // INTERNAL NESTED
      } else if (entry.getValue().getClass().isAssignableFrom(InternalNested.class)) {
        InternalNested nestedAggregation = (InternalNested) entry.getValue();
        extractStringTermAggregations(map, nestedAggregation.getAggregations());

        // OTHER TERMS ARE NOT SUPPORTED YET
      } else {
        throw new IllegalStateException("Not yet implemented");
      }
    }
  }

  /**
   * Create buckets for the given field and {@link StringTerms} aggregation.
   * 
   * @param field The documents field as determined by the aggregation name.
   * @param aggregation The aggregation which holds the buckets.
   * @return buckets for the given field and {@link StringTerms} aggregation.
   */
  protected Set<Bucket> getStringTermBuckets(DocumentField field, StringTerms aggregation) {
    Set<Bucket> buckets = new HashSet<>();

    aggregation.getBuckets().forEach(bucket -> {
        buckets.add(new Bucket(bucket.getKey(), bucket.getDocCount()));
      });

    return buckets;
  }
}
