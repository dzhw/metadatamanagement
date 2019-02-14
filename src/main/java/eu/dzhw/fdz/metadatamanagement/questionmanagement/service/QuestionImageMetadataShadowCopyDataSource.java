package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionImageMetadata;
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
 * Provides data for creating shadow copies of {@link QuestionImageMetadata}.
 */
@Service
public class QuestionImageMetadataShadowCopyDataSource
    implements ShadowCopyDataSource<QuestionImageMetadata> {

  private static final String ALL_QUESTION_IMAGES = "^/questions/.+/images/.+";
  private static final String QUESTION_IMAGE_PATH_FORMAT = "/questions/%s/images/%s";

  private GridFsOperations gridFsOperations;

  private GridFS gridFs;

  private MongoTemplate mongoTemplate;

  /**
   * Create a new instance.
   */
  public QuestionImageMetadataShadowCopyDataSource(GridFsOperations gridFsOperations,
                                                   GridFS gridFs, MongoTemplate mongoTemplate) {
    this.gridFsOperations = gridFsOperations;
    this.gridFs = gridFs;
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Stream<QuestionImageMetadata> getMasters(String dataAcquisitionProjectId) {
    Query query = new Query(GridFsCriteria.whereMetaData("dataAcquisitionProjectId")
        .is(dataAcquisitionProjectId)
        .andOperator(GridFsCriteria.whereFilename()
            .regex(ALL_QUESTION_IMAGES)
            .andOperator(GridFsCriteria.whereMetaData("shadow")
                .is(false))));

    return convertIterableToStream(gridFsOperations.find(query));
  }

  @Override
  public QuestionImageMetadata createShadowCopy(QuestionImageMetadata source,
                                                String version) {

    String derivedId = deriveId(source, version);

    Query query = new Query(GridFsCriteria.whereMetaData("_id").is(derivedId));
    GridFSFile gridFsFile = this.gridFsOperations.findOne(query);
    QuestionImageMetadata copy;

    if (gridFsFile == null) {
      copy = new QuestionImageMetadata();
      BeanUtils.copyProperties(source, copy);
    } else {
      MongoConverter converter = mongoTemplate.getConverter();
      copy = converter.read(QuestionImageMetadata.class, gridFsFile.getMetadata());
      BeanUtils.copyProperties(source, copy, "version");
    }

    copy.setDataAcquisitionProjectId(source.getDataAcquisitionProjectId() + "-" + version);
    copy.setQuestionId(source.getQuestionId() + "-" + version);
    copy.generateId();
    return copy;
  }

  @Override
  public Optional<QuestionImageMetadata> findPredecessorOfShadowCopy(String masterId) {
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
      return Optional.of(mongoTemplate.getConverter().read(QuestionImageMetadata.class,
          file.getMetadata()));
    }
  }

  @Override
  public void updatePredecessor(QuestionImageMetadata predecessor) {
    GridFSDBFile file = gridFs.findOne(String.format(QUESTION_IMAGE_PATH_FORMAT,
        predecessor.getQuestionId(), predecessor.getFileName()));
    BasicDBObject dbObject = new BasicDBObject((Document) mongoTemplate.getConverter()
        .convertToMongoType(predecessor));
    file.setMetaData(dbObject);
    file.save();
  }

  @Override
  public void saveShadowCopy(QuestionImageMetadata shadowCopy) {
    String originalFilePath = shadowCopy.getMasterId().replaceFirst("/public/files", "");
    GridFSDBFile file = gridFs.findOne(originalFilePath);
    String filename = String.format(QUESTION_IMAGE_PATH_FORMAT, shadowCopy.getQuestionId(),
        shadowCopy.getFileName());

    try (InputStream fIn = file.getInputStream()) {
      gridFsOperations.store(fIn, filename, getContentType(file), shadowCopy);
    } catch (IOException e) {
      throw new RuntimeException("IO error during shadow copy creation of " + shadowCopy.getId(),
          e);
    }
  }

  @Override
  public Stream<QuestionImageMetadata> findShadowCopiesWithDeletedMasters(String projectId,
       String previousVersion) {
    String oldProjectVersion = projectId + "-" + previousVersion;
    Query query = new Query(GridFsCriteria.whereMetaData("dataAcquisitionProjectId")
        .is(oldProjectVersion)
        .andOperator(GridFsCriteria.whereFilename()
            .regex(ALL_QUESTION_IMAGES)
        .andOperator(GridFsCriteria.whereMetaData("shadow")
            .is(true)
            .andOperator(GridFsCriteria.whereMetaData("successorId")
                .is(null)))));

    return convertIterableToStream(gridFsOperations.find(query));
  }

  private String deriveId(QuestionImageMetadata source, String version) {
    return String.format("/public/files/questions/%s-%s/attachments/%s", source.getQuestionId(),
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

  private Stream<QuestionImageMetadata> convertIterableToStream(GridFSFindIterable iterable) {
    MongoConverter converter = mongoTemplate.getConverter();

    return StreamSupport.stream(iterable.spliterator(), false)
        .map(file -> converter.read(QuestionImageMetadata.class, file.getMetadata()));
  }
}
