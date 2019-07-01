package eu.dzhw.fdz.metadatamanagement.filemanagement.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * This service handles the download of generic files from the GridFS / MongoDB.
 * 
 * @author Daniel Katzberg
 *
 */
@Service
public class FileService {

  @Autowired
  private GridFsOperations gridfsOperations;

  @Autowired
  private MongoOperations mongoOperations;

  @Value("${metadatamanagement.server.instance-index}")
  private Integer instanceId;

  /**
   * We expect filenames in GridFS to be unique.
   */
  @PostConstruct
  protected void setupFileNameIndex() {
    mongoOperations.indexOps("fs.files")
        .ensureIndex(new Index().on("filename", Direction.ASC).unique());
  }

  /**
   * This method load the gridfs file by its fileName. We expect filenames to be unique. Thus the
   * fileName has to include a path.
   * 
   * @param fileName The name of the file.
   * @return The GridFS representation of the file in the database.
   */
  @Nullable
  public GridFsResource findFile(String fileName) {
    return this.gridfsOperations.getResource(fileName);
  }

  /**
   * Delete all temp files from the GridFS / MongoDB. All temp files have a name, which starts with
   * /tmp/ . Cron Meaning: Every Day at 3 am.
   */
  @Scheduled(cron = "0 0 3 * * ?")
  public void deleteTempFiles() {
    if (instanceId != 0) {
      return;
    }
    // Regular Expression, which checks for filenames, which are starting with /tmp/
    Query query = new Query(GridFsCriteria.whereFilename().regex("^/tmp/"));

    this.gridfsOperations.delete(query);
  }

  /**
   * Delete the temporary file with the given fileName.
   * 
   * @param fileName the name of the file.
   */
  public void deleteTempFile(String fileName) {
    Query query = new Query(GridFsCriteria.whereFilename().is("/tmp/" + fileName));

    this.gridfsOperations.delete(query);
  }

  /**
   * Save the given stream to mongo in a "temp directory" and return the final filename.
   * 
   * @param stream The bytes to save
   * @param fileName The fileName which gets prefixed with /tmp/
   * @param contentType the content type of the file
   * @return the final filename
   * @throws IOException thrown when the input stream cannot be closed
   */
  public String saveTempFile(InputStream stream, String fileName, String contentType)
      throws IOException {
    try (InputStream inputStream = stream) {
      String filename = "/tmp/" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
          + "_" + fileName;
      this.gridfsOperations.store(inputStream, filename, contentType);
      return filename;
    }
  }
}
