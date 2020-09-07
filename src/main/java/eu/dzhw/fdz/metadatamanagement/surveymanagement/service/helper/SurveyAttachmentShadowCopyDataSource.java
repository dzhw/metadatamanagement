package eu.dzhw.fdz.metadatamanagement.surveymanagement.service.helper;

import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Component;

import com.mongodb.client.gridfs.model.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.common.service.AbstractAttachmentShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.common.service.GridFsMetadataUpdateService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyAttachmentMetadata;

/**
 * Provides data for creating shadow copies of {@link SurveyAttachmentMetadata}.
 */
@Component
public class SurveyAttachmentShadowCopyDataSource
    extends AbstractAttachmentShadowCopyDataSource<SurveyAttachmentMetadata> {

  /**
   * Create a new instance.
   */
  public SurveyAttachmentShadowCopyDataSource(GridFsOperations gridFsOperations,
      MongoTemplate mongoTemplate, GridFsMetadataUpdateService gridFsMetadataUpdateService) {
    super(gridFsOperations, mongoTemplate, gridFsMetadataUpdateService,
        SurveyAttachmentMetadata.class);
  }

  @Override
  public SurveyAttachmentMetadata createShadowCopy(SurveyAttachmentMetadata source,
      Release release) {

    String derivedId = deriveId(source, release.getVersion());

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

    copy.setDataAcquisitionProjectId(
        source.getDataAcquisitionProjectId() + "-" + release.getVersion());
    copy.setSurveyId(source.getSurveyId() + "-" + release.getVersion());
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
    return SurveyAttachmentFilenameBuilder.buildFileName(predecessor.getSurveyId(),
        predecessor.getFileName());
  }

  @Override
  protected String getShadowCopyFileName(SurveyAttachmentMetadata shadowCopy) {
    return SurveyAttachmentFilenameBuilder.buildFileName(shadowCopy);
  }
}
