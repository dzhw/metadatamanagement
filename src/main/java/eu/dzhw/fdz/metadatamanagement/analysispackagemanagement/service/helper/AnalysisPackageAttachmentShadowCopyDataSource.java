package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.helper;

import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Component;

import com.mongodb.client.gridfs.model.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackageAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.common.service.AbstractAttachmentShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.common.service.GridFsMetadataUpdateService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;

/**
 * Provides data for creating shadow copies of {@link AnalysisPackageAttachmentMetadata}.
 */
@Component
public class AnalysisPackageAttachmentShadowCopyDataSource
    extends AbstractAttachmentShadowCopyDataSource<AnalysisPackageAttachmentMetadata> {

  /**
   * Create a new instance.
   */
  public AnalysisPackageAttachmentShadowCopyDataSource(GridFsOperations gridFsOperations,
      MongoTemplate mongoTemplate, GridFsMetadataUpdateService gridFsMetadataUpdateService) {
    super(gridFsOperations, mongoTemplate, gridFsMetadataUpdateService,
        AnalysisPackageAttachmentMetadata.class);
  }

  @Override
  public AnalysisPackageAttachmentMetadata createShadowCopy(
      AnalysisPackageAttachmentMetadata source, Release release) {

    String derivedId = deriveId(source, release.getVersion());

    Query query = new Query(GridFsCriteria.whereMetaData("_id").is(derivedId));
    GridFSFile gridFsFile = this.gridFsOperations.findOne(query);
    AnalysisPackageAttachmentMetadata copy;

    if (gridFsFile == null) {
      copy = new AnalysisPackageAttachmentMetadata();
      BeanUtils.copyProperties(source, copy);
    } else {
      MongoConverter converter = mongoTemplate.getConverter();
      copy = converter.read(AnalysisPackageAttachmentMetadata.class, gridFsFile.getMetadata());
      BeanUtils.copyProperties(source, copy, "version");
    }

    copy.setDataAcquisitionProjectId(
        source.getDataAcquisitionProjectId() + "-" + release.getVersion());
    copy.setAnalysisPackageId(source.getAnalysisPackageId() + "-" + release.getVersion());
    copy.generateId();
    return copy;
  }

  private String deriveId(AnalysisPackageAttachmentMetadata source, String version) {
    return String.format("/public/files/analysis-packages/%s-%s/attachments/%s",
        source.getAnalysisPackageId(), version, source.getFileName());
  }

  @Override
  protected String getMasterFileNamePattern() {
    return AnalysisPackageAttachmentFilenameBuilder.ALL_ANALYSIS_PACKAGE_ATTACHMENTS;
  }

  @Override
  protected String getPredecessorFileName(AnalysisPackageAttachmentMetadata predecessor) {
    return AnalysisPackageAttachmentFilenameBuilder
        .buildFileName(predecessor.getAnalysisPackageId(), predecessor.getFileName());
  }

  @Override
  protected String getShadowCopyFileName(AnalysisPackageAttachmentMetadata shadowCopy) {
    return AnalysisPackageAttachmentFilenameBuilder.buildFileName(shadowCopy);
  }
}
