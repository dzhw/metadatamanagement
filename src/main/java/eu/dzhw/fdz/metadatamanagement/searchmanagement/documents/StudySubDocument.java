package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
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
public class StudySubDocument extends AbstractRdcDomainObject
    implements StudySubDocumentProjection {

  private static final long serialVersionUID = -8641253155035394262L;

  private String id;
  
  private String dataAcquisitionProjectId;
  
  private List<I18nString> institutions;
  
  private I18nString sponsor;
  
  private I18nString studySeries;
  
  private I18nString title;
  
  private List<Person> authors;
  
  private String doi;
  
  private I18nString surveyDesign;

  private I18nString dataAvailability;

  private String masterId;
  
  private String successorId;
  
  private boolean shadow;

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
  }
}
