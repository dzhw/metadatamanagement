package eu.dzhw.fdz.metadatamanagement.data.common.documents.filters;

import java.util.ArrayList;
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
   * Returns all filter by one named group. A name is the request parameter name like
   * "scaleLevel". This values are constans in the document classes like e.g.
   * VariableDocument.SCALE_LEVEL_FIELD.
   * 
   * @param name The name of a filter / request parameter.
   * @return A list with all known filter by the name
   */
  public List<AbstractFilter> getFilterGroupByName(String name) {
    List<AbstractFilter> abstractFilters = new ArrayList<>();

    this.filterMap.values().forEach(filter -> {
        if (filter.getName().equals(name)) {
          abstractFilters.add(filter);
        }
      });

    return abstractFilters;
  }

  /**
   * Updates the list by a key and the given buckets from the elastic search aggregation.
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

  /* GETTER / SETTER */
  public Map<String, AbstractFilter> getFilterMap() {
    return filterMap;
  }
}
