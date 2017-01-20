package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import javax.validation.constraints.Size;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * This statics class has statistical information about a variable. The information of this class
 * will be represented for a boxplot on the frontend.
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders")
public class Statistics {

  private Double meanValue;

  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.statistics.minimum.size")
  private String minimum;

  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.statistics.maximum.size")
  private String maximum;

  private Double skewness;

  private Double kurtosis;

  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.statistics.median.size")
  private String median;

  private Double standardDeviation;

  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.statistics.first-quartile.size")
  private String firstQuartile;

  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.statistics.third-quartile.size")
  private String thirdQuartile;

  private Double lowWhisker;

  private Double highWhisker;
  
  private String mode;
  
  private Double deviance;
 
  private Double meanDeviation;
  
  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("meanValue", meanValue)
      .add("minimum", minimum)
      .add("maximum", maximum)
      .add("skewness", skewness)
      .add("kurtosis", kurtosis)
      .add("median", median)
      .add("standardDeviation", standardDeviation)
      .add("firstQuartile", firstQuartile)
      .add("thirdQuartile", thirdQuartile)
      .add("lowWhisker", lowWhisker)
      .add("highWhisker", highWhisker)
      .add("mode", mode)
      .add("deviance", deviance)
      .add("meanDeviation", meanDeviation)
      .toString();
  }

  /* GETTER / SETTER */
  public Double getMeanValue() {
    return meanValue;
  }

  public void setMeanValue(Double meanValue) {
    this.meanValue = meanValue;
  }

  public Double getSkewness() {
    return skewness;
  }

  public void setSkewness(Double skewness) {
    this.skewness = skewness;
  }

  public Double getKurtosis() {
    return kurtosis;
  }

  public void setKurtosis(Double kurtosis) {
    this.kurtosis = kurtosis;
  }

  public Double getStandardDeviation() {
    return standardDeviation;
  }

  public void setStandardDeviation(Double standardDeviation) {
    this.standardDeviation = standardDeviation;
  }

  public Double getLowWhisker() {
    return lowWhisker;
  }

  public void setLowWhisker(Double lowWhisker) {
    this.lowWhisker = lowWhisker;
  }

  public Double getHighWhisker() {
    return highWhisker;
  }

  public void setHighWhisker(Double highWhisker) {
    this.highWhisker = highWhisker;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public Double getDeviance() {
    return deviance;
  }

  public void setDeviance(Double deviance) {
    this.deviance = deviance;
  }

  public Double getMeanDeviation() {
    return meanDeviation;
  }

  public void setMeanDeviation(Double meanDeviation) {
    this.meanDeviation = meanDeviation;
  }

  public String getMinimum() {
    return minimum;
  }

  public void setMinimum(String minimum) {
    this.minimum = minimum;
  }

  public String getMaximum() {
    return maximum;
  }

  public void setMaximum(String maximum) {
    this.maximum = maximum;
  }

  public String getMedian() {
    return median;
  }

  public void setMedian(String median) {
    this.median = median;
  }

  public String getFirstQuartile() {
    return firstQuartile;
  }

  public void setFirstQuartile(String firstQuartile) {
    this.firstQuartile = firstQuartile;
  }

  public String getThirdQuartile() {
    return thirdQuartile;
  }

  public void setThirdQuartile(String thirdQuartile) {
    this.thirdQuartile = thirdQuartile;
  }
}
