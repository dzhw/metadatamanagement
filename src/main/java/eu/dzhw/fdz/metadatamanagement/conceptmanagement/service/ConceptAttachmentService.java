package eu.dzhw.fdz.metadatamanagement.conceptmanagement.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.javers.core.Javers;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.gridfs.model.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.common.service.AttachmentMetadataHelper;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.ConceptAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing attachments for {@link Concept}s.
 */
@Service
@RequiredArgsConstructor
public class ConceptAttachmentService {

  private final GridFsOperations operations;

  private final MongoTemplate mongoTemplate;

  private final Javers javers;

  private final AttachmentMetadataHelper<ConceptAttachmentMetadata> attachmentMetadataHelper;

  /**
   * Save the attachment for a {@link Concept}.
   * @param metadata The metadata of the attachment.
   * @return The GridFs filename.
   * @throws IOException thrown when the input stream cannot be closed
   */
  public String createConceptAttachment(MultipartFile multipartFile,
      ConceptAttachmentMetadata metadata) throws IOException {
    String currentUser = SecurityUtils.getCurrentUserLogin();
    attachmentMetadataHelper.initAttachmentMetadata(metadata, currentUser);
    metadata.generateId();
    String filename = ConceptAttachmentFilenameBuilder.buildFileName(metadata);
    attachmentMetadataHelper.writeAttachmentMetadata(multipartFile, filename, metadata,
        currentUser);

    return filename;
  }

  /**
   * Update the metadata of the attachment.
   * @param metadata The new metadata.
   */
  public void updateAttachmentMetadata(ConceptAttachmentMetadata metadata) {
    String filePath = ConceptAttachmentFilenameBuilder.buildFileName(metadata.getConceptId(),
        metadata.getFileName());
    attachmentMetadataHelper.updateAttachmentMetadata(metadata, filePath);
  }

  /**
   * Delete all attachments of the given {@link Concept}.
   * @param conceptId the id of the {@link Concept}.
   */
  public void deleteAllByConceptId(String conceptId) {
    String currentUser = SecurityUtils.getCurrentUserLogin();
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(
            ConceptAttachmentFilenameBuilder.buildFileNamePrefix(conceptId))));
    Iterable<GridFSFile> files = this.operations.find(query);
    files.forEach(file -> {
      ConceptAttachmentMetadata metadata = mongoTemplate.getConverter().read(
          ConceptAttachmentMetadata.class, file.getMetadata());
      javers.commitShallowDelete(currentUser, metadata);
    });
    this.operations.delete(query);
  }

  /**
   * Load all metadata objects from gridfs (ordered by indexInConcept).
   * @param conceptId The id of the {@link Concept}.
   * @return A list of metadata.
   */
  public List<ConceptAttachmentMetadata> findAllByConcept(String conceptId) {
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote(
            ConceptAttachmentFilenameBuilder.buildFileNamePrefix(conceptId))));
    query.with(Sort.by(Sort.Direction.ASC, "metadata.indexInConcept"));
    Iterable<GridFSFile> files = this.operations.find(query);
    List<ConceptAttachmentMetadata> result = new ArrayList<>();
    files.forEach(gridfsFile -> {
      result.add(mongoTemplate.getConverter().read(ConceptAttachmentMetadata.class,
          gridfsFile.getMetadata()));
    });
    return result;
  }



  /**
   * Delete all attachments of all {@link Concept}s.
   */
  public void deleteAll() {
    String currentUser = SecurityUtils.getCurrentUserLogin();
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^" + Pattern.quote("/concepts/") + ".*" + Pattern.quote("/attachments/")));
    Iterable<GridFSFile> files = this.operations.find(query);
    files.forEach(file -> {
      ConceptAttachmentMetadata metadata = mongoTemplate.getConverter().read(
          ConceptAttachmentMetadata.class, file.getMetadata());
      javers.commitShallowDelete(currentUser, metadata);
    });
    this.operations.delete(query);
  }

  /**
   * Delete the attachment and its metadata from gridfs.
   * @param conceptId The id of the {@link Concept}.
   * @param filename The filename of the attachment.
   */
  public void deleteByConceptIdAndFilename(String conceptId, String filename) {
    Query fileQuery = new Query(GridFsCriteria.whereFilename().is(
        ConceptAttachmentFilenameBuilder.buildFileName(conceptId, filename)));
    GridFSFile file = this.operations.findOne(fileQuery);
    if (file == null) {
      return;
    }
    ConceptAttachmentMetadata metadata = mongoTemplate.getConverter().read(
        ConceptAttachmentMetadata.class, file.getMetadata());
    String currentUser = SecurityUtils.getCurrentUserLogin();
    this.operations.delete(fileQuery);
    javers.commitShallowDelete(currentUser, metadata);
  }
}
