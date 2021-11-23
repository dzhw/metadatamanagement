package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.javers.core.Javers;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.gridfs.model.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyCreateNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyDeleteNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.service.AttachmentMetadataHelper;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackageAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackageAttachmentTypes;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.helper.DataPackageAttachmentFilenameBuilder;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing attachments for dataPackages.
 */
@Service
@RequiredArgsConstructor
public class DataPackageAttachmentService {
  private final DataPackageRepository dataPackageRepository;

  private final GridFsOperations operations;

  private final MongoTemplate mongoTemplate;

  private final Javers javers;

  private final AttachmentMetadataHelper<DataPackageAttachmentMetadata> attachmentMetadataHelper;

  /**
   * Save the attachment for a dataPackage.
   *
   * @param metadata The metadata of the attachment.
   * @return The GridFs filename.
   * @throws IOException thrown when the input stream cannot be closed
   */
  public String createDataPackageAttachment(MultipartFile multipartFile,
      DataPackageAttachmentMetadata metadata) throws IOException {

    if (metadata.isShadow()) {
      throw new ShadowCopyCreateNotAllowedException();
    }

    String currentUser = SecurityUtils.getCurrentUserLogin();
    attachmentMetadataHelper.initAttachmentMetadata(metadata, currentUser);
    metadata.generateId();
    metadata.setMasterId(metadata.getId());
    String filename = DataPackageAttachmentFilenameBuilder.buildFileName(metadata);
    attachmentMetadataHelper.writeAttachmentMetadata(multipartFile, filename, metadata,
        currentUser);

    return filename;
  }

  /**
   * Update the metadata of the attachment.
   *
   * @param metadata The new metadata.
   */
  public void updateAttachmentMetadata(DataPackageAttachmentMetadata metadata) {
    String filePath = DataPackageAttachmentFilenameBuilder
        .buildFileName(metadata.getDataPackageId(), metadata.getFileName());
    attachmentMetadataHelper.updateAttachmentMetadata(metadata, filePath);
  }

  /**
   * Delete all attachments of the given dataPackage.
   *
   * @param dataPackageId the id of the dataPackage.
   */
  public void deleteAllByDataPackageId(String dataPackageId) {
    String currentUser = SecurityUtils.getCurrentUserLogin();
    Query query = new Query(GridFsCriteria.whereFilename().regex("^"
        + Pattern.quote(DataPackageAttachmentFilenameBuilder.buildFileNamePrefix(dataPackageId))));
    Iterable<GridFSFile> files = this.operations.find(query);
    files.forEach(file -> {
      DataPackageAttachmentMetadata metadata = mongoTemplate.getConverter()
          .read(DataPackageAttachmentMetadata.class, file.getMetadata());
      if (metadata.isShadow()) {
        throw new ShadowCopyDeleteNotAllowedException();
      }
      javers.commitShallowDelete(currentUser, metadata);
    });
    this.operations.delete(query);
  }

  /**
   * Load all metadata objects from gridfs (ordered by indexInDataPackage).
   *
   * @param dataPackageId The id of the dataPackage.
   * @return A list of metadata.
   */
  public List<DataPackageAttachmentMetadata> findAllByDataPackage(String dataPackageId) {
    Query query = new Query(GridFsCriteria.whereFilename().regex("^"
        + Pattern.quote(DataPackageAttachmentFilenameBuilder.buildFileNamePrefix(dataPackageId))));
    query.with(Sort.by(Sort.Direction.ASC, "metadata.indexInDataPackage"));
    Iterable<GridFSFile> files = this.operations.find(query);
    List<DataPackageAttachmentMetadata> result = new ArrayList<>();
    files.forEach(gridfsFile -> {
      result.add(mongoTemplate.getConverter().read(DataPackageAttachmentMetadata.class,
          gridfsFile.getMetadata()));
    });
    return result;
  }

  /**
   * Load all metadata objects from gridfs (ordered by indexInDataPackage).
   *
   * @param dataAcquisitionProjectId The id of the {@link DataAcquisitionProject}.
   * @return A list of metadata.
   */
  public List<DataPackageAttachmentMetadata> findAllByProject(String dataAcquisitionProjectId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex(DataPackageAttachmentFilenameBuilder.ALL_STUDY_ATTACHMENTS).andOperator(
            GridFsCriteria.whereMetaData("dataAcquisitionProjectId").is(dataAcquisitionProjectId)));
    query.with(Sort.by(Sort.Direction.ASC, "metadata.indexInDataPackage"));
    Iterable<GridFSFile> files = this.operations.find(query);
    List<DataPackageAttachmentMetadata> result = new ArrayList<>();
    AtomicInteger countByDataPackage = new AtomicInteger(0);
    files.forEach(gridfsFile -> {
      gridfsFile.getMetadata().put("indexInDataPackage", countByDataPackage.getAndIncrement());
      result.add(mongoTemplate.getConverter().read(DataPackageAttachmentMetadata.class,
          gridfsFile.getMetadata()));
    });
    return result;
  }

  /**
   * Delete the attachment and its metadata from gridfs.
   *
   * @param dataPackageId The id of the dataPackage.
   * @param filename The filename of the attachment.
   */
  public void deleteByDataPackageIdAndFilename(String dataPackageId, String filename) {
    Query fileQuery = new Query(GridFsCriteria.whereFilename()
        .is(DataPackageAttachmentFilenameBuilder.buildFileName(dataPackageId, filename)));
    GridFSFile file = this.operations.findOne(fileQuery);
    if (file == null) {
      return;
    }
    DataPackageAttachmentMetadata metadata =
        mongoTemplate.getConverter().read(DataPackageAttachmentMetadata.class, file.getMetadata());
    if (metadata.isShadow()) {
      throw new ShadowCopyDeleteNotAllowedException();
    }
    String currentUser = SecurityUtils.getCurrentUserLogin();
    this.operations.delete(fileQuery);
    javers.commitShallowDelete(currentUser, metadata);
  }

  /**
   * Attach the given file as data package overview to the data package.
   *
   * @param language The language of the report.
   * @param dataPackageId The id of a {@link DataPackage}.
   * @param overviewFile The pdf file.
   * @throws IOException Thrown if the multipart file cannot be read.
   */
  public void attachDataPackageOverview(String dataPackageId, String language,
      MultipartFile overviewFile) throws IOException {
    DataPackage dataPackage = dataPackageRepository.findById(dataPackageId).get();
    DataPackageAttachmentMetadata metadata = null;
    switch (language) {
      case "de":
        metadata = DataPackageAttachmentMetadata.builder().dataPackageId(dataPackageId)
            .dataAcquisitionProjectId(dataPackage.getDataAcquisitionProjectId())
            .fileName(dataPackage.getDataAcquisitionProjectId() + "_Overview_de.pdf")
            .title(dataPackage.getTitle().getDe())
            .description(new I18nString("Datenpaketübersicht", "Data Package Overview"))
            .type(DataPackageAttachmentTypes.OTHER)
            .language("de").indexInDataPackage(-1).build();
        break;
      case "en":
        metadata = DataPackageAttachmentMetadata.builder().dataPackageId(dataPackageId)
            .dataAcquisitionProjectId(dataPackage.getDataAcquisitionProjectId())
            .fileName(dataPackage.getDataAcquisitionProjectId() + "_Overview_en.pdf")
            .title(dataPackage.getTitle().getEn())
            .description(new I18nString("Datenpaketübersicht", "Data Package Overview"))
            .type(DataPackageAttachmentTypes.OTHER)
            .language("en").indexInDataPackage(-2).build();
        break;
      default:
        throw new IllegalArgumentException("Unsupported language '" + language + "'!");
    }
    deleteByDataPackageIdAndFilename(dataPackageId, metadata.getFileName());
    createDataPackageAttachment(overviewFile, metadata);
  }
}
