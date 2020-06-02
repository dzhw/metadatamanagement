package eu.dzhw.fdz.metadatamanagement.common.service;

import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;
import static org.springframework.data.mongodb.gridfs.GridFsCriteria.whereFilename;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GridFsMetadataUpdateService {

  private final MongoOperations mongoOperations;

  public void putMetadata(GridFsResource gridFsResource, AbstractRdcDomainObject metadata) {
    Document dbObject = (Document) mongoOperations.getConverter().convertToMongoType(metadata);
    dbObject.put("_contentType", gridFsResource.getContentType());
    mongoOperations.updateFirst(
        query(whereFilename().is(gridFsResource.getFilename())),
        update("metadata", dbObject), "fs.files");

  }
}
