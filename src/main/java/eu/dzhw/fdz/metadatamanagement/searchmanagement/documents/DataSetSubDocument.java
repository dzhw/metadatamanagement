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

  public DataSetSubDocument() {
    super();
  }
  
  public DataSetSubDocument(DataSetSubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
  }
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

  public I18nString getDescription() {
    return description;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }

  public I18nString getFormat() {
    return format;
  }

  public void setFormat(I18nString format) {
    this.format = format;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }
}
