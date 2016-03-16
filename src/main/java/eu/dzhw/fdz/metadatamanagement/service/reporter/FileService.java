package eu.dzhw.fdz.metadatamanagement.service.reporter;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mongodb.gridfs.GridFSDBFile;

/**
 * This service handles the download of generic files from the GridFS / MongoDB.
 * 
 * @author Daniel Katzberg
 *
 */
@Service
public class FileService {

  @Inject
  private GridFsOperations operations;

  /**
   * This method find a latex template in the GridFS / MongoDB by a given file name.
   * 
   * @param fileName The name of the file.
   * @return The GridFS representation of the file in the database.
   */
  public GridFSDBFile findTexTemplates(String fileName) {
    //Check for filename
    Query query = new Query(GridFsCriteria.whereFilename()
        .is(fileName));
        
    return this.operations.findOne(query);
  }

  /**
   * Delete all temp files from the GridFS / MongoDB. All temp files have a name, which starts with
   * /tmp/ . Cron Meaning: Every Day at 3 am.
   */
  @Scheduled(cron = "0 0 3 * * ?")
  public void deleteTempFiles() {
    // Regular Expression, which checks for filenames, which are starting with /tmp/
    Query query = new Query(GridFsCriteria.whereFilename()
        .regex("^/tmp/"));

    this.operations.delete(query);
  }
}
