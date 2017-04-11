package eu.dzhw.fdz.metadatamanagement.common.domain;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.base.MoreObjects;

import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A representation of a person. 
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.common.domain.builders")
public class Person {

  @NotEmpty(message = "global.error.person.first-name.not-empty")
  private String firstName;
  
  private String middleName;
  
  @NotEmpty(message = "global.error.person.last-name.not-empty")
  private String lastName;
  
  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("firstName", firstName)
      .add("middleName", middleName)
      .add("lastName", lastName)
      .toString();
  }

  /* GETTER / SETTER */
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
