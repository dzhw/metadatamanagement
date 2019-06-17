package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Attributes of a question which are stored in other search documents.
 *  
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class QuestionSubDocument extends AbstractRdcDomainObject
    implements QuestionSubDocumentProjection {
  private String id;
  
  private String dataAcquisitionProjectId;
  
  private String instrumentId;
  
  private Integer instrumentNumber;
  
  private String number;
  
  private I18nString questionText;
  
  private I18nString topic;
  
  private List<String> conceptIds;
  
  private String studyId;

  private String masterId;
  
  private String successorId;
  
  private boolean shadow;

  public QuestionSubDocument() {
    super();
  }
  
  /**
   * Create the subdocument.
   * 
   * @param projection the projection coming from mongo.
   */
  public QuestionSubDocument(QuestionSubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
  }
}
