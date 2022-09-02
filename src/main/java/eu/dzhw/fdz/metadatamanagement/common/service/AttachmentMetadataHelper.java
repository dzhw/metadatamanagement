package eu.dzhw.fdz.metadatamanagement.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import org.bson.Document;
import org.javers.core.Javers;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.gridfs.model.GridFSFile;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopySaveNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.filemanagement.util.MimeTypeDetector;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.SecurityUtils;

/**
 * Provides helper method for handling common {@link AbstractRdcDomainObject} fields.
 *
 * @param <T> {@link AbstractRdcDomainObject}
 */
@Service
public class AttachmentMetadataHelper<T extends AbstractRdcDomainObject> {

  private MimeTypeDetector mimeTypeDetector;

  private Javers javers;

  private GridFsOperations operations;

  private GridFsMetadataUpdateService gridFsMetadataUpdateService;

  /**
   * Create a new instance.
   */
  public AttachmentMetadataHelper(MimeTypeDetector mimeTypeDetector, Javers javers,
      GridFsOperations operations, GridFsMetadataUpdateService gridFsMetadataUpdateService) {
    this.mimeTypeDetector = mimeTypeDetector;
    this.javers = javers;
    this.operations = operations;
    this.gridFsMetadataUpdateService = gridFsMetadataUpdateService;
  }

  /**
   * Initialize fields of {@link AbstractShadowableRdcDomainObject}.
   *
   * @param metadata Metadata object
   * @param currentUser User name from the current session
   */
  public void initAttachmentMetadata(T metadata, String currentUser) {
    metadata.setVersion(0L);
    metadata.setCreatedDate(LocalDateTime.now());
    metadata.setCreatedBy(currentUser);
    metadata.setLastModifiedBy(currentUser);
    metadata.setLastModifiedDate(LocalDateTime.now());
  }

  /**
   * Write attachment metadata and the actual file to storage.
   *
   * @param multipartFile Multipart file to store
   * @param filename File name to use
   * @param metadata Metadata attachment
   * @param currentUser User name from the current session
   */
  @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
  public void writeAttachmentMetadata(MultipartFile multipartFile, String filename, T metadata,
      String currentUser) throws IOException {

    if (fileNameExists(filename)) {
      throw new DuplicateFilenameException(filename);
    }

    String contentType = mimeTypeDetector.detect(multipartFile);
    try (InputStream in = multipartFile.getInputStream()) {
      gridFsMetadataUpdateService.store(in, filename, contentType, metadata);
      javers.commit(currentUser, metadata);
    }
  }

  private boolean fileNameExists(String filename) {
    GridFSFile file = operations.findOne(Query.query(GridFsCriteria.whereFilename().is(filename)));
    return file != null;
  }

  /**
   * Updates the metadata of an attachment file.
   *
   * @param metadata New metadata
   * @param filePath The path to the file for which the metadata will be updated.
   */
  public void updateAttachmentMetadata(AbstractRdcDomainObject metadata, String filePath) {
    metadata.setVersion(metadata.getVersion() + 1);
    String currentUser = SecurityUtils.getCurrentUserLogin();
    metadata.setLastModifiedBy(currentUser);
    metadata.setLastModifiedDate(LocalDateTime.now());

    GridFsResource file = operations.getResource(filePath);

    Document gridFsMetadata = file.getOptions().getMetadata();
    if (Boolean.TRUE.equals(gridFsMetadata.get("shadow"))) {
      throw new ShadowCopySaveNotAllowedException();
    }
    gridFsMetadataUpdateService.putMetadata(file, metadata);
    javers.commit(currentUser, metadata);
  }
}
