package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.javers.core.Javers;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import com.mongodb.client.gridfs.model.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.helper.DataSetAttachmentFilenameBuilder;
import lombok.RequiredArgsConstructor;

/**
 * Service responsible for retrieving an initializing the dataSet attachment history.
 * 
 * @author René Reitmann
 */
@Service
@RequiredArgsConstructor
public class DataSetAttachmentVersionsService {
  private final Javers javers;

  private final GridFsOperations operations;
  
  private final MongoTemplate mongoTemplate;

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
