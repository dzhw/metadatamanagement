package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataFormat;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import io.swagger.annotations.ApiModel;
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
@ApiModel(
    description = "Go <a href='https://metadatamanagement.readthedocs.io/de/stable/javadoc/eu/dzhw/"
    + "fdz/metadatamanagement/ordermanagement/domain/Product.html'>here</a> for further details.")
public class Product {

  /**
   * The id of the {@link DataAcquisitionProject} in which this product was generated.
   * 
   * Must not be empty.
   */
  @NotEmpty
  private String dataAcquisitionProjectId;

  /**
   * The (partial) {@link OrderedStudy} of this product.
   * 
   * Must not be empty.
   */
  @NotNull
  @Valid
  private OrderedStudy study;

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
   * The available data formats of the study. It must not be empty.
   */
  @NotEmpty
  private Set<DataFormat> dataFormats;
}
