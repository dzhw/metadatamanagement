package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import com.mongodb.gridfs.GridFSFile;

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
  private MongoTemplate mongoTemplate;
  
  /**
   * Save the attachment for a survey. 
   * @param inputStream The inputStream of the attachment.
   * @param contentType The contentType of the attachment.
   * @param metadata The metadata of the attachment.
   * @return The GridFs filename.
   * @throws IOException thrown when the input stream is not closable
   */
  public String createSurveyAttachment(InputStream inputStream,
      String contentType, SurveyAttachmentMetadata metadata) throws IOException {
    try (InputStream in = inputStream) {
      String currentUser = SecurityUtils.getCurrentUserLogin();
      metadata.setVersion(0L);
      metadata.setCreatedDate(LocalDateTime.now());
      metadata.setCreatedBy(currentUser);
      metadata.setLastModifiedBy(currentUser);
      metadata.setLastModifiedDate(LocalDateTime.now());
      GridFSFile gridFsFile = this.operations.store(in, 
          buildFileName(metadata), contentType, metadata);
      gridFsFile.validate();
      return gridFsFile.getFilename();      
    }
  }
  
  /**
   * Delete all attachments of the given survey.
   * @param surveyId the id of the survey.
   */
  public void deleteAllBySurveyId(String surveyId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(buildFileNamePrefix(surveyId))));
    this.operations.delete(query);
  }
  
  /**
   * Load all metadata objects from gridfs.
   * @param surveyId the id of the survey.
   * @return A list of metadata.
   */
  public List<SurveyAttachmentMetadata> findAllBySurvey(String surveyId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(buildFileNamePrefix(surveyId))));
    return this.operations.find(query).stream().map(gridfsFile -> {
      return mongoTemplate.getConverter().read(SurveyAttachmentMetadata.class, 
          gridfsFile.getMetaData());
    }).collect(Collectors.toList());
  }
  
  private String buildFileName(SurveyAttachmentMetadata metadata) {
    return buildFileNamePrefix(metadata.getSurveyId()) + metadata.getFileName(); 
  }
  
  private String buildFileNamePrefix(String surveyId) {
    return "/surveys/" + surveyId + "/attachments/";
  }

  /**
   * Delete all attachments of all surveys.
   */
  public void deleteAll() {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote("/surveys/") + ".*" + Pattern.quote("/attachments/")));
    this.operations.delete(query);
  }
}
