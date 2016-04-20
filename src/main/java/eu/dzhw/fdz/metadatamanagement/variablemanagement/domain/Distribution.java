package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.UniqueCode;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.UniqueValueClass;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Domain object of the ValueSummary. Represent sums for all values of the variable.
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders")
public class Distribution {

  @NotNull(message = "{error.valueSummary.totalAbsoluteFrequency.notNull}")
  private Integer totalAbsoluteFrequency;

  @NotNull(message = "{error.valueSummary.totalValidAbsoluteFrequency.notNull}")
  private Integer totalValidAbsoluteFrequency;

  @NotNull(message = "{error.valueSummary.totalValidRelativeFrequency.notNull}")
  private Double totalValidRelativeFrequency;

  /* Nested Objects */
  // No validation
  private Histogram histogram;

  // No validation
  private Statistics statistics;

  @UniqueCode(message = "{error.variable.values.uniqueValueCode}")
  @Valid
  private List<Missing> missings;

  @UniqueValueClass(message = "{error.variable.values.uniqueValueClass}")
  @Valid
  private List<ValidResponse> validResponses;


  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("totalAbsoluteFrequency", totalAbsoluteFrequency)
      .add("totalValidAbsoluteFrequency", totalValidAbsoluteFrequency)
      .add("totalValidRelativeFrequency", totalValidRelativeFrequency)
      .add("histogram", histogram)
      .add("statistics", statistics)
      .add("missings", missings)
      .add("validResponses", validResponses)
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

  public Statistics getStatistics() {
    return statistics;
  }

  public void setStatistics(Statistics statistics) {
    this.statistics = statistics;
  }

  public List<Missing> getMissings() {
    return missings;
  }

  public void setMissings(List<Missing> missings) {
    this.missings = missings;
  }

  public List<ValidResponse> getValidResponses() {
    return validResponses;
  }

  public void setValidResponses(List<ValidResponse> validResponses) {
    this.validResponses = validResponses;
  }

  public Histogram getHistogram() {
    return histogram;
  }

  public void setHistogram(Histogram histogram) {
    this.histogram = histogram;
  }
}
