package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataFormat;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Data Product which can be ordered by a customer.
 *
 * @author René Reitmann
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Schema(
    description = "Go <a href='https://dzhw.github.io/metadatamanagement/"
    + "eu/dzhw/fdz/metadatamanagement/ordermanagement/domain/Product.html'>here</a>"
    + " for further details.")
public class Product implements Serializable {

  private static final long serialVersionUID = -1403870156469073381L;

  /**
   * The id of the {@link DataAcquisitionProject} in which this product was generated.
   *
   * Must not be empty.
   */
  @NotEmpty
  private String dataAcquisitionProjectId;

  /**
   * The (partial) {@link OrderedDataPackage} of this product.
   *
   * Must not be empty.
   *
   * @deprecated will be renamed to dataPackage in alignment with DLP
   */
  @NotNull
  @Valid
  private OrderedDataPackage study;
  
  /**
   * The (partial) {@link OrderedDataPackage} of this product.
   *
   * Must not be empty.
   */
  @NotNull
  @Valid
  private OrderedDataPackage dataPackage;

  /**
   * The access way to the {@link DataSet}s which the customer wants to have.
   */
  @NotEmpty
  private String accessWay;

  /**
   * The version of the {@link DataSet}s which the customer wants to have.
   */
  @NotEmpty
  private String version;

  /**
   * The available data formats of the dataPackage. It must not be empty.
   */
  @NotEmpty
  private Set<DataFormat> dataFormats;
}
