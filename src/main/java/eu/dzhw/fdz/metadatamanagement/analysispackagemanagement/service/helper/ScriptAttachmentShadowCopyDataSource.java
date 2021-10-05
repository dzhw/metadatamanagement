package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.helper;

import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Component;

import com.mongodb.client.gridfs.model.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.ScriptAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.common.service.AbstractAttachmentShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.common.service.GridFsMetadataUpdateService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;

/**
 * Provides data for creating shadow copies of {@link ScriptAttachmentMetadata}.
 */
@Component
public class ScriptAttachmentShadowCopyDataSource
    extends AbstractAttachmentShadowCopyDataSource<ScriptAttachmentMetadata> {

  /**
   * Create a new instance.
   */
  public ScriptAttachmentShadowCopyDataSource(GridFsOperations gridFsOperations,
      MongoTemplate mongoTemplate, GridFsMetadataUpdateService gridFsMetadataUpdateService) {
    super(gridFsOperations, mongoTemplate, gridFsMetadataUpdateService,
        ScriptAttachmentMetadata.class);
  }

  @Override
  public ScriptAttachmentMetadata createShadowCopy(ScriptAttachmentMetadata source,
      Release release) {

    String derivedId = deriveId(source, release.getVersion());

    Query query = new Query(GridFsCriteria.whereMetaData("_id").is(derivedId));
    GridFSFile gridFsFile = this.gridFsOperations.findOne(query);
    ScriptAttachmentMetadata copy;

    if (gridFsFile == null) {
      copy = new ScriptAttachmentMetadata();
      BeanUtils.copyProperties(source, copy);
    } else {
      MongoConverter converter = mongoTemplate.getConverter();
      copy = converter.read(ScriptAttachmentMetadata.class, gridFsFile.getMetadata());
      BeanUtils.copyProperties(source, copy, "version");
    }

    copy.setDataAcquisitionProjectId(
        source.getDataAcquisitionProjectId() + "-" + release.getVersion());
    copy.setAnalysisPackageId(source.getAnalysisPackageId() + "-" + release.getVersion());
    copy.generateId();
    return copy;
  }

  private String deriveId(ScriptAttachmentMetadata source, String version) {
    return String.format("/public/files/analysis-packages/%s-%s/scripts/%s/attachments/%s",
        source.getAnalysisPackageId(), version, source.getScriptUuid(), source.getFileName());
  }

  @Override
  protected String getMasterFileNamePattern() {
    return ScriptAttachmentFilenameBuilder.ALL_SCRIPT_ATTACHMENTS;
  }

  @Override
  protected String getPredecessorFileName(ScriptAttachmentMetadata predecessor) {
    return ScriptAttachmentFilenameBuilder.buildFileName(predecessor.getAnalysisPackageId(),
        predecessor.getScriptUuid(), predecessor.getFileName());
  }

  @Override
  protected String getShadowCopyFileName(ScriptAttachmentMetadata shadowCopy) {
    return ScriptAttachmentFilenameBuilder.buildFileName(shadowCopy);
  }
  
  @Override
  protected String getPredecessorId(ScriptAttachmentMetadata attachment, String previousVersion) {
    String masterId = attachment.getMasterId();
    int index = masterId.lastIndexOf("/scripts/");
    StringBuilder builder = new StringBuilder();
    builder.append(masterId, 0, index).append("-").append(previousVersion)
        .append(masterId.substring(index));
    return builder.toString();
  }
}
