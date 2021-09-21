package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.MetadataExportFormat;

public class DataCiteClientTest extends AbstractTest {
  @Autowired
  private DataCiteClient dataCiteClient;

  @Test
  public void testSuccessfullMetadataRetrieval() {
    String metadata = dataCiteClient
        .getMetadata("10.21249/DZHW:gra2005:2.0.1", MetadataExportFormat.data_cite_xml).getBody();
    String title = StringUtils.substringBetween(metadata, "<title xml:lang=\"de\">", "</title>");

    assertEquals("DZHW-Absolventenpanel 2005", title);;
  }
}
