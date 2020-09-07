package eu.dzhw.fdz.metadatamanagement.usermanagement.repository.changelogs;

import org.bson.Document;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * Setup all available authorities.
 * 
 * @author René Reitmann
 */
@ChangeLog
public class CreateAuthoritesChangeLog {
  /**
   * Setup available authorities.
   * 
   * @param db Mongo client to be used.
   */
  @ChangeSet(id = "createAuthorites", author = "rreitmann", order = "001")
  public void createAuthorites(MongoDatabase db) {
    MongoCollection<Document> mycollection = db.getCollection("jhi_authority");
    Document admin = new Document("_id", AuthoritiesConstants.ADMIN);
    mycollection.insertOne(admin);
    Document user = new Document("_id", AuthoritiesConstants.USER);
    mycollection.insertOne(user);
    Document publisher = new Document("_id", AuthoritiesConstants.PUBLISHER);
    mycollection.insertOne(publisher);
    Document dataProvider = new Document("_id", AuthoritiesConstants.DATA_PROVIDER);
    mycollection.insertOne(dataProvider);
    Document releaseManager = new Document("_id", AuthoritiesConstants.RELEASE_MANAGER);
    mycollection.insertOne(releaseManager);
  }
}
