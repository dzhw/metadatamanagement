
package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidCodeOrValueClass;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * The value includes the value itself, a label and frequencies. There are no calculations of the
 * frequencies.
 * 
 * @author Daniel Katzberg
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders")
@ValidCodeOrValueClass(message = "{error.value.absoluteFrequency.validCodeOrValueClass}")
public class Value {

  private Integer code;

  @I18nStringSize(max = StringLengths.MEDIUM, message = "{error.value.label.i18nStringSize}")
  private I18nString label;

  @NotNull(message = "{error.value.absoluteFrequency.notNull}")
  private Integer absoluteFrequency;

  @NotNull(message = "{error.value.relativeFrequency.notNull}")
  private Double relativeFrequency;

  private boolean isAMissing = false;

  @Size(max = StringLengths.SMALL, message = "{error.value.valueClass.size}")
  private String valueClass;

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
      .add("isAMissing", isAMissing)
      .add("valueClass", valueClass)
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

  public boolean getIsAMissing() {
    return isAMissing;
  }

  public void setisAMissing(boolean isAMissing) {
    this.isAMissing = isAMissing;
  }

  public String getValueClass() {
    return valueClass;
  }

  public void setValueClass(String valueClass) {
    this.valueClass = valueClass;
  }

  public void setAMissing(boolean isAMissing) {
    this.isAMissing = isAMissing;
  }


}
