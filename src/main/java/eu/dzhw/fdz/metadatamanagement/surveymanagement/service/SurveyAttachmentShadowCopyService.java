package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.helper.SurveyAttachmentShadowCopyDataSource;

/**
 * Service which generates shadow copies of all survey attachments of a project, when the project
 * has been released.
 * 
 * @author René Reitmann
 */
@Service
public class SurveyAttachmentShadowCopyService extends ShadowCopyHelper<SurveyAttachmentMetadata> {
  public SurveyAttachmentShadowCopyService(
      SurveyAttachmentShadowCopyDataSource shadowCopyDataSource) {
    super(shadowCopyDataSource);
  }
}
