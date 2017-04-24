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
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Metadata which will be stored in GridFS with each attachment for data sets.
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.builders")
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
  @Size(max = StringLengths.MEDIUM, message =
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

  /* GETTER / SETTER */
  public String getDataSetId() {
    return dataSetId;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public I18nString getDescription() {
    return description;
  }

  public String getTitle() {
    return title;
  }

  public String getLanguage() {
    return language;
  }

  public String getFileName() {
    return fileName;
  }

  public Integer getDataSetNumber() {
    return dataSetNumber;
  }

  public void setDataSetId(String dataSetId) {
    this.dataSetId = dataSetId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setDataSetNumber(Integer dataSetNumber) {
    this.dataSetNumber = dataSetNumber;
  }

  public Integer getIndexInDataSet() {
    return indexInDataSet;
  }

  public void setIndexInDataSet(Integer indexInDataSet) {
    this.indexInDataSet = indexInDataSet;
  }
}
