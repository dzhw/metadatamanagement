package eu.dzhw.fdz.metadatamanagement.variablemanagement.service;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyHelper;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.service.helper.VariableShadowCopyDataSource;

/**
 * Service which generates shadow copies of all variables of a project, when the project has been
 * released.
 * 
 * @author René Reitmann
 */
@Service
public class VariableShadowCopyService extends ShadowCopyHelper<Variable> {
  public VariableShadowCopyService(VariableShadowCopyDataSource variableShadowCopyDataSource) {
    super(variableShadowCopyDataSource);
  }
}
