package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service;

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

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackageAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.helper.AnalysisPackageAttachmentFilenameBuilder;
import lombok.RequiredArgsConstructor;

/**
 * Service responsible for retrieving and initializing the analysis package attachment history.
 * 
 * @author René Reitmann
 */
@Service
@RequiredArgsConstructor
public class AnalysisPackageAttachmentVersionsService {
  private final Javers javers;

  private final GridFsOperations operations;

  private final MongoTemplate mongoTemplate;

  /**
   * Get the previous 10 versions of the analysis package attachment.
   * 
   * @param analysisPackageId The id of the analysisPackage
   * @param filename The filename of the attachment
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous analysis package versions or null if no analysis package found
   */
  public List<AnalysisPackageAttachmentMetadata> findPreviousAnalysisPackageAttachmentVersions(
      String analysisPackageId, String filename, int limit, int skip) {
    GridFSFile file = operations.findOne(new Query(GridFsCriteria.whereFilename()
        .is(AnalysisPackageAttachmentFilenameBuilder.buildFileName(analysisPackageId, filename))));

    if (file == null) {
      return null;
    }

    AnalysisPackageAttachmentMetadata analysisPackageAttachmentMetadata = mongoTemplate
        .getConverter().read(AnalysisPackageAttachmentMetadata.class, file.getMetadata());

    QueryBuilder jqlQuery = QueryBuilder.byInstance(analysisPackageAttachmentMetadata);

    Stream<Shadow<AnalysisPackageAttachmentMetadata>> shadows =
        javers.findShadowsAndStream(jqlQuery.build());
    shadows = shadows.skip(skip).limit(limit);

    return shadows.map(shadow -> {
      AnalysisPackageAttachmentMetadata metadata = shadow.get();
      if (metadata.getId() == null) {
        // deleted shadow
        metadata.setLastModifiedBy(shadow.getCommitMetadata().getAuthor());
        metadata.setLastModifiedDate(shadow.getCommitMetadata().getCommitDate());
      }
      return metadata;
    }).collect(Collectors.toList());
  }
}
