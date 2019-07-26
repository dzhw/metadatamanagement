package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Details of a customer who has ordered {@link Product}s.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ApiModel(
    description = "Go <a href='https://metadatamanagement.readthedocs.io/de/stable/javadoc/eu/dzhw/"
    + "fdz/metadatamanagement/ordermanagement/domain/Customer.html'>here</a> for further details.")
public class Customer implements Serializable {

  private static final long serialVersionUID = -2116777327999664418L;

  /**
   * Name of the customer as given in the shopping cart.
   * 
   * Must not be empty.
   */
  @NotEmpty
  private String name;

  /**
   * Email address of the customer.
   * 
   * Must be a valid email address and must not be empty.
   */
  @Email
  @NotEmpty
  private String email;
}
