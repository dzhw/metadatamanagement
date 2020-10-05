package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.helper;

import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackageAttachmentMetadata;

/**
 * Utility class for building filenames for {@link DataPackageAttachmentMetadata} 
 * as they are stored in GridFS.
 * 
 * @author René Reitmann
 */
public class DataPackageAttachmentFilenameBuilder {
  public static final String ALL_STUDY_ATTACHMENTS = "^/data-packages/.+/attachments";
  
  public static String buildFileName(DataPackageAttachmentMetadata metadata) {
    return buildFileNamePrefix(metadata.getDataPackageId()) + metadata.getFileName(); 
  }
  
  public static String buildFileName(String dataPackageId, String filename) {
    return buildFileNamePrefix(dataPackageId) + filename; 
  }
  
  public static String buildFileNamePrefix(String dataPackageId) {
    return "/data-packages/" + dataPackageId + "/attachments/";
  }
}
