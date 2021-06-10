package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain;

import java.util.Set;

/**
 * Formats to which our metadata can be exported. The actual mapping is either powered by
 * <a href="https://www.da-ra.de/oaip/">da|ra's OAI-PMH service</a> or
 * <a href="https://commons.datacite.org/doi.org/10.21249/dzhw:gra2005:1.0.0">data cite</a>.
 *
 * @author René Reitmann
 */
public enum MetadataExportFormat {
  oai_dc("oai_dc", ".xml"), dara("dara", ".xml"), oai_ddi31("oai_ddi31", ".xml"), oai_ddi32(
      "oai_ddi32", ".xml"), oai_dara("oai_dara", ".xml"), mds("mds", ".xml"), data_cite_xml(
          "vnd.datacite.datacite+xml", ".xml"), data_cite_json("vnd.datacite.datacite+json",
              ".json"), schema_org_json_ld("vnd.schemaorg.ld+json", ".json");

  /**
   * Export formats powered by <a href="https://www.da-ra.de/oaip/">da|ra's OAI-PMH service</a>.
   */
  public static final Set<MetadataExportFormat> OAI_FORMATS =
      Set.of(oai_dc, dara, oai_ddi31, oai_ddi32, oai_dara, mds);

  /**
   * Export formats powered by
   * <a href="https://commons.datacite.org/doi.org/10.21249/dzhw:gra2005:1.0.0">data cite</a>.
   */
  public static final Set<MetadataExportFormat> DATACITE_FORMATS =
      Set.of(data_cite_xml, data_cite_json, schema_org_json_ld);

  /**
   * The format as it can be used in URLs.
   */
  public final String urlFormat;

  /**
   * The file extension for the downloaded format (xml or json).
   */
  public final String fileExtension;

  /**
   * Construct the enum.
   *
   * @param urlFormat The format as it can be used in URLs.
   * @param fileExtension The file extension for the downloaded format (xml or json).
   */
  private MetadataExportFormat(String urlFormat, String fileExtension) {
    this.urlFormat = urlFormat;
    this.fileExtension = fileExtension;
  }
}
