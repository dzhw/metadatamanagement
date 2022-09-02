package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.javers.core.Javers;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.gridfs.model.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyCreateNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyDeleteNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.service.AttachmentMetadataHelper;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.helper.SurveyAttachmentFilenameBuilder;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing attachments for surveys.
 */
@Service
@RequiredArgsConstructor
public class SurveyAttachmentService {

  private final GridFsOperations operations;

  private final MongoTemplate mongoTemplate;

  private final Javers javers;

  private final AttachmentMetadataHelper<SurveyAttachmentMetadata> attachmentMetadataHelper;

  /**
   * Save the attachment for a survey.
   *
   * @param metadata The metadata of the attachment.
   * @return The GridFs filename.
   * @throws IOException thrown when the input stream is not closable
   */
  public String createSurveyAttachment(MultipartFile multipartFile,
      SurveyAttachmentMetadata metadata) throws IOException {
    if (metadata.isShadow()) {
      throw new ShadowCopyCreateNotAllowedException();
    }

    String currentUser = SecurityUtils.getCurrentUserLogin();
    attachmentMetadataHelper.initAttachmentMetadata(metadata, currentUser);
    metadata.generateId();
    metadata.setMasterId(metadata.getId());
    String filename = SurveyAttachmentFilenameBuilder.buildFileName(metadata);
    attachmentMetadataHelper.writeAttachmentMetadata(multipartFile, filename, metadata,
        currentUser);
    return filename;
  }

  /**
   * Delete all attachments of the given survey.
   *
   * @param surveyId the id of the survey.
   */
  public void deleteAllBySurveyId(String surveyId) {
    String currentUser = SecurityUtils.getCurrentUserLogin();
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(SurveyAttachmentFilenameBuilder.buildFileNamePrefix(surveyId))));
    Iterable<GridFSFile> files = this.operations.find(query);
    files.forEach(file -> {
      SurveyAttachmentMetadata metadata =
          mongoTemplate.getConverter().read(SurveyAttachmentMetadata.class, file.getMetadata());
      if (metadata.isShadow()) {
        throw new ShadowCopyDeleteNotAllowedException();
      }
      javers.commitShallowDelete(currentUser, metadata);
    });
    this.operations.delete(query);
  }

  /**
   * Load all metadata objects from gridfs (ordered by indexInSurvey).
   *
   * @param surveyId the id of the survey.
   * @return A list of metadata.
   */
  public List<SurveyAttachmentMetadata> findAllBySurvey(String surveyId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(SurveyAttachmentFilenameBuilder.buildFileNamePrefix(surveyId))));
    query.with(Sort.by(Sort.Direction.ASC, "metadata.indexInSurvey"));
    Iterable<GridFSFile> files = this.operations.find(query);
    List<SurveyAttachmentMetadata> result = new ArrayList<>();
    files.forEach(gridfsFile -> {
      result.add(mongoTemplate.getConverter().read(SurveyAttachmentMetadata.class,
          gridfsFile.getMetadata()));
    });
    return result;
  }

  /**
   * Load all metadata objects from gridfs (ordered by surveyNumber and indexInSurvey).
   *
   * @param dataAcquisitionProjectId The id of the {@link DataAcquisitionProject}.
   * @return A list of metadata.
   */
  public List<SurveyAttachmentMetadata> findAllByProject(String dataAcquisitionProjectId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex(SurveyAttachmentFilenameBuilder.ALL_SURVEY_ATTACHMENTS).andOperator(
            GridFsCriteria.whereMetaData("dataAcquisitionProjectId").is(dataAcquisitionProjectId)));
    query.with(Sort.by(Order.asc("metadata.surveyNumber"), Order.asc("metadata.indexInSurvey")));
    Iterable<GridFSFile> files = this.operations.find(query);
    List<SurveyAttachmentMetadata> result = new ArrayList<>();
    AtomicInteger countBySurveyNumber = new AtomicInteger(0);
    AtomicInteger currentSurveyNumber = new AtomicInteger(-1);
    files.forEach(gridfsFile -> {
      Integer surveyNumber = gridfsFile.getMetadata().getInteger("surveyNumber");
      if (!surveyNumber.equals(currentSurveyNumber.get())) {
        currentSurveyNumber.set(surveyNumber);
        currentSurveyNumber.set(0);
      } else {
        currentSurveyNumber.incrementAndGet();
      }
      gridfsFile.getMetadata().put("indexInSurvey", countBySurveyNumber.get());
      result.add(mongoTemplate.getConverter().read(SurveyAttachmentMetadata.class,
          gridfsFile.getMetadata()));
    });
    return result;
  }

  /**
   * Delete all attachments of all surveys.
   */
  public void deleteAll() {
    String currentUser = SecurityUtils.getCurrentUserLogin();
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex(SurveyAttachmentFilenameBuilder.ALL_SURVEY_ATTACHMENTS));
    Iterable<GridFSFile> files = this.operations.find(query);
    files.forEach(file -> {
      SurveyAttachmentMetadata metadata =
          mongoTemplate.getConverter().read(SurveyAttachmentMetadata.class, file.getMetadata());
      javers.commitShallowDelete(currentUser, metadata);
    });
    this.operations.delete(query);
  }

  /**
   * Update the metadata of the attachment.
   *
   * @param metadata The new metadata.
   */
  public void updateAttachmentMetadata(SurveyAttachmentMetadata metadata) {
    String filePath = SurveyAttachmentFilenameBuilder.buildFileName(metadata.getSurveyId(),
        metadata.getFileName());
    attachmentMetadataHelper.updateAttachmentMetadata(metadata, filePath);
  }

  /**
   * Delete the attachment and its metadata from gridfs.
   *
   * @param surveyId The id of the survey.
   * @param filename The filename of the attachment.
   */
  public void deleteBySurveyIdAndFilename(String surveyId, String filename) {
    Query fileQuery = new Query(GridFsCriteria.whereFilename()
        .is(SurveyAttachmentFilenameBuilder.buildFileName(surveyId, filename)));
    GridFSFile file = this.operations.findOne(fileQuery);
    if (file == null) {
      return;
    }
    SurveyAttachmentMetadata metadata =
        mongoTemplate.getConverter().read(SurveyAttachmentMetadata.class, file.getMetadata());
    if (metadata.isShadow()) {
      throw new ShadowCopyDeleteNotAllowedException();
    }
    String currentUser = SecurityUtils.getCurrentUserLogin();
    this.operations.delete(fileQuery);
    javers.commitShallowDelete(currentUser, metadata);
  }
}
