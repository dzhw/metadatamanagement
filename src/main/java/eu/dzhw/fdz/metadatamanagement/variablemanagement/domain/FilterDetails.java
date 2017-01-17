package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidFilterExpressionLanguage;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * FilterDetails of variable.
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders")
public class FilterDetails {
  
  @NotEmpty(message = "variable-management.error.filter-details.expression.not-empty")
  @Size(max = StringLengths.MEDIUM, message = "error.filter-details.filterExpression.size")
  private String expression;

  @I18nStringSize(max = StringLengths.LARGE,
      message = "variable-management.error.filter-details.description.i18n-string-size")
  private I18nString description;

  @NotEmpty(message = "variable-management.error.filter-details." 
      + "expressionLanguage.not-empty")
  @ValidFilterExpressionLanguage(
      message = "variable-management.error.filter-details." 
          + "expression-language.valid-filter-expression-language")
  private String expressionLanguage;
  
  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("expression", expression)
      .add("description", description)
      .add("expressionLanguage", expressionLanguage)
      .toString();
  }

  /* GETTER / SETTER */
  public String getExpression() {
    return expression;
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }

  public I18nString getDescription() {
    return description;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }

  public String getExpressionLanguage() {
    return expressionLanguage;
  }

  public void setExpressionLanguage(String expressionLanguage) {
    this.expressionLanguage = expressionLanguage;
  }
}
