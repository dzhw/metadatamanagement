package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.helper.SurveyShadowCopyDataSource;

/**
 * Service which generates shadow copies of all surveys of a project, when the project has been
 * released.
 * 
 * @author René Reitmann
 */
@Service
public class SurveyShadowCopyService extends ShadowCopyHelper<Survey> {
  public SurveyShadowCopyService(SurveyShadowCopyDataSource surveyShadowCopyDataSource) {
    super(surveyShadowCopyDataSource);
  }
}
