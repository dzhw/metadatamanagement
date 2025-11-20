package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.projection.AnalysisPackageSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * NESTED subdocument used for filtering by analysisPackages.
 *
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class AnalysisPackageNestedDocument extends AbstractNestedSubDocument {

  private static final long serialVersionUID = -894338797585607648L;

  private String id;

  private I18nString title;

  private I18nString completeTitle;

  private String masterId;

  private boolean shadow;

  private String successorId;

  /**
   * Create a AnalysisPackageNestedDocument from a projection.
   *
   * @param projection an analysisPackage projection
   */
  public AnalysisPackageNestedDocument(AnalysisPackageSubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
    this.completeTitle = I18nString.builder()
        .de((projection.getTitle().getDe() != null ? projection.getTitle().getDe()
            : projection.getTitle().getEn()) + " (" + projection.getId() + ")")
        .en((projection.getTitle().getEn() != null ? projection.getTitle().getEn()
            : projection.getTitle().getDe()) + " (" + projection.getId() + ")")
        .build();
  }
}
