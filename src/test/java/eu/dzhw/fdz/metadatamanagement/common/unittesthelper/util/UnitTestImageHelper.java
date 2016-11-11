package eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.QuestionImageService;

public class UnitTestImageHelper {
  
  private static final String PATH_QUESTION_IMAGE = "src/test/resources/data/imageExample/abs2005-ins1-1.1.png";
  
  private static final String CONTENT_TYPE_PNG = "image/png";
  
  /**
   * Saves a image to the Mongo DB for the Unit Tests.
   * @param questionImageService A question Image Service.
   * @param questionId The Id of the Question
   */
  public static void saveQuestionImage(QuestionImageService questionImageService, String questionId) {
    
    try (InputStream inputStream = new BufferedInputStream(new FileInputStream(PATH_QUESTION_IMAGE))) {
      questionImageService.saveQuestionImage(inputStream, questionId, CONTENT_TYPE_PNG);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Deletes an test image from the Mongo DB for the Unit Test.
   * @param questionImageService A question Image Service.
   * @param questionId The Id of the Question
   */
  public static void deleteQuestionImage(QuestionImageService questionImageService, String questionId) {
    questionImageService.deleteQuestionImage(questionId);
  }

}
