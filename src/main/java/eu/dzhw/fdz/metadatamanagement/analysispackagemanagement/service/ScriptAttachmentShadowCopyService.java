package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.ScriptAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.helper.ScriptAttachmentShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;

/**
 * Service which generates shadow copies of all script attachments of a project, when the
 * project has been released.
 * 
 * @author René Reitmann
 */
@Service
public class ScriptAttachmentShadowCopyService
    extends ShadowCopyHelper<ScriptAttachmentMetadata> {
  public ScriptAttachmentShadowCopyService(
      ScriptAttachmentShadowCopyDataSource shadowCopyDataSource) {
    super(shadowCopyDataSource);
  }
}
