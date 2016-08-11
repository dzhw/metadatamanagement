package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import java.io.InputStream;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.filemanagement.service.FileService;

/**
 * Service for creating and updating images. Used for updating images in mongo
 */
@Service
public class ImageService {

  @Inject
  private FileService fileService;
  
  public static final String CONTENT_TYPE_IMAGE = "image/png";

  /**
   * This method save an image into GridFS/MongoDB based on a byteArrayOutputStream.
   * Existing image should be deleted before saving/updating an image
   * @param intputStream The image as byteArrayOutputStream
   * @param imageName The name of the image to be saved
   * @return return the name of the saved image in the GridFS / MongoDB.
   */
  public String saveQuestionImage(InputStream inputStream,
      String imageName) {
    deleteQuestionImage(imageName);
    return fileService.saveQuestionImage(inputStream, imageName, CONTENT_TYPE_IMAGE);
  }
  
  /**
   * This method delete an image from GridFS/MongoDB.
   * @param imageName The name of the image to be deleted
   */
  public void deleteQuestionImage(String imageName) {
    fileService.deleteOldImage(imageName);
  }
}
