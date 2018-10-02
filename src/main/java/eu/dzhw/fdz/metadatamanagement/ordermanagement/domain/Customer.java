package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Details of a customer who orders data products.
 * 
 * @author René Reitmann
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Customer {

  @NotEmpty
  private String name;

  @Email
  @NotEmpty
  private String email;
}
