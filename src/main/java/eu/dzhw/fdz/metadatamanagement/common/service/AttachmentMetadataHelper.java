package eu.dzhw.fdz.metadatamanagement.common.service;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.filemanagement.util.MimeTypeDetector;
import org.javers.core.Javers;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * Provides helper method for initializing common {@link AbstractShadowableRdcDomainObject} fields.
 * @param <T> {@link AbstractShadowableRdcDomainObject}
 */
@Service
public class AttachmentMetadataHelper<T extends AbstractShadowableRdcDomainObject> {

  private MimeTypeDetector mimeTypeDetector;

  private Javers javers;

  private GridFsOperations operations;

  /**
   * Create a new instance.
   */
  public AttachmentMetadataHelper(MimeTypeDetector mimeTypeDetector, Javers javers,
                                  GridFsOperations operations) {
    this.mimeTypeDetector = mimeTypeDetector;
    this.javers = javers;
    this.operations = operations;
  }


  /**
   * Initialize fields of {@link AbstractShadowableRdcDomainObject}.
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
   * @param multipartFile Multipart file to store
   * @param filename File name to use
   * @param metadata Metadata attachment
   * @param currentUser User name from the current session
   */
  public void writeAttachmentMetadata(MultipartFile multipartFile, String filename, T metadata,
                                      String currentUser) throws IOException {

    String contentType = mimeTypeDetector.detect(multipartFile);
    try (InputStream in = multipartFile.getInputStream()) {
      this.operations.store(in, filename, contentType, metadata);
      javers.commit(currentUser, metadata);
    }
  }
}
