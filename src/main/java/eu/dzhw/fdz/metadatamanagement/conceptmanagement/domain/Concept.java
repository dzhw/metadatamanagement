package eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Concept.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(collection = "concepts")
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.concept.domain.builders")
public class Concept extends AbstractRdcDomainObject {

  @Id
  @NotEmpty(message = "{error.concept.id.notEmpty}")
  private String id;

  private I18nString name;

  private I18nString description;


  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("id", id)
      .add("name", name)
      .add("description", description)
      .toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#getId()
   */
  @Override
  public String getId() {
    return this.id;
  }

  /* GETTER / SETTER */
  public I18nString getName() {
    return name;
  }

  public void setName(I18nString name) {
    this.name = name;
  }

  public I18nString getDescription() {
    return description;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }

  public void setId(String id) {
    this.id = id;
  }
}
