package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * The 'referenced' Projection of a variable domain object.
 * 'referenced' means only some attributes will be
 * displayed.
 */
@Projection(name = "referenced", types = Variable.class)
public interface ReferencedVariableProjection {
  String getId();
  
  String getName();

  I18nString getLabel();
}
