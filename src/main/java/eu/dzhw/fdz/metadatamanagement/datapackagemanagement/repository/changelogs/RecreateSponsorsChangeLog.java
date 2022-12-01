package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.changelogs;

import org.bson.Document;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Recreating sponsor data after model modification (e.g. fundingRef).
 *
 * @author cschwartze
 */
@Slf4j
@ChangeLog
public class RecreateSponsorsChangeLog {

  /**
   * Recreating sponsor data after model modification.
   *
   * @param db Mongo db client.
   */
  @ChangeSet(order = "1", id = "recreateSponsors", author = "cschwartze", runAlways = true)
  public void recreateSponsors(MongoDatabase db) {
    MongoCollection<Document> dataPackages = db.getCollection("data_packages");
    List results = new ArrayList<>();
    dataPackages.find().into(results);
    results.forEach(res -> {
      Object id = ((Document) res).get("dataAcquisitionProjectId");
      List<Document> sponsors = ((Document) res).getList("sponsors", Document.class);
      if (sponsors.get(0).keySet().contains("de")) {
        log.info("Recreate sponsors for " + id);
        List newSponsors = new ArrayList<>();
        sponsors.forEach(s -> {
          Document newSponsor = Document.parse("{\"name\" : " + s.toJson() + ", \"fundingRef\" : null}");
          newSponsors.add(newSponsor);
        });
        ((Document) res).append("sponsors", newSponsors);
        if (dataPackages.findOneAndReplace(Filters.eq("dataAcquisitionProjectId", id), (Document) res) != null) {
          log.info("Sponsors for " + id + " recreated.");
        } else {
          log.info("Sponsors for " + id + " could not recreated.");
        }
      }
    });
  }
}
