package eu.dzhw.fdz.metadatamanagement.common.service;

import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;
import static org.springframework.data.mongodb.gridfs.GridFsCriteria.whereFilename;

import java.io.InputStream;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import lombok.RequiredArgsConstructor;

/**
 * Service which helps updating the metadata of a GridFsResource without loosing the content type of
 * the file.
 */
@Service
@RequiredArgsConstructor
public class GridFsMetadataUpdateService {

  private final MongoOperations mongoOperations;

  private final GridFsOperations gridFsOperations;

  /**
   * Method which helps updating the metadata of a GridFsResource without loosing the content type
   * of the file.
   */
  public void putMetadata(GridFsResource gridFsResource, AbstractRdcDomainObject metadata) {
    Document dbObject = (Document) mongoOperations.getConverter().convertToMongoType(metadata);
    dbObject.put("_contentType", gridFsResource.getContentType());
    mongoOperations.updateFirst(query(whereFilename().is(gridFsResource.getFilename())),
        update("metadata", dbObject), "fs.files");

  }

  /**
   * Store the given input stream as gridf file with the contentType set correctly. This method is
   * required due to a bug in {@link GridFsOperations:store} which looses the contentType.
   * 
   * @param content must not be {@literal null}.
   * @param filename must not be {@literal null} or empty.
   * @param contentType must not be {@literal null}.
   * @param metadata must not be {@literal null}
   */
  public void store(InputStream content, String filename, String contentType,
      AbstractRdcDomainObject metadata) {
    Document document = (Document) mongoOperations.getConverter().convertToMongoType(metadata);
    document.put("_contentType", contentType);
    gridFsOperations.store(content, filename, document);
  }
}
