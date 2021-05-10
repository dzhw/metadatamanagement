package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

import java.nio.charset.Charset;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Charsets;

import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.MetadataExportFormat;

/**
 * Client for retrieving metadata of our data packages from the OAI PMH implementation at da|ra.
 * 
 * @author René Reitmann
 */
@Service
public class DaraOaiPmhClient {

  private RestTemplate restTemplate;

  /**
   * Initialize the RestTemplate.
   */
  public DaraOaiPmhClient() {
    restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    restTemplate.getMessageConverters().add(0,
        new StringHttpMessageConverter(Charset.forName("UTF-8")));
  }

  /**
   * Get the metadata for the given OAI-PMH Identifier in the given format.
   * 
   * @param oaiIdentifier A valid OAI-PMH Identifier.
   * @param format One of {@link MetadataExportFormat} powered by da|ra.
   * @return The json or xml depending on the desired format.
   */
  private ResponseEntity<String> getMetadataForOaiIdentifer(String oaiIdentifier,
      MetadataExportFormat format) {
    return restTemplate.getForEntity(
        "https://www.da-ra.de/oaip/oai?verb=GetRecord&metadataPrefix={format}&identifier={oaiIdentifier}",
        String.class, format.urlFormat, oaiIdentifier);
  }

  /**
   * Get the metadata for the given DOI in the given format.
   * 
   * @param doi A valid DOI of on of our data packages.
   * @param format One of {@link MetadataExportFormat} powered by da|ra.
   * @return The json or xml depending on the desired format.
   */
  public ResponseEntity<String> getMetadata(String doi, MetadataExportFormat format) {
    String oaiIdentifier = getOaiIdentifier(doi);
    if (StringUtils.isEmpty(oaiIdentifier)) {
      return ResponseEntity.notFound().build();
    }
    return getMetadataForOaiIdentifer(oaiIdentifier, format);
  }

  /**
   * Retrieve the OAI-PMH Identifier for the given DOI.
   * 
   * @param doi The doi of one of our data packages for instance.
   * @return The corresponding OAI-PMH identifier
   */
  private String getOaiIdentifier(String doi) {
    String solrQuery = "fq=doi:\"" + doi + "\"";
    String base64Query =
        Base64.getUrlEncoder().withoutPadding().encodeToString(solrQuery.getBytes(Charsets.UTF_8));
    String responseXml = restTemplate.getForObject(
        "https://www.da-ra.de/oaip/oai?verb=ListIdentifiers&metadataPrefix=oai_dc&set=~{base64Query}",
        String.class, base64Query);
    return StringUtils.substringBetween(responseXml, "<identifier>", "</identifier>");
  }
}
