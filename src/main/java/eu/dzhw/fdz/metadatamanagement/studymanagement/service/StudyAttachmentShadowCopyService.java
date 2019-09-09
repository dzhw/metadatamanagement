package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.studymanagement.service.helper.StudyAttachmentShadowCopyDataSource;

/**
 * Service which generates shadow copies of all study attachments of a project, when the project has
 * been released.
 * 
 * @author René Reitmann
 */
@Service
public class StudyAttachmentShadowCopyService extends ShadowCopyHelper<StudyAttachmentMetadata> {
  public StudyAttachmentShadowCopyService(
      StudyAttachmentShadowCopyDataSource shadowCopyDataSource) {
    super(shadowCopyDataSource);
  }
}
