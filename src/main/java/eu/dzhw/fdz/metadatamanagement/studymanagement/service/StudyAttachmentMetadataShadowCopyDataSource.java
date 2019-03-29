package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import eu.dzhw.fdz.metadatamanagement.common.service.AbstractAttachmentShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudyAttachmentMetadata;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

/**
 * Provides data for creating shadow copies of {@link StudyAttachmentMetadata}.
 */
@Service
public class StudyAttachmentMetadataShadowCopyDataSource
    extends AbstractAttachmentShadowCopyDataSource<StudyAttachmentMetadata> {

  /**
   * Create a new instance.
   */
  public StudyAttachmentMetadataShadowCopyDataSource(GridFsOperations gridFsOperations,
      GridFS gridFs, MongoTemplate mongoTemplate) {
    super(gridFsOperations, gridFs, mongoTemplate, StudyAttachmentMetadata.class);
  }

  @Override
  public StudyAttachmentMetadata createShadowCopy(StudyAttachmentMetadata source,
                                                    String version) {

    String derivedId = deriveId(source, version);

    Query query = new Query(GridFsCriteria.whereMetaData("_id").is(derivedId));
    GridFSFile gridFsFile = this.gridFsOperations.findOne(query);
    StudyAttachmentMetadata copy;

    if (gridFsFile == null) {
      copy = new StudyAttachmentMetadata();
      BeanUtils.copyProperties(source, copy);
    } else {
      MongoConverter converter = mongoTemplate.getConverter();
      copy = converter.read(StudyAttachmentMetadata.class, gridFsFile.getMetadata());
      BeanUtils.copyProperties(source, copy, "version");
    }

    copy.setDataAcquisitionProjectId(source.getDataAcquisitionProjectId() + "-" + version);
    copy.setStudyId(source.getStudyId() + "-" + version);
    copy.generateId();
    return copy;
  }

  private String deriveId(StudyAttachmentMetadata source, String version) {
    return String.format("/public/files/studies/%s-%s/attachments/%s", source.getStudyId(),
        version, source.getFileName());
  }

  @Override
  protected String getMasterFileNamePattern() {
    return StudyAttachmentFilenameBuilder.ALL_STUDY_ATTACHMENTS;
  }

  @Override
  protected String getPredecessorFileName(StudyAttachmentMetadata predecessor) {
    return StudyAttachmentFilenameBuilder
        .buildFileName(predecessor.getStudyId(), predecessor.getFileName());
  }

  @Override
  protected String getShadowCopyFileName(StudyAttachmentMetadata shadowCopy) {
    return StudyAttachmentFilenameBuilder.buildFileName(shadowCopy);
  }
}
