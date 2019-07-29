package eu.dzhw.fdz.metadatamanagement.variablemanagement.rest;

import java.net.URI;

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
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Variable REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
public class VariableResourceController extends GenericDomainObjectResourceController
    <Variable, CrudService<Variable>> {

  public VariableResourceController(CrudService<Variable> crudService) {
    super(crudService);
  }

  @Override
  @GetMapping(value = "/variables/{id:.+}")
  public ResponseEntity<Variable> getDomainObject(@PathVariable String id) {
    return super.getDomainObject(id);
  }

 
  @Override
  @PostMapping(value = "/variables")
  public ResponseEntity<?> postDomainObject(@RequestBody Variable variable) {
    return super.postDomainObject(variable);
  }

  @Override
  @PutMapping(value = "/variables/{id:.+}")
  public ResponseEntity<?> putDomainObject(@RequestBody Variable domainObject) {
    return super.putDomainObject(domainObject);
  }

  @Override
  @DeleteMapping("/variables/{id:.+}")
  public ResponseEntity<?> deleteDomainObject(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(Variable domainObject) {
    return UriComponentsBuilder.fromPath("/api/variables/" + domainObject.getId()).build().toUri();
  }
}
