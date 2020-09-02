package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

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

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.helper.InstrumentAttachmentFilenameBuilder;
import lombok.RequiredArgsConstructor;

/**
 * Service responsible for retrieving an initializing the instrument attachment history.
 * 
 * @author René Reitmann
 */
@Service
@RequiredArgsConstructor
public class InstrumentAttachmentVersionsService {
  private final Javers javers;

  private final GridFsOperations operations;
  
  private final MongoTemplate mongoTemplate;

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
