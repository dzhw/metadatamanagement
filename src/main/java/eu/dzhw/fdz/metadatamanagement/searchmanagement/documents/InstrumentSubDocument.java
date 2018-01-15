package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;

/**
 * Attributes of a instrument which are stored in other search documents.
 *  
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
public class InstrumentSubDocument implements InstrumentSubDocumentProjection {

  private String id;
  
  private String dataAcquisitionProjectId;
  
  private I18nString title;
  
  private I18nString subtitle;
  
  private I18nString description;
  
  private Integer number;
  
  private String type;
  
  private List<String> surveyIds;
  
  public InstrumentSubDocument() {
    super();
  }

  public InstrumentSubDocument(InstrumentSubDocumentProjection projection) {
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
  public I18nString getTitle() {
    return title;
  }

  public void setTitle(I18nString title) {
    this.title = title;
  }

  @Override
  public I18nString getDescription() {
    return description;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }

  @Override
  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  @Override
  public List<String> getSurveyIds() {
    return surveyIds;
  }

  public void setSurveyIds(List<String> surveyIds) {
    this.surveyIds = surveyIds;
  }

  @Override
  public I18nString getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(I18nString subtitle) {
    this.subtitle = subtitle;
  }

  @Override
  public java.lang.String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }  
}
