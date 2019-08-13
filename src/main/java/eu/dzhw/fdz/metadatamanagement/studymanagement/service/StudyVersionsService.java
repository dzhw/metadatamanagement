package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import javax.annotation.PostConstruct;

import org.javers.core.Javers;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.common.service.GenericDomainObjectVersionsService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;

/**
 * Service responsible for retrieving an initializing the study history.
 * 
 * @author René Reitmann
 */
@Service
public class StudyVersionsService 
    extends GenericDomainObjectVersionsService<Study, StudyRepository> {
  /**
   * Construct the service.
   */
  public StudyVersionsService(Javers javers, StudyRepository studyRepository,
      MetadataManagementProperties metadataManagementProperties) {
    super(Study.class, javers, studyRepository, metadataManagementProperties);
  }

  /**
   * Init Javers with all current studies if there are no study commits in Javers yet.
   */
  @PostConstruct
  public void initJaversForStudies() {
    super.initJaversWithCurrentVersions();
  }
}
