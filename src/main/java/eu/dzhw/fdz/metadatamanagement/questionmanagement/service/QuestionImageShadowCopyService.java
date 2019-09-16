package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionImageMetadata;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.helper.QuestionImageShadowCopyDataSource;

/**
 * Service which generates shadow copies of all question images of a project, when the project has
 * been released.
 * 
 * @author René Reitmann
 */
@Service
public class QuestionImageShadowCopyService extends ShadowCopyHelper<QuestionImageMetadata> {
  public QuestionImageShadowCopyService(
      QuestionImageShadowCopyDataSource shadowCopyDataSource) {
    super(shadowCopyDataSource);
  }
}
