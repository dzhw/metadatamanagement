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
   * @return This is a return message of a search dto. It contains all not null values of a search
   *         query and filters. Since the addition of filters, there are empty (null) values as a
   *         query. The error message was before like "found no results of the variable "null.")
   */
  public String getFillerForErrorMessage() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("(");

    if (StringUtils.hasText(this.query)) {
      buffer.append(this.query);
      buffer.append(",");
    }

    if (StringUtils.hasText(this.scaleLevel)) {
      buffer.append(this.scaleLevel);
      buffer.append(",");
    }
    buffer.append(")");

    // remove last ,
    return buffer.toString().replace(",)", ")");
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
