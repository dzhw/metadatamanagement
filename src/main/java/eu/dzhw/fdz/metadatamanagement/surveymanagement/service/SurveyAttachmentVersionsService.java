package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyAttachmentMetadata;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for retrieving an initializing the survey attachment history.
 * 
 * @author René Reitmann
 */
@Service
@Slf4j
public class SurveyAttachmentVersionsService {
  private Javers javers;
  
  private GridFsOperations operations;
  
  private MongoTemplate mongoTemplate;
  
  private MetadataManagementProperties metadataManagementProperties;

  /**
   * Construct the service.
   */
  @Autowired
  public SurveyAttachmentVersionsService(Javers javers, GridFsOperations operations, 
      MongoTemplate mongoTemplate, MetadataManagementProperties metadataManagementProperties) {
    this.javers = javers;
    this.operations = operations;
    this.mongoTemplate = mongoTemplate;
    this.metadataManagementProperties = metadataManagementProperties;
  }

  /**
   * Init Javers with all current survey attachments if there are no 
   * survey attachment commits in Javers yet.
   */
  @PostConstruct
  public void initJaversForSurveyAttachments() {
    if (!metadataManagementProperties.getServer().getInstanceIndex().equals(0)) {
      log.debug("This is server instance {} therefore skipping javers init for survey attachments.",
          metadataManagementProperties.getServer().getInstanceIndex());
      return;
    }
    List<CdoSnapshot> snapshots =
        javers.findSnapshots(QueryBuilder.byClass(SurveyAttachmentMetadata.class).limit(1).build());
    // only init if there are no studies yet
    if (snapshots.isEmpty()) {
      log.debug("Going to init javers with all current survey attachments");
      List<GridFSDBFile> files = operations.find(
          new Query(GridFsCriteria.whereFilename().regex(
              SurveyAttachmentFilenameBuilder.ALL_SURVEY_ATTACHMENTS)));
      files.stream().forEach(file -> {
        SurveyAttachmentMetadata surveyAttachmentMetadata = mongoTemplate.getConverter().read(
            SurveyAttachmentMetadata.class, file.getMetaData());
        surveyAttachmentMetadata.generateId();
        DBObject dbObject = new BasicDBObject();
        mongoTemplate.getConverter().write(surveyAttachmentMetadata, dbObject);
        file.setMetaData(dbObject);
        file.save();
        javers.commit(surveyAttachmentMetadata.getLastModifiedBy(), surveyAttachmentMetadata);
      });
    }
  }

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
    GridFSDBFile file = operations.findOne(
        new Query(GridFsCriteria.whereFilename().is(
            SurveyAttachmentFilenameBuilder.buildFileName(surveyId, filename))));
    
    if (file == null) {
      return null;
    }
    
    SurveyAttachmentMetadata surveyAttachmentMetadata = mongoTemplate.getConverter().read(
        SurveyAttachmentMetadata.class, file.getMetaData());
    
    QueryBuilder jqlQuery = QueryBuilder
        .byInstanceId(surveyAttachmentMetadata.getId(),SurveyAttachmentMetadata.class)
        .limit(limit).skip(skip);

    List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
    if (snapshots.isEmpty()) {
      return new ArrayList<>();
    }
    List<BigDecimal> commitIds = snapshots.stream()
        .map(snapshot -> snapshot.getCommitId().valueAsNumber())
        .collect(Collectors.toList());
    
    List<Shadow<SurveyAttachmentMetadata>> previousVersions = javers.findShadows(
        QueryBuilder.byInstance(surveyAttachmentMetadata)
        .withCommitIds(commitIds).build());

    return previousVersions.stream().map(shadow -> {
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
