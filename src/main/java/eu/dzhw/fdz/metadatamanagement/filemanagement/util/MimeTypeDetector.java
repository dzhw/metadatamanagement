package eu.dzhw.fdz.metadatamanagement.filemanagement.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

/**
 * Detect the mimetype of files using Apache Tika.
 * 
 * @author René Reitmann
 */
public class MimeTypeDetector {
  
  private Tika tika;
  
  public MimeTypeDetector(Tika tika) {
    this.tika = tika;
  }

  /**
   * Detect the mime type of a {@link MultipartFile}.
   * @param multipartFile An uploaded File.
   * @return the mime type of the file
   * @throws IOException if the uploaded file cannot be read
   */
  public String detect(MultipartFile multipartFile) throws IOException {
    try (InputStream is = multipartFile.getInputStream()) {
      return tika.detect(is);
    }
  }
}
