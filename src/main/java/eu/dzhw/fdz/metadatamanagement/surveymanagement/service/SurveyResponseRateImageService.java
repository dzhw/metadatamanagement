package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyCreateNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyDeleteNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.service.AttachmentMetadataHelper;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyResponseRateImageMetadata;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

/**
 * Service for creating and updating survey images. Used for updating images in mongo
 *
 * @author Daniel Katzberg
 */
@Service
@RequiredArgsConstructor
public class SurveyResponseRateImageService {

  private final GridFsOperations operations;

  private final SurveyRepository surveyRepository;

  private final AttachmentMetadataHelper<SurveyResponseRateImageMetadata> attachmentMetadataHelper;

  /**
   * This method save an image into GridFS/MongoDB based on a byteArrayOutputStream.
   * Existing image should be deleted before saving/updating an image
   * @param metadata the metadata for the image
   * @return return the name of the saved image in the GridFS / MongoDB.
   * @throws IOException thrown if the input stream cannot be closed
   */
  public String saveSurveyImage(MultipartFile multipartFile,
      SurveyResponseRateImageMetadata metadata, String fileName) throws IOException {

    if (metadata.isShadow()) {
      throw new ShadowCopyCreateNotAllowedException();
    }

    String currentUser = SecurityUtils.getCurrentUserLogin();
    attachmentMetadataHelper.initAttachmentMetadata(metadata, currentUser);
    metadata.generateId();
    metadata.setMasterId(metadata.getId());
    // ensure there is no existing image
    deleteSurveyImage(metadata.getSurveyId(), fileName);
    String relativePathWithName = buildFilename(metadata.getSurveyId(), fileName);
    attachmentMetadataHelper.writeAttachmentMetadata(multipartFile, relativePathWithName, metadata,
        currentUser);
    return relativePathWithName;
  }

  /**
   * Delete the image from GridFS.
   *
   * @param surveyId The id of the survey.
   * @param fileName The filename of the response rate image.
   * @throws IOException if gridfs access fails
   */
  public void deleteSurveyImage(String surveyId, String fileName) throws IOException {
    Optional<Survey> survey = surveyRepository.findById(surveyId);
    if (survey.isPresent() && survey.get().isShadow()) {
      throw new ShadowCopyDeleteNotAllowedException();
    }
    String filename = buildFilename(surveyId, fileName);
    Query query = new Query(GridFsCriteria.whereFilename()
        .is(filename));
    this.operations.delete(query);
  }

  private String buildFilename(String surveyId, String fileName) {
    return "/surveys/" + surveyId + "/" + fileName;
  }

  /**
   * This method deletes all images of a survey from GridFS/MongoDB.
   * @param surveyId The id of the image to be deleted
   */
  public void deleteAllSurveyImagesById(String surveyId) {

    Optional<Survey> survey = surveyRepository.findById(surveyId);
    if (survey.isPresent() && survey.get().isShadow()) {
      throw new ShadowCopyDeleteNotAllowedException();
    }

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
   * Get the file name of the german response rate image.
   *
   * @param surveyId The id of a survey.
   * @return The name of a response rate image in german.
   */
  private String getResponseRateFileNameGerman(String surveyId) {
    String[] surveyNumberWithDollar = surveyId.split("-sy");
    String[] surveyNumber = surveyNumberWithDollar[1].split("\\$");
    return surveyNumber[0] + "_responserate_de";
  }

  /**
   * Get the file name of the english response rate image.
   *
   * @param surveyId The id of a survey.
   * @return The name of a response rate image in english.
   */
  private String getResponseRateFileNameEnglish(String surveyId) {
    String[] surveyNumberWithDollar = surveyId.split("-sy");
    String[] surveyNumber = surveyNumberWithDollar[1].split("\\$");
    return surveyNumber[0] + "_responserate_en";
  }

}
