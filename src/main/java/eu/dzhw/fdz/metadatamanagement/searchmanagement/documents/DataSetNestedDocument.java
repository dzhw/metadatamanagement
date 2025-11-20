package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;

import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * NESTED subdocument used for filtering by dataSets.
 *
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class DataSetNestedDocument extends AbstractNestedSubDocument {

  private static final long serialVersionUID = -2760512563630819794L;

  private String id;

  private I18nString description;

  private I18nString completeTitle;

  private String dataPackageId;

  private List<String> surveyIds;

  private String masterId;

  private boolean shadow;

  private String successorId;

  /**
   * Create the sub document from the given projection.
   *
   * @param projection the data set projection
   */
  public DataSetNestedDocument(DataSetSubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
    this.completeTitle = I18nString.builder()
        .de((projection.getDescription().getDe() != null ? projection.getDescription().getDe()
            : projection.getDescription().getEn()) + " (" + projection.getId() + ")")
        .en((projection.getDescription().getEn() != null ? projection.getDescription().getEn()
            : projection.getDescription().getDe()) + " (" + projection.getId() + ")")
        .build();
  }
}
