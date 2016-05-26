package eu.dzhw.fdz.metadatamanagement.variablemanagement.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.search.VariableSearchDao;

/**
 * Service for creating and updating variable. Used for updating variables in mongo and
 * elasticsearch.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Service
@RepositoryEventHandler
public class VariableService {

  @Inject
  private VariableRepository variableRepository;

  @Inject
  private VariableSearchDao variableSearchDao;

  /**
   * Load all variables from mongo and update the search indices.
   */
  public void reindexAllVariables() {
    Pageable pageable = new PageRequest(0, 500);
    Page<Variable> variables = variableRepository.findAll(pageable);

    while (variables.hasContent()) {
      variableSearchDao.index(variables.getContent());
      pageable = pageable.next();
      variables = variableRepository.findAll(pageable);
    }
  }

  /**
   * Delete all variables when the dataAcquisitionProject was deleted.
   * 
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteVariablesByProjectId(dataAcquisitionProject.getId());
  }
  
  /**
   * A service method for deletion of variables within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   * @return List of deleted variables
   */
  public List<Variable> deleteVariablesByProjectId(String dataAcquisitionProjectId) {
    List<Variable> deletedVariables =
        variableRepository.deleteByDataAcquisitionProjectId(dataAcquisitionProjectId);
    variableSearchDao.delete(deletedVariables);
    return deletedVariables;
  }
}
