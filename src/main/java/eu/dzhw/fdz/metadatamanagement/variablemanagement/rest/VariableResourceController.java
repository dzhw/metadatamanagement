package eu.dzhw.fdz.metadatamanagement.variablemanagement.rest;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericShadowableDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;

/**
 * Variable REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
public class VariableResourceController 
    extends GenericShadowableDomainObjectResourceController<Variable, VariableRepository> {

  @Autowired
  public VariableResourceController(VariableRepository variableRepository,
                                    ApplicationEventPublisher applicationEventPublisher) {
    super(variableRepository, applicationEventPublisher);
  }

  /**
   * Override default get by id since it does not set cache headers correctly.
   * 
   * @param id a Variable id
   * @return the Variable or not found
   */
  @RequestMapping(method = RequestMethod.GET, value = "/variables/{id:.+}")
  public ResponseEntity<Variable> findVariable(@PathVariable String id) {
    return super.findDomainObject(id);
  }

  /**
   * Override default post to prevent clients from creating shadow copies.
   * @param variable Variable
   */
  @RequestMapping(method = RequestMethod.POST, value = "/variables")
  public ResponseEntity<?> postVariable(@RequestBody Variable variable) {
    return super.postDomainObject(variable);
  }

  /**
   * Override default put to prevent clients from creating shadow copies.
   * @param id Variable id
   * @param variable Variable
   */
  @RequestMapping(method = RequestMethod.PUT, value = "/variables/{id:.+}")
  public ResponseEntity<?> putVariable(@PathVariable String id, @RequestBody Variable variable) {
    return super.putDomainObject(id, variable);
  }

  /**
   * Override default delete to prevent clients from creating shadow copies.
   * @param id Variable id
   */
  @RequestMapping(method = RequestMethod.DELETE, value = "/variables/{id:.+}")
  public ResponseEntity<?> deleteVariable(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(Variable domainObject) {
    return null;
  }
}
