package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import com.mongodb.gridfs.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;

/**
 * Service for managing attachments for studies.
 */
@Service
public class StudyAttachmentService {

  @Autowired
  private GridFsOperations operations;
  
  @Autowired
  private MongoTemplate mongoTemplate;
  
  /**
   * Save the attachment for a study. 
   * @param inputStream The inputStream of the attachment.
   * @param contentType The contentType of the attachment.
   * @param metadata The metadata of the attachment.
   * @return The GridFs filename.
   * @throws IOException thrown when the input stream cannot be closed
   */
  public String createStudyAttachment(InputStream inputStream,
      String contentType, StudyAttachmentMetadata metadata) throws IOException {
    try (InputStream in = inputStream) {
      String currentUser = SecurityUtils.getCurrentUserLogin();
      metadata.setVersion(0L);
      metadata.setCreatedDate(LocalDateTime.now());
      metadata.setCreatedBy(currentUser);
      metadata.setLastModifiedBy(currentUser);
      metadata.setLastModifiedDate(LocalDateTime.now());
      GridFSFile gridFsFile = this.operations.store(inputStream, 
          buildFileName(metadata), contentType, metadata);
      gridFsFile.validate();
      return gridFsFile.getFilename();      
    }
  }
  
  /**
   * Delete all attachments of the given study.
   * @param studyId the id of the study.
   */
  public void deleteAllByStudyId(String studyId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(buildFileNamePrefix(studyId))));
    this.operations.delete(query);
  }
  
  /**
   * Load all metadata objects from gridfs (ordered by indexInStudy).
   * @param studyId The id of the study.
   * @return A list of metadata.
   */
  public List<StudyAttachmentMetadata> findAllByStudy(String studyId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(buildFileNamePrefix(studyId))));
    query.with(new Sort(Sort.Direction.ASC, "metadata.indexInStudy"));
    return this.operations.find(query).stream().map(gridfsFile -> {
      return mongoTemplate.getConverter().read(StudyAttachmentMetadata.class, 
          gridfsFile.getMetaData());
    }).collect(Collectors.toList());
  }
  
  private String buildFileName(StudyAttachmentMetadata metadata) {
    return buildFileNamePrefix(metadata.getStudyId()) + metadata.getFileName(); 
  }
  
  private String buildFileNamePrefix(String studyId) {
    return "/studies/" + studyId + "/attachments/";
  }

  /**
   * Delete all attachments of all studies.
   */
  public void deleteAll() {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote("/studies/") + ".*" + Pattern.quote("/attachments/")));
    this.operations.delete(query);
  }
}
