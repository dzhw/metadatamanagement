package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Product which can be ordered by a customer.
 * 
 * @author René Reitmann
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Product {

  @NotEmpty
  private String dataAcquisitionProjectId;

  @NotNull
  @Valid
  private Study study;

  @NotEmpty
  private String accessWay;

  @NotEmpty
  private String version;
}
