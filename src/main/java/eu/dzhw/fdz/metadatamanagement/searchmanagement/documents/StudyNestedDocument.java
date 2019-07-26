package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection.StudySubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * NESTED subdocument used for filtering by studies.
 * 
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class StudyNestedDocument extends AbstractNestedSubDocument {

  private static final long serialVersionUID = -894338797585607648L;

  private String id;

  private I18nString title;

  private I18nString completeTitle;

  private String masterId;
  
  private boolean shadow;
  
  private String successorId;

  /**
   * Create a StudyNesteddocument from a projection.
   * 
   * @param projection a study projection
   */
  public StudyNestedDocument(StudySubDocumentProjection projection) {
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
