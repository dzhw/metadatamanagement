package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import javax.annotation.PostConstruct;

import org.javers.core.Javers;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.common.service.GenericDomainObjectVersionsService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;

/**
 * Service for retrieving previous {@link Survey} versions.
 * 
 * @author René Reitmann
 */
@Service
public class SurveyVersionsService
    extends GenericDomainObjectVersionsService<Survey, SurveyRepository> {
  /**
   * Construct the service.
   */
  public SurveyVersionsService(Javers javers, SurveyRepository surveyRepository,
      MetadataManagementProperties metadataManagementProperties) {
    super(Survey.class, javers, surveyRepository, metadataManagementProperties);
  }

  /**
   * Init Javers with all current dataPackages if there are no dataPackage commits in Javers yet.
   */
  @PostConstruct
  public void initJaversForDataPackages() {
    super.initJaversWithCurrentVersions();
  }
}
