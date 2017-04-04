package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import com.mongodb.gridfs.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;
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
  
  /**
   * Save the attachment for a data set. 
   * @param inputStream The inputStream of the attachment.
   * @param contentType The contentType of the attachment.
   * @param metadata The metadata of the attachment.
   * @return The GridFs filename.
   * @throws IOException thrown when the input stream is not closable
   */
  public String createDataSetAttachment(InputStream inputStream,
      String contentType, DataSetAttachmentMetadata metadata) throws IOException {
    try (InputStream in = inputStream) {
      String currentUser = SecurityUtils.getCurrentUserLogin();
      metadata.setVersion(0L);
      metadata.setCreatedDate(LocalDateTime.now());
      metadata.setCreatedBy(currentUser);
      metadata.setLastModifiedBy(currentUser);
      metadata.setLastModifiedDate(LocalDateTime.now());
      String filename = buildFileName(metadata);
      GridFSFile gridFsFile = this.operations.store(in, 
          filename, contentType, metadata);
      gridFsFile.validate();
      return gridFsFile.getFilename();      
    }
  }
  
  /**
   * Delete all attachments of the given data set.
   * @param dataSetId the id of the data set.
   */
  public void deleteAllByDataSetId(String dataSetId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(buildFileNamePrefix(dataSetId))));
    this.operations.delete(query);
  }
  
  /**
   * Load all metadata objects from gridfs.
   * @param dataSetId the id of the data set.
   * @return A list of metadata.
   */
  public List<DataSetAttachmentMetadata> findAllByDataSet(String dataSetId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(buildFileNamePrefix(dataSetId))));
    return this.operations.find(query).stream().map(gridfsFile -> {
      return mongoTemplate.getConverter().read(DataSetAttachmentMetadata.class, 
          gridfsFile.getMetaData());
    }).collect(Collectors.toList());
  }
  
  private String buildFileName(DataSetAttachmentMetadata metadata) {
    return buildFileNamePrefix(metadata.getDataSetId()) + metadata.getFileName(); 
  }
  
  private String buildFileNamePrefix(String dataSetId) {
    return "/data-sets/" + dataSetId + "/attachments/";
  }

  /**
   * Delete all attachments of all data sets.
   */
  public void deleteAll() {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote("/data-sets/") + ".*" + Pattern.quote("/attachments/")));
    this.operations.delete(query);
  }
}
