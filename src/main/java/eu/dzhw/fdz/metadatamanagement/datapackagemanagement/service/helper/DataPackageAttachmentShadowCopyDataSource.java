package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.helper;

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
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackageAttachmentMetadata;

/**
 * Provides data for creating shadow copies of {@link DataPackageAttachmentMetadata}.
 */
@Component
public class DataPackageAttachmentShadowCopyDataSource
    extends AbstractAttachmentShadowCopyDataSource<DataPackageAttachmentMetadata> {

  /**
   * Create a new instance.
   */
  public DataPackageAttachmentShadowCopyDataSource(GridFsOperations gridFsOperations,
      MongoTemplate mongoTemplate, GridFsMetadataUpdateService gridFsMetadataUpdateService) {
    super(gridFsOperations, mongoTemplate, gridFsMetadataUpdateService,
        DataPackageAttachmentMetadata.class);
  }

  @Override
  public DataPackageAttachmentMetadata createShadowCopy(DataPackageAttachmentMetadata source,
      Release release) {

    String derivedId = deriveId(source, release.getVersion());

    Query query = new Query(GridFsCriteria.whereMetaData("_id").is(derivedId));
    GridFSFile gridFsFile = this.gridFsOperations.findOne(query);
    DataPackageAttachmentMetadata copy;

    if (gridFsFile == null) {
      copy = new DataPackageAttachmentMetadata();
      BeanUtils.copyProperties(source, copy);
    } else {
      MongoConverter converter = mongoTemplate.getConverter();
      copy = converter.read(DataPackageAttachmentMetadata.class, gridFsFile.getMetadata());
      BeanUtils.copyProperties(source, copy, "version");
    }

    copy.setDataAcquisitionProjectId(
        source.getDataAcquisitionProjectId() + "-" + release.getVersion());
    copy.setDataPackageId(source.getDataPackageId() + "-" + release.getVersion());
    copy.generateId();
    return copy;
  }

  private String deriveId(DataPackageAttachmentMetadata source, String version) {
    return String.format("/public/files/data-packages/%s-%s/attachments/%s",
        source.getDataPackageId(), version, source.getFileName());
  }

  @Override
  protected String getMasterFileNamePattern() {
    return DataPackageAttachmentFilenameBuilder.ALL_STUDY_ATTACHMENTS;
  }

  @Override
  protected String getPredecessorFileName(DataPackageAttachmentMetadata predecessor) {
    return DataPackageAttachmentFilenameBuilder.buildFileName(predecessor.getDataPackageId(),
        predecessor.getFileName());
  }

  @Override
  protected String getShadowCopyFileName(DataPackageAttachmentMetadata shadowCopy) {
    return DataPackageAttachmentFilenameBuilder.buildFileName(shadowCopy);
  }
}
