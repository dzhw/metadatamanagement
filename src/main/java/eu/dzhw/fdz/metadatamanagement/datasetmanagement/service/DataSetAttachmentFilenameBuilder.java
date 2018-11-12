package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;

/**
 * Utility class for building filenames for {@link DataSetAttachmentMetadata} as they are stored in
 * GridFS.
 * 
 * @author René Reitmann
 */
public class DataSetAttachmentFilenameBuilder {
  public static final String ALL_DATASET_ATTACHMENTS = "^/data-sets/.+/attachments";
  
  public static String buildFileName(DataSetAttachmentMetadata metadata) {
    return buildFileNamePrefix(metadata.getDataSetId()) + metadata.getFileName();
  }
  
  public static String buildFileName(String dataSetId, String filename) {
    return buildFileNamePrefix(dataSetId) + filename;
  }
  
  public static String buildFileNamePrefix(String dataSetId) {
    return "/data-sets/" + dataSetId + "/attachments/";
  }
}
