package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.helper;

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
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;

/**
 * Provides data for creating shadow copies of {@link InstrumentAttachmentMetadata}.
 */
@Component
public class InstrumentAttachmentShadowCopyDataSource
    extends AbstractAttachmentShadowCopyDataSource<InstrumentAttachmentMetadata> {

  /**
   * Create a new instance.
   */
  public InstrumentAttachmentShadowCopyDataSource(GridFsOperations gridFsOperations,
      GridFS gridFs, MongoTemplate mongoTemplate) {
    super(gridFsOperations, gridFs,mongoTemplate, InstrumentAttachmentMetadata.class);
  }

  @Override
  public InstrumentAttachmentMetadata createShadowCopy(InstrumentAttachmentMetadata source,
      String version) {

    String derivedId = deriveId(source, version);

    Query query = new Query(GridFsCriteria.whereMetaData("_id").is(derivedId));
    GridFSFile gridFsFile = this.gridFsOperations.findOne(query);
    InstrumentAttachmentMetadata copy;

    if (gridFsFile == null) {
      copy = new InstrumentAttachmentMetadata();
      BeanUtils.copyProperties(source, copy);
    } else {
      MongoConverter converter = mongoTemplate.getConverter();
      copy = converter.read(InstrumentAttachmentMetadata.class, gridFsFile.getMetadata());
      BeanUtils.copyProperties(source, copy, "version");
    }

    copy.setDataAcquisitionProjectId(source.getDataAcquisitionProjectId() + "-" + version);
    copy.setInstrumentId(source.getInstrumentId() + "-" + version);
    copy.generateId();
    return copy;
  }

  private String deriveId(InstrumentAttachmentMetadata source, String version) {
    return String.format("/public/files/instruments/%s-%s/attachments/%s", source.getInstrumentId(),
        version, source.getFileName());
  }

  @Override
  protected String getMasterFileNamePattern() {
    return InstrumentAttachmentFilenameBuilder.ALL_INSTRUMENT_ATTACHMENTS;
  }

  @Override
  protected String getPredecessorFileName(InstrumentAttachmentMetadata predecessor) {
    return InstrumentAttachmentFilenameBuilder
        .buildFileName(predecessor.getInstrumentId(), predecessor.getFileName());
  }

  @Override
  protected String getShadowCopyFileName(InstrumentAttachmentMetadata shadowCopy) {
    return InstrumentAttachmentFilenameBuilder.buildFileName(shadowCopy);
  }
}
