package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;

/**
 * Metadata which will be stored in GridFS with each attachment for instruments.
 * 
 * @author René Reitmann
 */
public class InstrumentAttachmentMetadata {
  @NotEmpty
  private String instrumentId;
  
  @NotEmpty
  private String dataAcquisitionProjectId;
  
  @NotNull
  @I18nStringSize(min = 1, max = StringLengths.SMALL)
  private I18nString type;
  
  @NotNull
  @I18nStringSize(min = 1, max = StringLengths.MEDIUM)
  private I18nString title;
  
  //TODO rreitmann: add validation messages
  @NotEmpty
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
