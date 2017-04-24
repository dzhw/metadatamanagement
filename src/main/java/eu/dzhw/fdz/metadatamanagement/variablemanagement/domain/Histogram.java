package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import com.google.common.base.MoreObjects;

import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Representation of data for a histogram.
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders")
public class Histogram {

  private Integer numberOfBins;

  private Double start;

  private Double end;

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("numberOfBins", numberOfBins)
      .add("start", start)
      .add("end", end)
      .toString();
  }

  /* GETTER / SETTER */
  public Integer getNumberOfBins() {
    return numberOfBins;
  }

  public void setNumberOfBins(Integer numberOfBins) {
    this.numberOfBins = numberOfBins;
  }

  public Double getStart() {
    return start;
  }

  public void setStart(Double start) {
    this.start = start;
  }

  public Double getEnd() {
    return end;
  }

  public void setEnd(Double end) {
    this.end = end;
  }
}
