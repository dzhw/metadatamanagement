package eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.QuestionImageService;

public class UnitTestImageHelper {
  
  private static final String PATH_QUESTION_IMAGE = "src/test/resources/data/imageExample/abs2005-ins1-1.1.png";
  
  /**
   * Saves a image to the Mongo DB for the Unit Tests.
   * @param questionImageService A question Image Service.
   * @param questionId The Id of the Question
   */
  public static void saveQuestionImage(QuestionImageService questionImageService, String questionId) {
    
    try (InputStream inputStream = new BufferedInputStream(new FileInputStream(PATH_QUESTION_IMAGE))) {
      MultipartFile multipartFile = new MockMultipartFile("file",
          PATH_QUESTION_IMAGE, "image/png", IOUtils.toByteArray(inputStream));
      questionImageService.saveQuestionImage(multipartFile, questionId);
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
