package eu.dzhw.fdz.metadatamanagement.studymanagement.service.helper;

import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudyAttachmentMetadata;

/**
 * Utility class for building filenames for {@link StudyAttachmentMetadata} 
 * as they are stored in GridFS.
 * 
 * @author René Reitmann
 */
public class StudyAttachmentFilenameBuilder {
  public static final String ALL_STUDY_ATTACHMENTS = "^/studies/.+/attachments";
  
  public static String buildFileName(StudyAttachmentMetadata metadata) {
    return buildFileNamePrefix(metadata.getStudyId()) + metadata.getFileName(); 
  }
  
  public static String buildFileName(String studyId, String filename) {
    return buildFileNamePrefix(studyId) + filename; 
  }
  
  public static String buildFileNamePrefix(String studyId) {
    return "/studies/" + studyId + "/attachments/";
  }
}
