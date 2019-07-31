package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.javers.core.metamodel.annotation.ValueObject;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation.ValidAccessWay;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.AccessWays;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A subdataset is part of a {@link DataSet} and describes the concrete analyzable file which is
 * accessible by a given access way.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ValueObject
public class SubDataSet implements Serializable {

  private static final long serialVersionUID = -818891534073744110L;

  /**
   * The filename of the subdataset without extension.
   *
   * Must not be empty and must not contain more than 32 characters.
   */
  @NotEmpty(message = "data-set-management.error.sub-data-set.name.not-empty")
  @Size(max = StringLengths.SMALL,
      message = "data-set-management.error.sub-data-set.name.size")
  private String name;

  /**
   * The number of rows (observations or episodes) which are present in this subdataset.
   *
   * Must not be empty.
   */
  @NotNull(message = "data-set-management.error.sub-data-set.number-of-observations.not-null")
  private Integer numberOfObservations;

  /**
   * The access way of this subdataset. Describes how the user will be able to work with the data
   * set.
   *
   * Must not be empty and be one of {@link AccessWays} but not {@link AccessWays#NOT_ACCESSIBLE}.
   */
  @NotNull(message = "data-set-management.error.sub-data-set.access-way.not-null")
  @ValidAccessWay(
      message = "data-set-management.error.sub-data-set.access-way.valid-access-way")
  private String accessWay;

  /**
   * A description for this subdataset.
   *
   * It must be specified in at least one language and it must not contain more than 512 characters.
   */
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "data-set-management.error.sub-data-set.description.i18n-string-size")
  @I18nStringNotEmpty(
      message = "data-set-management.error.sub-data-set.description.i18n-string-not-empty")
  private I18nString description;

  /**
   * A hint telling how to cite this subdataset in publications.
   *
   * It must be specified in at least one language and it must not contain more than 2048
   * characters.
   */
  @I18nStringSize(max = StringLengths.LARGE,
      message = "data-set-management.error.sub-data-set.citation-hint.i18n-string-size")
  @I18nStringNotEmpty(message = "data-set-management.error.sub-data-set.citation-hint."
      + "valid-citation")
  private I18nString citationHint;

  /**
   * Set of available file formats of the {@link SubDataSet}.
   */
  @NotEmpty(message = "data-set-management.error.sub-data-set.data-formats.not-empty")
  private Set<DataFormat> dataFormats;
}
