
package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import javax.validation.constraints.NotNull;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * The missing includes a code, a label and frequencies. There are no calculations of the
 * frequencies. This represent a missing and not a valid response. Use {@code ValidResponse} for the
 * representation of valid responses.
 *
 * @author Daniel Katzberg
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders")
public class Missing {

  @NotNull(message = "variable-management.error.missing.code.not-null")
  private Integer code;

  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "variable-management.error.missing.label.i18n-string-size")
  private I18nString label;

  @NotNull(message = "variable-management.error.missing.absolute-frequency.not-null")
  private Integer absoluteFrequency;

  @NotNull(message = "variable-management.error.missing.relative-frequency.not-null")
  private Double relativeFrequency;

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
      .toString();
  }

  /* GETTER / SETTER */
  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
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

  public Double getRelativeFrequency() {
    return relativeFrequency;
  }

  public void setRelativeFrequency(Double relativeFrequency) {
    this.relativeFrequency = relativeFrequency;
  }
}
