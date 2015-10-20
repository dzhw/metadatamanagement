package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.aggregations;

import java.util.HashSet;
import java.util.Set;

import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;

import eu.dzhw.fdz.metadatamanagement.config.elasticsearch.JacksonDocumentMapper;
import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.AggregationAndHighlightingResultMapper;
import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.Bucket;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.DocumentField;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * The aggregation result mapper is a sub class from the
 * {@link AggregationAndHighlightingResultMapper}. It extends the MapResuts method for saving the
 * aggregations. It uses the JacksonDocumentMapper for mapping e.g. LocalDate Object from Strings.
 * In difference to the {@link AggregationAndHighlightingResultMapper} it support specific order of
 * scale level or other filter buckets, which are different from the docCount sorting.
 * 
 * @author Daniel Katzberg
 * 
 */
public class VariableDocumentAggregrationResultMapper
    extends AggregationAndHighlightingResultMapper {

  private ScaleLevelProvider scaleLevelProvider;

  public VariableDocumentAggregrationResultMapper(JacksonDocumentMapper jacksonDocumentMapper,
      ScaleLevelProvider scaleLevelProvider) {
    super(jacksonDocumentMapper);
    this.scaleLevelProvider = scaleLevelProvider;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.common.aggregations.AggregationResultMapper#
   * getStringTermBuckets(eu.dzhw.fdz.metadatamanagement.data.common.documents.DocumentField,
   * org.elasticsearch.search.aggregations.bucket.terms.StringTerms)
   */
  @Override
  protected Set<Bucket> getStringTermBuckets(DocumentField field, StringTerms aggregation) {
    Set<Bucket> buckets = new HashSet<>();

    // scale level filter needs a different order by sorting. this is given by scale level buckets
    if (field.equals(VariableDocument.SCALE_LEVEL_FIELD)) {
      aggregation.getBuckets().forEach(bucket -> {
        buckets.add(
            new ScaleLevelBucket(bucket.getKey(), bucket.getDocCount(), this.scaleLevelProvider));
      });
      // default sorting by docCount. normal use of buckets.
    } else {
      return super.getStringTermBuckets(field, aggregation);
    }
    return buckets;
  }

}
