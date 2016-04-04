package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidFilterExpressionLanguage;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * FilterDetails of variable.
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
public class FilterDetails {
  
  @NotEmpty(message = "{error.filterDetails.filterExpression.notEmpty}")
  @Size(max = StringLengths.MEDIUM, message = "{error.filterDetails.filterExpression.size}")
  private String filterExpression;

  @I18nStringSize(max = StringLengths.LARGE,
      message = "{error.filterDetails.filterDescription.i18nStringSize}")
  private I18nString filterDescription;

  @NotEmpty(message = "{error.filterDetails.filterExpressionLanguage.notEmpty}")
  @ValidFilterExpressionLanguage(
      message = "{error.filterDetails.filterExpressionLanguage.validFilterExpressionLanguage}")
  private String filterExpressionLanguage;
  
  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("filterExpression", filterExpression)
      .add("filterDescription", filterDescription)
      .add("filterExpressionLanguage", filterExpressionLanguage)
      .toString();
  }

  /* GETTER / SETTER */
  public String getFilterExpression() {
    return filterExpression;
  }

  public void setFilterExpression(String filterExpression) {
    this.filterExpression = filterExpression;
  }

  public I18nString getFilterDescription() {
    return filterDescription;
  }

  public void setFilterDescription(I18nString filterDescription) {
    this.filterDescription = filterDescription;
  }

  public String getFilterExpressionLanguage() {
    return filterExpressionLanguage;
  }

  public void setFilterExpressionLanguage(String filterExpressionLanguage) {
    this.filterExpressionLanguage = filterExpressionLanguage;
  }

}
