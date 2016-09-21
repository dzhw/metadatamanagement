package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Period;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.AbstractRdcDomainObjectProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;

/**
 * Projection used to expose all attributes (including ids and versions), including sub resources.
 * Spring Data rest does not expose ids and version per default in the json.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Projection(name = "complete", types = Survey.class)
public interface CompleteSurveyProjection extends AbstractRdcDomainObjectProjection {
  I18nString getTitle();

  Period getFieldPeriod();

  String getDataAcquisitionProjectId();
  
  I18nString getPopulation();
  
  I18nString getSample();
  
  I18nString getSurveyMethod();

}
