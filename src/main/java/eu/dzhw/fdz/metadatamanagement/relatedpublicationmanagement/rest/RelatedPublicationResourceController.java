package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;

/**
 * RelatedPublication REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
public class RelatedPublicationResourceController 
    extends GenericDomainObjectResourceController<RelatedPublication, 
    RelatedPublicationRepository> {

  @Autowired
  public RelatedPublicationResourceController(
      RelatedPublicationRepository relatedPublicationRepository) {
    super(relatedPublicationRepository);
  }

  /**
   * Override default get by id since it does not set cache headers correctly.
   * 
   * @param id a RelatedPublication id
   * @return the RelatedPublication or not found
   */
  @RequestMapping(method = RequestMethod.GET, value = "/related-publications/{id:.+}")
  @Timed
  public ResponseEntity<RelatedPublication> findRelatedPublication(@PathVariable String id) {
    return super.findDomainObject(id);
  }
}
