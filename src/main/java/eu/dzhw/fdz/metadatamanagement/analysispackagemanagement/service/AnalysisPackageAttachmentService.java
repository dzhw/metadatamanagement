package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.javers.core.Javers;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.gridfs.model.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackageAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.helper.AnalysisPackageAttachmentFilenameBuilder;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyCreateNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyDeleteNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.service.AttachmentMetadataHelper;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing attachments for {@link AnalysisPackage}s.
 */
@Service
@RequiredArgsConstructor
public class AnalysisPackageAttachmentService {
  private final GridFsOperations operations;

  private final MongoTemplate mongoTemplate;

  private final Javers javers;

  private final AttachmentMetadataHelper<AnalysisPackageAttachmentMetadata>
      attachmentMetadataHelper;

  /**
   * Save the attachment for an analysis package.
   *
   * @param metadata The metadata of the attachment.
   * @return The GridFs filename.
   * @throws IOException thrown when the input stream cannot be closed
   */
  public String createAnalysisPackageAttachment(MultipartFile multipartFile,
      AnalysisPackageAttachmentMetadata metadata) throws IOException {

    if (metadata.isShadow()) {
      throw new ShadowCopyCreateNotAllowedException();
    }

    String currentUser = SecurityUtils.getCurrentUserLogin();
    attachmentMetadataHelper.initAttachmentMetadata(metadata, currentUser);
    metadata.generateId();
    metadata.setMasterId(metadata.getId());
    String filename = AnalysisPackageAttachmentFilenameBuilder.buildFileName(metadata);
    attachmentMetadataHelper.writeAttachmentMetadata(multipartFile, filename, metadata,
        currentUser);

    return filename;
  }

  /**
   * Update the metadata of the attachment.
   *
   * @param metadata The new metadata.
   */
  public void updateAttachmentMetadata(AnalysisPackageAttachmentMetadata metadata) {
    String filePath = AnalysisPackageAttachmentFilenameBuilder
        .buildFileName(metadata.getAnalysisPackageId(), metadata.getFileName());
    attachmentMetadataHelper.updateAttachmentMetadata(metadata, filePath);
  }

  /**
   * Delete all attachments of the given analysis package.
   *
   * @param analysisPackageId the id of the analysis package.
   */
  public void deleteAllByAnalysisPackageId(String analysisPackageId) {
    String currentUser = SecurityUtils.getCurrentUserLogin();
    Query query = new Query(GridFsCriteria.whereFilename().regex("^" + Pattern
        .quote(AnalysisPackageAttachmentFilenameBuilder.buildFileNamePrefix(analysisPackageId))));
    Iterable<GridFSFile> files = this.operations.find(query);
    files.forEach(file -> {
      AnalysisPackageAttachmentMetadata metadata = mongoTemplate.getConverter()
          .read(AnalysisPackageAttachmentMetadata.class, file.getMetadata());
      if (metadata.isShadow()) {
        throw new ShadowCopyDeleteNotAllowedException();
      }
      javers.commitShallowDelete(currentUser, metadata);
    });
    this.operations.delete(query);
  }

  /**
   * Load all metadata objects from gridfs (ordered by indexInAnalysisPackage).
   *
   * @param analysisPackageId The id of the {@link AnalysisPackage}.
   * @return A list of metadata.
   */
  public List<AnalysisPackageAttachmentMetadata> findAllByAnalysisPackage(
      String analysisPackageId) {
    Query query = new Query(GridFsCriteria.whereFilename().regex("^" + Pattern
        .quote(AnalysisPackageAttachmentFilenameBuilder.buildFileNamePrefix(analysisPackageId))));
    query.with(Sort.by(Sort.Direction.ASC, "metadata.indexInAnalysisPackage"));
    Iterable<GridFSFile> files = this.operations.find(query);
    List<AnalysisPackageAttachmentMetadata> result = new ArrayList<>();
    files.forEach(gridfsFile -> {
      result.add(mongoTemplate.getConverter().read(AnalysisPackageAttachmentMetadata.class,
          gridfsFile.getMetadata()));
    });
    return result;
  }

  /**
   * Load all metadata objects from gridfs (ordered by indexInAnalysisPackage).
   *
   * @param dataAcquisitionProjectId The id of the {@link DataAcquisitionProject}.
   * @return A list of metadata.
   */
  public List<AnalysisPackageAttachmentMetadata> findAllByProject(String dataAcquisitionProjectId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex(AnalysisPackageAttachmentFilenameBuilder.ALL_ANALYSIS_PACKAGE_ATTACHMENTS)
        .andOperator(
            GridFsCriteria.whereMetaData("dataAcquisitionProjectId").is(dataAcquisitionProjectId)));
    query.with(Sort.by(Sort.Direction.ASC, "metadata.indexInAnalysisPackage"));
    Iterable<GridFSFile> files = this.operations.find(query);
    List<AnalysisPackageAttachmentMetadata> result = new ArrayList<>();
    AtomicInteger countByAnalysisPackage = new AtomicInteger(0);
    files.forEach(gridfsFile -> {
      gridfsFile.getMetadata().put("indexInAnalysisPackage",
          countByAnalysisPackage.getAndIncrement());
      result.add(mongoTemplate.getConverter().read(AnalysisPackageAttachmentMetadata.class,
          gridfsFile.getMetadata()));
    });
    return result;
  }

  /**
   * Delete the attachment and its metadata from gridfs.
   *
   * @param analysisPackageId The id of the analysis package.
   * @param filename The filename of the attachment.
   */
  public void deleteByAnalysisPackageIdAndFilename(String analysisPackageId, String filename) {
    Query fileQuery = new Query(GridFsCriteria.whereFilename()
        .is(AnalysisPackageAttachmentFilenameBuilder.buildFileName(analysisPackageId, filename)));
    GridFSFile file = this.operations.findOne(fileQuery);
    if (file == null) {
      return;
    }
    AnalysisPackageAttachmentMetadata metadata = mongoTemplate.getConverter()
        .read(AnalysisPackageAttachmentMetadata.class, file.getMetadata());
    if (metadata.isShadow()) {
      throw new ShadowCopyDeleteNotAllowedException();
    }
    String currentUser = SecurityUtils.getCurrentUserLogin();
    this.operations.delete(fileQuery);
    javers.commitShallowDelete(currentUser, metadata);
  }
}
