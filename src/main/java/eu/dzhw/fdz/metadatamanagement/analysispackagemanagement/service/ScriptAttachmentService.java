package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.javers.core.Javers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.gridfs.model.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.Script;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.ScriptAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.helper.ScriptAttachmentFilenameBuilder;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyCreateNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyDeleteNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.service.AttachmentMetadataHelper;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing attachments for {@link Script}s.
 */
@Service
@RequiredArgsConstructor
@RepositoryEventHandler
public class ScriptAttachmentService {
  private final GridFsOperations operations;

  private final MongoTemplate mongoTemplate;

  private final Javers javers;

  private final AttachmentMetadataHelper<ScriptAttachmentMetadata> attachmentMetadataHelper;

  private final AnalysisPackageChangesProvider analysisPackageChangesProvider;

  private final AnalysisPackageRepository analysisPackageRepository;

  @Value("${metadatamanagement.server.instance-index}")
  private Integer instanceId;

  /**
   * Delete all {@link Script} attachments when the {@link Script} is removed from the given
   * {@link AnalysisPackage}.
   *
   * @param analysisPackage The changed analysis package
   */
  @HandleAfterSave
  public void onAnalysisPackageChanged(AnalysisPackage analysisPackage) {
    analysisPackageChangesProvider.getDeletedScripts(analysisPackage.getId())
        .forEach(script -> deleteByAnalysisPackageIdAndScriptUuid(analysisPackage.getId(),
            script.getUuid()));
  }

  /**
   * Save the attachment for a {@link Script}.
   *
   * @param metadata The metadata of the attachment.
   * @return The GridFs filename.
   * @throws IOException thrown when the input stream cannot be closed
   */
  public String createScriptAttachment(MultipartFile multipartFile,
      ScriptAttachmentMetadata metadata) throws IOException {

    if (metadata.isShadow()) {
      throw new ShadowCopyCreateNotAllowedException();
    }

    String currentUser = SecurityUtils.getCurrentUserLogin();
    attachmentMetadataHelper.initAttachmentMetadata(metadata, currentUser);
    metadata.generateId();
    metadata.setMasterId(metadata.getId());
    String filename = ScriptAttachmentFilenameBuilder.buildFileName(metadata);
    attachmentMetadataHelper.writeAttachmentMetadata(multipartFile, filename, metadata,
        currentUser);

    return filename;
  }

  /**
   * Update the metadata of the attachment.
   *
   * @param metadata The new metadata.
   */
  public void updateAttachmentMetadata(ScriptAttachmentMetadata metadata) {
    String filePath = ScriptAttachmentFilenameBuilder.buildFileName(metadata.getAnalysisPackageId(),
        metadata.getScriptUuid(), metadata.getFileName());
    attachmentMetadataHelper.updateAttachmentMetadata(metadata, filePath);
  }

  /**
   * Delete all script attachments of the given {@link AnalysisPackage}.
   *
   * @param analysisPackageId the id of the analysis package.
   */
  public void deleteAllByAnalysisPackageId(String analysisPackageId) {
    Query query = new Query(GridFsCriteria.whereFilename().regex("^"
        + Pattern.quote(ScriptAttachmentFilenameBuilder.buildFileNamePrefix(analysisPackageId))));
    deleteByQuery(query);
  }

  /**
   * Delete the given script attachments of the given {@link AnalysisPackage}.
   *
   * @param analysisPackageId the id of the analysis package.
   * @param scriptUuid the id of the {@link Script}.
   */
  public void deleteByAnalysisPackageIdAndScriptUuid(String analysisPackageId, String scriptUuid) {
    Query query = new Query(GridFsCriteria.whereFilename().regex("^" + Pattern.quote(
        ScriptAttachmentFilenameBuilder.buildFileNamePrefix(analysisPackageId, scriptUuid))));
    deleteByQuery(query);
  }

  private void deleteByQuery(Query query) {
    String currentUser = SecurityUtils.getCurrentUserLogin();
    Iterable<GridFSFile> files = this.operations.find(query);
    files.forEach(file -> {
      ScriptAttachmentMetadata metadata =
          mongoTemplate.getConverter().read(ScriptAttachmentMetadata.class, file.getMetadata());
      if (metadata.isShadow()) {
        throw new ShadowCopyDeleteNotAllowedException();
      }
      javers.commitShallowDelete(currentUser, metadata);
    });
    this.operations.delete(query);
  }

  /**
   * Load all metadata objects from gridfs.
   *
   * @param analysisPackageId The analysisPackageId of the {@link Script}.
   * @return A list of metadata.
   */
  public List<ScriptAttachmentMetadata> findAllByAnalysisPackage(String analysisPackageId) {
    Query query = new Query(GridFsCriteria.whereFilename().regex("^"
        + Pattern.quote(ScriptAttachmentFilenameBuilder.buildFileNamePrefix(analysisPackageId))));
    Iterable<GridFSFile> files = this.operations.find(query);
    List<ScriptAttachmentMetadata> result = new ArrayList<>();
    files.forEach(gridfsFile -> {
      result.add(mongoTemplate.getConverter().read(ScriptAttachmentMetadata.class,
          gridfsFile.getMetadata()));
    });
    return result;
  }

  /**
   * Load all metadata objects from gridfs.
   *
   * @param dataAcquisitionProjectId The id of the {@link DataAcquisitionProject}.
   * @return A list of metadata.
   */
  public List<ScriptAttachmentMetadata> findAllByProject(String dataAcquisitionProjectId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex(ScriptAttachmentFilenameBuilder.ALL_SCRIPT_ATTACHMENTS).andOperator(
            GridFsCriteria.whereMetaData("dataAcquisitionProjectId").is(dataAcquisitionProjectId)));
    Iterable<GridFSFile> files = this.operations.find(query);
    List<ScriptAttachmentMetadata> result = new ArrayList<>();
    files.forEach(gridfsFile -> {
      result.add(mongoTemplate.getConverter().read(ScriptAttachmentMetadata.class,
          gridfsFile.getMetadata()));
    });
    return result;
  }

  /**
   * Delete the attachment and its metadata from gridfs.
   *
   * @param analysisPackageId The id of the analysis package.
   * @param scriptUuid The uuid of the script.
   * @param filename The filename of the attachment.
   */
  public void deleteByAnalysisPackageIdAndScriptUuidAndFilename(String analysisPackageId,
      String scriptUuid, String filename) {
    Query fileQuery = new Query(GridFsCriteria.whereFilename().is(
        ScriptAttachmentFilenameBuilder.buildFileName(analysisPackageId, scriptUuid, filename)));
    deleteByQuery(fileQuery);
  }

  /**
   * Orphaned {@link ScriptAttachmentMetadata} files wil be deleted. This is scheduled to get fired
   * everyday, at 02:00 (am).
   */
  @Scheduled(cron = "0 0 2 * * ?")
  public void removeOrphanedAttachments() {
    if (instanceId != 0) {
      return;
    }
    Query fileQuery = new Query(GridFsCriteria.whereFilename()
        .regex(ScriptAttachmentFilenameBuilder.ALL_SCRIPT_ATTACHMENTS));
    Iterable<GridFSFile> files = this.operations.find(fileQuery);
    files.forEach(gridFsFile -> {
      ScriptAttachmentMetadata metadata = mongoTemplate.getConverter()
          .read(ScriptAttachmentMetadata.class, gridFsFile.getMetadata());
      analysisPackageRepository.findById(metadata.getAnalysisPackageId())
          .ifPresentOrElse(analysisPackage -> {
            if (analysisPackage.getScripts() == null || !analysisPackage.getScripts().stream()
                .anyMatch(script -> script.getUuid().equals(metadata.getScriptUuid()))) {
              deleteByAnalysisPackageIdAndScriptUuidAndFilename(metadata.getAnalysisPackageId(),
                  metadata.getScriptUuid(), metadata.getFileName());
            }
          }, () -> {
              deleteByAnalysisPackageIdAndScriptUuidAndFilename(metadata.getAnalysisPackageId(),
                  metadata.getScriptUuid(), metadata.getFileName());
            });

    });
  }
}
