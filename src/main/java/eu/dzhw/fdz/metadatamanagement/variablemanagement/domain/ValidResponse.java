
package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * The value includes the value itself, a label and frequencies. There are no calculations of the
 * frequencies.
 * 
 * @author Daniel Katzberg
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders")
public class ValidResponse {

  @I18nStringSize(max = StringLengths.LARGE,
      message = "variable-management.error.validResponse.label.i18nStringSize")
  private I18nString label;

  @NotNull(
      message = "variable-management.error.validResponse.absoluteFrequency.not-null")
  private Integer absoluteFrequency;

  @NotNull(
      message = "variable-management.error.validResponse.relativeFrequency.not-null")
  private Double relativeFrequency;

  @NotNull(message = "variable-management.error.validResponse.value.not-null")
  @Size(max = StringLengths.SMALL, 
      message = "variable-management.error.validResponse.value.size")
  private String value;

  @NotNull(
      message = "variable-management.error.validResponse.validRelativeFrequency.not-null")
  private Double validRelativeFrequency;

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("label", label)
      .add("absoluteFrequency", absoluteFrequency)
      .add("relativeFrequency", relativeFrequency)
      .add("value", value)
      .add("validRelativeFrequency", validRelativeFrequency)
      .toString();
  }

  /* GETTER / SETTER */
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

  public Double getRelativeFrequency() {
    return relativeFrequency;
  }

  public void setRelativeFrequency(Double relativeFrequency) {
    this.relativeFrequency = relativeFrequency;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Double getValidRelativeFrequency() {
    return validRelativeFrequency;
  }

  public void setValidRelativeFrequency(Double validRelativeFrequency) {
    this.validRelativeFrequency = validRelativeFrequency;
  }
}
