package eu.dzhw.fdz.metadatamanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;
import eu.dzhw.fdz.metadatamanagement.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.domain.Period;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;

/**
 * Interface used in projections to expose all attributes. Id and version are handled differently
 * per default in spring data rest!
 * 
 * @author René Reitmann
 */
@Projection(name = "complete", types = Survey.class)
public interface CompleteSurveyProjection extends AbstractFdzDomainObjectProjection {

  I18nString getTitle();

  Period getFieldPeriod();

  FdzProject getFdzProject();
  
  String getFdzId();
}
