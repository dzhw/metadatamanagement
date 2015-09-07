package eu.dzhw.fdz.metadatamanagement.data.common.documents.filter;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * This class manage filter and the depending status. Is a filter choosen? Which value has which
 * filter?
 * 
 * @author Daniel Katzberg
 *
 */
public class FilterManager {

  private List<ScaleLevelFilter> scaleLevelFilters;

  /**
   * Initialize the filter manager with empty lists.
   */
  public FilterManager() {
    this.scaleLevelFilters = new ArrayList<>();
  }

  /**
   * build a new list for the scale level filters.
   * 
   * @param buckets The buckets from the repository layer
   * @param scaleLevelRequestParam The given scaleLevelRequestParam from the web layer
   */
  public void initScaleLevelFilters(List<Terms.Bucket> buckets, String scaleLevelRequestParam) {
    this.scaleLevelFilters = new ArrayList<>();

    buckets.forEach(b -> {

      // check for choosen / clicked
        boolean choosen = false;
        if (b.getKey().equals(scaleLevelRequestParam)) {
          choosen = true;
        }

        // add scale level filter to list
        this.scaleLevelFilters.add(new ScaleLevelFilter(choosen, b.getKey(), b.getDocCount(),
            VariableDocument.SCALE_LEVEL_FIELD));
      });
  }

  /* GETTER / SETTER */
  public List<ScaleLevelFilter> getScaleLevelFilters() {
    return scaleLevelFilters;
  }

  public void setScaleLevelFilters(List<ScaleLevelFilter> scaleLevelFilters) {
    this.scaleLevelFilters = scaleLevelFilters;
  }
}
