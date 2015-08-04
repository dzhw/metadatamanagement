package eu.dzhw.fdz.metadatamanagement.service.variablemanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariablesRepository;

/**
 * A service for searching variables.
 * 
 * @author Amine Limouri
 */
@Service
public class VariableService {

  private final VariablesRepository variablesRepository;

  @Autowired
  public VariableService(VariablesRepository variablesRepository) {
    this.variablesRepository = variablesRepository;
  }

  public Page<VariableDocument> search(String query, Pageable pageable) {
    return variablesRepository.searchAllFields(query, pageable);
  }


}
