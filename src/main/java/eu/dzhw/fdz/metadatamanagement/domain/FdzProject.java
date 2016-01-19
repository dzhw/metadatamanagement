package eu.dzhw.fdz.metadatamanagement.domain;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.domain.util.Patterns;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * The FDZ Project collects all data which are going to be published by our FDZ.
 */
@Document(collection = "fdz_projects")
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
public class FdzProject extends AbstractFdzDomainObject {
  @Indexed(unique = true)
  @NotEmpty
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_SPACE)
  private String name;

  private String sufDoi;

  private String cufDoi;
  
  @DBRef(lazy = true)
  @Transient
  private List<Variable> variables;

  public List<Variable> getVariables() {
    return variables;
  }

  public void setVariables(List<Variable> variables) {
    this.variables = variables;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSufDoi() {
    return sufDoi;
  }

  public void setSufDoi(String sufDoi) {
    this.sufDoi = sufDoi;
  }

  public String getCufDoi() {
    return cufDoi;
  }

  public void setCufDoi(String cufDoi) {
    this.cufDoi = cufDoi;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("name", name)
      .add("sufDoi", sufDoi)
      .add("cufDoi", cufDoi)
      .toString();
  }
}
