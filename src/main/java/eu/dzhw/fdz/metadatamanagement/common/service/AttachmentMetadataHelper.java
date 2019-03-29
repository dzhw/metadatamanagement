package eu.dzhw.fdz.metadatamanagement.common.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyUpdateNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.filemanagement.util.MimeTypeDetector;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;
import org.bson.Document;
import org.javers.core.Javers;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * Provides helper method for handling common {@link AbstractShadowableRdcDomainObject} fields.
 * @param <T> {@link AbstractShadowableRdcDomainObject}
 */
@Service
public class AttachmentMetadataHelper<T extends AbstractShadowableRdcDomainObject> {

  private MimeTypeDetector mimeTypeDetector;

  private Javers javers;

  private GridFsOperations operations;

  private GridFS gridFs;

  private MongoTemplate mongoTemplate;

  /**
   * Create a new instance.
   */
  public AttachmentMetadataHelper(MimeTypeDetector mimeTypeDetector, Javers javers,
                                  GridFsOperations operations, GridFS gridFs,
                                  MongoTemplate mongoTemplate) {
    this.mimeTypeDetector = mimeTypeDetector;
    this.javers = javers;
    this.operations = operations;
    this.gridFs = gridFs;
    this.mongoTemplate = mongoTemplate;
  }

  /**
   * Initialize fields of {@link AbstractShadowableRdcDomainObject}.
   * @param metadata    Metadata object
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
   * @param filename      File name to use
   * @param metadata      Metadata attachment
   * @param currentUser   User name from the current session
   */
  public void writeAttachmentMetadata(MultipartFile multipartFile, String filename, T metadata,
                                      String currentUser) throws IOException {

    String contentType = mimeTypeDetector.detect(multipartFile);
    try (InputStream in = multipartFile.getInputStream()) {
      this.operations.store(in, filename, contentType, metadata);
      javers.commit(currentUser, metadata);
    }
  }

  /**
   * Updates the metadata of an attachment file.
   * @param metadata         New metadata
   * @param filePath The path to the file for which the metadata will be updated.
   */
  public void updateAttachmentMetadata(AbstractShadowableRdcDomainObject metadata,
                                       String filePath) {
    metadata.setVersion(metadata.getVersion() + 1);
    String currentUser = SecurityUtils.getCurrentUserLogin();
    metadata.setLastModifiedBy(currentUser);
    metadata.setLastModifiedDate(LocalDateTime.now());

    GridFSDBFile file = gridFs.findOne(filePath);

    DBObject gridFsMetadata = file.getMetaData();
    if (Boolean.TRUE.equals(gridFsMetadata.get("shadow"))) {
      throw new ShadowCopyUpdateNotAllowedException();
    }
    BasicDBObject dbObject =
        new BasicDBObject((Document) mongoTemplate.getConverter().convertToMongoType(metadata));
    /*
     * Mongo's GridFs and Springs MongoOperations have a different approach regarding the mapping
     * of file metadata. Saving a file without moving the contentType to GridFSDBFile' metadata
     * results in a lost content type.
     */
    String contentType = getContentType(file);
    dbObject.append("_contentType", contentType);
    file.setMetaData(dbObject);
    file.save();
    javers.commit(currentUser, metadata);
  }

  private String getContentType(GridFSDBFile gridFsDbFile) {
    String contentType = gridFsDbFile.getContentType();
    if (StringUtils.hasText(contentType)) {
      return contentType;
    } else {
      return (String) gridFsDbFile.getMetaData().get("_contentType");
    }
  }
}
