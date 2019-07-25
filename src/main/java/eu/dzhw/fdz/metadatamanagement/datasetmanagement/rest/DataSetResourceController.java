package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;

/**
 * DataSet REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
public class DataSetResourceController extends GenericDomainObjectResourceController
    <DataSet, CrudService<DataSet>> {

  @Autowired
  public DataSetResourceController(CrudService<DataSet> crudService) {
    super(crudService);
  }

  @Override
  @GetMapping(value = "/data-sets/{id:.+}")
  public ResponseEntity<DataSet> getDomainObject(@PathVariable String id) {
    return super.getDomainObject(id);
  }

 
  @Override
  @PostMapping(value = "/data-sets")
  public ResponseEntity<?> postDomainObject(@RequestBody DataSet DataSet) {
    return super.postDomainObject(DataSet);
  }

  @Override
  @PutMapping(value = "/data-sets/{id:.+}")
  public ResponseEntity<?> putDomainObject(@RequestBody DataSet DataSet) {
    return super.putDomainObject(DataSet);
  }

  @Override
  @DeleteMapping("/data-sets/{id:.+}")
  public ResponseEntity<?> deleteDomainObject(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(DataSet domainObject) {
    return UriComponentsBuilder.fromPath("/api/data-sets/" + domainObject.getId()).build().toUri();
  }
}
