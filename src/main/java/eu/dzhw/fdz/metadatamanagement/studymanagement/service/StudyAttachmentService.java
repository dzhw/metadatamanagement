package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyCreateNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyDeleteNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.service.AttachmentMetadataHelper;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.studymanagement.service.helper.StudyAttachmentFilenameBuilder;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing attachments for studies.
 */
@Service
@RequiredArgsConstructor
public class StudyAttachmentService {

  private final GridFsOperations operations;
  
  private final MongoTemplate mongoTemplate;
  
  private final Javers javers;

  private final AttachmentMetadataHelper<StudyAttachmentMetadata> attachmentMetadataHelper;

  /**
   * Save the attachment for a study. 
   * @param metadata The metadata of the attachment.
   * @return The GridFs filename.
   * @throws IOException thrown when the input stream cannot be closed
   */
  public String createStudyAttachment(MultipartFile multipartFile,
      StudyAttachmentMetadata metadata) throws IOException {

    if (metadata.isShadow()) {
      throw new ShadowCopyCreateNotAllowedException();
    }

    String currentUser = SecurityUtils.getCurrentUserLogin();
    attachmentMetadataHelper.initAttachmentMetadata(metadata, currentUser);
    metadata.generateId();
    metadata.setMasterId(metadata.getId());
    String filename = StudyAttachmentFilenameBuilder.buildFileName(metadata);
    attachmentMetadataHelper.writeAttachmentMetadata(multipartFile, filename, metadata,
        currentUser);

    return filename;
  }

  /**
   * Update the metadata of the attachment.
   * @param metadata The new metadata.
   */
  public void updateAttachmentMetadata(StudyAttachmentMetadata metadata) {
    String filePath = StudyAttachmentFilenameBuilder.buildFileName(metadata.getStudyId(),
        metadata.getFileName());
    attachmentMetadataHelper.updateAttachmentMetadata(metadata, filePath);
  }
  
  /**
   * Delete all attachments of the given study.
   * @param studyId the id of the study.
   */
  public void deleteAllByStudyId(String studyId) {
    String currentUser = SecurityUtils.getCurrentUserLogin();
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(
            StudyAttachmentFilenameBuilder.buildFileNamePrefix(studyId))));
    Iterable<GridFSFile> files = this.operations.find(query);
    files.forEach(file -> {
      StudyAttachmentMetadata metadata = mongoTemplate.getConverter().read(
          StudyAttachmentMetadata.class, file.getMetadata());
      if (metadata.isShadow()) {
        throw new ShadowCopyDeleteNotAllowedException();
      }
      javers.commitShallowDelete(currentUser, metadata);
    });
    this.operations.delete(query);
  }
  
  /**
   * Load all metadata objects from gridfs (ordered by indexInStudy).
   * @param studyId The id of the study.
   * @return A list of metadata.
   */
  public List<StudyAttachmentMetadata> findAllByStudy(String studyId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(
            StudyAttachmentFilenameBuilder.buildFileNamePrefix(studyId))));
    query.with(new Sort(Sort.Direction.ASC, "metadata.indexInStudy"));
    Iterable<GridFSFile> files = this.operations.find(query);
    List<StudyAttachmentMetadata> result = new ArrayList<>();
    files.forEach(gridfsFile -> {
      result.add(mongoTemplate.getConverter().read(StudyAttachmentMetadata.class, 
          gridfsFile.getMetadata()));
    });
    return result;
  }
  
  

  /**
   * Delete all attachments of all studies.
   */
  public void deleteAll() {
    String currentUser = SecurityUtils.getCurrentUserLogin();
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote("/studies/") + ".*" + Pattern.quote("/attachments/")));
    Iterable<GridFSFile> files = this.operations.find(query);
    files.forEach(file -> {
      StudyAttachmentMetadata metadata = mongoTemplate.getConverter().read(
          StudyAttachmentMetadata.class, file.getMetadata());
      if (metadata.isShadow()) {
        throw new ShadowCopyDeleteNotAllowedException();
      }
      javers.commitShallowDelete(currentUser, metadata);
    });
    this.operations.delete(query);
  }

  /**
   * Delete the attachment and its metadata from gridfs.
   * @param studyId The id of the study.
   * @param filename The filename of the attachment.
   */
  public void deleteByStudyIdAndFilename(String studyId, String filename) {
    Query fileQuery = new Query(GridFsCriteria.whereFilename().is(
        StudyAttachmentFilenameBuilder.buildFileName(studyId, filename)));
    GridFSFile file = this.operations.findOne(fileQuery);
    if (file == null) {
      return;
    }
    StudyAttachmentMetadata metadata = mongoTemplate.getConverter().read(
        StudyAttachmentMetadata.class, file.getMetadata());
    if (metadata.isShadow()) {
      throw new ShadowCopyDeleteNotAllowedException();
    }
    String currentUser = SecurityUtils.getCurrentUserLogin();
    this.operations.delete(fileQuery);
    javers.commitShallowDelete(currentUser, metadata);
  }
}
