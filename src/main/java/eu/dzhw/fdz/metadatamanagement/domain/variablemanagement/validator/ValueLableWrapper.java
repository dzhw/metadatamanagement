/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Daniel Katzberg
 *
 */
public class ValueLableWrapper {
  private String valueLable;

  public ValueLableWrapper(String valueLable) {
    this.valueLable = valueLable;
  }

  @Min(value = 1)
  @Max(value = 60)
  @NotEmpty
  String getValueLable() {
    return valueLable;
  }
}
