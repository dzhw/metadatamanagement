package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.javers.core.Javers;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.gridfs.model.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyCreateNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyDeleteNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.service.AttachmentMetadataHelper;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.helper.InstrumentAttachmentFilenameBuilder;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing attachments for instruments.
 *
 * @author René Reitmann
 */
@Service
@RequiredArgsConstructor
public class InstrumentAttachmentService {

  private final GridFsOperations operations;

  private final MongoTemplate mongoTemplate;

  private final Javers javers;

  private final AttachmentMetadataHelper<InstrumentAttachmentMetadata> attachmentMetadataHelper;

  /**
   * Save the attachment for an instrument.
   *
   * @param metadata The metadata of the attachment.
   * @return The GridFs filename.
   * @throws IOException thrown when the input stream cannot be closed
   */
  public String createInstrumentAttachment(MultipartFile multipartFile,
      InstrumentAttachmentMetadata metadata) throws IOException {
    if (metadata.isShadow()) {
      throw new ShadowCopyCreateNotAllowedException();
    }

    String currentUser = SecurityUtils.getCurrentUserLogin();
    attachmentMetadataHelper.initAttachmentMetadata(metadata, currentUser);
    metadata.generateId();
    metadata.setMasterId(metadata.getId());
    String filename = InstrumentAttachmentFilenameBuilder.buildFileName(metadata);
    attachmentMetadataHelper.writeAttachmentMetadata(multipartFile, filename, metadata,
        currentUser);
    return filename;
  }

  /**
   * Update the metadata of the attachment.
   *
   * @param metadata The new metadata.
   */
  public void updateAttachmentMetadata(InstrumentAttachmentMetadata metadata) {
    String filePath = InstrumentAttachmentFilenameBuilder.buildFileName(metadata.getInstrumentId(),
        metadata.getFileName());
    attachmentMetadataHelper.updateAttachmentMetadata(metadata, filePath);
  }

  /**
   * Delete all attachments of the given instrument.
   *
   * @param instrumentId the id of the instrument.
   */
  public void deleteAllByInstrumentId(String instrumentId) {
    String currentUser = SecurityUtils.getCurrentUserLogin();
    Query query = new Query(GridFsCriteria.whereFilename().regex("^"
        + Pattern.quote(InstrumentAttachmentFilenameBuilder.buildFileNamePrefix(instrumentId))));
    Iterable<GridFSFile> files = this.operations.find(query);
    files.forEach(file -> {
      InstrumentAttachmentMetadata metadata =
          mongoTemplate.getConverter().read(InstrumentAttachmentMetadata.class, file.getMetadata());
      if (metadata.isShadow()) {
        throw new ShadowCopyDeleteNotAllowedException();
      }
      javers.commitShallowDelete(currentUser, metadata);
    });
    this.operations.delete(query);
  }

  /**
   * Load all metadata objects from gridfs (ordered by indexInInstrument).
   *
   * @param instrumentId The id of the instrument.
   * @return A list of metadata.
   */
  public List<InstrumentAttachmentMetadata> findAllByInstrument(String instrumentId) {
    Query query = new Query(GridFsCriteria.whereFilename().regex("^"
        + Pattern.quote(InstrumentAttachmentFilenameBuilder.buildFileNamePrefix(instrumentId))));
    query.with(Sort.by(Sort.Direction.ASC, "metadata.indexInInstrument"));
    Iterable<GridFSFile> files = this.operations.find(query);
    List<InstrumentAttachmentMetadata> result = new ArrayList<>();
    files.forEach(gridfsFile -> {
      result.add(mongoTemplate.getConverter().read(InstrumentAttachmentMetadata.class,
          gridfsFile.getMetadata()));
    });
    return result;
  }

  /**
   * Load all metadata objects from gridfs (ordered by instrumentNumber and indexInInstrument).
   *
   * @param dataAcquisitionProjectId The id of the {@link DataAcquisitionProject}.
   * @return A list of metadata.
   */
  public List<InstrumentAttachmentMetadata> findAllByProject(String dataAcquisitionProjectId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex(InstrumentAttachmentFilenameBuilder.ALL_INSTRUMENT_ATTACHMENTS).andOperator(
            GridFsCriteria.whereMetaData("dataAcquisitionProjectId").is(dataAcquisitionProjectId)));
    query.with(
        Sort.by(Order.asc("metadata.instrumentNumber"), Order.asc("metadata.indexInInstrument")));
    Iterable<GridFSFile> files = this.operations.find(query);
    List<InstrumentAttachmentMetadata> result = new ArrayList<>();
    AtomicInteger countByInstrumentNumber = new AtomicInteger(0);
    AtomicInteger currentInstrumentNumber = new AtomicInteger(-1);
    files.forEach(gridfsFile -> {
      Integer instrumentNumber = gridfsFile.getMetadata().getInteger("instrumentNumber");
      if (!instrumentNumber.equals(currentInstrumentNumber.get())) {
        currentInstrumentNumber.set(instrumentNumber);
        countByInstrumentNumber.set(0);
      } else {
        countByInstrumentNumber.incrementAndGet();
      }
      gridfsFile.getMetadata().put("indexInInstrument", countByInstrumentNumber.get());
      result.add(mongoTemplate.getConverter().read(InstrumentAttachmentMetadata.class,
          gridfsFile.getMetadata()));
    });
    return result;
  }

  /**
   * Delete all attachments of all instruments.
   */
  public void deleteAll() {
    String currentUser = SecurityUtils.getCurrentUserLogin();
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote("/instruments/") + ".*" + Pattern.quote("/attachments/")));
    Iterable<GridFSFile> files = this.operations.find(query);
    files.forEach(file -> {
      InstrumentAttachmentMetadata metadata =
          mongoTemplate.getConverter().read(InstrumentAttachmentMetadata.class, file.getMetadata());
      if (metadata.isShadow()) {
        throw new ShadowCopyDeleteNotAllowedException();
      }
      javers.commitShallowDelete(currentUser, metadata);
    });
    this.operations.delete(query);
  }

  /**
   * Delete the attachment and its metadata from gridfs.
   *
   * @param instrumentId The id of the instrument.
   * @param filename The filename of the attachment.
   */
  public void deleteByInstrumentIdAndFilename(String instrumentId, String filename) {
    Query fileQuery = new Query(GridFsCriteria.whereFilename()
        .is(InstrumentAttachmentFilenameBuilder.buildFileName(instrumentId, filename)));
    GridFSFile file = this.operations.findOne(fileQuery);
    if (file == null) {
      return;
    }
    InstrumentAttachmentMetadata metadata =
        mongoTemplate.getConverter().read(InstrumentAttachmentMetadata.class, file.getMetadata());
    if (metadata.isShadow()) {
      throw new ShadowCopyDeleteNotAllowedException();
    }
    String currentUser = SecurityUtils.getCurrentUserLogin();
    this.operations.delete(fileQuery);
    javers.commitShallowDelete(currentUser, metadata);
  }
}
