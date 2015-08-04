package eu.dzhw.fdz.metadatamanagement.service.variablemanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableRepository;

/**
 * A service for searching variables.
 * 
 * @author Amine Limouri
 */
@Service
public class VariableService {

  private final VariableRepository variableRepository;

  @Autowired
  public VariableService(VariableRepository variablesRepository) {
    this.variableRepository = variablesRepository;
  }

  public Page<VariableDocument> search(String query, Pageable pageable) {
    return variableRepository.searchAllFields(query, pageable);
  }
}
