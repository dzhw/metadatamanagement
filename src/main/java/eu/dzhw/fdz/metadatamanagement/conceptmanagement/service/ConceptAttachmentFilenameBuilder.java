package eu.dzhw.fdz.metadatamanagement.conceptmanagement.service;

import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.ConceptAttachmentMetadata;

/**
 * Utility class for building filenames for {@link ConceptAttachmentMetadata} 
 * as they are stored in GridFS.
 * 
 * @author René Reitmann
 */
public class ConceptAttachmentFilenameBuilder {
  public static final String ALL_CONCEPT_ATTACHMENTS = "^/concepts/.+/attachments";
  
  public static String buildFileName(ConceptAttachmentMetadata metadata) {
    return buildFileNamePrefix(metadata.getConceptId()) + metadata.getFileName(); 
  }
  
  public static String buildFileName(String conceptId, String filename) {
    return buildFileNamePrefix(conceptId) + filename; 
  }
  
  public static String buildFileNamePrefix(String conceptId) {
    return "/concepts/" + conceptId + "/attachments/";
  }
}
