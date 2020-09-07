package eu.dzhw.fdz.metadatamanagement.usermanagement.repository.changelogs;

import org.bson.Document;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Setup all available users during unittesting and locally.
 * 
 * @author René Reitmann
 */
@ChangeLog
public class CreateUsersChangeLog {

  /**
   * Setup all available users during unittesting and locally.
   * 
   * @param db Mongo client to be used.
   */
  @ChangeSet(id = "createUsers", author = "rreitmann", order = "001")
  public void createUsers(MongoDatabase db) {
    MongoCollection<Document> mycollection = db.getCollection("jhi_user");
    Document user0 = Document
        .parse("{\n" + "\"_id\" : \"user-0\",\n" + "\"version\": 0,\n" + "\"login\" : \"system\",\n"
        + "\"password\" : \"$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG\",\n"
            + "\"first_name\": \"\",\n" + "\"last_name\": \"System\",\n"
        + "\"email\": \"system@localhost\",\n" + "\"activated\": \"true\",\n"
            + "\"lang_key\": \"en\",\n" + "\"createdBy\": \"system\",\n"
        + "\"createdDate\": new Date(),\n"
        + "\"authorities\": [{\"_id\": \"ROLE_ADMIN\"}, {\"_id\": \"ROLE_USER\"}]\n" + "}");
    mycollection.insertOne(user0);
    Document user1 = Document.parse(
        "{\n" + "\"_id\" : \"user-1\",\n" + "\"version\": 0,\n" + "\"login\" : \"anonymousUser\",\n"
        + "\"password\" : \"$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO\",\n"
        + "\"first_name\": \"Anonymous\",\n" + "\"last_name\": \"User\",\n"
        + "\"email\": \"anonymous@localhost\",\n" + "\"activated\": \"true\",\n"
            + "\"lang_key\": \"en\",\n" + "\"createdBy\": \"system\",\n"
            + "\"createdDate\": new Date(),\n" + "\"authorities\": []\n" + "}");
    mycollection.insertOne(user1);
    Document user2 = Document
        .parse("{\n" + "\"_id\" : \"user-2\",\n" + "\"login\" : \"admin\",\n" + "\"version\": 0,\n"
        + "\"password\" : \"$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC\",\n"
        + "\"first_name\": \"admin\",\n" + "\"last_name\": \"Administrator\",\n"
        + "\"email\": \"admin@localhost\",\n" + "\"activated\": \"true\",\n"
            + "\"lang_key\": \"en\",\n" + "\"createdBy\": \"system\",\n"
        + "\"createdDate\": new Date(),\n"
        + "\"authorities\": [{\"_id\": \"ROLE_ADMIN\"}, {\"_id\": \"ROLE_USER\"},"
        + " {\"_id\": \"ROLE_PUBLISHER\"}, {\"_id\": \"ROLE_DATA_PROVIDER\"}]\n" + "}");
    mycollection.insertOne(user2);
    Document user3 = Document.parse("{\n" + "\"_id\" : \"user-3\",\n" + "\"version\": 0,\n"
        + "\"login\" : \"user\",\n"
        + "\"password\" : \"$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K\",\n"
        + "\"first_name\": \"\",\n" + "\"last_name\": \"User\",\n"
        + "\"email\": \"user@localhost\",\n" + "\"activated\": \"true\",\n"
        + "\"lang_key\": \"en\",\n" + "\"createdBy\": \"system\",\n"
        + "\"createdDate\": new Date(),\n" + "\"authorities\": [{\"_id\": \"ROLE_USER\"}]\n" + "}");
    mycollection.insertOne(user3);
  }
}
