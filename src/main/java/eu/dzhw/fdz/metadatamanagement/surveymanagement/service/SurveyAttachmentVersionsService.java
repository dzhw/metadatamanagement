package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

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

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.helper.SurveyAttachmentFilenameBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for retrieving an initializing the survey attachment history.
 * 
 * @author René Reitmann
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SurveyAttachmentVersionsService {
  private final Javers javers;
  
  private final GridFsOperations operations;

  private final MongoTemplate mongoTemplate;
  
  private final MetadataManagementProperties metadataManagementProperties;

  /**
   * Get the previous 10 versions of the survey attachment.
   * 
   * @param surveyId The id of the survey
   * @param filename The filename of the attachment
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous survey versions or null if no survey found
   */
  public List<SurveyAttachmentMetadata> findPreviousSurveyAttachmentVersions(String surveyId,
      String filename, int limit, int skip) {
    GridFSFile file = operations.findOne(
        new Query(GridFsCriteria.whereFilename().is(
            SurveyAttachmentFilenameBuilder.buildFileName(surveyId, filename))));
    
    if (file == null) {
      return null;
    }
    
    SurveyAttachmentMetadata surveyAttachmentMetadata = mongoTemplate.getConverter().read(
        SurveyAttachmentMetadata.class, file.getMetadata());
    
    QueryBuilder jqlQuery = QueryBuilder
        .byInstanceId(surveyAttachmentMetadata.getId(),SurveyAttachmentMetadata.class);

    Stream<Shadow<SurveyAttachmentMetadata>> shadows = javers.findShadowsAndStream(
        jqlQuery.build());
    shadows = shadows.skip(skip).limit(limit);

    return shadows.map(shadow -> {
      SurveyAttachmentMetadata metadata = shadow.get();
      if (metadata.getId() == null) {
        // deleted shadow        
        metadata.setLastModifiedBy(shadow.getCommitMetadata().getAuthor());
        metadata.setLastModifiedDate(shadow.getCommitMetadata().getCommitDate());
      }
      return metadata;
    }).collect(Collectors.toList());
  }
}
