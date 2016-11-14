package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import java.io.InputStream;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

/**
 * Service for creating and updating images. Used for updating images in mongo
 */
@Service
public class QuestionImageService {

  @Inject
  private GridFsOperations operations;

  /**
   * This method save an image into GridFS/MongoDB based on a byteArrayOutputStream.
   * Existing image should be deleted before saving/updating an image
   * @param inputStream The image as byteArrayOutputStream
   * @param questionId The id of the question to be saved
   * @param contentType The mime-type of the image
   * @return return the name of the saved image in the GridFS / MongoDB.
   */
  public String saveQuestionImage(InputStream inputStream,
      String questionId, String contentType) {
    GridFSFile gridFsFile = this.operations.store(inputStream, 
        "/questions/" + questionId, contentType);
    gridFsFile.validate();
    return gridFsFile.getFilename();
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
   * @param imageName The name of the image to be deleted
   */
  public void deleteQuestionImage(String imageName) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .is("/questions/" + imageName));
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
