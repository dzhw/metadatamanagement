package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection.StudySubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Attributes of a study which are stored in other search documents.
 *  
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class StudySubDocument extends AbstractNestedSubDocument
    implements StudySubDocumentProjection {
  private String id;
  
  private String dataAcquisitionProjectId;
  
  private I18nString institution;
  
  private I18nString sponsor;
  
  private I18nString studySeries;
  
  private I18nString title;
  
  private List<Person> authors;
  
  private String doi;
  
  private I18nString surveyDesign;
  
  private I18nString completeTitle;

  public StudySubDocument() {
    super();
  }
  
  /**
   * Create a StudySubdocument from a projection and a doi.
   * @param projection a study projection
   * @param doi a doi or null
   */
  public StudySubDocument(StudySubDocumentProjection projection, String doi) {
    super();
    BeanUtils.copyProperties(projection, this);
    this.doi = doi;
    this.completeTitle = I18nString.builder()
        .de((projection.getTitle().getDe() != null ? projection.getTitle().getDe()
            : projection.getTitle().getEn()) + " (" + projection.getId() + ")")
        .en((projection.getTitle().getEn() != null ? projection.getTitle().getEn()
            : projection.getTitle().getDe()) + " (" + projection.getId() + ")")
        .build();
  }
}
