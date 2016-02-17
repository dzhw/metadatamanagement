package eu.dzhw.fdz.metadatamanagement.domain.projections;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.domain.Value;
import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.DataType;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.ScaleLevel;

/**
 * Projection used to expose all attributes (including ids and versions), including sub resources.
 * Spring Data rest does not expose ids and version per default in the json.
 * 
 * @author René Reitmann
 */
@Projection(name = "complete", types = Variable.class)
public interface CompleteVariableProjection extends AbstractRdcDomainObjectProjection {
  String getName();

  DataType getDataType();

  ScaleLevel getScaleLevel();

  String getLabel();

  String getFdzProjectId();

  String getSurveyId();

  List<Value> getValues();
}
