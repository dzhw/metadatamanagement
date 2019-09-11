package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.helper.DataSetAttachmentShadowCopyDataSource;

/**
 * Service which generates shadow copies of all dataSet attachments of a project, when the project
 * has been released.
 * 
 * @author René Reitmann
 */
@Service
public class DataSetAttachmentShadowCopyService
    extends ShadowCopyHelper<DataSetAttachmentMetadata> {
  public DataSetAttachmentShadowCopyService(
      DataSetAttachmentShadowCopyDataSource shadowCopyDataSource) {
    super(shadowCopyDataSource);
  }
}
