package eu.dzhw.fdz.metadatamanagement.conceptmanagement.service;

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

import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.ConceptAttachmentMetadata;
import lombok.RequiredArgsConstructor;

/**
 * Service responsible for retrieving an initializing the concept attachment history.
 * 
 * @author René Reitmann
 */
@Service
@RequiredArgsConstructor
public class ConceptAttachmentVersionsService {
  private final Javers javers;

  private final GridFsOperations operations;

  private final MongoTemplate mongoTemplate;

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
