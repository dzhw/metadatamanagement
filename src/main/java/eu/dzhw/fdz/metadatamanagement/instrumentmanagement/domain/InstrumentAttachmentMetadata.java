package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidInstrumentAttachmentType;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidIsoLanguage;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Metadata which will be stored in GridFS with each attachment for instruments.
 *
 * @author René Reitmann
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.builders")
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
      "instrument-management.error.instrument-attachment-metadata.title.not-null")
  @Size(max = StringLengths.MEDIUM, message =
      "instrument-management.error.instrument-attachment-metadata.title.string-size")
  private String title;

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

  public String getInstrumentId() {
    return instrumentId;
  }

  public void setInstrumentId(String instrumentId) {
    this.instrumentId = instrumentId;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

  public I18nString getType() {
    return type;
  }

  public void setType(I18nString type) {
    this.type = type;
  }

  public I18nString getDescription() {
    return description;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Integer getInstrumentNumber() {
    return instrumentNumber;
  }

  public void setInstrumentNumber(Integer instrumentNumber) {
    this.instrumentNumber = instrumentNumber;
  }

  @Override
  public String getId() {
    return "/public/files/instruments/" + instrumentId + "/attachments/" + fileName;
  }
}
