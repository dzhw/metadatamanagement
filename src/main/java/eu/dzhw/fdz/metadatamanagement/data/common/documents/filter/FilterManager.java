package eu.dzhw.fdz.metadatamanagement.data.common.documents.filter;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;

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
  private List<ScaleLevelFilter> scaleLevelFilters;

  /**
   * Injected entity of scale level provider for get the "values" of the scale levels.
   */
  private ScaleLevelProvider scaleLevelProvider;

  /**
   * Initialize the filter manager with default filled list. The list are depending on different
   * class. For more information visit the linked pages.
   * 
   * @param scaleLevelProvider Injected entity of scale level provider for get the "values" of the
   *        scale levels.
   * @see ScaleLevelProvider
   */
  public FilterManager(ScaleLevelProvider scaleLevelProvider) {
    this.scaleLevelProvider = scaleLevelProvider;
    this.initScalelevelFilters();
  }

  private void initScalelevelFilters() {
    this.scaleLevelFilters = new ArrayList<>();

    for (String scaleLevel : this.scaleLevelProvider.getAllScaleLevel()) {
      this.scaleLevelFilters.add(new ScaleLevelFilter(false, scaleLevel, 0L));
    }
  }

  /**
   * Builds a new list for the scale level filters.
   * 
   * @param buckets The buckets from the repository layer
   * @param scaleLevelRequestParam The given scaleLevelRequestParam from the web layer
   */
  public void updateScaleLevelFilters(List<Terms.Bucket> buckets, String scaleLevelRequestParam) {

    // reset scale level filter
    this.scaleLevelFilters.forEach( scaleFilter -> { 
        scaleFilter.setDocCount(0L);
        if (scaleLevelRequestParam != null) {
          if (scaleFilter.getValue().equals(scaleLevelRequestParam)) {
            scaleFilter.setChoosen(true);
          } else {
            scaleFilter.setChoosen(false);
          }
        }
      });

    // complexity with n2. but there are only three elements at the moment.
    buckets.forEach(bucket -> {

        // set doc count
        int index = this.getIndexByValue(this.scaleLevelFilters, bucket.getKey());
  
        if (index > -1) {
          this.scaleLevelFilters.get(index).setDocCount(bucket.getDocCount());
        }
      });
  }

  /**
   * @param list a given list
   * @param value a given value by an abstract element
   * @return The index of an element which has the parameter value.
   */
  private int getIndexByValue(List<ScaleLevelFilter> list, String value) {

    for (AbstractFilter<String> element : list) {
      if (element.getValue().equals(value)) {
        return list.indexOf(element);
      }
    }

    return -1;
  }

  /* GETTER / SETTER */
  public List<ScaleLevelFilter> getScaleLevelFilters() {
    return scaleLevelFilters;
  }

  public void setScaleLevelFilters(List<ScaleLevelFilter> scaleLevelFilters) {
    this.scaleLevelFilters = scaleLevelFilters;
  }
}
