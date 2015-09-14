package eu.dzhw.fdz.metadatamanagement.web.common.dtos;

import java.util.Map;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.Field;

/**
 * This query is a abstract version of all Data Transfer Object which are used for different kind of
 * queries.
 * 
 * @author Daniel Katzberg
 */
public abstract class AbstractQueryDto {

  /**
   * The value for a term filter. 1_ means filter and the last 001 means term.
   */
  public static final int FILTER_TERM = 1_001;

  /**
   * The value for a range filter. 1_ means filter and the last 002 means range.
   */
  public static final int FILTER_RANGE = 1_002;

  /**
   * The value for a term aggregation. 2_ means aggregation and the last 001 term mean term.
   */
  public static final int AGGREGATION_TERM = 2_001;

  /**
   * @return A map with all filter values.
   */
  public abstract Map<Field, String> getAllFilters();

  /**
   * @return Returns a map with all filter fields. The value of the map is the key for the kind of
   *         filter. (like term filter)
   */
  public abstract Map<Field, Integer> getFilterFields();

  /**
   * @return Returns a List with all aggregation fields.The value of the map is the key for the kind
   *         of filter. (like term aggregation)
   */
  public abstract Map<Field, Integer> getAggregationFields();

  /**
   * @return Returns the basic query as String.
   */
  public abstract String getQuery();
}
