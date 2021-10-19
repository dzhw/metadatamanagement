package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.helper;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.ScriptAttachmentMetadata;

/**
 * Utility class for building filenames for {@link ScriptAttachmentMetadata} as they are stored in
 * GridFS.
 * 
 * @author René Reitmann
 */
public class ScriptAttachmentFilenameBuilder {
  public static final String ALL_SCRIPT_ATTACHMENTS =
      "^/analysis-packages/[^/]+/scripts/[^/]+/attachments";

  public static String buildFileName(ScriptAttachmentMetadata metadata) {
    return buildFileNamePrefix(metadata.getAnalysisPackageId(), metadata.getScriptUuid())
        + metadata.getFileName();
  }

  public static String buildFileName(String analysisPackageId, String scriptUuid, String filename) {
    return buildFileNamePrefix(analysisPackageId, scriptUuid) + filename;
  }

  public static String buildFileNamePrefix(String analysisPackageId) {
    return "/analysis-packages/" + analysisPackageId + "/scripts/";
  }
  
  public static String buildFileNamePrefix(String analysisPackageId, String scriptUuid) {
    return "/analysis-packages/" + analysisPackageId + "/scripts/" + scriptUuid + "/attachments/";
  }
}
