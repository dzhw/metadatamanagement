package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyResponseRateImageMetadata;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.helper.SurveyResponseRateImageShadowCopyDataSource;

/**
 * Service which generates shadow copies of all response rate images of a project, when the project
 * has been released.
 * 
 * @author René Reitmann
 */
@Service
public class SurveyResponseRateImageShadowCopyService
    extends ShadowCopyHelper<SurveyResponseRateImageMetadata> {

  public SurveyResponseRateImageShadowCopyService(
      SurveyResponseRateImageShadowCopyDataSource shadowCopyDataSource) {
    super(shadowCopyDataSource);
  }
}
