package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;

/**
 * Utility class for building filenames for {@link InstrumentAttachmentMetadata} as they are stored
 * in GridFS.
 * 
 * @author René Reitmann
 */
public class InstrumentAttachmentFilenameBuilder {
  public static final String ALL_INSTRUMENT_ATTACHMENTS = "^/instruments/.+/attachments";
  
  public static String buildFileName(InstrumentAttachmentMetadata metadata) {
    return buildFileNamePrefix(metadata.getInstrumentId()) + metadata.getFileName();
  }
  
  public static String buildFileName(String instrumentId, String filename) {
    return buildFileNamePrefix(instrumentId) + filename;
  }
  
  public static String buildFileNamePrefix(String instrumentId) {
    return "/instruments/" + instrumentId + "/attachments/";
  }
}
