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
  
  private I18nString description;
  
  private Integer number;
  
  private List<String> surveyIds;
  
  public InstrumentSubDocument() {
    super();
  }

  public InstrumentSubDocument(InstrumentSubDocumentProjection projection) {
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

  public I18nString getTitle() {
    return title;
  }

  public void setTitle(I18nString title) {
    this.title = title;
  }

  public I18nString getDescription() {
    return description;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public List<String> getSurveyIds() {
    return surveyIds;
  }

  public void setSurveyIds(List<String> surveyIds) {
    this.surveyIds = surveyIds;
  }
}
