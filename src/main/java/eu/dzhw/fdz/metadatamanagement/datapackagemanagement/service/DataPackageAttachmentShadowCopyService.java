package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackageAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.helper.DataPackageAttachmentShadowCopyDataSource;

/**
 * Service which generates shadow copies of all dataPackage attachments of a project, when the
 * project has been released.
 * 
 * @author René Reitmann
 */
@Service
public class DataPackageAttachmentShadowCopyService
    extends ShadowCopyHelper<DataPackageAttachmentMetadata> {
  public DataPackageAttachmentShadowCopyService(
      DataPackageAttachmentShadowCopyDataSource shadowCopyDataSource) {
    super(shadowCopyDataSource);
  }
}
