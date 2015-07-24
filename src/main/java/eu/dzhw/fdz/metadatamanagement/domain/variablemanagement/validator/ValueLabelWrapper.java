package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * This class wraps a String for the validation of String in a List.
 * 
 * @author Daniel Katzberg
 *
 */
public class ValueLabelWrapper {
  private String valueLabel;

  public ValueLabelWrapper(String valueLabel) {
    this.valueLabel = valueLabel;
  }

  @Size(max = 60)
  @NotEmpty
  String getValueLabel() {
    return valueLabel;
  }
}
