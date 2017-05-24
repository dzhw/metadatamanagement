package eu.dzhw.fdz.metadatamanagement.filemanagement.config;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import eu.dzhw.fdz.metadatamanagement.filemanagement.util.MimeTypeDetector;

/**
 * Configure Apache Tika which we currently use for mime type detection of uploaded files.
 * 
 * @author René Reitmann
 */
@Configuration
public class TikaConfiguration {
  @Bean
  public Tika tika() {
    return new Tika();
  }
  
  @Bean
  public MimeTypeDetector mimeTypeDetector(Tika tika) {
    return new MimeTypeDetector(tika);
  }
}
