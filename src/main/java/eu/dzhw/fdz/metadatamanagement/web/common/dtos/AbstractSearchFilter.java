package eu.dzhw.fdz.metadatamanagement.web.common.dtos;

import java.util.Map;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.Field;

/**
 * This class declares methods which are common to all search filters.
 * 
 * @author Daniel Katzberg
 */
public abstract class AbstractSearchFilter {
  /**
   * @return A map with all filter values.
   */
  public abstract Map<Field, String> getAllFilterValues();

  /**
   * @return Returns a map {@link Field} -> {@link FilterType}.
   */
  public abstract Map<Field, FilterType> getAllFilterTypes();

  /**
   * @return Return a map {@link Field} -> {@link AggregationType}.
   */
  public abstract Map<Field, AggregationType> getAllAggregationTypes();

  /**
   * @return Returns the basic query as String.
   */
  public abstract String getQuery();
}
