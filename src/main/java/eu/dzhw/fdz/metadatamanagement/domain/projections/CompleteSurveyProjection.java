package eu.dzhw.fdz.metadatamanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.domain.Period;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;

/**
 * Projection used to expose all attributes (including ids and versions), including sub resources.
 * Spring Data rest does not expose ids and version per default in the json.
 * 
 * @author René Reitmann
 */
@Projection(name = "complete", types = Survey.class)
public interface CompleteSurveyProjection extends AbstractFdzDomainObjectProjection {
  I18nString getTitle();

  Period getFieldPeriod();

  CompleteFdzProjectProjection getFdzProject();
}
