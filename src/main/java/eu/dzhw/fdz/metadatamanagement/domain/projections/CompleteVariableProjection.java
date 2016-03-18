package eu.dzhw.fdz.metadatamanagement.domain.projections;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.domain.GenerationDetails;
import eu.dzhw.fdz.metadatamanagement.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.domain.I18nSvg;
import eu.dzhw.fdz.metadatamanagement.domain.Statistics;
import eu.dzhw.fdz.metadatamanagement.domain.Value;
import eu.dzhw.fdz.metadatamanagement.domain.Variable;

/**
 * Projection used to expose all attributes (including ids and versions), including sub resources.
 * Spring Data rest does not expose ids and version per default in the json.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Projection(name = "complete", types = Variable.class)
public interface CompleteVariableProjection
    extends AbstractRdcDomainObjectWithProjectSurveyProjection {
  String getName();

  I18nString getDataType();

  I18nString getScaleLevel();

  I18nString getLabel();

  List<Value> getValues();

  I18nString getDescription();

  List<String> getAccessWays();

  String getFilterExpression();

  I18nString getFilterDescription();

  String getFilterExpressionLanguage();

  I18nSvg getDistributionSvg();

  List<String> getSameVariablesInPanel();

  Statistics getStatistics();

  GenerationDetails getGenerationDetails();

}
