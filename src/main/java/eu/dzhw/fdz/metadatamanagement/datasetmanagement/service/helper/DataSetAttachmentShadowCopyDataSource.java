package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.helper;

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
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;

/**
 * Provides data for creating shadow copies of {@link DataSetAttachmentMetadata}.
 */
@Component
public class DataSetAttachmentShadowCopyDataSource
    extends AbstractAttachmentShadowCopyDataSource<DataSetAttachmentMetadata> {

  /**
   * Create a new instance.
   */
  public DataSetAttachmentShadowCopyDataSource(GridFsOperations gridFsOperations,
      MongoTemplate mongoTemplate, GridFsMetadataUpdateService gridFsMetadataUpdateService) {
    super(gridFsOperations, mongoTemplate, gridFsMetadataUpdateService,
        DataSetAttachmentMetadata.class);
  }

  @Override
  public DataSetAttachmentMetadata createShadowCopy(DataSetAttachmentMetadata source,
      Release release) {
    String derivedId = deriveId(source, release.getVersion());

    Query query = new Query(GridFsCriteria.whereMetaData("_id").is(derivedId));
    GridFSFile gridFsFile = this.gridFsOperations.findOne(query);
    DataSetAttachmentMetadata copy;

    if (gridFsFile == null) {
      copy = new DataSetAttachmentMetadata();
      BeanUtils.copyProperties(source, copy);
    } else {
      MongoConverter converter = mongoTemplate.getConverter();
      copy = converter.read(DataSetAttachmentMetadata.class, gridFsFile.getMetadata());
      BeanUtils.copyProperties(source, copy, "version");
    }

    copy.setDataAcquisitionProjectId(
        source.getDataAcquisitionProjectId() + "-" + release.getVersion());
    copy.setDataSetId(source.getDataSetId() + "-" + release.getVersion());
    copy.generateId();
    return copy;
  }

  private String deriveId(DataSetAttachmentMetadata source, String version) {
    return String.format("/public/files/data-sets/%s-%s/attachments/%s", source.getDataSetId(),
        version, source.getFileName());
  }

  @Override
  protected String getMasterFileNamePattern() {
    return DataSetAttachmentFilenameBuilder.ALL_DATASET_ATTACHMENTS;
  }

  @Override
  protected String getPredecessorFileName(DataSetAttachmentMetadata predecessor) {
    return DataSetAttachmentFilenameBuilder.buildFileName(predecessor.getDataSetId(),
        predecessor.getFileName());
  }

  @Override
  protected String getShadowCopyFileName(DataSetAttachmentMetadata shadowCopy) {
    return DataSetAttachmentFilenameBuilder.buildFileName(shadowCopy);
  }
}
