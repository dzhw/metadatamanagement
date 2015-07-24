package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement;

import java.util.List;

import javax.validation.Valid;

import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator.ValueLabelWrapper;

/**
 * This value class includes a list of values and the depending value labels.
 * 
 * @author Daniel Katzberg
 *
 */
public class Value {

  /**
   * A list of answer values for the variable.
   */
  private List<String> values;

  /**
   * A list of labels for the answer values.
   */
  @Valid
  private List<ValueLabelWrapper> valueLabels;

  /**
   * Output is a summarize of the values (answer options). Example:
   * {@code Value [Value.values.size=5, Values.valueLabels.size=5]}
   * 
   * @return A String which will summarizes the object value.
   */
  @Override
  public String toString() {

    return "Value [Value.values.size=" + this.values.size() + ", "
        + "Values.valueLabels.size=" + this.valueLabels.size() + "]";
  }

  /* GETTER / SETTER */
  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }

  public List<ValueLabelWrapper> getValueLabels() {
    return valueLabels;
  }

  public void setValueLabels(List<ValueLabelWrapper> valueLabels) {
    this.valueLabels = valueLabels;
  }
}
