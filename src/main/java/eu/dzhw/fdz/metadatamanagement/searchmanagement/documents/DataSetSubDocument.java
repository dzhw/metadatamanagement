package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;
import java.util.stream.Collectors;

import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.SubDataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Attributes of a data set which are stored in other search documents.
 *
 * @author René Reitmann
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class DataSetSubDocument extends AbstractRdcDomainObject
    implements DataSetSubDocumentProjection {

  private static final long serialVersionUID = -8248418426807103732L;

  private String id;

  private String dataAcquisitionProjectId;

  private I18nString description;

  private I18nString type;

  private I18nString format;

  private Integer number;

  private List<SubDataSet> subDataSets;

  private List<String> languages;

  private Integer maxNumberOfObservations;

  private List<String> accessWays;

  private String dataPackageId;

  private List<String> surveyIds;

  private String successorId;

  private boolean shadow;

  private String masterId;

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
  }
}
