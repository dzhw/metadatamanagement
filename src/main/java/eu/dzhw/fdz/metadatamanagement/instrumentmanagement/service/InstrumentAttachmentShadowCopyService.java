package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.helper.InstrumentAttachmentShadowCopyDataSource;

/**
 * Service which generates shadow copies of all instrument attachments of a project, when the
 * project has been released.
 * 
 * @author René Reitmann
 */
@Service
public class InstrumentAttachmentShadowCopyService
    extends ShadowCopyHelper<InstrumentAttachmentMetadata> {
  public InstrumentAttachmentShadowCopyService(
      InstrumentAttachmentShadowCopyDataSource shadowCopyDataSource) {
    super(shadowCopyDataSource);
  }
}
