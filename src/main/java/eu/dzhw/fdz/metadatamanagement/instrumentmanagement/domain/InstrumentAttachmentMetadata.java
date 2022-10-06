package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain;

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
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidInstrumentAttachmentType;
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
 * Metadata which will be stored with each attachment of a {@link Instrument}.
 */
@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class InstrumentAttachmentMetadata extends AbstractShadowableRdcDomainObject {

  private static final long serialVersionUID = -2708314412008289447L;

  /**
   * The id of the attachment. Holds the complete path which can be used to download the file.
   */
  @Id
  @Setter(AccessLevel.NONE)
  private String id;

  /**
   * The master id of the instrument attachment metadata.
   */
  @Setter(AccessLevel.NONE)
  private String masterId;

  /**
   * The id of the {@link Instrument} to which this attachment belongs.
   *
   * Must not be empty.
   */
  @NotEmpty(message =
      "instrument-management.error.instrument-attachment-metadata.instrument-id.not-empty")
  private String instrumentId;

  /**
   * The id of the {@link DataAcquisitionProject} to which the {@link Instrument} of this attachment
   * belongs.
   *
   * Must not be empty.
   */
  @NotEmpty(message =
      "instrument-management.error.instrument-attachment-metadata.project-id.not-empty")
  private String dataAcquisitionProjectId;

  /**
   * The type of this attachment.
   *
   * Must not be empty and must be one of {@link InstrumentAttachmentTypes}.
   */
  @NotNull(message =
      "instrument-management.error.instrument-attachment-metadata.type.not-null")
  @I18nStringSize(min = 1, max = StringLengths.SMALL, message =
      "instrument-management.error.instrument-attachment-metadata.type.i18n-string-size")
  @ValidInstrumentAttachmentType(message =
      "instrument-management.error.instrument-attachment-metadata.type.valid-type")
  private I18nString type;

  /**
   * A description for this attachment.
   *
   * It must be specified in at least one language and it must not contain more than 512 characters.
   */
  @NotNull(message =
      "instrument-management.error.instrument-attachment-metadata.description.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM, message =
      "instrument-management.error.instrument-attachment-metadata.description.i18n-string-size")
  @I18nStringNotEmpty(message = "instrument-management.error.instrument-attachment-metadata."
      + "description.i18n-string-not-empty")
  private I18nString description;


  /**
   * The language of the attachments content.
   *
   * Must not be empty and must be specified as ISO 639 language code.
   */
  @NotNull(message =
      "instrument-management.error.instrument-attachment-metadata.language.not-null")
  @ValidIsoLanguage(message =
      "instrument-management.error.instrument-attachment-metadata.language.not-supported")
  private String language;

  /**
   * The filename of the attachment.
   *
   * Must not be empty and must contain only (german) alphanumeric characters and "_" and "-" and
   * ".".
   */
  @NotEmpty(message =
      "instrument-management.error.instrument-attachment-metadata.filename.not-empty")
  @Pattern(message =
      "instrument-management.error.instrument-attachment-metadata.filename.not-valid",
       regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT)
  private String fileName;

  /**
   * The number of the {@link Instrument} to which this attachment belongs.
   *
   * Must not be empty.
   */
  @NotNull(message =
      "instrument-management.error.instrument-attachment-metadata.instrument-number.not-null")
  private Integer instrumentNumber;

  /**
   * The index in the {@link Instrument} of this attachment. Used for sorting the attachments of
   * this {@link Instrument}.
   *
   * Must not be empty.
   */
  @NotNull(message =
      "instrument-management.error.instrument-attachment-metadata.index-in-instrument.not-null")
  private Integer indexInInstrument;

  /**
   * The doi of the attachment.
   *
   * Must not contain more than 512 characters.
   *
   * Must match the pattern of a doi-url https://doi.org/{id}
   */
  @Size(max = StringLengths.MEDIUM, message = "attachment.error.doi.size")
  @Pattern(
    message = "instrument-management.error.instrument-attachment-metadata.filename.not-valid",
    regexp = Patterns.DOI)
  private String doi;

  /**
   * Generate the id of this attachment from the instrumentId and the fileName.
   */
  public void generateId() {
    this.setId("/public/files/instruments/" + instrumentId + "/attachments/" + fileName);
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
