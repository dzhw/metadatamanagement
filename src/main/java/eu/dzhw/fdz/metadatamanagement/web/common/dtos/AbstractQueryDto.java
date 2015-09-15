package eu.dzhw.fdz.metadatamanagement.web.common.dtos;

import java.util.Map;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.Field;

/**
 * This query is a abstract version of all Data Transfer Object which are used for different kind of
 * queries.
 * The static values are group and every position is define by value.
 * <p></p>
 * First Number: (General)
 *              1_ = FILTER
 *              2_ = AGGREGATION
 *<p></p>
 * Middle Number: (Type of the General)
 *              001_ = TERM
 *              002_ = RANGE
 * <p></p>             
 * Last Number: (Additional information)
 *              000 = Nothing special.
 *              051 = GTE (Greater Than Or Equals)
 *              049 = LTE (Lower Than Or Equals)
 * 
 * @author Daniel Katzberg
 */
public abstract class AbstractQueryDto {

  /**
   * The value for a term filter. 1_ means filter and the last 001 means term.
   */
  public static final int FILTER_TERM = 1_001_000;

  /**
   * The value for a range filter. 1_ means filter. 
   * The middle 002 means range filter. 
   * The last 051 means greater than equals.
   */
  public static final int FILTER_RANGE_GTE = 1_002_051;
  
  /**
   * The value for a range filter. 1_ means filter. 
   * The middle 002 means range filter. 
   * The last 049 means lower than equals.
   */
  public static final int FILTER_RANGE_LTE = 1_002_049;

  /**
   * The value for a term aggregation. 2_ means aggregation and the last 001 term mean term.
   */
  public static final int AGGREGATION_TERM = 2_001_000;

  /**
   * @return A map with all filter values.
   */
  public abstract Map<Field, String> getAllFilters();

  /**
   * @return Returns a map with all filter fields. The value of the map is type of
   *         filter. (like term filter)
   */
  public abstract Map<Field, Integer> getFilterFields();

  /**
   * @return Returns a List with all aggregation fields.The value of the map is type
   *         of aggregation. (like term aggregation)
   */
  public abstract Map<Field, Integer> getAggregationFields();

  /**
   * @return Returns the basic query as String.
   */
  public abstract String getQuery();
}
