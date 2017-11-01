package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidIsoLanguage;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidInstrumentAttachmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Metadata which will be stored in GridFS with each attachment for instruments.
 *
 * @author René Reitmann
 */
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class InstrumentAttachmentMetadata extends AbstractRdcDomainObject {
  @NotEmpty(message =
      "instrument-management.error.instrument-attachment-metadata.instrument-id.not-empty")
  private String instrumentId;

  @NotEmpty(message =
      "instrument-management.error.instrument-attachment-metadata.project-id.not-empty")
  private String dataAcquisitionProjectId;

  @NotNull(message =
      "instrument-management.error.instrument-attachment-metadata.type.not-null")
  @I18nStringSize(min = 1, max = StringLengths.SMALL, message =
      "instrument-management.error.instrument-attachment-metadata.type.i18n-string-size")
  @ValidInstrumentAttachmentType(message =
      "instrument-management.error.instrument-attachment-metadata.type.valid-type")
  private I18nString type;

  @NotNull(message =
      "instrument-management.error.instrument-attachment-metadata.description.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM, message =
      "instrument-management.error.instrument-attachment-metadata.description.i18n-string-size")
  @I18nStringNotEmpty(message = "instrument-management.error.instrument-attachment-metadata."
      + "description.i18n-string-not-empty")
  private I18nString description;

  @NotNull(message =
      "instrument-management.error.instrument-attachment-metadata.language.not-null")
  @ValidIsoLanguage(message =
      "instrument-management.error.instrument-attachment-metadata.language.not-supported")
  private String language;

  @NotEmpty(message =
      "instrument-management.error.instrument-attachment-metadata.filename.not-empty")
  private String fileName;

  @NotNull(message =
      "instrument-management.error.instrument-attachment-metadata.instrument-number.not-null")
  private Integer instrumentNumber;
  
  @NotNull(message =
      "instrument-management.error.instrument-attachment-metadata.index-in-instrument.not-null")
  private Integer indexInInstrument;

  @Override
  public String getId() {
    return "/public/files/instruments/" + instrumentId + "/attachments/" + fileName;
  }
}
