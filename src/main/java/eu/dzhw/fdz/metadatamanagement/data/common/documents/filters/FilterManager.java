package eu.dzhw.fdz.metadatamanagement.data.common.documents.filters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.SearchFormDto;

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
   * Injected entity of scale level provider for get the "values" of the scale levels.
   */
  private ScaleLevelProvider scaleLevelProvider;

  /**
   * The data transfer object of the search form.
   */
  private SearchFormDto searchFormDto;

  /**
   * Initialize the filter manager with default filled list. The list are depending on different
   * class. For more information visit the linked pages.
   * 
   * @param scaleLevelProvider Injected entity of scale level provider for get the "values" of the
   *        scale levels.
   * @see ScaleLevelProvider
   */
  public FilterManager(ScaleLevelProvider scaleLevelProvider, SearchFormDto searchFormDto) {
    this.scaleLevelProvider = scaleLevelProvider;
    this.searchFormDto = searchFormDto;
    this.initScalelevelFilters();
  }

  private void initScalelevelFilters() {
    this.filterMap = new HashMap<>();

    for (String scaleLevel : this.scaleLevelProvider.getAllScaleLevel()) {
      this.filterMap.put(scaleLevel, new ScaleLevelFilter(false, scaleLevel, 0L));
    }
  }
  
  /**
   * update the list by a key and the given buckets from the elastic search aggregation.
   * 
   * @param buckets The buckets from the repository layer
   */
  public void updateAllFilter(List<Terms.Bucket> buckets) {
    
    // reset count and choosen parameter
    this.filterMap.values().forEach(filter -> {
        filter.setDocCount(0L);
        filter.setChoosen(false);
      });
    
    //update all choosen status of used filter
    this.searchFormDto.getAllFilterValues().forEach(filter -> {
        this.updateFilters(filter);
      });
        
    // update the doc count.
    buckets.forEach(bucket -> {
        AbstractFilter abstractFilter = this.filterMap.remove(bucket.getKey());
  
        if (abstractFilter != null) {
          abstractFilter.setDocCount(bucket.getDocCount());
          this.filterMap.put(bucket.getKey(), abstractFilter);
        }
  
      });
  }

  /**
   * update the filter status of choosen by a key. 
   * 
   * @param buckets The buckets from the repository layer
   * @param key The key is a value of the request parameter. e.g.: ?requestParameter=value
   */
  private void updateFilters(String key) {

    // update choosen field
    if (key != null) {
      AbstractFilter abstractFilter = this.filterMap.remove(key);
      if (abstractFilter != null) {
        abstractFilter.setChoosen(true);
        this.filterMap.put(key, abstractFilter);
      }
    }
  }

  /* GETTER / SETTER */
  public Map<String, AbstractFilter> getFilterMap() {
    return filterMap;
  }
}
