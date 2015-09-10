package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import net.karneim.pojobuilder.GeneratePojoBuilder;

import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * The SearchForm Data transfer object (dto). This dto
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.builders")
public class VariableSearchFormDto {

  private String query;

  private String scaleLevel;

  private String surveyTitel;

  @Valid
  private DateRange dateRange;

  /**
   * @return A list with all filter values.
   */
  public Map<String, String> getAllFilters() {
    Map<String, String> filterValues = new HashMap<>();

    // ScaleLevel
    if (StringUtils.hasText(this.scaleLevel)) {
      filterValues.put(VariableDocument.SCALE_LEVEL_FIELD, this.scaleLevel);
    }

    // Survey Title
    if (StringUtils.hasText(this.surveyTitel)) {
      filterValues.put(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD, this.surveyTitel);
    }

    return filterValues;
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

  public String getSurveyTitel() {
    return surveyTitel;
  }

  public void setSurveyTitel(String surveyTitel) {
    this.surveyTitel = surveyTitel;
  }

  public DateRange getDateRange() {
    return dateRange;
  }

  public void setDateRange(DateRange dateRange) {
    this.dateRange = dateRange;
  }
}
