package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

import java.nio.charset.Charset;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.MetadataExportFormat;

/**
 * Client for retrieving metadata of our data packages from DataCite.
 * 
 * @author René Reitmann
 */
@Service
public class DataCiteClient {
  private RestTemplate restTemplate;

  /**
   * Initialize the RestTemplate.
   */
  public DataCiteClient() {
    restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    restTemplate.getMessageConverters().add(0,
        new StringHttpMessageConverter(Charset.forName("UTF-8")));
  }

  /**
   * Get the metadata for the given DOI in the given format.
   * 
   * @param doi A valid DOI of on of our data packages.
   * @param format One of {@link MetadataExportFormat} powered by DataCite.
   * @return The json or xml depending on the desired format.
   */
  public ResponseEntity<String> getMetadata(String doi, MetadataExportFormat format) {
    return restTemplate.getForEntity("https://api.datacite.org/application/{format}/{doi}",
        String.class, format.urlFormat, doi);
  }
}
