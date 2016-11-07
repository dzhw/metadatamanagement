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
      String surveyId, String fileName, String contentType) {
    
    
    String relativePathWithName = "/surveys/" + surveyId + "/" + fileName;
    this.deleteSurveyImage(relativePathWithName);
    
    GridFSFile gridFsFile = this.operations.store(inputStream, relativePathWithName, contentType);
    gridFsFile.validate();
    
    return gridFsFile.getFilename();
  }
  
  /**
   * This method delete an image from GridFS/MongoDB.
   * @param relativePathWithName The path of the image which should be deleted.
   */
  public void deleteSurveyImage(String relativePathWithName) {
        
    Query query = new Query(GridFsCriteria.whereFilename()
        .is(relativePathWithName));
    this.operations.delete(query);
  }
  
  /**
   * This method deletes all images of a survey from GridFS/MongoDB.
   * @param surveyId The id of the image to be deleted
   */
  public void deleteAllSurveyImagesById(String surveyId) {
        
    Query queryDe = new Query(GridFsCriteria.whereFilename()
        .is("/surveys/" + surveyId + "/" + surveyId + "_responserate_de"));
    this.operations.delete(queryDe);
    
    Query queryEn = new Query(GridFsCriteria.whereFilename()
        .is("/surveys/" + surveyId + "/" + surveyId + "_responserate_en"));
    this.operations.delete(queryEn);
  }
}
