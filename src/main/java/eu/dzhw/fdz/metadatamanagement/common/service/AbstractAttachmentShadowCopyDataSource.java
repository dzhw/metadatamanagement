package eu.dzhw.fdz.metadatamanagement.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;

import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;

/**
 * Provides common implementation shared between {@link ShadowCopyDataSource} that handle
 * attachments.
 * @param <T> {@link AbstractShadowableRdcDomainObject}
 */
public abstract class AbstractAttachmentShadowCopyDataSource
    <T extends AbstractShadowableRdcDomainObject> implements ShadowCopyDataSource<T> {

  protected final GridFsOperations gridFsOperations;

  protected final MongoTemplate mongoTemplate;

  protected final GridFsMetadataUpdateService gridFsMetadataUpdateService;

  private final Class<T> attachmentClass;

  /**
   * Creates a new instance.
   */
  protected AbstractAttachmentShadowCopyDataSource(GridFsOperations gridFsOperations,
      MongoTemplate mongoTemplate, GridFsMetadataUpdateService gridFsMetadataUpdateService,
      Class<T> attachmentClass) {
    this.gridFsOperations = gridFsOperations;
    this.mongoTemplate = mongoTemplate;
    this.gridFsMetadataUpdateService = gridFsMetadataUpdateService;
    this.attachmentClass = attachmentClass;
  }

  @Override
  public Stream<T> getMasters(String dataAcquisitionProjectId) {
    Query query = new Query(
        GridFsCriteria.whereMetaData("dataAcquisitionProjectId").is(dataAcquisitionProjectId)
            .andOperator(GridFsCriteria.whereFilename().regex(getMasterFileNamePattern())
                .andOperator(GridFsCriteria.whereMetaData("shadow").is(false))));

    return convertIterableToStream(gridFsOperations.find(query));
  }


  @Override
  public Optional<T> findPredecessorOfShadowCopy(T shadowCopy, String previousVersion) {

    Query query = new Query(
        GridFsCriteria.whereMetaData("_id").is(getPredecessorId(shadowCopy, previousVersion)));

    GridFSFile file = this.gridFsOperations.findOne(query);
    if (file == null) {
      return Optional.empty();
    } else {
      return Optional.of(mongoTemplate.getConverter().read(attachmentClass, file.getMetadata()));
    }
  }

  @Override
  public void updatePredecessor(T predecessor) {
    GridFsResource file = gridFsOperations.getResource(getPredecessorFileName(predecessor));
    gridFsMetadataUpdateService.putMetadata(file, predecessor);
  }

  @Override
  @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
  public void saveShadowCopy(T shadowCopy) {
    String originalFilePath = shadowCopy.getMasterId().replaceFirst("/public/files", "");
    GridFsResource file = gridFsOperations.getResource(originalFilePath);
    String filename = getShadowCopyFileName(shadowCopy);

    deleteExistingShadowCopy(filename);

    try (InputStream fIn = file.getInputStream()) {
      gridFsMetadataUpdateService.store(fIn, filename, file.getContentType(), shadowCopy);
    } catch (IOException e) {
      throw new RuntimeException("IO error during shadow copy creation of " + shadowCopy.getId(),
          e);
    }
  }

  @Override
  public Stream<T> findShadowCopiesWithDeletedMasters(String projectId, String previousVersion) {
    String oldProjectVersion = projectId + "-" + previousVersion;
    Query query =
        new Query(GridFsCriteria.whereMetaData("dataAcquisitionProjectId").is(oldProjectVersion)
            .andOperator(GridFsCriteria.whereFilename().regex(getMasterFileNamePattern()),
                GridFsCriteria.whereMetaData("shadow").is(true)));

    return convertIterableToStream(gridFsOperations.find(query)).filter(attachment -> {
      String masterId = attachment.getMasterId();
      Query existsQuery = new Query(GridFsCriteria.whereMetaData("_id").is(masterId));
      return !mongoTemplate.exists(existsQuery, "fs.files");
    });
  }

  private void deleteExistingShadowCopy(String filename) {
    Query query = new Query(GridFsCriteria.where("filename").is(filename));
    gridFsOperations.delete(query);
  }

  private Stream<T> convertIterableToStream(GridFSFindIterable iterable) {
    MongoConverter converter = mongoTemplate.getConverter();

    return StreamSupport.stream(iterable.spliterator(), false)
        .map(file -> converter.read(attachmentClass, file.getMetadata()));
  }

  protected abstract String getMasterFileNamePattern();

  protected abstract String getPredecessorFileName(T predecessor);

  protected abstract String getShadowCopyFileName(T shadowCopy);

  protected String getPredecessorId(T attachment, String previousVersion) {
    String masterId = attachment.getMasterId();
    int index = masterId.lastIndexOf("/attachments/");
    StringBuilder builder = new StringBuilder();
    builder.append(masterId, 0, index).append("-").append(previousVersion)
        .append(masterId.substring(index));
    return builder.toString();
  }

  @Override
  public void deleteExistingShadowCopies(String projectId, String version) {
    String oldProjectId = projectId + "-" + version;
    Query query =
        new Query(GridFsCriteria.whereMetaData("dataAcquisitionProjectId").is(oldProjectId)
            .andOperator(GridFsCriteria.whereFilename().regex(getMasterFileNamePattern()),
                GridFsCriteria.whereMetaData("shadow").is(true),
                GridFsCriteria.whereMetaData("successorId").is(null)));
    gridFsOperations.delete(query);
  }

  @Override
  public void updateElasticsearch(String dataAcquisitionProjectId, String releaseVersion,
      String previousVersion) {
    // there is currently no elasticsearch index for attachments
  }

  @Override
  public void hideExistingShadowCopies(String projectId, String version) {
    setHiddenState(projectId, version, true);
  }

  @Override
  public void unhideExistingShadowCopies(String projectId, String version) {
    setHiddenState(projectId, version, false);
  }

  private void setHiddenState(String projectId, String version, boolean hidden) {
    String shadowId = projectId + "-" + version;
    Query query =
        new Query(GridFsCriteria.whereMetaData("dataAcquisitionProjectId").is(shadowId)
            .andOperator(GridFsCriteria.whereFilename().regex(getMasterFileNamePattern()),
                GridFsCriteria.whereMetaData("shadow").is(true)));

    try (Stream<T> attachments = convertIterableToStream(gridFsOperations.find(query))) {
      attachments.forEach(attachment -> {
        attachment.setHidden(hidden);
        updatePredecessor(attachment);
      });
    }
  }
}
