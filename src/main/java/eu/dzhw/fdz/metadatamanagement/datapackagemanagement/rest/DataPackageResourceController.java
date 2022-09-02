package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.MetadataExportFormat;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.DaraOaiPmhClient;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.DataCiteClient;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataPackageSearchDocument;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * DataPackage REST Controller which overrides default spring data rest methods.
 *
 * @author René Reitmann
 */
@Controller
@Tag(name = "DataPackage Resource",
    description = "Endpoints used by the MDM to manage dataPackages.")
@Slf4j
public class DataPackageResourceController
    extends GenericDomainObjectResourceController<DataPackage, CrudService<DataPackage>> {

  private DaraOaiPmhClient oaiPmhClient;

  private DataCiteClient dataCiteClient;

  private Environment environment;

  /**
   * Construct the controller.
   */
  public DataPackageResourceController(
      CrudService<DataPackage> crudService,
      DaraOaiPmhClient oaiPmhClient,
      DataCiteClient dataCiteClient,
      Environment environment
  ) {
    super(crudService);
    this.oaiPmhClient = oaiPmhClient;
    this.dataCiteClient = dataCiteClient;
    this.environment = environment;
  }

  /**
   * See OpenAPI documentation below.
   */
  @Operation(
      summary = "Get the dataPackage in the given format. If no format is given it will return our"
          + " default MDM json. Public users will get the latest version "
          + "of the dataPackage. If the id is postfixed with the version number it will return"
          + " exactly the requested version, if available.")
  @GetMapping(value = "/api/data-packages/{id:.+}")
  public ResponseEntity<?> getDataPackage(@PathVariable String id,
      @RequestParam(required = false) MetadataExportFormat format) {
    ResponseEntity<DataPackage> dataPackageEntity = super.getDomainObject(id);
    if (dataPackageEntity.getStatusCode().equals(HttpStatus.OK) && format != null) {
      DataPackageSearchDocument dataPackageSearchDocument =
          (DataPackageSearchDocument) dataPackageEntity.getBody();
      if (StringUtils.isEmpty(dataPackageSearchDocument.getDoi())) {
        return ResponseEntity.notFound().build();
      }
      // hard code a DOI for all tests
      if (!environment.acceptsProfiles(Profiles.of(Constants.SPRING_PROFILE_PROD))) {
        dataPackageSearchDocument.setDoi("10.21249/DZHW:gra2005:2.0.1");
      }
      ResponseEntity<String> responseFromExportProvider;
      String extractedMetadata = null;
      if (MetadataExportFormat.DATACITE_FORMATS.contains(format)) {
        responseFromExportProvider =
            dataCiteClient.getMetadata(dataPackageSearchDocument.getDoi(), format);
      } else if (MetadataExportFormat.OAI_FORMATS.contains(format)) {
        responseFromExportProvider =
            oaiPmhClient.getMetadata(dataPackageSearchDocument.getDoi(), format);
        extractedMetadata = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + StringUtils
            .substringBetween(responseFromExportProvider.getBody(), "<metadata>", "</metadata>");
      } else {
        log.error("Unsupported data package export format: " + format);
        return ResponseEntity.notFound().build();
      }

      return ResponseEntity.status(responseFromExportProvider.getStatusCode())
          .cacheControl(CacheControl.noStore())
          .contentType(responseFromExportProvider.getHeaders().getContentType())
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "inline; filename=\"" + id.replace("$", "").replace(".", "_") + "_" + format.name()
                  + format.fileExtension + "\"")
          .body(
              extractedMetadata != null ? extractedMetadata : responseFromExportProvider.getBody());
    } else {
      return dataPackageEntity;
    }
  }

  @Override
  @PostMapping(value = "/api/data-packages")
  public ResponseEntity<?> postDomainObject(@RequestBody DataPackage dataPackage) {
    return super.postDomainObject(dataPackage);
  }

  @Override
  @PutMapping(value = "/api/data-packages/{id:.+}")
  public ResponseEntity<?> putDomainObject(@RequestBody DataPackage dataPackage) {
    return super.putDomainObject(dataPackage);
  }

  @Override
  @DeleteMapping("/api/data-packages/{id:.+}")
  public ResponseEntity<?> deleteDomainObject(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(DataPackage domainObject) {
    return UriComponentsBuilder.fromPath("/api/data-packages/" + domainObject.getId()).build()
        .toUri();
  }
}
