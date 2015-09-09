package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

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

  private String query;

  private String scaleLevel;

  /**
   * @return A list with all filter values.
   */
  public List<String> getAllFilterValues() {
    List<String> filterValues = new ArrayList<>();

    // ScaleLevel
    if (StringUtils.hasText(this.scaleLevel)) {
      filterValues.add(this.scaleLevel);
    }

    return filterValues;
  }

  /**
   * @return Return true if in any field is text.
   */
  public boolean hasTextInAnyField() {
    return StringUtils.hasText(this.query) || StringUtils.hasText(this.scaleLevel);
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
}
