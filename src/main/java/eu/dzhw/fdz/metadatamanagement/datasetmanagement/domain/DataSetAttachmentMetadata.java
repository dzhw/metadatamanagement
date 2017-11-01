package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidIsoLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Metadata which will be stored in GridFS with each attachment for data sets.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class DataSetAttachmentMetadata extends AbstractRdcDomainObject {
  @NotEmpty(message =
      "data-set-management.error.data-set-attachment-metadata.data-set-id.not-empty")
  private String dataSetId;

  @NotEmpty(message =
      "data-set-management.error.data-set-attachment-metadata.project-id.not-empty")
  private String dataAcquisitionProjectId;

  @NotNull(message =
      "data-set-management.error.data-set-attachment-metadata.description.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM, message =
      "data-set-management.error.data-set-attachment-metadata.description.i18n-string-size")
  @I18nStringNotEmpty(message = "data-set-management.error.data-set-attachment-metadata."
      + "description.i18n-string-not-empty")
  private I18nString description;

  @NotNull(message =
      "data-set-management.error.data-set-attachment-metadata.title.not-null")
  @Size(max = StringLengths.LARGE, message =
      "data-set-management.error.data-set-attachment-metadata.title.string-size")
  private String title;

  @NotNull(message =
      "data-set-management.error.data-set-attachment-metadata.language.not-null")
  @ValidIsoLanguage(message =
      "data-set-management.error.data-set-attachment-metadata.language.not-supported")
  private String language;

  @NotEmpty(message =
      "data-set-management.error.data-set-attachment-metadata.filename.not-empty")
  private String fileName;

  @NotNull(message =
      "data-set-management.error.data-set-attachment-metadata.data-set-number.not-null")
  private Integer dataSetNumber;
  
  @NotNull(message =
      "data-set-management.error.data-set-attachment-metadata.index-in-data-set.not-null")
  private Integer indexInDataSet;

  @Override
  public String getId() {
    return "/public/files/data-sets/" + dataSetId + "/attachments/" + fileName;
  }
}
