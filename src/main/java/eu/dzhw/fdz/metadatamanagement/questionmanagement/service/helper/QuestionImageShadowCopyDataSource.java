package eu.dzhw.fdz.metadatamanagement.questionmanagement.service.helper;

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
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionImageMetadata;

/**
 * Provides data for creating shadow copies of {@link QuestionImageMetadata}.
 */
@Component
public class QuestionImageShadowCopyDataSource
    extends AbstractAttachmentShadowCopyDataSource<QuestionImageMetadata> {

  private static final String QUESTION_IMAGE_PATH_FORMAT = "/questions/%s/images/%s";

  /**
   * Create a new instance.
   */
  public QuestionImageShadowCopyDataSource(GridFsOperations gridFsOperations,
                                                   GridFS gridFs, MongoTemplate mongoTemplate) {
    super(gridFsOperations, gridFs, mongoTemplate, QuestionImageMetadata.class);
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

  private String deriveId(QuestionImageMetadata source, String version) {
    return String.format("/public/files/questions/%s-%s/attachments/%s", source.getQuestionId(),
        version, source.getFileName());
  }

  @Override
  protected String getMasterFileNamePattern() {
    return "^/questions/.+/images/.+";
  }

  @Override
  protected String getPredecessorFileName(QuestionImageMetadata predecessor) {
    return String.format(QUESTION_IMAGE_PATH_FORMAT,
        predecessor.getQuestionId(), predecessor.getFileName());
  }

  @Override
  protected String getShadowCopyFileName(QuestionImageMetadata shadowCopy) {
    return String.format(QUESTION_IMAGE_PATH_FORMAT, shadowCopy.getQuestionId(),
        shadowCopy.getFileName());
  }

  @Override
  protected String getPredecessorId(QuestionImageMetadata attachment, String previousVersion) {
    String masterId = attachment.getMasterId();
    int index = masterId.lastIndexOf("/images/");
    StringBuilder builder = new StringBuilder();
    builder.append(masterId, 0, index)
        .append("-").append(previousVersion)
        .append(masterId.substring(index));
    return builder.toString();
  }
}
