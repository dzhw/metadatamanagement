package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import java.io.InputStream;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import com.mongodb.gridfs.GridFSFile;

/**
 * Service for creating and updating survey images. Used for updating images in mongo
 * 
 * @author Daniel Katzberg
 */
@Service
public class SurveyImageService {

  @Inject
  private GridFsOperations operations;

  /**
   * This method save an image into GridFS/MongoDB based on a byteArrayOutputStream.
   * Existing image should be deleted before saving/updating an image
   * @param inputStream The image as byteArrayOutputStream
   * @param surveyId The id of the question to be saved
   * @param contentType The mime-type of the image
   * @return return the name of the saved image in the GridFS / MongoDB.
   */
  public String saveSurveyImage(InputStream inputStream,
      String surveyId, String contentType) {
    deleteSurveyImage(surveyId);
    GridFSFile gridFsFile = this.operations.store(inputStream, 
        "/surveys/" + surveyId, contentType);
    gridFsFile.validate();
    return gridFsFile.getFilename();
  }
  
  /**
   * This method delete an image from GridFS/MongoDB.
   * @param imageName The name of the image to be deleted
   */
  public void deleteSurveyImage(String imageName) {
        
    Query query = new Query(GridFsCriteria.whereFilename()
        .is("/surveys/" + imageName));
    this.operations.delete(query);
  }
}
