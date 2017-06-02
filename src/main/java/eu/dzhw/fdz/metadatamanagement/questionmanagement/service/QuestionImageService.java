package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.filemanagement.util.MimeTypeDetector;

/**
 * Service for creating and updating images. Used for updating images in mongo
 */
@Service
public class QuestionImageService {

  @Autowired
  private GridFsOperations operations;
  
  @Autowired
  private MimeTypeDetector mimeTypeDetector;

  /**
   * This method save an image into GridFS/MongoDB based on a byteArrayOutputStream.
   * Existing image should be deleted before saving/updating an image
   * @param questionId The id of the question to be saved
   * @return return the name of the saved image in the GridFS / MongoDB.
   * @throws IOException Thrown when the input stream cannot be closed
   */
  public String saveQuestionImage(MultipartFile multipartFile,
      String questionId) throws IOException {
    try (InputStream in = multipartFile.getInputStream()) {
      String contentType = mimeTypeDetector.detect(multipartFile);
      GridFSFile gridFsFile = this.operations.store(in, 
          "/questions/" + questionId, contentType);
      gridFsFile.validate();
      return gridFsFile.getFilename();      
    }
  }
  
  /**
   * This method  finds an image from GridFS/MongoDB. If no image was found, it returns by default
   * a null value.
   * @param questionId The id of a question
   */
  public GridFSDBFile findQuestionImage(String questionId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .is("/questions/" + questionId));
    return this.operations.findOne(query);
  }
  
  /**
   * This method delete an image from GridFS/MongoDB.
   * @param questionId the id of the question to which the image belongs
   */
  public void deleteQuestionImage(String questionId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .is("/questions/" + questionId));
    this.operations.delete(query);
  }
  
  /**
   * Delete all question images.
   */
  public void deleteAll() {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^/questions/"));
    this.operations.delete(query);
  }
}
