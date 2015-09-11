package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.Field;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * The SearchForm Data transfer object (dto). This dto
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.builders")
public class VariableSearchFormDto {

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
   * @return A list with all filter values.
   */
  public Map<Field, String> getAllFilters() {
    Map<Field, String> filterValues = new HashMap<>();

    // ScaleLevel
    if (StringUtils.hasText(this.scaleLevel)) {
      filterValues.put(VariableDocument.SCALE_LEVEL_FIELD, this.scaleLevel);
    }

    // Survey Title
    if (StringUtils.hasText(this.surveyTitle)) {
      filterValues.put(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD, this.surveyTitle);
    }

    return filterValues;
  }

  /**
   * @return Returns a List with all filternames.
   */
  public List<Field> getFilterNames() {
    List<Field> filterNames = new ArrayList<>();

    // ScaleLevel
    filterNames.add(VariableDocument.SCALE_LEVEL_FIELD);

    // Survey Title
    filterNames.add(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD);

    return filterNames;
  }

  /* GETTER / SETTER */
  public String getQuery() {
    return query;
  }

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
