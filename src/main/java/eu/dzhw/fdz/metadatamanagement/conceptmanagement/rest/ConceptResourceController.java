package eu.dzhw.fdz.metadatamanagement.conceptmanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository.ConceptRepository;

/**
 * Concept REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
public class ConceptResourceController 
    extends GenericDomainObjectResourceController<Concept, 
    ConceptRepository> {

  @Autowired
  public ConceptResourceController(
      ConceptRepository conceptRepository) {
    super(conceptRepository);
  }

  /**
   * Override default get by id since it does not set cache headers correctly.
   * 
   * @param id a {@link Concept} id
   * @return the {@link Concept} or not found
   */
  @RequestMapping(method = RequestMethod.GET, value = "/concepts/{id:.+}")
  public ResponseEntity<Concept> findConcept(@PathVariable String id) {
    return super.findDomainObject(id);
  }
}
