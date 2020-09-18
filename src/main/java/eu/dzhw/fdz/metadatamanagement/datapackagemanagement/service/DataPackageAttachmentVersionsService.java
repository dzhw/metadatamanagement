package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

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

import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackageAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.helper.DataPackageAttachmentFilenameBuilder;
import lombok.RequiredArgsConstructor;

/**
 * Service responsible for retrieving an initializing the dataPackage attachment history.
 * 
 * @author René Reitmann
 */
@Service
@RequiredArgsConstructor
public class DataPackageAttachmentVersionsService {
  private final Javers javers;

  private final GridFsOperations operations;

  private final MongoTemplate mongoTemplate;

  /**
   * Get the previous 10 versions of the dataPackage attachment.
   * 
   * @param dataPackageId The id of the dataPackage
   * @param filename The filename of the attachment
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous dataPackage versions or null if no dataPackage found
   */
  public List<DataPackageAttachmentMetadata> findPreviousDataPackageAttachmentVersions(
      String dataPackageId, String filename, int limit, int skip) {
    GridFSFile file = operations.findOne(new Query(GridFsCriteria.whereFilename()
        .is(DataPackageAttachmentFilenameBuilder.buildFileName(dataPackageId, filename))));

    if (file == null) {
      return null;
    }

    DataPackageAttachmentMetadata dataPackageAttachmentMetadata =
        mongoTemplate.getConverter().read(DataPackageAttachmentMetadata.class, file.getMetadata());

    QueryBuilder jqlQuery = QueryBuilder.byInstance(dataPackageAttachmentMetadata);

    Stream<Shadow<DataPackageAttachmentMetadata>> shadows =
        javers.findShadowsAndStream(jqlQuery.build());
    shadows = shadows.skip(skip).limit(limit);

    return shadows.map(shadow -> {
      DataPackageAttachmentMetadata metadata = shadow.get();
      if (metadata.getId() == null) {
        // deleted shadow
        metadata.setLastModifiedBy(shadow.getCommitMetadata().getAuthor());
        metadata.setLastModifiedDate(shadow.getCommitMetadata().getCommitDate());
      }
      return metadata;
    }).collect(Collectors.toList());
  }
}
