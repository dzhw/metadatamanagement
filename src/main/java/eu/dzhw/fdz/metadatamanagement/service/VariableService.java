package eu.dzhw.fdz.metadatamanagement.service;

import javax.inject.Inject;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.service.exception.EntityExistsException;
import eu.dzhw.fdz.metadatamanagement.service.exception.EntityNotFoundException;

/**
 * Service for creating and updating variable. Used for updating variables in mongo and
 * elasticsearch.
 * 
 * @author René Reitmann
 */
@Service
public class VariableService {

  //private final Logger log = LoggerFactory.getLogger(VariableService.class);

  @Inject
  private VariableRepository variableRepository;

  /**
   * Create a variable in mongo and elasticsearch.
   * 
   * @param variable The variable to create.
   * @return The created variable.
   * @throws EntityExistsException if there is already a variable with the given id
   */
  public Variable createVariable(Variable variable) throws EntityExistsException {
    // TODO write to VariableSearchRepository
    try {
      return variableRepository.insert(variable);      
    } catch (DuplicateKeyException e) {
      throw new EntityExistsException(Variable.class, variable.getId(), e);
    }
  }

  /**
   * Update an existing variable in mongo and elasticsearch.
   * 
   * @param variable the variable to update.
   * @return The updated variable.
   * @throws EntityNotFoundException if there is no variable with the given id
   */
  public Variable updateVariable(Variable variable) throws EntityNotFoundException {
    if (!variableRepository.exists(variable.getId())) {
      throw new EntityNotFoundException(Variable.class, variable.getId());
    }
    // TODO write to VariableSearchRepository
    return variableRepository.save(variable);
  }

  public Page<Variable> getAllVariables(Pageable pageable) {
    return variableRepository.findAll(pageable);
  }

  public Variable getVariable(String id) {
    return variableRepository.findOne(id);
  }

  public void deleteVariable(String id) {
    // TODO delete from VariableSearchRepository
    variableRepository.delete(id);
  }
}
