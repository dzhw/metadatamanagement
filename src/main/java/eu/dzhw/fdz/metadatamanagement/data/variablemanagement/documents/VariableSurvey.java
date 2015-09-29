package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractNestedSurvey;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Create;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Edit;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * This class is a representation of a survey. This is a nested object of a survey variable.
 * 
 * @see VariableDocument
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders")
public class VariableSurvey extends AbstractNestedSurvey {

  // Public constants which are used in queries as fieldnames.
  public static final String VARIABLE_ALIAS_FIELD = "variableAlias";

  /**
   * The alias is by default a copy of the {@code VariableDocument.getName()}. It will be used for
   * the front end. If the alias is different from the {@code VariableDocument.getName()}, the
   * system displays this alias.
   */
  @Size(max = 32, groups = {Create.class, Edit.class})
  @NotBlank(groups = {Create.class, Edit.class})
  private String variableAlias;

  public VariableSurvey() {
    super();
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), variableAlias);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (object != null && getClass() == object.getClass()) {
      if (!super.equals(object)) {
        return false;
      }  
      VariableSurvey that = (VariableSurvey) object;
      return Objects.equal(this.variableAlias, that.variableAlias);
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("super", super.toString())
        .add("variableAlias", variableAlias).toString();
  }

  /* GETTER / SETTER */
  public String getVariableAlias() {
    return variableAlias;
  }

  public void setVariableAlias(String alias) {
    this.variableAlias = alias;
  }
}
