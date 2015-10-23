package eu.dzhw.fdz.metadatamanagement.data.common.documents;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Create;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Edit;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.ValidDataType;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.ValidScaleLevel;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * This is a reduced version of a variable in comparison to the {@link VariableDocument}.
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.data.common.documents.builders")
public class RelatedVariable {

  // Basic Fields
  public static final DocumentField ID_FIELD = new DocumentField("id");
  public static final DocumentField NAME_FIELD = new DocumentField("name");
  public static final DocumentField DATA_TYPE_FIELD = new DocumentField("dataType");
  public static final DocumentField LABEL_FIELD = new DocumentField("label");
  public static final DocumentField SCALE_LEVEL_FIELD = new DocumentField("scaleLevel");

  /**
   * A fdzID as primary key for the identification of the variable of a survey.
   */
  @Id
  @Size(max = 32, groups = {Create.class, Edit.class})
  @NotBlank(groups = {Create.class, Edit.class})
  @Pattern(regexp = "^[0-9a-zA-Z_-]*", groups = {Create.class, Edit.class})
  private String id;

  /**
   * The name of the variable.
   */
  @Size(max = 32, groups = {Create.class, Edit.class})
  @NotBlank(groups = {Create.class, Edit.class})
  private String name;

  /**
   * The data type of the variable.
   */
  @ValidDataType(groups = {Create.class, Edit.class})
  private String dataType;

  /**
   * The label of the variable.
   */
  @Size(max = 80, groups = {Create.class, Edit.class})
  @NotBlank(groups = {Create.class, Edit.class})
  private String label;

  /**
   * A optional scale level of the variable, if the variable is e.g. not a String.
   */
  @ValidScaleLevel(groups = {Create.class, Edit.class})
  private String scaleLevel;

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(id, name, dataType, label, scaleLevel);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (object != null && getClass() == object.getClass()) {
      RelatedVariable that = (RelatedVariable) object;
      return Objects.equal(this.id, that.id) && Objects.equal(this.name, that.name)
          && Objects.equal(this.dataType, that.dataType) && Objects.equal(this.label, that.label)
          && Objects.equal(this.scaleLevel, that.scaleLevel);
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).add("name", name)
        .add("dataType", dataType).add("label", label).add("scaleLevel", scaleLevel).toString();
  }

  /* GETTER / SETTER */
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getScaleLevel() {
    return scaleLevel;
  }

  public void setScaleLevel(String scaleLevel) {
    this.scaleLevel = scaleLevel;
  }
}
