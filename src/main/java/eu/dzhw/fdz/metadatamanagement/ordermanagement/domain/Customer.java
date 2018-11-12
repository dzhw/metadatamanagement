package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

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
public class Customer {

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
