package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import com.mongodb.client.gridfs.model.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudyAttachmentMetadata;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for retrieving an initializing the study attachment history.
 * 
 * @author René Reitmann
 */
@Service
@Slf4j
public class StudyAttachmentVersionsService {
  private Javers javers;
  
  private GridFsOperations operations;
  
  private MongoTemplate mongoTemplate;
  
  private MetadataManagementProperties metadataManagementProperties;

  /**
   * Construct the service.
   */
  @Autowired
  public StudyAttachmentVersionsService(Javers javers, GridFsOperations operations, 
      MongoTemplate mongoTemplate, MetadataManagementProperties metadataManagementProperties) {
    this.javers = javers;
    this.operations = operations;
    this.mongoTemplate = mongoTemplate;
    this.metadataManagementProperties = metadataManagementProperties;
  }

  /**
   * Init Javers with all current study attachments if there are no 
   * study attachment commits in Javers yet.
   */
  @PostConstruct
  public void initJaversForStudyAttachments() {
    if (!metadataManagementProperties.getServer().getInstanceIndex().equals(0)) {
      log.debug("This is server instance {} therefore skipping javers init for study attachments.", 
          metadataManagementProperties.getServer().getInstanceIndex());
      return;
    }
    List<CdoSnapshot> snapshots =
        javers.findSnapshots(QueryBuilder.byClass(StudyAttachmentMetadata.class).limit(1).build());
    // only init if there are no studies yet
    if (snapshots.isEmpty()) {
      log.debug("Going to init javers with all current study attachments");
      Iterable<GridFSFile> files = operations.find(
          new Query(GridFsCriteria.whereFilename().regex(
              StudyAttachmentFilenameBuilder.ALL_STUDY_ATTACHMENTS)));
      files.forEach(file -> {
        StudyAttachmentMetadata studyAttachmentMetadata = mongoTemplate.getConverter().read(
            StudyAttachmentMetadata.class, file.getMetadata());
        javers.commit(studyAttachmentMetadata.getLastModifiedBy(), studyAttachmentMetadata);
      });
    }
  }

  /**
   * Get the previous 10 versions of the study attachment.
   * 
   * @param studyId The id of the study
   * @param filename The filename of the attachment
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous study versions or null if no study found
   */
  public List<StudyAttachmentMetadata> findPreviousStudyAttachmentVersions(String studyId,
      String filename, int limit, int skip) {
    GridFSFile file = operations.findOne(
        new Query(GridFsCriteria.whereFilename().is(
            StudyAttachmentFilenameBuilder.buildFileName(studyId, filename))));
    
    if (file == null) {
      return null;
    }
    
    StudyAttachmentMetadata studyAttachmentMetadata = mongoTemplate.getConverter().read(
        StudyAttachmentMetadata.class, file.getMetadata());
    
    QueryBuilder jqlQuery = QueryBuilder.byInstance(studyAttachmentMetadata);

    Stream<Shadow<StudyAttachmentMetadata>> shadows = javers.findShadowsAndStream(jqlQuery.build());
    shadows = shadows.skip(skip).limit(limit);

    return shadows.map(shadow -> {
      StudyAttachmentMetadata metadata = shadow.get();
      if (metadata.getId() == null) {
        // deleted shadow        
        metadata.setLastModifiedBy(shadow.getCommitMetadata().getAuthor());
        metadata.setLastModifiedDate(shadow.getCommitMetadata().getCommitDate());
      }
      return metadata;
    }).collect(Collectors.toList());
  }
}
