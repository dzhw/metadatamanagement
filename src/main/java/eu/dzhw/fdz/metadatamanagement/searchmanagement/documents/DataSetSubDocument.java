package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.SubDataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;

/**
 * Attributes of a data set which are stored in other search documents.
 *  
 * @author René Reitmann
 */
public class DataSetSubDocument extends AbstractSubDocument
    implements DataSetSubDocumentProjection {
  private String id;
  
  private String dataAcquisitionProjectId;
  
  private I18nString description;
  
  private I18nString type;
  
  private I18nString format;
  
  private Integer number;
  
  private List<SubDataSet> subDataSets;
  
  private Integer maxNumberOfObservations;
  
  private List<String> accessWays;

  private I18nString completeTitle;
 
  /**
   * Create the sub document from the given projection.
   * @param projection the data set projection
   */
  public DataSetSubDocument(DataSetSubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
    this.maxNumberOfObservations = projection.getSubDataSets().stream()
        .map(subDataSet -> subDataSet.getNumberOfObservations()).reduce(Integer::max).get();
    this.accessWays = projection.getSubDataSets().stream()
        .map(subDataSet -> subDataSet.getAccessWay()).collect(Collectors.toList());
    this.completeTitle = I18nString.builder()
        .de((projection.getDescription().getDe() != null ? projection.getDescription().getDe()
            : projection.getDescription().getEn()) + " (" + projection.getId() + ")")
        .en((projection.getDescription().getEn() != null ? projection.getDescription().getEn()
            : projection.getDescription().getDe()) + " (" + projection.getId() + ")")
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
  public I18nString getType() {
    return type;
  }
  
  public void setType(I18nString type) {
    this.type = type;
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
  public List<SubDataSet> getSubDataSets() {
    return subDataSets;
  }

  public void setSubDataSets(List<SubDataSet> subDataSets) {
    this.subDataSets = subDataSets;
  }

  public Integer getMaxNumberOfObservations() {
    return maxNumberOfObservations;
  }

  public List<String> getAccessWays() {
    return accessWays;
  }

  @Override
  public I18nString getCompleteTitle() {
    return completeTitle;
  }
}
