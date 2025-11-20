package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;

import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * NESTED subdocument used for filtering by instruments.
 *
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class InstrumentNestedDocument extends AbstractNestedSubDocument {

  private static final long serialVersionUID = 2575112956356021554L;

  private String id;

  private I18nString description;

  private I18nString completeTitle;

  private String dataPackageId;

  private List<String> surveyIds;

  private String masterId;

  private boolean shadow;

  private String successorId;

  /**
   * Create the subdocument.
   *
   * @param projection The projection loaded from mongo.
   */
  public InstrumentNestedDocument(InstrumentSubDocumentProjection projection) {
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
