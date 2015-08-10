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
   * Show variable search page.
   * 
   * @return variableSearch.html
   */
  @Autowired
  public VariableService(VariableRepository variablesRepository) {
    this.variableRepository = variablesRepository;
  }

  /**
   * Show variable search page.
   * 
   * @return variableSearch.html
   */
  public Page<VariableDocument> search(String query, Pageable pageable) {
    if (!StringUtils.isEmpty(query)) {
      return variableRepository.searchAllFields(query, pageable);
    }
    return variableRepository.searchAllFields("name", pageable);
  }

  /**
   * Show variable search page.
   * 
   * @return variableSearch.html
   */
  public Page<VariableDocument> searchPhrasePrefix(String query, Pageable pageable) {
    if (!StringUtils.isEmpty(query)) {
      return variableRepository.searchPhrasePrefix(query, pageable);
    }
    return variableRepository.searchPhrasePrefix("name", pageable);
  }

  public VariableDocument get(String id) {
    return variableRepository.findOne(id);
  }
}
