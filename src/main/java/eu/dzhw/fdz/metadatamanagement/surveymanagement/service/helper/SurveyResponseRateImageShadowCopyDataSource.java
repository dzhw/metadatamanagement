package eu.dzhw.fdz.metadatamanagement.surveymanagement.service.helper;

import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Component;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;

import eu.dzhw.fdz.metadatamanagement.common.service.AbstractAttachmentShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyResponseRateImageMetadata;

/**
 * Provides data for creating shadow copies of {@link SurveyResponseRateImageMetadata}.
 */
@Component
public class SurveyResponseRateImageShadowCopyDataSource
    extends AbstractAttachmentShadowCopyDataSource<SurveyResponseRateImageMetadata> {

  private static final String RATE_IMAGE_PATTERN = "/surveys/.+/.+_responserate_*";

  /**
   * Create a new instance.
   */
  public SurveyResponseRateImageShadowCopyDataSource(GridFsOperations gridFsOperations,
      GridFS gridFs, MongoTemplate mongoTemplate) {
    super(gridFsOperations,gridFs,mongoTemplate,SurveyResponseRateImageMetadata.class);
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

  private String deriveId(SurveyResponseRateImageMetadata source, String version) {
    return String.format("/surveys/%s-%s/%s", source.getSurveyId(),
        version, source.getFileName());
  }

  @Override
  protected String getMasterFileNamePattern() {
    return RATE_IMAGE_PATTERN;
  }

  @Override
  protected String getPredecessorFileName(SurveyResponseRateImageMetadata predecessor) {
    return createFilePath(predecessor);
  }

  @Override
  protected String getShadowCopyFileName(SurveyResponseRateImageMetadata shadowCopy) {
    return createFilePath(shadowCopy);
  }

  @Override
  protected String getPredecessorId(SurveyResponseRateImageMetadata attachment,
                                    String previousVersion) {
    String masterId = attachment.getMasterId();
    String suffix = "/" + attachment.getFileName();
    int index = masterId.lastIndexOf(suffix);
    StringBuilder builder = new StringBuilder();
    builder.append(masterId, 0, index)
        .append("-").append(previousVersion)
        .append(masterId.substring(index));
    return builder.toString();
  }

  private String createFilePath(SurveyResponseRateImageMetadata metadata) {
    return String.format("/surveys/%s/%s", metadata.getSurveyId(),
        metadata.getFileName());
  }
}
