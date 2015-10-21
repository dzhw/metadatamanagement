package eu.dzhw.fdz.metadatamanagement.web.common.dtos;

import java.util.List;
import java.util.Map;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DocumentField;

/**
 * This interface declares methods which are common to all search filters.
 * 
 * @author Daniel Katzberg
 */
public interface SearchFilter {
  /**
   * @return A map with all filter values.
   */
  Map<DocumentField, String> getAllFilterValues();

  /**
   * @return Returns a map {@link DocumentField} -> {@link FilterType}.
   */
  Map<DocumentField, FilterType> getAllFilterTypes();

  /**
   * @return Returns a map {@link DocumentField} -> {@link AggregationType}.
   */
  Map<DocumentField, AggregationType> getAllAggregationTypes();
  
  /**
   * @return Returns a set with all field which are used by the highlighting.
   */
  List<DocumentField> getSupportedHighlightingFields();

  /**
   * @return Returns the basic query as String.
   */
  String getQuery();
}
