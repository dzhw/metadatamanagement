package eu.dzhw.fdz.metadatamanagement.conceptmanagement.service;

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
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.ConceptAttachmentMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for retrieving an initializing the concept attachment history.
 * 
 * @author René Reitmann
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ConceptAttachmentVersionsService {
  private final Javers javers;

  private final GridFS gridFs;

  private final GridFsOperations operations;

  private final MongoTemplate mongoTemplate;

  private final MetadataManagementProperties metadataManagementProperties;

  /**
   * Init Javers with all current concept attachments if there are no concept attachment commits in
   * Javers yet.
   */
  @PostConstruct
  public void initJaversForConceptAttachments() {
    if (!metadataManagementProperties.getServer().getInstanceIndex().equals(0)) {
      log.debug(
          "This is server instance {} therefore skipping javers init for concept attachments.",
          metadataManagementProperties.getServer().getInstanceIndex());
      return;
    }
    List<CdoSnapshot> snapshots = javers
        .findSnapshots(QueryBuilder.byClass(ConceptAttachmentMetadata.class).limit(1).build());
    // only init if there are no studies yet
    if (snapshots.isEmpty()) {
      log.debug("Going to init javers with all current concept attachments");
      BasicDBObject regQuery = new BasicDBObject();
      regQuery.append("$regex", ConceptAttachmentFilenameBuilder.ALL_CONCEPT_ATTACHMENTS);
      BasicDBObject filename = new BasicDBObject();
      filename.append("filename", regQuery);
      List<GridFSDBFile> files = gridFs.find(filename);
      files.forEach(file -> {
        ConceptAttachmentMetadata conceptAttachmentMetadata = mongoTemplate.getConverter()
            .read(ConceptAttachmentMetadata.class, (BasicDBObject) file.getMetaData());
        conceptAttachmentMetadata.generateId();
        BasicDBObject metadata = new BasicDBObject();
        mongoTemplate.getConverter().write(conceptAttachmentMetadata, metadata);
        file.setMetaData(metadata);
        file.save();
        javers.commit(conceptAttachmentMetadata.getLastModifiedBy(), conceptAttachmentMetadata);
      });
    }
  }

  /**
   * Get the previous 10 versions of the concept attachment.
   * 
   * @param conceptId The id of the concept
   * @param filename The filename of the attachment
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous concept versions or null if no concept found
   */
  public List<ConceptAttachmentMetadata> findPreviousConceptAttachmentVersions(String conceptId,
      String filename, int limit, int skip) {
    GridFSFile file = operations.findOne(new Query(GridFsCriteria.whereFilename()
        .is(ConceptAttachmentFilenameBuilder.buildFileName(conceptId, filename))));

    if (file == null) {
      return null;
    }

    ConceptAttachmentMetadata conceptAttachmentMetadata =
        mongoTemplate.getConverter().read(ConceptAttachmentMetadata.class, file.getMetadata());

    QueryBuilder jqlQuery = QueryBuilder.byInstance(conceptAttachmentMetadata);

    Stream<Shadow<ConceptAttachmentMetadata>> shadows =
        javers.findShadowsAndStream(jqlQuery.build());
    shadows = shadows.skip(skip).limit(limit);

    return shadows.map(shadow -> {
      ConceptAttachmentMetadata metadata = shadow.get();
      if (metadata.getId() == null) {
        // deleted shadow
        metadata.setLastModifiedBy(shadow.getCommitMetadata().getAuthor());
        metadata.setLastModifiedDate(shadow.getCommitMetadata().getCommitDate());
      }
      return metadata;
    }).collect(Collectors.toList());
  }
}
