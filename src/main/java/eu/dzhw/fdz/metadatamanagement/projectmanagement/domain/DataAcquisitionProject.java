package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * The Data Acquisition Project collects all data which are going to be published by our RDC.
 *
 * @author Daniel Katzberg
 */
@Document(collection = "data_acquisition_projects")
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.builders")
public class DataAcquisitionProject extends AbstractRdcDomainObject {

  /* Domain Object Attributes */
  @Id
  @NotEmpty(message = "data-acquisition-project.error.data-acquisition-project.id.not-empty")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC,
      message = "data-acquisition-project.error.data-acquisition-project.id.pattern")
  @Size(max = StringLengths.SMALL,
      message = "data-acquisition-project.error.data-acquisition-project.id.size")
  private String id;
  
  @NotNull(message = "data-acquisition-project.error.data-acquisition-project."
      + "has-been-released-before.not-null")
  private boolean hasBeenReleasedBefore;
  
  @Valid
  private Release release;

  /*
   * (non-Javadoc)
   *
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#getId()
   */
  @Override
  public String getId() {
    return id;
  }

  /*
   * (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("id", id)
      .add("hasBeenReleasedBefore", hasBeenReleasedBefore)
      .add("release", release)
      .toString();
  }

  /* GETTER / SETTER */
  public void setId(String id) {
    this.id = id;
  }

  public Release getRelease() {
    return release;
  }

  public void setRelease(Release release) {
    this.release = release;
  }

  public boolean isHasBeenReleasedBefore() {
    return hasBeenReleasedBefore;
  }

  /**
   * If the has been released before field is set to true, it can never be set to false.
   * @param hasBeenReleasedBefore New value for the has been released before field.
   */
  public void setHasBeenReleasedBefore(boolean hasBeenReleasedBefore) {
    //if the element is changed once to true, it can never be back set to false.
    if (!this.hasBeenReleasedBefore) {
      this.hasBeenReleasedBefore = hasBeenReleasedBefore;
    }  
  }
}
