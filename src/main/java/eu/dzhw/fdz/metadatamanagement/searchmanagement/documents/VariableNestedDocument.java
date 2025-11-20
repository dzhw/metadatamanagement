package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;

import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * NESTED subdocument used for filtering by variables.
 *
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class VariableNestedDocument extends AbstractNestedSubDocument {

  private static final long serialVersionUID = -3761244650513357760L;

  private String id;

  private I18nString label;

  private I18nString completeTitle;

  private String dataSetId;

  private List<String> surveyIds;

  private String dataPackageId;

  private String masterId;

  private String pid;

  private boolean shadow;

  private String successorId;

  /**
   * Create the subdocument.
   *
   * @param projection the projection coming from mongo.
   */
  public VariableNestedDocument(VariableSubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
    this.completeTitle = I18nString.builder()
        .de((projection.getLabel().getDe() != null ? projection.getLabel().getDe()
            : projection.getLabel().getEn()) + " (" + projection.getId() + ")")
        .en((projection.getLabel().getEn() != null ? projection.getLabel().getEn()
            : projection.getLabel().getDe()) + " (" + projection.getId() + ")")
        .build();
  }
}
