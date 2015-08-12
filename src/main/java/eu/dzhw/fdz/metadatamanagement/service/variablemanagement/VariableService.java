package eu.dzhw.fdz.metadatamanagement.service.variablemanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

  /**
   * A constructor.
   */
  @Autowired
  public VariableService(VariableRepository variablesRepository) {
    this.variableRepository = variablesRepository;
  }

  /**
   * Search variables by query. If the query string does not contain text the first n variables are
   * returned as defined in the pageable.
   * 
   * @param query the query for the search in name field.
   * @param pageable a pageable object.
   * 
   * @return Page of VariableDocuments
   */
  public Page<VariableDocument> search(String query, Pageable pageable) {
    if (StringUtils.hasText(query)) {
      return variableRepository.multiMatchQueryOnAllStringFields(query, pageable);
    }
    return variableRepository.findAll(pageable);
  }

  /**
   * Load variable by id.
   * 
   * @param id the id for the document.
   * 
   * @return VariableDocument
   */
  public VariableDocument get(String id) {
    return variableRepository.findOne(id);
  }
}
