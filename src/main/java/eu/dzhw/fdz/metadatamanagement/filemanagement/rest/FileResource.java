package eu.dzhw.fdz.metadatamanagement.filemanagement.rest;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import eu.dzhw.fdz.metadatamanagement.filemanagement.service.FileService;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for downloading generic files from the GridFS / MongoDB.
 * 
 * @author Daniel Katzberg
 *
 */
@Controller
@RequestMapping(value = "/public")
@RequiredArgsConstructor
public class FileResource {

  private final FileService fileService;

  /**
   * Download a file from the GridFS / MongoDB by a given filename.
   * @throws IOException If the file cannot be accessed
   */
  @GetMapping(value = "/files/**")
  public ResponseEntity<?> downloadFile(HttpServletRequest request) throws IOException {
    String completePath =
        (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    String fileName = URLDecoder.decode(completePath.replaceFirst("/public/files", ""), "UTF-8");
    // find file in grid fs / mongo db
    GridFsResource gridFsFile = this.fileService.findFile(fileName);

    if (gridFsFile == null || !gridFsFile.exists()) {
      return ResponseEntity.notFound().build();
    }
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_DISPOSITION, 
        "filename=" + removeFolders(gridFsFile.getFilename())); 
    // Return Status 200 or 304 if not modified
    return ResponseEntity.ok()
      .headers(headers)
      .contentLength(gridFsFile.contentLength())
      .cacheControl(CacheControl.maxAge(0, TimeUnit.MILLISECONDS).mustRevalidate()
          .cachePrivate().cachePublic())
      .eTag(String.valueOf(gridFsFile.lastModified()))
      .lastModified(gridFsFile.lastModified())
      .contentType(MediaType.parseMediaType(gridFsFile.getContentType()))
      .body(gridFsFile);
  }
  
  private String removeFolders(String filename) {
    if (filename == null) {
      return "";
    }
    int index = filename.lastIndexOf('/');
    if (index > -1) {
      return filename.substring(index + 1, filename.length());
    } else {
      return filename;
    }
  }
}
