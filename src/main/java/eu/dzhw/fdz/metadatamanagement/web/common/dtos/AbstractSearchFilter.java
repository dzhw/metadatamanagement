package eu.dzhw.fdz.metadatamanagement.web.common.dtos;

import java.util.Map;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DocumentField;

/**
 * This class declares methods which are common to all search filters.
 * 
 * @author Daniel Katzberg
 */
public abstract class AbstractSearchFilter {
  /**
   * @return A map with all filter values.
   */
  public abstract Map<DocumentField, String> getAllFilterValues();

  /**
   * @return Returns a map {@link DocumentField} -> {@link FilterType}.
   */
  public abstract Map<DocumentField, FilterType> getAllFilterTypes();

  /**
   * @return Return a map {@link DocumentField} -> {@link AggregationType}.
   */
  public abstract Map<DocumentField, AggregationType> getAllAggregationTypes();

  /**
   * @return Returns the basic query as String.
   */
  public abstract String getQuery();
}
