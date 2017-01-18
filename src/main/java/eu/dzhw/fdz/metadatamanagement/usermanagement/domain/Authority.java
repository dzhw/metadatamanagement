package eu.dzhw.fdz.metadatamanagement.usermanagement.domain;


import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * An authority (a security role) used by Spring Security.
 */
@Document(collection = "jhi_authority")
public class Authority implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull(message = "user-management.error.authority.name.not-null")
  @Size(min = 0, max = 50)
  @Id
  private String name;

  public Authority() {
  }
  
  public Authority(String name) {
    this.name = name;
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    Authority authority = (Authority) object;

    if (name != null ? !name.equals(authority.name) : authority.name != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Authority{" + "name='" + name + '\'' + "}";
  }
}
