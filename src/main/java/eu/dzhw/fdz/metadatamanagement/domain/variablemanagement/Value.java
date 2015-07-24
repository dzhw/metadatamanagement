package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator;

import java.util.List;

import javax.validation.Valid;

/**
 * This value class includes a list of values and the depending value lables.
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
   * A list of lables for the answer values.
   */
  @Valid
  private List<ValueLableWrapper> valueLables;

  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }

  public List<ValueLableWrapper> getValueLables() {
    return valueLables;
  }

  public void setValueLables(List<ValueLableWrapper> valueLables) {
    this.valueLables = valueLables;
  }
}
