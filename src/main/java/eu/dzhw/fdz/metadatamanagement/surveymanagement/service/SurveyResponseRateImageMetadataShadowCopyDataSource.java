package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyResponseRateImageMetadata;
import org.bson.Document;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Provides data for creating shadow copies of {@link SurveyResponseRateImageMetadata}.
 */
@Service
public class SurveyResponseRateImageMetadataShadowCopyDataSource
    implements ShadowCopyDataSource<SurveyResponseRateImageMetadata> {

  private GridFsOperations gridFsOperations;

  private GridFS gridFs;

  private MongoTemplate mongoTemplate;

  /**
   * Create a new instance.
   */
  public SurveyResponseRateImageMetadataShadowCopyDataSource(GridFsOperations gridFsOperations,
      GridFS gridFs, MongoTemplate mongoTemplate) {
    this.gridFsOperations = gridFsOperations;
    this.gridFs = gridFs;
    this.mongoTemplate = mongoTemplate;
  }


  @Override
  public Stream<SurveyResponseRateImageMetadata> getMasters(String dataAcquisitionProjectId) {
    Query query = new Query(GridFsCriteria.whereMetaData("dataAcquisitionProjectId")
        .is(dataAcquisitionProjectId)
        .andOperator(GridFsCriteria.whereMetaData("_class")
            .is(SurveyResponseRateImageMetadata.class.getName())
            .andOperator(GridFsCriteria.whereMetaData("shadow")
                .is(false))));

    Iterable<GridFSFile> files = this.gridFsOperations.find(query);
    MongoConverter converter = mongoTemplate.getConverter();

    return StreamSupport.stream(files.spliterator(), false)
        .map(file -> converter.read(SurveyResponseRateImageMetadata.class, file.getMetadata()));
  }

  @Override
  public SurveyResponseRateImageMetadata createShadowCopy(SurveyResponseRateImageMetadata source,
                                                          String version) {
    String derivedId = deriveId(source, version);

    Query query = new Query(GridFsCriteria.whereMetaData("_id").is(derivedId));
    GridFSFile gridFsFile = this.gridFsOperations.findOne(query);
    SurveyResponseRateImageMetadata copy;

    if (gridFsFile == null) {
      copy = new SurveyResponseRateImageMetadata();
      BeanUtils.copyProperties(source, copy, "version");
      copy.setVersion(0L);
    } else {
      MongoConverter converter = mongoTemplate.getConverter();
      copy = converter.read(SurveyResponseRateImageMetadata.class, gridFsFile.getMetadata());
      BeanUtils.copyProperties(source, copy);
    }

    copy.setDataAcquisitionProjectId(source.getDataAcquisitionProjectId() + "-" + version);
    copy.setSurveyId(source.getSurveyId() + "-" + version);
    copy.generateId();
    return copy;
  }

  @Override
  public Stream<SurveyResponseRateImageMetadata> getLastShadowCopies(
      String dataAcquisitionProjectId) {

    Query query = new Query(GridFsCriteria.whereMetaData("shadow")
        .is(true)
        .andOperator(GridFsCriteria.whereMetaData("_class")
            .is(SurveyResponseRateImageMetadata.class.getName())
            .andOperator(GridFsCriteria.whereMetaData("successorId")
                .is(null))));
    GridFSFindIterable gridFsFiles = this.gridFsOperations.find(query);
    MongoConverter converter = mongoTemplate.getConverter();
    return StreamSupport.stream(gridFsFiles.spliterator(), false)
        .map(file -> converter.read(SurveyResponseRateImageMetadata.class, file.getMetadata()));
  }

  @Override
  public List<SurveyResponseRateImageMetadata> saveShadowCopies(
      List<SurveyResponseRateImageMetadata> shadowCopies) {

    return shadowCopies.stream().peek(copy -> {
      /*
       * List contains updated shadows as well. We must check if we find those to avoid creating
       * another file blob copy.
       */
      String fileName = String.format("/surveys/%s/%s", copy.getSurveyId(), copy.getFileName());

      GridFSDBFile file = gridFs.findOne(fileName);

      if (file != null) {
        updateMetadataOnly(copy, file);
      } else {
        createCopyWithFileBlob(copy);
      }
    }).collect(Collectors.toList());
  }

  private void updateMetadataOnly(SurveyResponseRateImageMetadata copy, GridFSDBFile file) {
    BasicDBObject dbObject = new BasicDBObject((Document) mongoTemplate.getConverter()
        .convertToMongoType(copy));
    file.setMetaData(dbObject);
    file.save();
  }

  private void createCopyWithFileBlob(SurveyResponseRateImageMetadata copy) {
    String originalFilePath = copy.getMasterId().replaceFirst("/public/files","");
    GridFSDBFile file = gridFs.findOne(originalFilePath);
    String filename = String.format("/surveys/%s/%s", copy.getSurveyId(), copy.getFileName());

    try (InputStream fIn = file.getInputStream()) {
      gridFsOperations.store(fIn, filename, file.getContentType(), copy);
    } catch (IOException e) {
      throw new RuntimeException("IO error during shadow copy creation of " + copy.getId(), e);
    }
  }

  private String deriveId(SurveyResponseRateImageMetadata source, String version) {
    return String.format("/surveys/%s-%s/%s", source.getSurveyId(),
        version, source.getFileName());
  }
}
