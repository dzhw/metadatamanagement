package eu.dzhw.fdz.metadatamanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.web.rest.util.HeaderUtil;
import eu.dzhw.fdz.metadatamanagement.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Variable.
 */
@RestController
@RequestMapping("/api")
public class VariableResource {

  private final Logger log = LoggerFactory.getLogger(VariableResource.class);

  @Inject
  private VariableRepository variableRepository;

  /**
   * POST /variables -> Create a new variable.
   */
  @RequestMapping(value = "/variables", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Variable> createVariable(@Valid @RequestBody Variable variable)
      throws URISyntaxException {
    log.debug("REST request to save Variable : {}", variable);
    Variable result = variableRepository.save(variable);
    return ResponseEntity.created(new URI("/api/variables/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert("variable", result.getId()))
        .body(result);
  }

  /**
   * PUT /variables -> Updates an existing variable.
   */
  @RequestMapping(value = "/variables", method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Variable> updateVariable(@Valid @RequestBody Variable variable)
      throws URISyntaxException {
    log.debug("REST request to update Variable : {}", variable);
    if (variable.getId() == null) {
      return createVariable(variable);
    }
    Variable result = variableRepository.save(variable);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert("variable", variable.getId()))
        .body(result);
  }

  /**
   * GET /variables -> get all the variables.
   */
  @RequestMapping(value = "/variables", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<List<Variable>> getAllVariables(Pageable pageable)
      throws URISyntaxException {
    Page<Variable> page = variableRepository.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/variables");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET /variables/:id -> get the "id" variable.
   */
  @RequestMapping(value = "/variables/{id}", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Variable> getVariable(@PathVariable String id) {
    log.debug("REST request to get Variable : {}", id);
    return Optional.ofNullable(variableRepository.findOne(id))
        .map(variable -> new ResponseEntity<>(variable, HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * DELETE /variables/:id -> delete the "id" variable.
   */
  @RequestMapping(value = "/variables/{id}", method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Void> deleteVariable(@PathVariable String id) {
    log.debug("REST request to delete Variable : {}", id);
    variableRepository.delete(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert("variable", id)).build();
  }
}
