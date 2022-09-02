package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.gridfs.model.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyCreateNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.service.AttachmentMetadataHelper;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionImageMetadata;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

/**
 * Service for creating and updating images. Used for updating images in mongo.
 *
 * @author Daniel Katzberg
 */
@Service
@RequiredArgsConstructor
public class QuestionImageService {

  private final GridFsOperations operations;

  private final MongoTemplate mongoTemplate;

  private final AttachmentMetadataHelper<QuestionImageMetadata> attachmentMetadataHelper;

  /**
   * This method save an image into GridFS/MongoDB based on a byteArrayOutputStream.
   * Existing image should be deleted before saving/updating an image
   * @param questionImageMetadata The metadata of a question image to be saved
   * @return return the name of the saved image in the GridFS / MongoDB.
   * @throws IOException Thrown when the input stream cannot be closed
   */
  public String saveQuestionImage(MultipartFile multipartFile,
      QuestionImageMetadata questionImageMetadata) throws IOException {

    if (questionImageMetadata.isShadow()) {
      throw new ShadowCopyCreateNotAllowedException();
    }

    String currentUser = SecurityUtils.getCurrentUserLogin();
    attachmentMetadataHelper.initAttachmentMetadata(questionImageMetadata, currentUser);
    questionImageMetadata.generateId();
    questionImageMetadata.setMasterId(questionImageMetadata.getId());
    String filename = buildFileName(questionImageMetadata);
    attachmentMetadataHelper.writeAttachmentMetadata(multipartFile, filename,
        questionImageMetadata, currentUser);
    return filename;
  }

  private String buildFileName(QuestionImageMetadata metadata) {
    return buildFileNamePrefix(metadata.getQuestionId()) + metadata.getFileName();
  }

  private String buildFileNamePrefix(String questionId) {
    return "/questions/" + questionId + "/images/";
  }

  /**
   * This method finds images from GridFS/MongoDB.
   * @param questionId The id of a question
   */
  public List<QuestionImageMetadata> findByQuestionId(String questionId) {
    Query query = new Query(new GridFsCriteria("metadata.questionId").is(questionId))
        .with(Sort.by(Sort.Direction.ASC, "metadata.indexInQuestion"));
    Iterable<GridFSFile> files = this.operations.find(query);
    List<QuestionImageMetadata> result = new ArrayList<>();
    files.forEach(gridfsFile -> {
      result.add(mongoTemplate.getConverter().read(QuestionImageMetadata.class,
          gridfsFile.getMetadata()));
    });
    return result;
  }

  /**
   * This method delete an image from GridFS/MongoDB.
   * @param questionId the id of the question to which the image belongs
   */
  public void deleteQuestionImages(String questionId) {
    Query query = new Query(GridFsCriteria.whereMetaData("questionId").is(questionId)
        .andOperator(GridFsCriteria.whereMetaData("shadow").is(false)));
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
