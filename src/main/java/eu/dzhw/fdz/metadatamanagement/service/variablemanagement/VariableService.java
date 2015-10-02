package eu.dzhw.fdz.metadatamanagement.service.variablemanagement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.data.common.aggregations.PageWithBuckets;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableDocumentRepository;
import eu.dzhw.fdz.metadatamanagement.service.common.SearchService;
import eu.dzhw.fdz.metadatamanagement.web.common.dtos.AbstractSearchFilter;

/**
 * A service for searching variables. The Service annotation is given by the interface.
 * 
 * @author Amine Limouri
 * @author Daniel Katzberg
 */
@Service
public class VariableService implements SearchService<VariableDocument> {

  private final VariableDocumentRepository variableRepository;

  /**
   * @param variablesRepository A reference to the repositoy for the variables.
   */
  @Autowired
  public VariableService(VariableDocumentRepository variablesRepository) {
    this.variableRepository = variablesRepository;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.service.common.SearchServiceInterface#search(eu.dzhw.fdz.
   * metadatamanagement.web.common.dtos.AbstractSearchFilter,
   * org.springframework.data.domain.Pageable)
   */
  @Override
  public PageWithBuckets<VariableDocument> search(AbstractSearchFilter searchFilter,
      Pageable pageable) {
    return variableRepository.search(searchFilter, pageable);
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

  /**
   * Suggest search terms for the last word of the query string.
   * 
   * @param query the query string as given by the user containing multiple words
   * @return A list of suggested terms (the first words remain constant)
   */
  public List<String> suggest(String query) {
    String[] words = query.split(" ");
    // suggest terms for the last word
    List<String> suggestedTags = this.variableRepository.suggest(words[words.length - 1]);
    if (words.length > 1) {
      // re-append the first words to the suggestions
      List<String> suggestedTagsWithPrefix = new ArrayList<>(suggestedTags.size());
      StringBuilder prefixBuilder = new StringBuilder();
      for (int i = 0; i < words.length - 1; i++) {
        prefixBuilder.append(words[i]);
        prefixBuilder.append(' ');
      }
      String prefix = prefixBuilder.toString();
      for (String suggestion : suggestedTags) {
        suggestedTagsWithPrefix.add(prefix + suggestion);
      }
      return suggestedTagsWithPrefix;
    } else {
      // return the suggested terms
      return suggestedTags;
    }
  }

}
