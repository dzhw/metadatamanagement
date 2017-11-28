package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

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
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.gridfs.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.filemanagement.util.MimeTypeDetector;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionImageMetadata;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;

/**
 * Service for creating and updating images. Used for updating images in mongo.
 * 
 * @author Daniel Katzberg
 */
@Service
public class QuestionImageService {

  @Autowired
  private GridFsOperations operations;
  
  @Autowired
  private MongoTemplate mongoTemplate;
  
  @Autowired
  private MimeTypeDetector mimeTypeDetector;

  /**
   * This method save an image into GridFS/MongoDB based on a byteArrayOutputStream.
   * Existing image should be deleted before saving/updating an image
   * @param questionImageMetadata The metadata of a question image to be saved
   * @return return the name of the saved image in the GridFS / MongoDB.
   * @throws IOException Thrown when the input stream cannot be closed
   */
  public String saveQuestionImage(MultipartFile multipartFile,
      QuestionImageMetadata questionImageMetadata) throws IOException {
    try (InputStream in = multipartFile.getInputStream()) {
      String currentUser = SecurityUtils.getCurrentUserLogin();
      questionImageMetadata.setVersion(0L);
      questionImageMetadata.setCreatedDate(LocalDateTime.now());
      questionImageMetadata.setCreatedBy(currentUser);
      questionImageMetadata.setLastModifiedBy(currentUser);
      questionImageMetadata.setLastModifiedDate(LocalDateTime.now());
      String filename = buildFileName(questionImageMetadata);
      String contentType = mimeTypeDetector.detect(multipartFile);
      GridFSFile gridFsFile = 
          this.operations.store(in, filename, contentType, questionImageMetadata);
      gridFsFile.validate();
      return gridFsFile.getFilename();      
    }
  }
  
  private String buildFileName(QuestionImageMetadata metadata) {
    return buildFileNamePrefix(metadata.getQuestionId()) + metadata.getFileName(); 
  }
  
  private String buildFileNamePrefix(String questionId) {
    return "/questions/" + questionId + "/images/";
  }
  
  /**
   * This method finds images from GridFS/MongoDB. If no image was found, it returns by default
   * a null value.
   * @param questionId The id of a question
   */
  public List<QuestionImageMetadata> findQuestionImages(String questionId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(buildFileNamePrefix(questionId))));
    return this.operations.find(query).stream().map(gridfsFile -> {
      return mongoTemplate.getConverter().read(QuestionImageMetadata.class, 
          gridfsFile.getMetaData());
    }).collect(Collectors.toList());
  }
  
  /**
   * This method delete an image from GridFS/MongoDB.
   * @param questionId the id of the question to which the image belongs
   */
  public void deleteQuestionImages(String questionId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(buildFileNamePrefix(questionId))));
    this.operations.delete(query);
  }
  
  /**
   * Delete all question images.
   */
  public void deleteAll() {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote("/questions/") + ".*" + Pattern.quote("/images/")));
    this.operations.delete(query);
  }
}
