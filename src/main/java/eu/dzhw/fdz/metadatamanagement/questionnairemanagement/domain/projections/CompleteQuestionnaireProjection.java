package eu.dzhw.fdz.metadatamanagement.questionnairemanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.AbstractRdcDomainObjectProjection;
import eu.dzhw.fdz.metadatamanagement.questionnairemanagement.domain.Questionnaire;

/**
 * Complete Projection Questionnaire. This projection displays all attributes from the
 * questionnaire.
 * 
 * @author Daniel Katzberg
 *
 */
@Projection(name = "complete", types = Questionnaire.class)
public interface CompleteQuestionnaireProjection extends AbstractRdcDomainObjectProjection {

  String getDataAcquisitionProjectId();
  
  String getSurveyId();
}
