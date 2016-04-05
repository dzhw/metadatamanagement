package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import com.google.common.base.MoreObjects;

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

  private Double minimum;

  private Double maximum;

  private Double skewness;

  private Double kurtosis;

  private Double median;

  private Double standardDeviation;

  private Double firstQuartile;

  private Double thirdQuartile;

  private Double lowWhisker;

  private Double highWhisker;

  /*
   * (non-Javadoc)
   * 
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
      .toString();
  }

  /* GETTER / SETTER */
  public Double getMeanValue() {
    return meanValue;
  }

  public void setMeanValue(Double meanValue) {
    this.meanValue = meanValue;
  }

  public Double getMinimum() {
    return minimum;
  }

  public void setMinimum(Double minimum) {
    this.minimum = minimum;
  }

  public Double getMaximum() {
    return maximum;
  }

  public void setMaximum(Double maximum) {
    this.maximum = maximum;
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

  public Double getMedian() {
    return median;
  }

  public void setMedian(Double median) {
    this.median = median;
  }

  public Double getStandardDeviation() {
    return standardDeviation;
  }

  public void setStandardDeviation(Double standardDeviation) {
    this.standardDeviation = standardDeviation;
  }

  public Double getFirstQuartile() {
    return firstQuartile;
  }

  public void setFirstQuartile(Double firstQuartile) {
    this.firstQuartile = firstQuartile;
  }

  public Double getThirdQuartile() {
    return thirdQuartile;
  }

  public void setThirdQuartile(Double thirdQuartile) {
    this.thirdQuartile = thirdQuartile;
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

}
