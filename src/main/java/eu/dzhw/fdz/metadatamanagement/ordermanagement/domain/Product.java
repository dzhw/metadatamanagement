package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain;

import java.io.Serializable;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataFormat;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.validation.AccessWayNotEmptyForDataPackage;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.validation.DataFormatsNotEmptyForDataPackage;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.validation.EitherAnalysisPackageOrDataPackageOrdered;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Go <a href='https://dzhw.github.io/metadatamanagement/"
    + "eu/dzhw/fdz/metadatamanagement/ordermanagement/domain/Product.html'>here</a>"
    + " for further details.")
@EitherAnalysisPackageOrDataPackageOrdered
@AccessWayNotEmptyForDataPackage
@DataFormatsNotEmptyForDataPackage
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
   * Either this or analysisPackage must be present.
   */
  @Valid
  private OrderedDataPackage dataPackage;


  /**
   * The (partial) {@link OrderedAnalysisPackage} of this product.
   *
   * Either this or dataPackage must be present.
   */
  @Valid
  private OrderedAnalysisPackage analysisPackage;

  /**
   * The access way to the {@link OrderedDataPackage} which the customer wants to have.
   *
   * It must not be empty if dataPackage is present.
   */
  private String accessWay;

  /**
   * The version of the {@link OrderedAnalysisPackage} or {@link OrderedDataPackage} which the
   * customer wants to have.
   */
  @NotEmpty
  private String version;

  /**
   * The available data formats of the {@link OrderedDataPackage}.
   *
   * It must not be empty if dataPackage is present.
   */
  private Set<DataFormat> dataFormats;
}
