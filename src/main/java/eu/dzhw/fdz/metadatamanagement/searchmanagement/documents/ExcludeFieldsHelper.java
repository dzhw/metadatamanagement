package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;

/**
 * Helper which returns the fields of a {@link SearchDocumentInterface} which should not be loaded
 * from elasticsearch.
 * 
 * @author René Reitmann
 */
public class ExcludeFieldsHelper {
  /**
   * Get the fields of the given {@link SearchDocumentInterface} which should not be loaded from
   * elasticsearch.
   * 
   * @param clazz The class implementing the {@link SearchDocumentInterface}
   * @return An array of patterns which will be used to exclude fields.
   */
  public static String[] getFieldsToExcludeOnDeserialization(
      Class<? extends AbstractRdcDomainObject> clazz) {
    if (DataPackageSearchDocument.class.isAssignableFrom(clazz)) {
      return DataPackageSearchDocument.FIELDS_TO_EXCLUDE_ON_DESERIALIZATION;
    }
    if (SurveySearchDocument.class.isAssignableFrom(clazz)) {
      return SurveySearchDocument.FIELDS_TO_EXCLUDE_ON_DESERIALIZATION;
    }
    if (InstrumentSearchDocument.class.isAssignableFrom(clazz)) {
      return InstrumentSearchDocument.FIELDS_TO_EXCLUDE_ON_DESERIALIZATION;
    }
    if (QuestionSearchDocument.class.isAssignableFrom(clazz)) {
      return QuestionSearchDocument.FIELDS_TO_EXCLUDE_ON_DESERIALIZATION;
    }
    if (DataSetSearchDocument.class.isAssignableFrom(clazz)) {
      return DataSetSearchDocument.FIELDS_TO_EXCLUDE_ON_DESERIALIZATION;
    }
    if (VariableSearchDocument.class.isAssignableFrom(clazz)) {
      return VariableSearchDocument.FIELDS_TO_EXCLUDE_ON_DESERIALIZATION;
    }
    if (RelatedPublicationSearchDocument.class.isAssignableFrom(clazz)) {
      return RelatedPublicationSearchDocument.FIELDS_TO_EXCLUDE_ON_DESERIALIZATION;
    }
    if (ConceptSearchDocument.class.isAssignableFrom(clazz)) {
      return ConceptSearchDocument.FIELDS_TO_EXCLUDE_ON_DESERIALIZATION;
    }
    throw new IllegalArgumentException(clazz.getSimpleName() + " is not yet supported!");
  }
}
