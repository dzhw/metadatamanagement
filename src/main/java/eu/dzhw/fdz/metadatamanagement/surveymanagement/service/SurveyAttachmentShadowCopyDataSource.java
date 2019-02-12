package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyAttachmentMetadata;
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
 * Provides data for creating shadow copies of {@link SurveyAttachmentMetadata}.
 */
@Service
public class SurveyAttachmentShadowCopyDataSource
    implements ShadowCopyDataSource<SurveyAttachmentMetadata> {

  private GridFsOperations gridFsOperations;

  private GridFS gridFs;

  private MongoTemplate mongoTemplate;

  /**
   * Create a new instance.
   */
  public SurveyAttachmentShadowCopyDataSource(GridFsOperations gridFsOperations,
                                              GridFS gridFs,
                                              MongoTemplate mongoTemplate) {
    this.gridFsOperations = gridFsOperations;
    this.gridFs = gridFs;
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Stream<SurveyAttachmentMetadata> getMasters(String dataAcquisitionProjectId) {
    Query query = new Query(GridFsCriteria.whereMetaData("dataAcquisitionProjectId")
        .is(dataAcquisitionProjectId)
        .andOperator(GridFsCriteria.whereMetaData("_class")
            .is(SurveyAttachmentMetadata.class.getName())
            .andOperator(GridFsCriteria.whereMetaData("shadow")
                .is(false))));

    Iterable<GridFSFile> files = this.gridFsOperations.find(query);
    MongoConverter converter = mongoTemplate.getConverter();

    return StreamSupport.stream(files.spliterator(), false)
        .map(file -> converter.read(SurveyAttachmentMetadata.class, file.getMetadata()));
  }

  @Override
  public SurveyAttachmentMetadata createShadowCopy(SurveyAttachmentMetadata source,
                                                   String version) {

    String derivedId = deriveId(source, version);

    Query query = new Query(GridFsCriteria.whereMetaData("_id").is(derivedId));
    GridFSFile gridFsFile = this.gridFsOperations.findOne(query);
    SurveyAttachmentMetadata copy;

    if (gridFsFile == null) {
      copy = new SurveyAttachmentMetadata();
    } else {
      MongoConverter converter = mongoTemplate.getConverter();
      copy = converter.read(SurveyAttachmentMetadata.class, gridFsFile.getMetadata());
    }

    BeanUtils.copyProperties(source, copy, "version");
    copy.setShadow(true);
    copy.setDataAcquisitionProjectId(source.getDataAcquisitionProjectId() + "-" + version);
    copy.setSurveyId(source.getSurveyId() + "-" + version);
    copy.generateId();
    return copy;
  }

  private String deriveId(SurveyAttachmentMetadata source, String version) {
    return String.format("/public/files/surveys/%s-%s/attachments/%s", source.getSurveyId(),
        version, source.getFileName());
  }

  @Override
  public Stream<SurveyAttachmentMetadata> getLastShadowCopies(String dataAcquisitionProjectId) {
    Query query = new Query(GridFsCriteria.whereMetaData("shadow")
        .is(true)
        .andOperator(GridFsCriteria.whereMetaData("_class")
            .is(SurveyAttachmentMetadata.class.getName())
        .andOperator(GridFsCriteria.whereMetaData("successorId")
            .is(null))));
    GridFSFindIterable gridFsFiles = this.gridFsOperations.find(query);
    MongoConverter converter = mongoTemplate.getConverter();
    return StreamSupport.stream(gridFsFiles.spliterator(), false)
        .map(file -> converter.read(SurveyAttachmentMetadata.class, file.getMetadata()));
  }

  @Override
  public List<SurveyAttachmentMetadata> saveShadowCopies(
      List<SurveyAttachmentMetadata> shadowCopies) {
    return shadowCopies.stream().peek(copy -> {
      /*
       * List contains updated shadows as well. We must check if we find those to avoid creating
       * another file blob copy.
       */
      GridFSDBFile file = gridFs.findOne(SurveyAttachmentFilenameBuilder.buildFileName(
          copy.getSurveyId(), copy.getFileName()));

      if (file != null) {
        updateMetadataOnly(copy, file);
      } else {
        createCopyWithFileBlob(copy);
      }
    }).collect(Collectors.toList());
  }

  private void updateMetadataOnly(SurveyAttachmentMetadata copy, GridFSDBFile file) {
    BasicDBObject dbObject = new BasicDBObject((Document) mongoTemplate.getConverter()
        .convertToMongoType(copy));
    file.setMetaData(dbObject);
    file.save();
  }

  private void createCopyWithFileBlob(SurveyAttachmentMetadata copy) {
    String originalFilePath = copy.getMasterId().replaceFirst("/public/files","");
    GridFSDBFile file = gridFs.findOne(originalFilePath);
    BasicDBObject metadata = new BasicDBObject((Document)mongoTemplate.getConverter()
        .convertToMongoType(copy));
    String filename = SurveyAttachmentFilenameBuilder.buildFileName(copy);

    try (InputStream fIn = file.getInputStream()) {
      gridFsOperations.store(fIn, filename, file.getContentType(), metadata);
    } catch (IOException e) {
      throw new RuntimeException("IO error during shadow copy creation of " + copy.getId(), e);
    }
  }
}
