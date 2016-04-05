package eu.dzhw.fdz.metadatamanagement.domain.projections;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.domain.Period;
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

  String getQuestionnaireId();
  
  List<String> getDataSetIds();
}
