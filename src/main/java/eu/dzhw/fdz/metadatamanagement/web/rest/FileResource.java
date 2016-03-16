package eu.dzhw.fdz.metadatamanagement.web.rest;

import javax.inject.Inject;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mongodb.gridfs.GridFSDBFile;

import eu.dzhw.fdz.metadatamanagement.service.reporter.FileService;

/**
 * REST controller for downloading generic files from the GridFS / MongoDB.
 * 
 * @author Daniel Katzberg
 *
 */
@Controller
@RequestMapping(value = "/public")
public class FileResource {

  @Inject
  private FileService fileDownloadService;

  /**
   * Download a file from the GridFS / MongoDB by a given filename.
   * 
   * @param fileName The name of the file in the GridFS / MongoDB.
   */
  @RequestMapping(value = "/files")
  public ResponseEntity<InputStreamResource> downloadFile(
      @RequestParam(value = "fileName") String fileName) {

    // find file in grid fs / mongo db
    GridFSDBFile gridFsFile = this.fileDownloadService.findTexTemplates(fileName);

    // Return ok. Status 200. download file
    return ResponseEntity.ok()
      .header("Content-Disposition", ("inline;filename=" + fileName))
      .contentLength(gridFsFile.getLength())
      .contentType(MediaType.parseMediaType(gridFsFile.getContentType()))
      .body(new InputStreamResource(gridFsFile.getInputStream()));
  }

}
