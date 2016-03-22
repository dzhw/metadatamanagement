package eu.dzhw.fdz.metadatamanagement.domain;

import com.google.common.base.MoreObjects;

import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * FilterDetails of variable.
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
public class FilterDetails {
  
  private String filterExpression;

  private I18nString filterDescription;

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
