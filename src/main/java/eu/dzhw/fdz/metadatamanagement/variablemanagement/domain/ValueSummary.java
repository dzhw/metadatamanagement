package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import javax.validation.constraints.NotNull;

import com.google.common.base.MoreObjects;

import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Domain object of the ValueSummary. Represent sums for all values of the variable.
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders")
public class ValueSummary {

  @NotNull(message = "{error.valueSummary.totalAbsoluteFrequency.notNull}")
  private Integer totalAbsoluteFrequency;

  @NotNull(message = "{error.valueSummary.totalValidAbsoluteFrequency.notNull}")
  private Integer totalValidAbsoluteFrequency;

  @NotNull(message = "{error.valueSummary.totalValidRelativeFrequency.notNull}")
  private Double totalValidRelativeFrequency;

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("totalAbsoluteFrequency", totalAbsoluteFrequency)
      .add("totalValidAbsoluteFrequency", totalValidAbsoluteFrequency)
      .add("totalValidRelativeFrequency", totalValidRelativeFrequency)
      .toString();
  }

  /*
   * GETTER / SETTER
   */
  public Integer getTotalAbsoluteFrequency() {
    return totalAbsoluteFrequency;
  }

  public void setTotalAbsoluteFrequency(Integer totalAbsoluteFrequency) {
    this.totalAbsoluteFrequency = totalAbsoluteFrequency;
  }

  public Integer getTotalValidAbsoluteFrequency() {
    return totalValidAbsoluteFrequency;
  }

  public void setTotalValidAbsoluteFrequency(Integer totalValidAbsoluteFrequency) {
    this.totalValidAbsoluteFrequency = totalValidAbsoluteFrequency;
  }

  public Double getTotalValidRelativeFrequency() {
    return totalValidRelativeFrequency;
  }

  public void setTotalValidRelativeFrequency(Double totalValidRelativeFrequency) {
    this.totalValidRelativeFrequency = totalValidRelativeFrequency;
  }
}
