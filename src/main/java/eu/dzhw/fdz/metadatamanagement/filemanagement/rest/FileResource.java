package eu.dzhw.fdz.metadatamanagement.filemanagement.rest;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import com.mongodb.gridfs.GridFSDBFile;

import eu.dzhw.fdz.metadatamanagement.filemanagement.service.FileService;

/**
 * REST controller for downloading generic files from the GridFS / MongoDB.
 * 
 * @author Daniel Katzberg
 *
 */
@Controller
@RequestMapping(value = "/public")
public class FileResource {

  @Autowired
  private FileService fileService;

  /**
   * Download a file from the GridFS / MongoDB by a given filename.
   */
  @RequestMapping(value = "/files/**")
  public ResponseEntity<?> downloadFile(HttpServletRequest request) {
    String completePath =
        (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    String fileName = completePath.replaceFirst("/public/files", "");
    // find file in grid fs / mongo db
    GridFSDBFile gridFsFile = this.fileService.findFile(fileName);

    if (gridFsFile == null) {
      return ResponseEntity.notFound().build();
    }
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_DISPOSITION, 
        "filename=" + removeFolders(gridFsFile.getFilename())); 
    // Return Status 200 or 304 if not modified
    return ResponseEntity.ok()
      .headers(headers)
      .contentLength(gridFsFile.getLength())
      .cacheControl(CacheControl.maxAge(0, TimeUnit.MILLISECONDS).mustRevalidate()
          .cachePrivate().cachePublic())
      .eTag(String.valueOf(gridFsFile.getUploadDate().getTime()))
      .lastModified(gridFsFile.getUploadDate().getTime())
      .contentType(MediaType.parseMediaType(gridFsFile.getContentType()))
      .body(new InputStreamResource(gridFsFile.getInputStream()));
  }
  
  private String removeFolders(String filename) {
    int index = filename.lastIndexOf('/');
    if (index > -1) {
      return filename.substring(index + 1, filename.length());
    } else {
      return filename;
    }
  }
}
