package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.Field;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.AggregationType;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.FilterType;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * The SearchForm Data transfer object (dto). This dto
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.builders")
public class VariableSearchFilter extends AbstractSearchFilter {

  /**
   * This is the request parameter of the query. The value is the search query.
   */
  private String query;

  /**
   * This is the request parameter of the scaleLevel filter.
   */
  private String scaleLevel;

  /**
   * This is the request parameter of the surveyTitle filter.
   */
  private String surveyTitle;

  /**
   * This is the request parameter of the dateRange filter.
   */
  @Valid
  private DateRange dateRange;

  /**
   * Static map holding all filter types for the filter fields.
   */
  private static final Map<Field, FilterType> filterTypes;

  /**
   * Static map holding all aggregation types for the filter fields.
   */
  private static final Map<Field, AggregationType> aggregationTypes;

  static {
    filterTypes = new HashMap<>();

    // ScaleLevel
    filterTypes.put(VariableDocument.SCALE_LEVEL_FIELD, FilterType.TERM);

    // Survey Title
    filterTypes.put(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD, FilterType.TERM);

    // Date Range Start Date
    filterTypes.put(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE,
        FilterType.RANGE_LTE);

    // Date Range End Date
    filterTypes.put(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE,
        FilterType.RANGE_GTE);

    aggregationTypes = new HashMap<>();

    // ScaleLevel
    aggregationTypes.put(VariableDocument.SCALE_LEVEL_FIELD, AggregationType.TERM);

    // Survey Title
    aggregationTypes.put(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD, AggregationType.TERM);
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter#getAllFilterValues()
   */
  @Override
  public Map<Field, String> getAllFilterValues() {
    Map<Field, String> filterValues = new HashMap<>();

    // ScaleLevel
    if (StringUtils.hasText(this.scaleLevel)) {
      filterValues.put(VariableDocument.SCALE_LEVEL_FIELD, this.scaleLevel);
    }

    // Survey Title
    if (StringUtils.hasText(this.surveyTitle)) {
      filterValues.put(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD, this.surveyTitle);
    }

    // Date Range Start Date value. This value have to be checked by the end field
    // StartDate (Input by User) <= EndDate (Variable)
    if (this.dateRange != null) {
      if (this.dateRange.getStartDate() != null) {
        filterValues.put(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE,
            this.dateRange.getStartDate().toString());

      }

      // Date Range End Date This value have to be checked by the start field
      // StartDate (Variable) <= EndDate (Input by User)
      if (this.dateRange.getEndDate() != null) {
        filterValues.put(VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE,
            this.dateRange.getEndDate().toString());
      }
    }

    return filterValues;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter#getAllFilterTypes()
   */
  @Override
  public Map<Field, FilterType> getAllFilterTypes() {
    return filterTypes;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter#getAllAggregationTypes()
   */
  @Override
  public Map<Field, AggregationType> getAllAggregationTypes() {
    return aggregationTypes;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractQueryDto#getQuery()
   */
  @Override
  public String getQuery() {
    return query;
  }

  /* GETTER / SETTER */
  public void setQuery(String query) {
    this.query = query;
  }

  public String getScaleLevel() {
    return scaleLevel;
  }

  public void setScaleLevel(String scaleLevel) {
    this.scaleLevel = scaleLevel;
  }

  public String getSurveyTitle() {
    return surveyTitle;
  }

  public void setSurveyTitle(String surveyTitle) {
    this.surveyTitle = surveyTitle;
  }

  public DateRange getDateRange() {
    return dateRange;
  }

  public void setDateRange(DateRange dateRange) {
    this.dateRange = dateRange;
  }
}
