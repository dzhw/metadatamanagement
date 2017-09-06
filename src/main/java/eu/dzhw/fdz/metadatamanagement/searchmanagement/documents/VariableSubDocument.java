package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;

/**
 * Attributes of a variable which are stored in other search documents.
 *  
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
public class VariableSubDocument implements VariableSubDocumentProjection {
  private String id;
  
  private String dataAcquisitionProjectId;
  
  private String name;
  
  private I18nString label;
  
  private String dataSetId;
  
  private Integer dataSetNumber;

  public VariableSubDocument() {
    super();
  }
  
  public VariableSubDocument(VariableSubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
  }
  
  public VariableSubDocument(Variable variable) {
    super();
    BeanUtils.copyProperties(variable, this);
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public I18nString getLabel() {
    return label;
  }

  public void setLabel(I18nString label) {
    this.label = label;
  }

  public String getDataSetId() {
    return dataSetId;
  }

  public void setDataSetId(String dataSetId) {
    this.dataSetId = dataSetId;
  }

  public Integer getDataSetNumber() {
    return dataSetNumber;
  }

  public void setDataSetNumber(Integer dataSetNumber) {
    this.dataSetNumber = dataSetNumber;
  }
}
