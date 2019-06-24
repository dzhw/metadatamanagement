package eu.dzhw.fdz.metadatamanagement.conceptmanagement.service;

import javax.annotation.PostConstruct;

import org.javers.core.Javers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.common.service.GenericDomainObjectVersionsService;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository.ConceptRepository;

/**
 * Service responsible for retrieving an initializing the concept history.
 * 
 * @author René Reitmann
 */
@Service
public class ConceptVersionsService 
    extends GenericDomainObjectVersionsService<Concept, ConceptRepository> {
  /**
   * Construct the service.
   */
  @Autowired
  public ConceptVersionsService(Javers javers, ConceptRepository conceptRepository,
      MetadataManagementProperties metadataManagementProperties) {
    super(Concept.class, javers, conceptRepository, metadataManagementProperties);
  }

  /**
   * Init Javers with all current concepts if there are no concept commits in Javers yet.
   */
  @PostConstruct
  public void initJaversForStudies() {
    super.initJaversWithCurrentVersions();
  }
}
