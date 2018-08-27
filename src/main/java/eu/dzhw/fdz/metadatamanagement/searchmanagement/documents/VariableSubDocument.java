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
public class VariableSubDocument extends AbstractSubDocument
    implements VariableSubDocumentProjection {
  private String id;
  
  private String dataAcquisitionProjectId;
  
  private String name;
  
  private I18nString label;
  
  private String dataSetId;
  
  private Integer dataSetNumber;

  private I18nString completeTitle;

  public VariableSubDocument() {
    super();
  }
  
  /**
   * Create the subdocument.
   * 
   * @param projection the projection coming from mongo.
   */
  public VariableSubDocument(VariableSubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
    this.completeTitle =
        I18nString.builder()
            .de((projection.getLabel().getDe() != null ? projection.getLabel().getDe()
                : projection.getLabel().getEn()) + " (" + projection.getId() + ")")
            .en((projection.getLabel().getEn() != null ? projection.getLabel().getEn()
                : projection.getLabel().getDe()) + " (" + projection.getId() + ")")
            .build();
  }
  
  /**
   * Create the subdocument.
   * 
   * @param variable the complete variable coming from mongo.
   */
  public VariableSubDocument(Variable variable) {
    super();
    BeanUtils.copyProperties(variable, this);
    this.completeTitle =
        I18nString.builder()
            .de((variable.getLabel().getDe() != null ? variable.getLabel().getDe()
                : variable.getLabel().getEn()) + " (" + variable.getId() + ")")
            .en((variable.getLabel().getEn() != null ? variable.getLabel().getEn()
                : variable.getLabel().getDe()) + " (" + variable.getId() + ")")
            .build();
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
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public I18nString getLabel() {
    return label;
  }

  public void setLabel(I18nString label) {
    this.label = label;
  }

  @Override
  public String getDataSetId() {
    return dataSetId;
  }

  public void setDataSetId(String dataSetId) {
    this.dataSetId = dataSetId;
  }

  @Override
  public Integer getDataSetNumber() {
    return dataSetNumber;
  }

  public void setDataSetNumber(Integer dataSetNumber) {
    this.dataSetNumber = dataSetNumber;
  }

  @Override
  public I18nString getCompleteTitle() {
    return completeTitle;
  }
}
