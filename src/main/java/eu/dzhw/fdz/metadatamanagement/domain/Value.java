
package eu.dzhw.fdz.metadatamanagement.domain;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.base.MoreObjects;

import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * The value includes the value itself, a label and frequencies. There are no calculations of the
 * frequencies.
 * 
 * @author Daniel Katzberg
 */
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
public class Value {

  @NotEmpty
  private String code;

  private I18nString label;

  private Integer absoluteFrequency;

  private BigDecimal relativeFrequency;

  @NotNull
  private boolean missing;

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("code", code)
      .add("label", label)
      .add("absoluteFrequency", absoluteFrequency)
      .add("relativeFrequency", relativeFrequency)
      .add("missing", missing)
      .toString();
  }

  /* GETTER / SETTER */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public I18nString getLabel() {
    return label;
  }

  public void setLabel(I18nString label) {
    this.label = label;
  }

  public Integer getAbsoluteFrequency() {
    return absoluteFrequency;
  }

  public void setAbsoluteFrequency(Integer absoluteFrequency) {
    this.absoluteFrequency = absoluteFrequency;
  }

  public BigDecimal getRelativeFrequency() {
    return relativeFrequency;
  }

  public void setRelativeFrequency(BigDecimal relativeFrequency) {
    this.relativeFrequency = relativeFrequency;
  }

  public boolean isMissing() {
    return missing;
  }

  public void setMissing(boolean missing) {
    this.missing = missing;
  }
}
