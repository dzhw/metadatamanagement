package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.javers.core.metamodel.annotation.Entity;
import org.springframework.data.annotation.Id;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidIsoLanguage;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Metadata which will be stored with each attachment of a {@link DataSet}.
 */
@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class DataSetAttachmentMetadata extends AbstractShadowableRdcDomainObject {

  private static final long serialVersionUID = 4092063953336360313L;

  /**
   * The id of the attachment. Holds the complete path which can be used to download the file.
   */
  @Id
  @Setter(AccessLevel.NONE)
  private String id;

  @Setter(AccessLevel.NONE)
  private String masterId;

  /**
   * The id of the {@link DataSet} to which this attachment belongs.
   *
   * Must not be empty.
   */
  @NotEmpty(message =
      "data-set-management.error.data-set-attachment-metadata.data-set-id.not-empty")
  private String dataSetId;

  /**
   * The id of the {@link DataAcquisitionProject} to which the {@link DataSet} of this attachment
   * belongs.
   *
   * Must not be empty.
   */
  @NotEmpty(message =
      "data-set-management.error.data-set-attachment-metadata.project-id.not-empty")
  private String dataAcquisitionProjectId;

  /**
   * A description for this attachment.
   *
   * It must be specified in at least one language and it must not contain more than 512 characters.
   */
  @NotNull(message =
      "data-set-management.error.data-set-attachment-metadata.description.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM, message =
      "data-set-management.error.data-set-attachment-metadata.description.i18n-string-size")
  @I18nStringNotEmpty(message = "data-set-management.error.data-set-attachment-metadata."
      + "description.i18n-string-not-empty")
  private I18nString description;

  /**
   * The title of the attachment in the language of the attachment.
   *
   * Must not be empty and must not contain more than 2048 characters.
   */
  @NotEmpty(message =
      "data-set-management.error.data-set-attachment-metadata.title.not-null")
  @Size(max = StringLengths.LARGE, message =
      "data-set-management.error.data-set-attachment-metadata.title.string-size")
  private String title;

  /**
   * The language of the attachments content.
   *
   * Must not be empty and must be specified as ISO 639 language code.
   */
  @NotNull(message =
      "data-set-management.error.data-set-attachment-metadata.language.not-null")
  @ValidIsoLanguage(message =
      "data-set-management.error.data-set-attachment-metadata.language.not-supported")
  private String language;

  /**
   * The filename of the attachment.
   *
   * Must not be empty and must contain only (german) alphanumeric characters and "_" and "-" and
   * ".".
   */
  @NotEmpty(message =
      "data-set-management.error.data-set-attachment-metadata.filename.not-empty")
  @Pattern(message =
      "data-set-management.error.data-set-attachment-metadata.filename.not-valid",
      regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT)
  private String fileName;

  /**
   * The number of the {@link DataSet} to which this attachment belongs.
   *
   * Must not be empty.
   */
  @NotNull(message =
      "data-set-management.error.data-set-attachment-metadata.data-set-number.not-null")
  private Integer dataSetNumber;

  /**
   * The index in the {@link DataSet} of this attachment. Used for sorting the attachments of this
   * {@link DataSet}.
   *
   * Must not be empty.
   */
  @NotNull(message =
      "data-set-management.error.data-set-attachment-metadata.index-in-data-set.not-null")
  private Integer indexInDataSet;

  /**
   * The doi of the attachment.
   *
   * Must not contain more than 512 characters.
   *
   * Must match the pattern of a doi-url https://doi.org/{id}
   */
  @Size(max = StringLengths.MEDIUM, message = "attachment.error.doi.size")
  @Pattern(
    message = "data-set-management.error.data-set-attachment-metadata.filename.not-valid",
    regexp = Patterns.DOI)
  private String doi;

  /**
   * Generate the id of this attachment from the dataSetId and the fileName.
   */
  public void generateId() {
    this.setId("/public/files/data-sets/" + dataSetId + "/attachments/" + fileName);
  }

  @Override
  protected void setMasterIdInternal(String masterId) {
    this.masterId = masterId;
  }

  @Override
  protected void setIdInternal(String id) {
    this.id = id;
  }
}
