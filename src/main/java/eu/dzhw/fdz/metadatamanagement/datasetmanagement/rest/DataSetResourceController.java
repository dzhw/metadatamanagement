package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * DataSet REST Controller which overrides default spring data rest methods.
 *
 * @author René Reitmann
 */
@Controller
@Tag(name = "Dataset Resource", description = "Endpoints used by the MDM to manage datasets.")
public class DataSetResourceController
    extends GenericDomainObjectResourceController<DataSet, CrudService<DataSet>> {

  public DataSetResourceController(CrudService<DataSet> crudService) {
    super(crudService);
  }

  @Override
  @Operation(
      summary = "Get the dataset. Public users will get the latest version of the dataset."
          + " If the id is postfixed with the version number it will return exactly the "
          + "requested version, if available.")
  @GetMapping(value = "/api/data-sets/{id:.+}")
  @ResponseBody
  public ResponseEntity<DataSet> getDomainObject(@PathVariable String id) {
    return super.getDomainObject(id);
  }


  @Override
  @PostMapping(value = "/api/data-sets")
  public ResponseEntity<?> postDomainObject(@RequestBody DataSet dataSet) {
    return super.postDomainObject(dataSet);
  }

  @Override
  @PutMapping(value = "/api/data-sets/{id:.+}")
  public ResponseEntity<?> putDomainObject(@RequestBody DataSet dataSet) {
    return super.putDomainObject(dataSet);
  }

  @Override
  @DeleteMapping("/api/data-sets/{id:.+}")
  public ResponseEntity<?> deleteDomainObject(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(DataSet domainObject) {
    return UriComponentsBuilder.fromPath("/api/data-sets/" + domainObject.getId()).build().toUri();
  }
}
