package eu.dzhw.fdz.metadatamanagement.data.questionmanagement.aggregations;

import java.util.Set;

import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;

import eu.dzhw.fdz.metadatamanagement.config.elasticsearch.JacksonDocumentMapper;
import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.AggregationAndHighlightingResultMapper;
import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.Bucket;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.DocumentField;

/**
 * The aggregation result mapper is a sub class from the
 * {@link AggregationAndHighlightingResultMapper}. It extends the MapResuts method for saving the
 * aggregations. It uses the JacksonDocumentMapper for mapping e.g. LocalDate Object from Strings.
 * In difference to the {@link AggregationAndHighlightingResultMapper} it support specific order of
 * filter buckets, which are different from the docCount sorting.
 * 
 * @author Daniel Katzberg
 *
 */
public class QuestionDocumentAggregationResultMapper
    extends AggregationAndHighlightingResultMapper {

  public QuestionDocumentAggregationResultMapper(JacksonDocumentMapper jacksonDocumentMapper) {
    super(jacksonDocumentMapper);
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
    return super.getStringTermBuckets(field, aggregation);
  }

}
