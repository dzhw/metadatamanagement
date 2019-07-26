package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

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
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.helper.InstrumentAttachmentFilenameBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for retrieving an initializing the instrument attachment history.
 * 
 * @author René Reitmann
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class InstrumentAttachmentVersionsService {
  private final Javers javers;
  
  private final GridFS gridFs;

  private final GridFsOperations operations;
  
  private final MongoTemplate mongoTemplate;
  
  private final MetadataManagementProperties metadataManagementProperties;

  /**
   * Init Javers with all current instrument attachments if there are no instrument attachment
   * commits in Javers yet.
   */
  @PostConstruct
  public void initJaversForInstrumentAttachments() {
    if (!metadataManagementProperties.getServer().getInstanceIndex().equals(0)) {
      log.debug(
          "This is server instance {} therefore skipping javers init for instrument attachments.",
          metadataManagementProperties.getServer().getInstanceIndex());
      return;
    }
    List<CdoSnapshot> snapshots =
        javers.findSnapshots(
            QueryBuilder.byClass(InstrumentAttachmentMetadata.class).limit(1).build());
    // only init if there are no instruments yet
    if (snapshots.isEmpty()) {
      log.debug("Going to init javers with all current instrument attachments");
      BasicDBObject regQuery = new BasicDBObject();
      regQuery.append("$regex",
          InstrumentAttachmentFilenameBuilder.ALL_INSTRUMENT_ATTACHMENTS);
      BasicDBObject filename = new BasicDBObject();
      filename.append("filename", regQuery);
      List<GridFSDBFile> files = gridFs.find(filename);
      files.forEach(file -> {
        InstrumentAttachmentMetadata instrumentAttachmentMetadata = mongoTemplate.getConverter()
            .read(InstrumentAttachmentMetadata.class, (BasicDBObject) file.getMetaData());
        instrumentAttachmentMetadata.generateId();
        BasicDBObject metadata = new BasicDBObject();
        mongoTemplate.getConverter().write(instrumentAttachmentMetadata, metadata);
        file.setMetaData(metadata);
        file.save();
        javers.commit(instrumentAttachmentMetadata.getLastModifiedBy(),
            instrumentAttachmentMetadata);
      });
    }
  }

  /**
   * Get the previous 10 versions of the instrument attachment.
   * 
   * @param instrumentId The id of the instrument
   * @param filename The filename of the attachment
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous instrument versions or null if no instrument found
   */
  public List<InstrumentAttachmentMetadata> findPreviousInstrumentAttachmentVersions(
      String instrumentId,
      String filename, int limit, int skip) {
    GridFSFile file = operations.findOne(
        new Query(GridFsCriteria.whereFilename().is(
            InstrumentAttachmentFilenameBuilder.buildFileName(instrumentId, filename))));
    
    if (file == null) {
      return null;
    }
    
    InstrumentAttachmentMetadata instrumentAttachmentMetadata =
        mongoTemplate.getConverter().read(InstrumentAttachmentMetadata.class, file.getMetadata());
    
    QueryBuilder jqlQuery = QueryBuilder.byInstance(instrumentAttachmentMetadata);

    Stream<Shadow<InstrumentAttachmentMetadata>> shadows =
        javers.findShadowsAndStream(jqlQuery.build());
    shadows = shadows.skip(skip).limit(limit);

    return shadows.map(shadow -> {
      InstrumentAttachmentMetadata metadata = shadow.get();
      if (metadata.getId() == null) {
        // deleted shadow        
        metadata.setLastModifiedBy(shadow.getCommitMetadata().getAuthor());
        metadata.setLastModifiedDate(shadow.getCommitMetadata().getCommitDate());
      }
      return metadata;
    }).collect(Collectors.toList());
  }
}
