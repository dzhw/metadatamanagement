package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackageAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.helper.AnalysisPackageAttachmentShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;

/**
 * Service which generates shadow copies of all analysis package attachments of a project, when the
 * project has been released.
 * 
 * @author René Reitmann
 */
@Service
public class AnalysisPackageAttachmentShadowCopyService
    extends ShadowCopyHelper<AnalysisPackageAttachmentMetadata> {
  public AnalysisPackageAttachmentShadowCopyService(
      AnalysisPackageAttachmentShadowCopyDataSource shadowCopyDataSource) {
    super(shadowCopyDataSource);
  }
}
