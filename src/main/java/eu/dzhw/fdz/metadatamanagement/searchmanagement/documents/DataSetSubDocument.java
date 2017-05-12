package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;

/**
 * Attributes of a data set which are stored in other search documents.
 *  
 * @author René Reitmann
 */
public class DataSetSubDocument implements DataSetSubDocumentProjection {

  private String id;
  
  private String dataAcquisitionProjectId;
  
  private I18nString description;
  
  private I18nString format;
  
  private Integer number;
  
  private I18nString annotations;

  public DataSetSubDocument() {
    super();
  }
  
  public DataSetSubDocument(DataSetSubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
  }
  
  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

  @Override
  public I18nString getDescription() {
    return description;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }

  @Override
  public I18nString getFormat() {
    return format;
  }

  public void setFormat(I18nString format) {
    this.format = format;
  }

  @Override
  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  @Override
  public I18nString getAnnotations() {
    return annotations;
  }

  public void setAnnotations(I18nString annotations) {
    this.annotations = annotations;
  }
}
