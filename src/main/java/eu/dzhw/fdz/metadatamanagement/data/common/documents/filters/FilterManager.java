package eu.dzhw.fdz.metadatamanagement.data.common.documents.filters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.VariableSearchFormDto;

/**
 * This class manage filter and the depending status. Is a filter choosen? Which value has which
 * filter? The filter manager can manage different kind of filter.
 * 
 * @author Daniel Katzberg
 *
 */
public class FilterManager {

  /**
   * A list with all scaleLevelFilter.
   */
  private Map<String, AbstractFilter> filterMap;

  /**
   * The data transfer object of the search form.
   */
  private VariableSearchFormDto variableSearchFormDto;

  /**
   * Initialize the filter manager with default filled list. The list are depending on different
   * class. For more information visit the linked pages.
   * 
   * @param scaleLevelProvider Injected entity of scale level provider for get the "values" of the
   *        scale levels.
   * @see ScaleLevelProvider
   */
  public FilterManager(VariableSearchFormDto variableSearchFormDto) {
    this.variableSearchFormDto = variableSearchFormDto;
    this.initScalelevelFilters();
  }

  private void initScalelevelFilters() {
    this.filterMap = new HashMap<>();
  }

  /**
   * update the list by a key and the given buckets from the elastic search aggregation.
   * 
   * @param buckets The buckets from the repository layer
   */
  public void updateAllFilter(List<Terms.Bucket> buckets) {

    // Clear Map
    this.filterMap.clear();

    // put buckets to the list
    buckets.forEach(bucket -> {
        this.filterMap.put(bucket.getKey(),
            new ScaleLevelFilter(bucket.getKey(), bucket.getDocCount()));
      });

    // update all choosen status of used filter
    this.variableSearchFormDto.getAllFilterValues().forEach(filter -> {

        if (this.getFilterMap().get(filter) == null) {
          this.filterMap.put(filter, new ScaleLevelFilter(filter, 0L));
        }
      });
  }

  /**
   * update the filter status of choosen by a key.
   * 
   * @param buckets The buckets from the repository layer
   * @param key The key is a value of the request parameter. e.g.: ?requestParameter=value
   */
  // private void updateFilters(List<Terms.Bucket> buckets, String key) {
  //
  //
  //
  // }

  /* GETTER / SETTER */
  public Map<String, AbstractFilter> getFilterMap() {
    return filterMap;
  }
}
