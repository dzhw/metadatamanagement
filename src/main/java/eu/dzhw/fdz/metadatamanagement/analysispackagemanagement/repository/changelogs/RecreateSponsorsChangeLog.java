package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.changelogs;

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
  @ChangeSet(order = "1", id = "recreateAnalysisPackageSponsors", author = "cschwartze", runAlways = true)
  public void recreateSponsors(MongoDatabase db) {
    MongoCollection<Document> analysisPackages = db.getCollection("analysis_packages");
    List results = new ArrayList<>();
    analysisPackages.find().into(results);
    results.forEach(analysisPckg -> {
      Object id = ((Document) analysisPckg).get("dataAcquisitionProjectId");
      List<Document> sponsors = ((Document) analysisPckg).getList("sponsors", Document.class);
      if (sponsors != null) {
        if (sponsors.get(0).keySet().contains("de")) {
          log.info("Recreate sponsors for " + id);
          List newSponsors = new ArrayList<>();
          sponsors.forEach(s -> {
            Document newSponsor = Document.parse("{\"name\" : " + s.toJson() + ", \"fundingRef\" : null}");
            newSponsors.add(newSponsor);
          });
          ((Document) analysisPckg).append("sponsors", newSponsors);
          if (analysisPackages.findOneAndReplace(
                  Filters.eq("dataAcquisitionProjectId", id), (Document) analysisPckg) != null) {
            log.info("Sponsors for analysis package " + id + " recreated.");
          } else {
            log.warn("Sponsors for analysis package " + id + " could not recreated.");
          }
        }
        if (sponsors.get(0).keySet().contains("name")) {
          log.info("Sponsor data appears to have already been migrated for analysis package " + id + ".");
        }
      }
    });
  }
}