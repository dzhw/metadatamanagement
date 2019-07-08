package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.javers.core.Javers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
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
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ProjectReleasedEvent;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;

/**
 * Service for managing attachments for data sets.
 *
 */
@Service
public class DataSetAttachmentService {

  @Autowired
  private GridFsOperations operations;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private Javers javers;

  @Autowired
  private DataSetRepository dataSetRepository;

  @Autowired
  private DataSetAttachmentMetadataShadowCopyDataSource shadowCopyDataSource;

  @Autowired
  private ShadowCopyService<DataSetAttachmentMetadata> shadowCopyService;

  @Autowired
  private AttachmentMetadataHelper<DataSetAttachmentMetadata> attachmentMetadataHelper;

  /**
   * Save the attachment for a data set.
   * 
   * @param metadata The metadata of the attachment.
   * @return The GridFs filename.
   * @throws IOException thrown when the input stream is not closable
   */
  public String createDataSetAttachment(MultipartFile multipartFile,
      DataSetAttachmentMetadata metadata) throws IOException {

    if (metadata.isShadow()) {
      throw new ShadowCopyCreateNotAllowedException();
    }

    String currentUser = SecurityUtils.getCurrentUserLogin();
    attachmentMetadataHelper.initAttachmentMetadata(metadata, currentUser);
    metadata.generateId();
    metadata.setMasterId(metadata.getId());
    String filename = DataSetAttachmentFilenameBuilder.buildFileName(metadata);
    attachmentMetadataHelper.writeAttachmentMetadata(multipartFile, filename, metadata,
        currentUser);

    return filename;
  }

  /**
   * Update the metadata of the attachment.
   * 
   * @param metadata The new metadata.
   */
  public void updateAttachmentMetadata(DataSetAttachmentMetadata metadata) {
    String filePath = DataSetAttachmentFilenameBuilder.buildFileName(metadata.getDataSetId(),
        metadata.getFileName());
    attachmentMetadataHelper.updateAttachmentMetadata(metadata, filePath);
  }

  /**
   * Delete all attachments of the given dataSet.
   *
   * @param dataSetId the id of the dataSet.
   */
  public void deleteAllByDataSetId(String dataSetId) {
    String currentUser = SecurityUtils.getCurrentUserLogin();
    Query query = new Query(GridFsCriteria.whereFilename().regex(
        "^" + Pattern.quote(DataSetAttachmentFilenameBuilder.buildFileNamePrefix(dataSetId))));
    Iterable<GridFSFile> files = this.operations.find(query);
    files.forEach(file -> {
      DataSetAttachmentMetadata metadata =
          mongoTemplate.getConverter().read(DataSetAttachmentMetadata.class, file.getMetadata());
      if (metadata.isShadow()) {
        throw new ShadowCopyDeleteNotAllowedException();
      }
      javers.commitShallowDelete(currentUser, metadata);
    });
    this.operations.delete(query);
  }

  /**
   * Load all metadata objects from gridfs (ordered by indexInDataSet).
   *
   * @param dataSetId The id of the dataSet.
   * @return A list of metadata.
   */
  public List<DataSetAttachmentMetadata> findAllByDataSet(String dataSetId) {
    Query query = new Query(GridFsCriteria.whereFilename().regex(
        "^" + Pattern.quote(DataSetAttachmentFilenameBuilder.buildFileNamePrefix(dataSetId))));
    query.with(new Sort(Sort.Direction.ASC, "metadata.indexInDataSet"));
    Iterable<GridFSFile> files = this.operations.find(query);
    List<DataSetAttachmentMetadata> result = new ArrayList<>();
    files.forEach(gridfsFile -> {
      result.add(mongoTemplate.getConverter().read(DataSetAttachmentMetadata.class,
          gridfsFile.getMetadata()));
    });
    return result;
  }

  /**
   * Delete all attachments of all dataSets.
   */
  public void deleteAll() {
    String currentUser = SecurityUtils.getCurrentUserLogin();
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote("/data-sets/") + ".*" + Pattern.quote("/attachments/")));
    Iterable<GridFSFile> files = this.operations.find(query);
    files.forEach(file -> {
      DataSetAttachmentMetadata metadata =
          mongoTemplate.getConverter().read(DataSetAttachmentMetadata.class, file.getMetadata());
      if (metadata.isShadow()) {
        throw new ShadowCopyDeleteNotAllowedException();
      }
      javers.commitShallowDelete(currentUser, metadata);
    });
    this.operations.delete(query);
  }

  /**
   * Delete the attachment and its metadata from gridfs.
   *
   * @param dataSetId The id of the dataSet.
   * @param filename The filename of the attachment.
   */
  public void deleteByDataSetIdAndFilename(String dataSetId, String filename) {
    Query fileQuery = new Query(GridFsCriteria.whereFilename()
        .is(DataSetAttachmentFilenameBuilder.buildFileName(dataSetId, filename)));
    GridFSFile file = this.operations.findOne(fileQuery);
    if (file == null) {
      return;
    }
    DataSetAttachmentMetadata metadata =
        mongoTemplate.getConverter().read(DataSetAttachmentMetadata.class, file.getMetadata());
    if (metadata.isShadow()) {
      throw new ShadowCopyDeleteNotAllowedException();
    }
    String currentUser = SecurityUtils.getCurrentUserLogin();
    this.operations.delete(fileQuery);
    javers.commitShallowDelete(currentUser, metadata);
  }

  /**
   * Attach the given file as data set report to the data set.
   * 
   * @param dataSetId The id of a {@link DataSet}.
   * @param reportFile The pdf file.
   * @throws IOException Thrown if the multipart file cannot be read.
   */
  public void attachDataSetReport(String dataSetId, MultipartFile reportFile) throws IOException {
    DataSet dataSet = dataSetRepository.findById(dataSetId).get();
    DataSetAttachmentMetadata metadata = DataSetAttachmentMetadata.builder().dataSetId(dataSetId)
        .dataAcquisitionProjectId(dataSet.getDataAcquisitionProjectId())
        .dataSetNumber(dataSet.getNumber())
        .fileName("dsreport-"
            + dataSet.getDataAcquisitionProjectId() + "-ds" + dataSet.getNumber() + ".pdf")
        .title("Datensatzreport:\n" + dataSet.getDescription().getDe())
        .description(new I18nString(
            "Codebook/Variablenreport/Datensatzreport von \"" + dataSet.getDescription().getDe()
                + "\"",
            "Codebook/Variable Report/Dataset Report of \"" + dataSet.getDescription().getEn()
                + "\""))
        .language("de").indexInDataSet(0).build();
    deleteByDataSetIdAndFilename(dataSetId, metadata.getFileName());
    createDataSetAttachment(reportFile, metadata);
  }

  /**
   * Create shadow copies for {@link DataSetAttachmentMetadata} on project release.
   * 
   * @param projectReleasedEvent Released project event
   */
  @EventListener
  public void onProjectReleasedEvent(ProjectReleasedEvent projectReleasedEvent) {
    shadowCopyService.createShadowCopies(projectReleasedEvent.getDataAcquisitionProject(),
        projectReleasedEvent.getPreviousReleaseVersion(), shadowCopyDataSource);
  }
}
