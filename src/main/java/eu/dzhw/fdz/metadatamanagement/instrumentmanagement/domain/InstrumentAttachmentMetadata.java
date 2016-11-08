package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Metadata which will be stored in GridFS with each attachment for instruments.
 * 
 * @author René Reitmann
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.builders")
public class InstrumentAttachmentMetadata {
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
  private I18nString type;
  
  @NotNull(message = 
      "instrument-management.error.instrument-attachment-metadata.title.not-null")
  @I18nStringSize(min = 1, max = StringLengths.MEDIUM, message =
      "instrument-management.error.instrument-attachment-metadata.title.i18n-string-size")
  private I18nString title;
  
  @NotEmpty(message = 
      "instrument-management.error.instrument-attachment-metadata.filename.not-empty")
  private String fileName;

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

  public I18nString getTitle() {
    return title;
  }

  public void setTitle(I18nString title) {
    this.title = title;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
}
