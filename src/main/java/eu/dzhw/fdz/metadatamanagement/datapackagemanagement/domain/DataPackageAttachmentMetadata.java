package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.javers.core.metamodel.annotation.Entity;
import org.javers.core.metamodel.annotation.TypeName;
import org.springframework.data.annotation.Id;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidIsoLanguage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.validation.ValidDataPackageAttachmentType;
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
 * Metadata which will be stored with each attachment of a {@link DataPackage}.
 */
@Entity
@TypeName("eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudyAttachmentMetadata")
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class DataPackageAttachmentMetadata extends AbstractShadowableRdcDomainObject {

  private static final long serialVersionUID = 6430402823602559992L;

  /**
   * The id of the attachment. Holds the complete path which can be used to download the file.
   */
  @Id
  @Setter(AccessLevel.NONE)
  private String id;

  /**
   * The master id of the dataPackage attachment.
   */
  @Setter(AccessLevel.NONE)
  private String masterId;

  /**
   * The id of the {@link DataPackage} to which this attachment belongs.
   *
   * Must not be empty.
   */
  @NotEmpty(message = "data-package-management.error.data-package-attachment-metadata"
      + ".data-package-id.not-empty")
  private String dataPackageId;

  /**
   * The id of the {@link DataAcquisitionProject} to which the {@link DataPackage} of this
   * attachment belongs.
   *
   * Must not be empty.
   */
  @NotEmpty(message = "data-package-management.error.data-package-attachment-metadata"
      + ".project-id.not-empty")
  private String dataAcquisitionProjectId;

  /**
   * The index in the {@link DataPackage} of this attachment. Used for sorting the attachments of
   * this {@link DataPackage}.
   *
   * Must not be empty.
   */
  @NotNull(message = "data-package-management.error.data-package-attachment-metadata"
      + ".index-in-data-package.not-null")
  private Integer indexInDataPackage;

  /**
   * An optional title of this attachment in the attachments' language.
   *
   * Must not be empty and it must not contain more than 2048 characters.
   */
  @NotEmpty(
      message = "data-package-management.error.data-package-attachment-metadata.title.not-null")
  @Size(max = StringLengths.LARGE,
      message = "data-package-management.error.data-package-attachment-metadata.title.string-size")
  private String title;

  /**
   * A description for this attachment.
   *
   * It must be specified in at least one language and it must not contain more than 512 characters.
   */
  @NotNull(message = "data-package-management.error.data-package-attachment-metadata.description."
      + "not-null")
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "data-package-management.error.data-package-attachment-metadata.description"
          + ".i18n-string-size")
  @I18nStringNotEmpty(message = "data-package-management.error.data-package-attachment-metadata."
      + "description.i18n-string-not-empty")
  private I18nString description;

  /**
   * Additional details required to generate a citation hint for Method Reports.
   *
   * Can be null for other attachment types than method reports. Can also be null for legacy method
   * reports.
   */
  @Valid
  private MethodReportCitationDetails citationDetails;

  /**
   * The type of the attachment.
   *
   * Must be one of {@link DataPackageAttachmentTypes} and must not be empty.
   */
  @NotNull(message = "data-package-management.error.data-package-attachment-metadata.type.not-null")
  @I18nStringSize(min = 1, max = StringLengths.SMALL,
      message = "data-package-management.error.data-package-attachment-metadata.type."
          + "i18n-string-size")
  @ValidDataPackageAttachmentType(
      message = "data-package-management.error.data-package-attachment-metadata.type.valid-type")
  private I18nString type;

  /**
   * The filename of the attachment.
   *
   * Must not be empty and must contain only (german) alphanumeric characters and "_" and "-" and
   * ".".
   */
  @NotEmpty(
      message = "data-package-management.error.data-package-attachment-metadata.filename.not-empty")
  @Pattern(
      message = "data-package-management.error.data-package-attachment-metadata.filename.not-valid",
      regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT)
  private String fileName;

  /**
   * The language of the attachments content.
   *
   * Must not be empty and must be specified as ISO 639 language code.
   */
  @NotNull(
      message = "data-package-management.error.data-package-attachment-metadata.language.not-null")
  @ValidIsoLanguage(
      message = "data-package-management.error.data-package-attachment-metadata.language."
          + "not-supported")
  private String language;

  /**
   * The doi of the attachment.
   *
   * Must not contain more than 512 characters.
   * 
   * Must match the pattern of a doi-url https://doi.org/{id}
   */
  @Size(max = StringLengths.MEDIUM, message = "attachment.error.doi.size")
  @Pattern(
        message = "data-package-management.error.data-package-attachment-metadata.filename.not-valid",
        regexp = Patterns.DOI)
  private String doi;

  /**
   * Generate the id of this attachment from the dataPackageId and the fileName.
   */
  public void generateId() {
    // hack to satisfy javers
    this.setId("/public/files/data-packages/" + dataPackageId + "/attachments/" + fileName);
  }

  @Override
  protected void setMasterIdInternal(String masterId) {
    this.masterId = masterId;
  }

  /**
   * Returns the master id of the dataPackage attachment.
   *
   * @return Master Id
   */
  @Override
  public String getMasterId() {
    return masterId;
  }

  @Override
  protected void setIdInternal(String id) {
    this.id = id;
  }
}
