package eu.dzhw.fdz.metadatamanagement.service.variablemanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.PageWithBuckets;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableDocumentRepository;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search.dto.VariableSearchFilter;

/**
 * A service for searching variables.
 * 
 * @author Amine Limouri
 * @author Daniel Katzberg
 */
@Service
public class VariableService {

  private final VariableDocumentRepository variableRepository;

  /**
   * @param variablesRepository A reference to the repositoy for the variables.
   */
  @Autowired
  public VariableService(VariableDocumentRepository variablesRepository) {
    this.variableRepository = variablesRepository;
  }

  /**
   * Search variables by query. If the query string does not contain text the first n variables are
   * returned as defined in the pageable.
   * 
   * @param variableSearchFormDto the data transfer object of the search. has all request parameter
   * @param pageable a pageable object.
   * 
   * @return Page with buckets from the filter of the VariableDocuments and Aggregations
   */
  public PageWithBuckets<VariableDocument> search(
      VariableSearchFilter variableSearchFormDto, Pageable pageable) {
    return variableRepository.search(variableSearchFormDto, pageable);

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

  /**
   * Saves a variable document to the repository.
   * 
   * @param variableDocument The variableDocument which should be save.
   * @return The saved VariableDocument
   * @see VariableDocument
   */
  public VariableDocument save(VariableDocument variableDocument) {
    return this.variableRepository.save(variableDocument);
  }

  /**
   * Deletes a variable document from the repository by a given id.
   * 
   * @param id The id of the variable document.
   */
  public void delete(String id) {
    this.variableRepository.delete(id);
  }

}
