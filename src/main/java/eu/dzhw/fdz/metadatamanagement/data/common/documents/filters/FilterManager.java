package eu.dzhw.fdz.metadatamanagement.data.common.documents.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
  private Map<String, List<FilterBucket>> filterMap;

  /**
   * The data transfer object of the search form.
   */
  private VariableSearchFormDto variableSearchFormDto;

  /**
   * Initialize the filter manager with default filled list. The list are depending on different
   * class. For more information visit the linked pages.
   * 
   * @param variableSearchFormDto A instance of the variable search form dto. It has all actual
   *        values of the query and all filter within the url as request parameter..
   */
  public FilterManager(VariableSearchFormDto variableSearchFormDto, 
      Map<String, List<FilterBucket>> filterMap) {
    this.variableSearchFormDto = variableSearchFormDto;
    this.filterMap = filterMap;
    this.mergeFilterWithDtoInformation();
  }

  /**
   * Updates the list by a key and the given buckets from the elastic search aggregation.
   */
  private void mergeFilterWithDtoInformation() {
    
    // merge dto
    this.variableSearchFormDto.getAllFilterValues().keySet().forEach(filterName -> {
        String filterValue = this.variableSearchFormDto.getAllFilterValues().get(filterName);
        FilterBucket dtoFilterBucket = new FilterBucket(filterValue, 0L);
        
        if (!this.filterMap.containsKey(filterName)) {
          List<FilterBucket> filterBucketList = new ArrayList<>();
          filterBucketList.add(dtoFilterBucket);
          this.filterMap.put(filterName, filterBucketList);
          //okay group is in the map, check here for the value
        } else {
          if (!this.filterMap.get(filterName).contains(dtoFilterBucket)) {
            List<FilterBucket> filterBucketList = this.filterMap.get(filterName);
            filterBucketList.add(dtoFilterBucket);
            this.getFilterMap().put(filterName, filterBucketList);
          }
        }
      });
  }

  /* GETTER / SETTER */
  public Map<String, List<FilterBucket>> getFilterMap() {
    return filterMap;
  }  
}
