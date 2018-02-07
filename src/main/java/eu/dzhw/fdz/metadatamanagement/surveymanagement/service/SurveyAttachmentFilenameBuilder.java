package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyAttachmentMetadata;

/**
 * Utility class for building filenames for {@link SurveyAttachmentMetadata} 
 * as they are stored in GridFS.
 * 
 * @author René Reitmann
 */
public class SurveyAttachmentFilenameBuilder {
  public static final String ALL_SURVEY_ATTACHMENTS = "^/surveys/.+/attachments";
  
  public static String buildFileName(SurveyAttachmentMetadata metadata) {
    return buildFileNamePrefix(metadata.getSurveyId()) + metadata.getFileName(); 
  }
  
  public static String buildFileName(String surveyId, String filename) {
    return buildFileNamePrefix(surveyId) + filename; 
  }
  
  public static String buildFileNamePrefix(String surveyId) {
    return "/surveys/" + surveyId + "/attachments/";
  }
}
