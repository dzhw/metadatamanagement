package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto;

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
public class SearchFormDto {

  private String query;

  private String scaleLevel;

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
