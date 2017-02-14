package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
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
public class SurveyResponseRateImageService {

  @Autowired
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
    GridFSFile gridFsFile = this.operations.store(inputStream, relativePathWithName, contentType);
    gridFsFile.validate();
    
    return gridFsFile.getFilename();
  }
  
  /**
   * This method deletes all images of a survey from GridFS/MongoDB.
   * @param surveyId The id of the image to be deleted
   */
  public void deleteAllSurveyImagesById(String surveyId) {
        
    Query queryDe = new Query(GridFsCriteria.whereFilename()
        .is("/surveys/" + surveyId + "/" + this.getResponseRateFileNameGerman(surveyId)));
    this.operations.delete(queryDe);
    
    Query queryEn = new Query(GridFsCriteria.whereFilename()
        .is("/surveys/" + surveyId + "/" + this.getResponseRateFileNameEnglish(surveyId)));
    this.operations.delete(queryEn);
  }
  
  /**
   * This method checks the response rate file name, if it is correct. 
   * If it is a valid response rate file name, it returns true.
   * @param surveyId The id of the survey.
   * @param fileName The original file name of an uploaded reponserate file
   * @return returns true, if fileName is a valid name.
   */
  public boolean checkResponseRateFileName(String surveyId, String fileName) {
    return this.getResponseRateFileNameGerman(surveyId).equals(fileName)
        || this.getResponseRateFileNameEnglish(surveyId).equals(fileName);
  }
  
  /** 
   * @param surveyId The id of a survey.
   * @return The name of a response rate image in german. 
   */
  private String getResponseRateFileNameGerman(String surveyId) {
    String[] surveyNumberWithExclamationMark = surveyId.split("-sy");
    String[] surveyNumber = surveyNumberWithExclamationMark[1].split("!");
    return surveyNumber[0] + "_responserate_de.svg";
  }
  
  /**
   * @param surveyId The id of a survey.
   * @return The name of a response rate image in english.
   */
  private String getResponseRateFileNameEnglish(String surveyId) {
    String[] surveyNumberWithExclamationMark = surveyId.split("-sy");
    String[] surveyNumber = surveyNumberWithExclamationMark[1].split("!");
    return surveyNumber[0] + "_responserate_en.svg";
  }
  
}
