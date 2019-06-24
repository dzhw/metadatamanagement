package eu.dzhw.fdz.metadatamanagement.conceptmanagement.rest;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorDto;
import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorListDto;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.ConceptInUseException;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository.ConceptRepository;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.service.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Concept REST Controller which overrides default spring data rest methods.
 *
 * @author René Reitmann
 */
@RepositoryRestController
public class ConceptResourceController
    extends GenericDomainObjectResourceController<Concept, ConceptRepository> {

  private final ConceptService conceptService;

  @Autowired
  public ConceptResourceController(ConceptRepository conceptRepository,
                                   ConceptService conceptService) {
    super(conceptRepository);
    this.conceptService = conceptService;
  }

  /**
   * Override default get by id since it does not set cache headers correctly.
   *
   * @param id a {@link Concept} id
   * @return the {@link Concept} or not found
   */
  @RequestMapping(method = RequestMethod.GET, value = "/concepts/{id:.+}")
  public ResponseEntity<Concept> findConcept(@PathVariable String id) {
    return super.findDomainObject(id);
  }

  @DeleteMapping(path = "/concepts/{id:.+}")
  public ResponseEntity<Void> deleteConcept(@PathVariable String id) {
    conceptService.deleteConcept(id);
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(ConceptInUseException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorListDto handleConceptInUseException(ConceptInUseException exception) {
    ErrorListDto errorListDto = new ErrorListDto();

    if (!exception.getInstrumentIds().isEmpty()) {
      ErrorDto usedByInstruments = new ErrorDto(null, "concept-management.error."
          + "concept.in-use.instruments", exception.getInstrumentIds(), null);
      errorListDto.add(usedByInstruments);
    }

    if (!exception.getQuestionIds().isEmpty()) {
      ErrorDto usedByQuestions = new ErrorDto(null, "concept-management.error."
          + "concept.in-use.questions", exception.getQuestionIds(), null);
      errorListDto.add(usedByQuestions);
    }
    return errorListDto;
  }
}
