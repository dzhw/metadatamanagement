package eu.dzhw.fdz.metadatamanagement.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.search.VariableSearchDao;
import eu.dzhw.fdz.metadatamanagement.search.document.VariableSearchDocument;
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

  // private final Logger log = LoggerFactory.getLogger(VariableService.class);

  @Inject
  private VariableRepository variableRepository;

  @Inject
  private VariableSearchDao variableSearchDao;

  /**
   * Create a variable in mongo and elasticsearch.
   * 
   * @param variable The variable to create.
   * @return The created variable.
   * @throws EntityExistsException if there is already a variable with the given id
   */
  public Variable createVariable(Variable variable) {
    try {
      variable = variableRepository.insert(variable);
    } catch (DuplicateKeyException e) {
      String message = e.getMessage();
      
      //Double ID
      if(message.contains("$_id_")) {      
        throw new EntityExistsException(Variable.class, variable.getId());
      }  
      
      //Double Compound index of name/fdzProjectName
      if(message.contains("$name_1_fdz_project_name_1")) {
        String[] fields = {variable.getName(), variable.getFdzProjectName()};
        throw new EntityExistsException(Variable.class, fields);
      }
      
    }
    updateSearchIndices(variable);
    return variable;
  }

  private void updateSearchIndices(Variable variable) {
    for (String index : ElasticsearchAdminService.INDICES) {
      //TODO create different search documents for different languages
      variableSearchDao.save(new VariableSearchDocument(variable), index);
    }
  }

  /**
   * Update an existing variable in mongo and elasticsearch.
   * 
   * @param variable the variable to update.
   * @return The updated variable.
   * @throws EntityNotFoundException if there is no variable with the given id
   */
  public Variable updateVariable(Variable variable) {
    if (!variableRepository.exists(variable.getId())) {
      throw new EntityNotFoundException(Variable.class, variable.getId());
    }
    variable = variableRepository.save(variable);
    updateSearchIndices(variable);
    return variable;
  }

  public Page<Variable> getAllVariables(Pageable pageable) {
    return variableRepository.findAll(pageable);
  }

  public Variable getVariable(String id) {
    return variableRepository.findOne(id);
  }

  /**
   * Delete the variable with the given id.
   * 
   * @param id The id of the variable to delete
   */
  public void deleteVariable(String id) {
    variableRepository.delete(id);
    for (String index : ElasticsearchAdminService.INDICES) {
      variableSearchDao.delete(id, index);
    }
  }

  /**
   * Load all variables from mongo and update the search indices.
   */
  public void reindexAllVariables() {
    Pageable pageable = new PageRequest(0, 50);
    Page<Variable> variables = variableRepository.findAll(pageable);

    while (variables.hasContent()) {
      List<VariableSearchDocument> searchDocuments = new ArrayList<>(50);
      for (Variable variable : variables) {
        //TODO create different search documents for different languages
        searchDocuments.add(new VariableSearchDocument(variable));
      }
      for (String index : ElasticsearchAdminService.INDICES) {      
        variableSearchDao.save(searchDocuments, index);
      }
      variables = variableRepository.findAll(pageable.next());
    }
  }
}
