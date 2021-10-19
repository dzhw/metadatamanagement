package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.helper;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackageAttachmentMetadata;

/**
 * Utility class for building filenames for {@link AnalysisPackageAttachmentMetadata} as they are
 * stored in GridFS.
 * 
 * @author René Reitmann
 */
public class AnalysisPackageAttachmentFilenameBuilder {
  public static final String ALL_ANALYSIS_PACKAGE_ATTACHMENTS =
      "^/analysis-packages/[^/]+/attachments";

  public static String buildFileName(AnalysisPackageAttachmentMetadata metadata) {
    return buildFileNamePrefix(metadata.getAnalysisPackageId()) + metadata.getFileName();
  }

  public static String buildFileName(String analysisPackageId, String filename) {
    return buildFileNamePrefix(analysisPackageId) + filename;
  }

  public static String buildFileNamePrefix(String analysisPackageId) {
    return "/analysis-packages/" + analysisPackageId + "/attachments/";
  }
}
