package eu.dzhw.fdz.metadatamanagement.common.repository.changelogs;

import org.bson.Document;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Initial setup for sequences/counters.
 * 
 * @author René Reitmann
 */
@ChangeLog
public class CreateCountersChangeLog {

  /**
   * Setup sequences/counters.
   * 
   * @param db Mongo client to be used.
   */
  @ChangeSet(id = "createCounters", author = "rreitmann", order = "001")
  public void createCounters(MongoDatabase db) {
    MongoCollection<Document> mycollection = db.getCollection("counters");
    Document orders = new Document("_id", "orders").append("seq", 0);
    mycollection.insertOne(orders);
    Document tasks = new Document("_id", "tasks").append("seq", 0);
    mycollection.insertOne(tasks);
  }

}
