package eu.dzhw.fdz.metadatamanagement.service.common;

/**
 * This interface defines the basic methods for the service.
 * 
 * @param <D> The class of the document
 * 
 * @author Daniel Katzberg
 *
 */
public interface BasicService<D> {


  /**
   * Load a document by id.
   * 
   * @param id the id for the document.
   * 
   * @return A document of the document class {@code <D>}
   */
  D get(String id);


  /**
   * Saves a document of the class {@code <D>} to the repository.
   * 
   * @param document The document of the class {@code <D>} which should be save.
   * @return The saved VariableDocument
   */
  D save(D document);

  /**
   * Deletes a document of the class {@code <D>} from the repository by a given id.
   * 
   * @param id The id of the document.
   */
  void delete(String id);

}
