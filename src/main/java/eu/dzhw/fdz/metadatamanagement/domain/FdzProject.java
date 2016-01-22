package eu.dzhw.fdz.metadatamanagement.domain;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
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
  @Id
  @NotEmpty
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_SPACE)
  private String id;

  private String sufDoi;

  private String cufDoi;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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
      .add("sufDoi", sufDoi)
      .add("cufDoi", cufDoi)
      .toString();
  }
}
