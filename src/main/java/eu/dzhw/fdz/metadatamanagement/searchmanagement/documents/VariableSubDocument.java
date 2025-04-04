package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.RelatedQuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Attributes of a variable which are stored in other search documents.
 *
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class VariableSubDocument extends AbstractRdcDomainObject
    implements VariableSubDocumentProjection {

  private static final long serialVersionUID = 6069902793033684835L;

  private String id;

  private String dataAcquisitionProjectId;

  private String name;

  private I18nString label;

  private String dataSetId;

  private Integer dataSetNumber;

  private String dataPackageId;

  private List<String> surveyIds;

  private List<RelatedQuestionSubDocumentProjection> relatedQuestions;

  private String masterId;

  private String pid;

  private String successorId;

  private boolean shadow;

  public VariableSubDocument() {
    super();
  }

  /**
   * Create the subdocument.
   *
   * @param projection the projection coming from mongo.
   */
  public VariableSubDocument(VariableSubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
  }

  /**
   * Create the subdocument.
   *
   * @param variable the complete variable coming from mongo.
   */
  public VariableSubDocument(Variable variable) {
    super();
    BeanUtils.copyProperties(variable, this);
  }
}
