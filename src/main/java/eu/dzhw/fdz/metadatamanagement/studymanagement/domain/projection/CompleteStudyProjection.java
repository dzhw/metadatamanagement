package eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.AbstractRdcDomainObjectProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;

/**
 * Projection used to expose all attributes (including ids and versions), including sub resources.
 * Spring Data rest does not expose ids and version per default in the json.
 * 
 * @author René Reitmann
 */
@Projection(name = "complete", types = Study.class)
public interface CompleteStudyProjection extends AbstractRdcDomainObjectProjection {
  String getDataAcquisitionProjectId();
  
  I18nString getTitle();
  
  I18nString getDescription();
  
  I18nString getInstitution();
  
  I18nString getStudySeries();
  
  I18nString getSponsor();
  
  I18nString getAuthors();
  
  I18nString getDataAvailability();
  
  I18nString getSurveyDesign();
  
  I18nString getAnnotations();
}
