package eu.dzhw.fdz.metadatamanagement.web.rest;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for uploading a file into the mongo db.
 * 
 * @author Daniel Katzberg
 *
 */
@Controller
@RequestMapping("/api")
public class FileResource {

  /**
   * Yeah. TODO DKatzberg
   * 
   * @param file Yeah. TODO DKatzberg
   * @param rcdId Yeah. TODO DKatzberg
   * @throws IOException Yeah. TODO DKatzberg
   */
  @RequestMapping(value = "/files/upload")
  public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
      @RequestParam("rdcId") String rcdId) throws IOException {
    byte[] bytes;
    if (!file.isEmpty()) {
      bytes = file.getBytes();
      // store file in storage
      System.out.println("File: " + new String(bytes, "UTF-8"));
    }
    System.out.println(String.format("receive %s from %s", file.getOriginalFilename(), rcdId));
    return ResponseEntity.ok()
      .build();
  }

}
