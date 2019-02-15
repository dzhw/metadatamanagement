package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import eu.dzhw.fdz.metadatamanagement.common.service.AbstractAttachmentShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyAttachmentMetadata;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

/**
 * Provides data for creating shadow copies of {@link SurveyAttachmentMetadata}.
 */
@Service
public class SurveyAttachmentMetadataShadowCopyDataSource
    extends AbstractAttachmentShadowCopyDataSource<SurveyAttachmentMetadata> {

  /**
   * Create a new instance.
   */
  public SurveyAttachmentMetadataShadowCopyDataSource(GridFsOperations gridFsOperations,
                                                      GridFS gridFs,
                                                      MongoTemplate mongoTemplate) {
    super(gridFsOperations, gridFs, mongoTemplate, SurveyAttachmentMetadata.class);
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
      BeanUtils.copyProperties(source, copy);
    } else {
      MongoConverter converter = mongoTemplate.getConverter();
      copy = converter.read(SurveyAttachmentMetadata.class, gridFsFile.getMetadata());
      BeanUtils.copyProperties(source, copy, "version");
    }

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
  protected String getMasterFileNamePattern() {
    return SurveyAttachmentFilenameBuilder.ALL_SURVEY_ATTACHMENTS;
  }

  @Override
  protected String getPredecessorFileName(SurveyAttachmentMetadata predecessor) {
    return SurveyAttachmentFilenameBuilder.buildFileName(
        predecessor.getSurveyId(), predecessor.getFileName());
  }

  @Override
  protected String getShadowCopyFileName(SurveyAttachmentMetadata shadowCopy) {
    return SurveyAttachmentFilenameBuilder.buildFileName(shadowCopy);
  }
}
