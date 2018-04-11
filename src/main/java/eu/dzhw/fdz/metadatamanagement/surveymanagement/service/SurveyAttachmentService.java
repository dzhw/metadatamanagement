package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.javers.core.Javers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

import eu.dzhw.fdz.metadatamanagement.filemanagement.util.MimeTypeDetector;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;

/**
 * Service for managing attachments for surveys.
 * 
 */
@Service
public class SurveyAttachmentService {

  @Autowired
  private GridFsOperations operations;
  
  @Autowired
  private GridFS gridFs;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private MimeTypeDetector mimeTypeDetector;

  @Autowired
  private Javers javers;

  /**
   * Save the attachment for a survey.
   * 
   * @param metadata The metadata of the attachment.
   * @return The GridFs filename.
   * @throws IOException thrown when the input stream is not closable
   */
  public String createSurveyAttachment(MultipartFile multipartFile,
      SurveyAttachmentMetadata metadata) throws IOException {
    try (InputStream in = multipartFile.getInputStream()) {
      String currentUser = SecurityUtils.getCurrentUserLogin();
      metadata.setVersion(0L);
      metadata.setCreatedDate(LocalDateTime.now());
      metadata.setCreatedBy(currentUser);
      metadata.setLastModifiedBy(currentUser);
      metadata.setLastModifiedDate(LocalDateTime.now());
      metadata.generateId();
      String contentType = mimeTypeDetector.detect(multipartFile);
      String filename = SurveyAttachmentFilenameBuilder.buildFileName(metadata);
      this.operations.store(in, filename, contentType, metadata);
      javers.commit(currentUser, metadata);
      return filename;
    }
  }

  /**
   * Delete all attachments of the given survey.
   * 
   * @param surveyId the id of the survey.
   */
  public void deleteAllBySurveyId(String surveyId) {
    String currentUser = SecurityUtils.getCurrentUserLogin();
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(SurveyAttachmentFilenameBuilder.buildFileNamePrefix(surveyId))));
    Iterable<GridFSFile> files = this.operations.find(query);
    files.forEach(file -> {
      SurveyAttachmentMetadata metadata =
          mongoTemplate.getConverter().read(SurveyAttachmentMetadata.class, file.getMetadata());
      javers.commitShallowDelete(currentUser, metadata);
    });
    this.operations.delete(query);
  }

  /**
   * Load all metadata objects from gridfs (ordered by indexInSurvey).
   * 
   * @param surveyId the id of the survey.
   * @return A list of metadata.
   */
  public List<SurveyAttachmentMetadata> findAllBySurvey(String surveyId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(SurveyAttachmentFilenameBuilder.buildFileNamePrefix(surveyId))));
    query.with(new Sort(Sort.Direction.ASC, "metadata.indexInSurvey"));
    Iterable<GridFSFile> files = this.operations.find(query);
    List<SurveyAttachmentMetadata> result = new ArrayList<>();
    files.forEach(gridfsFile -> {
      result.add(mongoTemplate.getConverter().read(SurveyAttachmentMetadata.class, 
          gridfsFile.getMetadata()));
    });
    return result;
  }

  /**
   * Delete all attachments of all surveys.
   */
  public void deleteAll() {
    String currentUser = SecurityUtils.getCurrentUserLogin();
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex(SurveyAttachmentFilenameBuilder.ALL_SURVEY_ATTACHMENTS));
    Iterable<GridFSFile> files = this.operations.find(query);
    files.forEach(file -> {
      SurveyAttachmentMetadata metadata = mongoTemplate.getConverter().read(
          SurveyAttachmentMetadata.class, file.getMetadata());
      javers.commitShallowDelete(currentUser, metadata);
    });
    this.operations.delete(query);
  }

  /**
   * Update the metadata of the attachment.
   * 
   * @param metadata The new metadata.
   */
  public void updateAttachmentMetadata(SurveyAttachmentMetadata metadata) {
    metadata.setVersion(metadata.getVersion() + 1);
    String currentUser = SecurityUtils.getCurrentUserLogin();
    metadata.setLastModifiedBy(currentUser);
    metadata.setLastModifiedDate(LocalDateTime.now());
    GridFSDBFile file = gridFs.findOne(metadata.getFileName());
    DBObject dbObject = (DBObject) mongoTemplate.getConverter().convertToMongoType(metadata);
    file.setMetaData(dbObject);
    file.save();
    javers.commit(currentUser, metadata);
  }

  /**
   * Delete the attachment and its metadata from gridfs.
   * 
   * @param surveyId The id of the survey.
   * @param filename The filename of the attachment.
   */
  public void deleteBySurveyIdAndFilename(String surveyId, String filename) {
    Query fileQuery = new Query(GridFsCriteria.whereFilename()
        .is(SurveyAttachmentFilenameBuilder.buildFileName(surveyId, filename)));
    GridFSFile file = this.operations.findOne(fileQuery);
    if (file == null) {
      return;
    }
    SurveyAttachmentMetadata metadata =
        mongoTemplate.getConverter().read(SurveyAttachmentMetadata.class, file.getMetadata());
    String currentUser = SecurityUtils.getCurrentUserLogin();
    this.operations.delete(fileQuery);
    javers.commitShallowDelete(currentUser, metadata);
  }
}
