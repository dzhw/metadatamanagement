package eu.dzhw.fdz.metadatamanagement.conceptmanagement.rest;

import java.net.URI;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriComponentsBuilder;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorDto;
import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorListDto;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.ConceptInUseException;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Concept REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
@Tag(name = "Concept Resource", description = "Endpoints used by the MDM to manage concepts.")
@RequestMapping("/api")
public class ConceptResourceController
    extends GenericDomainObjectResourceController<Concept, CrudService<Concept>> {

  public ConceptResourceController(CrudService<Concept> crudService,
      UserInformationProvider userInformationProvider) {
    super(crudService, userInformationProvider);
  }

  @Override
  @Operation(description = "Get the concept.")
  @GetMapping(value = "/concepts/{id:.+}")
  public ResponseEntity<Concept> getDomainObject(@PathVariable String id) {
    return super.getDomainObject(id);
  }


  @Override
  @PostMapping(value = "/concepts")
  public ResponseEntity<?> postDomainObject(@RequestBody Concept concept) {
    return super.postDomainObject(concept);
  }

  @Override
  @PutMapping(value = "/concepts/{id:.+}")
  public ResponseEntity<?> putDomainObject(@RequestBody Concept concept) {
    return super.putDomainObject(concept);
  }

  @Override
  @DeleteMapping("/concepts/{id:.+}")
  public ResponseEntity<?> deleteDomainObject(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(Concept domainObject) {
    return UriComponentsBuilder.fromPath("/api/concepts/" + domainObject.getId()).build().toUri();
  }

  @ExceptionHandler(ConceptInUseException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ErrorListDto handleConceptInUseException(ConceptInUseException exception) {
    ErrorListDto errorListDto = new ErrorListDto();

    if (!exception.getInstrumentIds().isEmpty()) {
      ErrorDto usedByInstruments =
          new ErrorDto(null, "concept-management.error." + "concept.in-use.instruments",
              exception.getInstrumentIds(), null);
      errorListDto.add(usedByInstruments);
    }

    if (!exception.getQuestionIds().isEmpty()) {
      ErrorDto usedByQuestions =
          new ErrorDto(null, "concept-management.error." + "concept.in-use.questions",
              exception.getQuestionIds(), null);
      errorListDto.add(usedByQuestions);
    }
    return errorListDto;
  }
}
