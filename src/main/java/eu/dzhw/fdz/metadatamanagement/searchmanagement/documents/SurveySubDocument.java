package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Period;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Population;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Attributes of a survey which are stored in other search documents.
 *
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class SurveySubDocument extends AbstractRdcDomainObject
    implements SurveySubDocumentProjection {

  private static final long serialVersionUID = -1730150306159593533L;

  private String id;

  private String dataAcquisitionProjectId;

  private Integer number;

  private Population population;

  private I18nString surveyMethod;

  private I18nString title;

  private Period fieldPeriod;

  private I18nString sample;

  private Integer serialNumber;

  private I18nString dataType;

  private String dataPackageId;

  private String masterId;

  private String successorId;

  private boolean shadow;

  public SurveySubDocument() {
    super();
  }

  /**
   * Create the subdocument.
   *
   * @param projection The projection coming from mongo.
   */
  public SurveySubDocument(SurveySubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
  }
}
