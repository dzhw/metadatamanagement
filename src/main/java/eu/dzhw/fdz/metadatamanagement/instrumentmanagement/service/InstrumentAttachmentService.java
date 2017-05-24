package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.gridfs.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.filemanagement.util.MimeTypeDetector;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;

/**
 * Service for managing attachments for instruments.
 * 
 * @author René Reitmann
 */
@Service
public class InstrumentAttachmentService {

  @Autowired
  private GridFsOperations operations;
  
  @Autowired
  private MongoTemplate mongoTemplate;
  
  @Autowired
  private MimeTypeDetector mimeTypeDetector;
  
  /**
   * Save the attachment for an instrument. 
   * @param metadata The metadata of the attachment.
   * @return The GridFs filename.
   * @throws IOException thrown when the input stream cannot be closed
   */
  public String createInstrumentAttachment(MultipartFile multipartFile, 
      InstrumentAttachmentMetadata metadata) throws IOException {
    try (InputStream in = multipartFile.getInputStream()) {
      String currentUser = SecurityUtils.getCurrentUserLogin();
      metadata.setVersion(0L);
      metadata.setCreatedDate(LocalDateTime.now());
      metadata.setCreatedBy(currentUser);
      metadata.setLastModifiedBy(currentUser);
      metadata.setLastModifiedDate(LocalDateTime.now());
      String contentType = mimeTypeDetector.detect(multipartFile);
      GridFSFile gridFsFile = this.operations.store(in, 
          buildFileName(metadata), contentType, metadata);
      gridFsFile.validate();
      return gridFsFile.getFilename();      
    }
  }
  
  /**
   * Delete all attachments of the given instrument.
   * @param instrumentId the id of the instrument.
   */
  public void deleteAllByInstrumentId(String instrumentId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(buildFileNamePrefix(instrumentId))));
    this.operations.delete(query);
  }
  
  /**
   * Load all metadata objects from gridfs (ordered by indexInInstrument).
   * @param instrumentId The id of the instrument.
   * @return A list of metadata.
   */
  public List<InstrumentAttachmentMetadata> findAllByInstrument(String instrumentId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(buildFileNamePrefix(instrumentId))));
    query.with(new Sort(Sort.Direction.ASC, "metadata.indexInInstrument"));
    return this.operations.find(query).stream().map(gridfsFile -> {
      return mongoTemplate.getConverter().read(InstrumentAttachmentMetadata.class, 
          gridfsFile.getMetaData());
    }).collect(Collectors.toList());
  }
  
  private String buildFileName(InstrumentAttachmentMetadata metadata) {
    return buildFileNamePrefix(metadata.getInstrumentId()) + metadata.getFileName(); 
  }
  
  private String buildFileNamePrefix(String instrumentId) {
    return "/instruments/" + instrumentId + "/attachments/";
  }

  /**
   * Delete all attachments of all instruments.
   */
  public void deleteAll() {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote("/instruments/") + ".*" + Pattern.quote("/attachments/")));
    this.operations.delete(query);
  }
}
