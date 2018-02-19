package eu.dzhw.fdz.metadatamanagement.variablemanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

/**
 * Variable REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
public class VariableResourceController 
    extends GenericDomainObjectResourceController<Variable, VariableRepository> {

  @Autowired
  public VariableResourceController(VariableRepository variableRepository) {
    super(variableRepository);
  }

  /**
   * Override default get by id since it does not set cache headers correctly.
   * 
   * @param id a Variable id
   * @return the Variable or not found
   */
  @RequestMapping(method = RequestMethod.GET, value = "/variables/{id:.+}")
  @Timed
  public ResponseEntity<Variable> findVariable(@PathVariable String id) {
    return super.findDomainObject(id);
  }
}
