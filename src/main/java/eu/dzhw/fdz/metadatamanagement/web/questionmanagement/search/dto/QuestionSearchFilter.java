package eu.dzhw.fdz.metadatamanagement.web.questionmanagement.search.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DocumentField;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.SearchFilter;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.AggregationType;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.FilterType;

/**
 * The SearchForm Data transfer object (dto) for the questions.
 * 
 * @author Daniel Katzberg
 *
 */
public class QuestionSearchFilter implements SearchFilter {

  /**
   * This is the request parameter of the query. The value is the search query.
   */
  private String query;

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter#getAllFilterValues()
   */
  @Override
  public Map<DocumentField, String> getAllFilterValues() {
    return new HashMap<>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter#getAllFilterTypes()
   */
  @Override
  public Map<DocumentField, FilterType> getAllFilterTypes() {
    return new HashMap<>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter#getAllAggregationTypes()
   */
  @Override
  public Map<DocumentField, AggregationType> getAllAggregationTypes() {
    return new HashMap<>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter#
   * getSupportedHighlightingFields()
   */
  @Override
  public List<DocumentField> getSupportedHighlightingFields() {
    return new ArrayList<>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter#getQuery()
   */
  @Override
  public String getQuery() {
    return this.query;
  }

  /* GETTER / SETTER */
  public void setQuery(String query) {
    this.query = query;
  }

}
