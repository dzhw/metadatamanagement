package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.helper.DataSetAttachmentFilenameBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for retrieving an initializing the dataSet attachment history.
 * 
 * @author René Reitmann
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DataSetAttachmentVersionsService {
  private final Javers javers;
  
  private final GridFS gridFs;

  private final GridFsOperations operations;
  
  private final MongoTemplate mongoTemplate;
  
  private final MetadataManagementProperties metadataManagementProperties;

  /**
   * Init Javers with all current dataSet attachments if there are no dataSet attachment commits in
   * Javers yet.
   */
  @PostConstruct
  public void initJaversForDataSetAttachments() {
    if (!metadataManagementProperties.getServer().getInstanceIndex().equals(0)) {
      log.debug(
          "This is server instance {} therefore skipping javers init for dataSet attachments.",
          metadataManagementProperties.getServer().getInstanceIndex());
      return;
    }
    List<CdoSnapshot> snapshots =
        javers.findSnapshots(
            QueryBuilder.byClass(DataSetAttachmentMetadata.class).limit(1).build());
    // only init if there are no dataSets yet
    if (snapshots.isEmpty()) {
      log.debug("Going to init javers with all current dataSet attachments");
      BasicDBObject regQuery = new BasicDBObject();
      regQuery.append("$regex",
          DataSetAttachmentFilenameBuilder.ALL_DATASET_ATTACHMENTS);
      BasicDBObject filename = new BasicDBObject();
      filename.append("filename", regQuery);
      List<GridFSDBFile> files = gridFs.find(filename);
      files.forEach(file -> {
        DataSetAttachmentMetadata dataSetAttachmentMetadata = mongoTemplate.getConverter()
            .read(DataSetAttachmentMetadata.class, (BasicDBObject) file.getMetaData());
        dataSetAttachmentMetadata.generateId();
        BasicDBObject metadata = new BasicDBObject();
        mongoTemplate.getConverter().write(dataSetAttachmentMetadata, metadata);
        file.setMetaData(metadata);
        file.save();
        javers.commit(dataSetAttachmentMetadata.getLastModifiedBy(), dataSetAttachmentMetadata);
      });
    }
  }

  /**
   * Get the previous 10 versions of the dataSet attachment.
   * 
   * @param dataSetId The id of the dataSet
   * @param filename The filename of the attachment
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous dataSet versions or null if no dataSet found
   */
  public List<DataSetAttachmentMetadata> findPreviousDataSetAttachmentVersions(String dataSetId,
      String filename, int limit, int skip) {
    GridFSFile file = operations.findOne(
        new Query(GridFsCriteria.whereFilename().is(
            DataSetAttachmentFilenameBuilder.buildFileName(dataSetId, filename))));
    
    if (file == null) {
      return null;
    }
    
    DataSetAttachmentMetadata dataSetAttachmentMetadata =
        mongoTemplate.getConverter().read(DataSetAttachmentMetadata.class, file.getMetadata());
    
    QueryBuilder jqlQuery = QueryBuilder.byInstance(dataSetAttachmentMetadata);

    Stream<Shadow<DataSetAttachmentMetadata>> shadows =
        javers.findShadowsAndStream(jqlQuery.build());
    shadows = shadows.skip(skip).limit(limit);

    return shadows.map(shadow -> {
      DataSetAttachmentMetadata metadata = shadow.get();
      if (metadata.getId() == null) {
        // deleted shadow        
        metadata.setLastModifiedBy(shadow.getCommitMetadata().getAuthor());
        metadata.setLastModifiedDate(shadow.getCommitMetadata().getCommitDate());
      }
      return metadata;
    }).collect(Collectors.toList());
  }
}
