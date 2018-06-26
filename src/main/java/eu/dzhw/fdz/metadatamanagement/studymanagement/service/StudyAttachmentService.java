package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;
import org.javers.core.Javers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

import eu.dzhw.fdz.metadatamanagement.filemanagement.util.MimeTypeDetector;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;

/**
 * Service for managing attachments for studies.
 */
@Service
public class StudyAttachmentService {

  @Autowired
  private GridFS gridFs;
  
  @Autowired
  private GridFsOperations operations;
  
  @Autowired
  private MongoTemplate mongoTemplate;
  
  @Autowired
  private MimeTypeDetector mimeTypeDetector;
  
  @Autowired
  private Javers javers;
  
  /**
   * Save the attachment for a study. 
   * @param metadata The metadata of the attachment.
   * @return The GridFs filename.
   * @throws IOException thrown when the input stream cannot be closed
   */
  public String createStudyAttachment(MultipartFile multipartFile,
      StudyAttachmentMetadata metadata) throws IOException {
    try (InputStream in = multipartFile.getInputStream()) {
      String currentUser = SecurityUtils.getCurrentUserLogin();
      metadata.setVersion(0L);
      metadata.setCreatedDate(LocalDateTime.now());
      metadata.setCreatedBy(currentUser);
      metadata.setLastModifiedBy(currentUser);
      metadata.setLastModifiedDate(LocalDateTime.now());
      metadata.generateId();
      String contentType = mimeTypeDetector.detect(multipartFile);
      String filename = StudyAttachmentFilenameBuilder.buildFileName(metadata); 
      this.operations.store(in, filename, contentType, metadata);
      javers.commit(currentUser, metadata);
      return filename;      
    }
  }
  
  /**
   * Update the metadata of the attachment.
   * 
   * @param metadata The new metadata.
   */
  public void updateAttachmentMetadata(StudyAttachmentMetadata metadata) {
    metadata.setVersion(metadata.getVersion() + 1);
    String currentUser = SecurityUtils.getCurrentUserLogin();
    metadata.setLastModifiedBy(currentUser);
    metadata.setLastModifiedDate(LocalDateTime.now());
    GridFSDBFile file = gridFs.findOne(StudyAttachmentFilenameBuilder.buildFileName(
        metadata.getStudyId(), metadata.getFileName()));
    BasicDBObject dbObject = new BasicDBObject(
        (Document) mongoTemplate.getConverter().convertToMongoType(metadata));
    file.setMetaData(dbObject);
    file.save();
    javers.commit(currentUser, metadata);
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
    String currentUser = SecurityUtils.getCurrentUserLogin();
    this.operations.delete(fileQuery);
    javers.commitShallowDelete(currentUser, metadata);
  }
}
