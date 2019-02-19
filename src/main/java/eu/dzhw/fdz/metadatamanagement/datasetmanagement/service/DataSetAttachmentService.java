package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.filemanagement.util.MimeTypeDetector;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ProjectReleasedEvent;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;
import org.bson.Document;
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

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service for managing attachments for data sets.
 * 
 */
@Service
public class DataSetAttachmentService {

  @Autowired
  private GridFS gridFs;

  @Autowired
  private GridFsOperations operations;
  
  @Autowired
  private MongoTemplate mongoTemplate;
  
  @Autowired
  private MimeTypeDetector mimeTypeDetector;
  
  @Autowired
  private Javers javers;

  @Autowired
  private DataSetAttachmentMetadataShadowCopyDataSource shadowCopyDataSource;

  @Autowired
  private ShadowCopyService<DataSetAttachmentMetadata> shadowCopyService;

  /**
   * Save the attachment for a data set. 
   * @param metadata The metadata of the attachment.
   * @return The GridFs filename.
   * @throws IOException thrown when the input stream is not closable
   */
  public String createDataSetAttachment(MultipartFile multipartFile,
      DataSetAttachmentMetadata metadata) throws IOException {
    try (InputStream in = multipartFile.getInputStream()) {
      String currentUser = SecurityUtils.getCurrentUserLogin();
      metadata.setVersion(0L);
      metadata.setCreatedDate(LocalDateTime.now());
      metadata.setCreatedBy(currentUser);
      metadata.setLastModifiedBy(currentUser);
      metadata.setLastModifiedDate(LocalDateTime.now());
      metadata.generateId();
      String contentType = mimeTypeDetector.detect(multipartFile);
      String filename = DataSetAttachmentFilenameBuilder.buildFileName(metadata);
      this.operations.store(in, filename, contentType, metadata);
      javers.commit(currentUser, metadata);
      return filename;      
    }
  }
  
  /**
   * Update the metadata of the attachment.
   * 
   * @param metadata The new metadata.
   */
  public void updateAttachmentMetadata(DataSetAttachmentMetadata metadata) {
    metadata.setVersion(metadata.getVersion() + 1);
    String currentUser = SecurityUtils.getCurrentUserLogin();
    metadata.setLastModifiedBy(currentUser);
    metadata.setLastModifiedDate(LocalDateTime.now());
    GridFSDBFile file = gridFs.findOne(DataSetAttachmentFilenameBuilder
        .buildFileName(metadata.getDataSetId(), metadata.getFileName()));
    BasicDBObject dbObject =
        new BasicDBObject((Document) mongoTemplate.getConverter().convertToMongoType(metadata));
    file.setMetaData(dbObject);
    file.save();
    javers.commit(currentUser, metadata);
  }

  /**
   * Delete all attachments of the given dataSet.
   * 
   * @param dataSetId the id of the dataSet.
   */
  public void deleteAllByDataSetId(String dataSetId) {
    String currentUser = SecurityUtils.getCurrentUserLogin();
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex(
            "^" + Pattern.quote(DataSetAttachmentFilenameBuilder.buildFileNamePrefix(dataSetId))));
    Iterable<GridFSFile> files = this.operations.find(query);
    files.forEach(file -> {
      DataSetAttachmentMetadata metadata =
          mongoTemplate.getConverter().read(DataSetAttachmentMetadata.class, file.getMetadata());
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
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex(
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
    String currentUser = SecurityUtils.getCurrentUserLogin();
    this.operations.delete(fileQuery);
    javers.commitShallowDelete(currentUser, metadata);
  }

  /**
   * Create shadow copies for {@link DataSetAttachmentMetadata} on project release.
   * @param projectReleasedEvent Released project event
   */
  @EventListener
  public void onProjectReleasedEvent(ProjectReleasedEvent projectReleasedEvent) {
    shadowCopyService.createShadowCopies(projectReleasedEvent.getDataAcquisitionProject(),
        projectReleasedEvent.getPreviousReleaseVersion(), shadowCopyDataSource);
  }
}
