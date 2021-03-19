package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

import java.net.URI;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * DataPackage REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
@Tag(name = "DataPackage Resource",
    description = "Endpoints used by the MDM to manage dataPackages.")
public class DataPackageResourceController
    extends GenericDomainObjectResourceController<DataPackage, CrudService<DataPackage>> {

  public DataPackageResourceController(CrudService<DataPackage> crudService,
      UserInformationProvider userInformationProvider) {
    super(crudService, userInformationProvider);
  }

  @Override
  @Operation(
      summary = "Get the dataPackage. Public users will get the latest version of the dataPackage."
          + " If the id is postfixed with the version number it will return exactly the "
          + "requested version, if available.")
  @GetMapping(value = "/data-packages/{id:.+}")
  @ResponseBody
  public ResponseEntity<DataPackage> getDomainObject(@PathVariable String id) {
    return super.getDomainObject(id);
  }

  @Override
  @PostMapping(value = "/data-packages")
  public ResponseEntity<?> postDomainObject(@RequestBody DataPackage dataPackage) {
    return super.postDomainObject(dataPackage);
  }

  @Override
  @PutMapping(value = "/data-packages/{id:.+}")
  public ResponseEntity<?> putDomainObject(@RequestBody DataPackage dataPackage) {
    return super.putDomainObject(dataPackage);
  }

  @Override
  @DeleteMapping("/data-packages/{id:.+}")
  public ResponseEntity<?> deleteDomainObject(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(DataPackage domainObject) {
    return UriComponentsBuilder.fromPath("/api/data-packages/" + domainObject.getId()).build()
        .toUri();
  }
}
