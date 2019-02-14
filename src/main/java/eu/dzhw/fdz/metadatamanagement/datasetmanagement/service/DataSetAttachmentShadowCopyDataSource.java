package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;
import org.bson.Document;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Provides data for creating shadow copies of {@link DataSetAttachmentMetadata}.
 */
@Service
public class DataSetAttachmentShadowCopyDataSource
    implements ShadowCopyDataSource<DataSetAttachmentMetadata> {

  private GridFsOperations gridFsOperations;

  private GridFS gridFs;

  private MongoTemplate mongoTemplate;

  /**
   * Create a new instance.
   */
  public DataSetAttachmentShadowCopyDataSource(GridFsOperations gridFsOperations,
                                                      GridFS gridFs,
                                                      MongoTemplate mongoTemplate) {
    this.gridFsOperations = gridFsOperations;
    this.gridFs = gridFs;
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Stream<DataSetAttachmentMetadata> getMasters(String dataAcquisitionProjectId) {
    Query query = new Query(GridFsCriteria.whereMetaData("dataAcquisitionProjectId")
        .is(dataAcquisitionProjectId)
        .andOperator(GridFsCriteria.whereFilename()
            .regex(DataSetAttachmentFilenameBuilder.ALL_DATASET_ATTACHMENTS)
            .andOperator(GridFsCriteria.whereMetaData("shadow")
                .is(false))));

    return convertIterableToStream(gridFsOperations.find(query));
  }

  @Override
  public DataSetAttachmentMetadata createShadowCopy(DataSetAttachmentMetadata source,
                                                   String version) {

    String derivedId = deriveId(source, version);

    Query query = new Query(GridFsCriteria.whereMetaData("_id").is(derivedId));
    GridFSFile gridFsFile = this.gridFsOperations.findOne(query);
    DataSetAttachmentMetadata copy;

    if (gridFsFile == null) {
      copy = new DataSetAttachmentMetadata();
      BeanUtils.copyProperties(source, copy);
    } else {
      MongoConverter converter = mongoTemplate.getConverter();
      copy = converter.read(DataSetAttachmentMetadata.class, gridFsFile.getMetadata());
      BeanUtils.copyProperties(source, copy, "version");
    }

    copy.setDataAcquisitionProjectId(source.getDataAcquisitionProjectId() + "-" + version);
    copy.setDataSetId(source.getDataSetId() + "-" + version);
    copy.generateId();
    return copy;
  }

  @Override
  public Optional<DataSetAttachmentMetadata> findPredecessorOfShadowCopy(String masterId) {
    Query query = new Query(GridFsCriteria.whereMetaData("shadow")
        .is(true)
        .andOperator(GridFsCriteria.whereMetaData("masterId")
            .is(masterId)
            .andOperator(GridFsCriteria.whereMetaData("successorId")
                .is(null))));
    GridFSFile file = this.gridFsOperations.findOne(query);
    if (file == null) {
      return Optional.empty();
    } else {
      return Optional.of(mongoTemplate.getConverter().read(DataSetAttachmentMetadata.class,
          file.getMetadata()));
    }
  }

  @Override
  public void updatePredecessor(DataSetAttachmentMetadata predecessor) {
    GridFSDBFile file = gridFs.findOne(DataSetAttachmentFilenameBuilder
        .buildFileName(predecessor.getDataSetId(), predecessor.getFileName()));
    BasicDBObject dbObject = new BasicDBObject((Document) mongoTemplate.getConverter()
        .convertToMongoType(predecessor));
    file.setMetaData(dbObject);
    file.save();
  }

  @Override
  public void saveShadowCopy(DataSetAttachmentMetadata shadowCopy) {
    String originalFilePath = shadowCopy.getMasterId().replaceFirst("/public/files", "");
    GridFSDBFile file = gridFs.findOne(originalFilePath);
    String filename = DataSetAttachmentFilenameBuilder.buildFileName(shadowCopy);

    try (InputStream fIn = file.getInputStream()) {
      gridFsOperations.store(fIn, filename, getContentType(file), shadowCopy);
    } catch (IOException e) {
      throw new RuntimeException("IO error during shadow copy creation of " + shadowCopy.getId(),
          e);
    }
  }

  @Override
  public Stream<DataSetAttachmentMetadata> findShadowCopiesWithDeletedMasters(String projectId,
      String previousVersion) {
    String oldProjectVersion = projectId + "-" + previousVersion;
    Query query = new Query(GridFsCriteria.whereMetaData("dataAcquisitionProjectId")
        .is(oldProjectVersion)
        .andOperator(GridFsCriteria.whereFilename()
            .regex(DataSetAttachmentFilenameBuilder.ALL_DATASET_ATTACHMENTS)
            .andOperator(GridFsCriteria.whereMetaData("shadow")
                .is(true)
                .andOperator(GridFsCriteria.whereMetaData("successorId")
                    .is(null)))));

    return convertIterableToStream(gridFsOperations.find(query));
  }

  private String deriveId(DataSetAttachmentMetadata source, String version) {
    return String.format("/public/files/data-sets/%s-%s/attachments/%s", source.getDataSetId(),
        version, source.getFileName());
  }

  private String getContentType(GridFSDBFile gridFsDbFile) {
    String contentType = gridFsDbFile.getContentType();
    if (StringUtils.hasText(contentType)) {
      return contentType;
    } else {
      return (String) gridFsDbFile.getMetaData().get("_contentType");
    }
  }

  private Stream<DataSetAttachmentMetadata> convertIterableToStream(GridFSFindIterable iterable) {
    MongoConverter converter = mongoTemplate.getConverter();

    return StreamSupport.stream(iterable.spliterator(), false)
        .map(file -> converter.read(DataSetAttachmentMetadata.class, file.getMetadata()));
  }
}
