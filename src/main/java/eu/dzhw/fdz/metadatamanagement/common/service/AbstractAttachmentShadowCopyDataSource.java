package eu.dzhw.fdz.metadatamanagement.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.util.StringUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;

/**
 * Provides common implementation shared between {@link ShadowCopyDataSource} that handle
 * attachments.
 * @param <T> {@link AbstractShadowableRdcDomainObject}
 */
public abstract class AbstractAttachmentShadowCopyDataSource
    <T extends AbstractShadowableRdcDomainObject> implements ShadowCopyDataSource<T> {

  protected final GridFsOperations gridFsOperations;

  protected final GridFS gridFs;

  protected final MongoTemplate mongoTemplate;

  private final Class<T> attachmentClass;

  /**
   * Creates a new instance.
   */
  protected AbstractAttachmentShadowCopyDataSource(GridFsOperations gridFsOperations, GridFS gridFs,
      MongoTemplate mongoTemplate, Class<T> attachmentClass) {
    this.gridFsOperations = gridFsOperations;
    this.gridFs = gridFs;
    this.mongoTemplate = mongoTemplate;
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
    GridFSDBFile file = gridFs.findOne(getPredecessorFileName(predecessor));
    BasicDBObject dbObject =
        new BasicDBObject((Document) mongoTemplate.getConverter().convertToMongoType(predecessor));
    // _contentType gets lost after metadata conversion, so we have to set it again explicitly.
    dbObject.append("_contentType", getContentType(file));
    file.setMetaData(dbObject);
    file.save();
  }

  @Override
  public void saveShadowCopy(T shadowCopy) {
    String originalFilePath = shadowCopy.getMasterId().replaceFirst("/public/files", "");
    GridFSDBFile file = gridFs.findOne(originalFilePath);
    String filename = getShadowCopyFileName(shadowCopy);

    deleteExistingShadowCopy(filename);

    try (InputStream fIn = file.getInputStream()) {
      gridFsOperations.store(fIn, filename, getContentType(file), shadowCopy);
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

  private String getContentType(GridFSDBFile gridFsDbFile) {
    String contentType = gridFsDbFile.getContentType();
    if (StringUtils.hasText(contentType)) {
      return contentType;
    } else {
      return (String) gridFsDbFile.getMetaData().get("_contentType");
    }
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
    throw new IllegalAccessError("Attachment indexing has not yet been implemented!");
  }
}
