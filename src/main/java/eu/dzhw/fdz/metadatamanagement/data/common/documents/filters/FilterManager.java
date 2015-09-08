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
      this.filterMap.put(scaleLevel,
          new ScaleLevelFilter(false, scaleLevel, 0L));
    }
  }

  /**
   * Builds a new list for the scale level filters.
   * 
   * @param buckets The buckets from the repository layer
   */
  public void updateScaleLevelFilters(List<Terms.Bucket> buckets) {

    // reset count and save the request parameter to the variables 
    this.filterMap.values().forEach(filter -> {
        filter.setDocCount(0L);
        if (this.searchFormDto.getScaleLevel() != null) {
          if (filter.getValue().equals(this.searchFormDto.getScaleLevel())) {
            filter.setChoosen(true);
          } else {
            filter.setChoosen(false);
          }
        }
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

  /* GETTER / SETTER */
  public Map<String, AbstractFilter> getFilterMap() {
    return filterMap;
  }
}
