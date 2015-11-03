package eu.dzhw.fdz.metadatamanagement.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.repository.search.VariableSearchRepository;
import eu.dzhw.fdz.metadatamanagement.web.rest.util.HeaderUtil;
import eu.dzhw.fdz.metadatamanagement.web.rest.util.PaginationUtil;
import eu.dzhw.fdz.metadatamanagement.web.rest.dto.VariableDTO;
import eu.dzhw.fdz.metadatamanagement.web.rest.mapper.VariableMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Variable.
 */
@RestController
@RequestMapping("/api")
public class VariableResource {

    private final Logger log = LoggerFactory.getLogger(VariableResource.class);

    @Inject
    private VariableRepository variableRepository;

    @Inject
    private VariableMapper variableMapper;

    @Inject
    private VariableSearchRepository variableSearchRepository;

    /**
     * POST  /variables -> Create a new variable.
     */
    @RequestMapping(value = "/variables",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VariableDTO> createVariable(@Valid @RequestBody VariableDTO variableDTO) throws URISyntaxException {
        log.debug("REST request to save Variable : {}", variableDTO);
        if (variableDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new variable cannot already have an ID").body(null);
        }
        Variable variable = variableMapper.variableDTOToVariable(variableDTO);
        Variable result = variableRepository.save(variable);
        variableSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/variables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("variable", result.getId().toString()))
            .body(variableMapper.variableToVariableDTO(result));
    }

    /**
     * PUT  /variables -> Updates an existing variable.
     */
    @RequestMapping(value = "/variables",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VariableDTO> updateVariable(@Valid @RequestBody VariableDTO variableDTO) throws URISyntaxException {
        log.debug("REST request to update Variable : {}", variableDTO);
        if (variableDTO.getId() == null) {
            return createVariable(variableDTO);
        }
        Variable variable = variableMapper.variableDTOToVariable(variableDTO);
        Variable result = variableRepository.save(variable);
        variableSearchRepository.save(variable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("variable", variableDTO.getId().toString()))
            .body(variableMapper.variableToVariableDTO(result));
    }

    /**
     * GET  /variables -> get all the variables.
     */
    @RequestMapping(value = "/variables",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<VariableDTO>> getAllVariables(Pageable pageable)
        throws URISyntaxException {
        Page<Variable> page = variableRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/variables");
        return new ResponseEntity<>(page.getContent().stream()
            .map(variableMapper::variableToVariableDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /variables/:id -> get the "id" variable.
     */
    @RequestMapping(value = "/variables/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VariableDTO> getVariable(@PathVariable Long id) {
        log.debug("REST request to get Variable : {}", id);
        return Optional.ofNullable(variableRepository.findOne(id))
            .map(variableMapper::variableToVariableDTO)
            .map(variableDTO -> new ResponseEntity<>(
                variableDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /variables/:id -> delete the "id" variable.
     */
    @RequestMapping(value = "/variables/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVariable(@PathVariable Long id) {
        log.debug("REST request to delete Variable : {}", id);
        variableRepository.delete(id);
        variableSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("variable", id.toString())).build();
    }

    /**
     * SEARCH  /_search/variables/:query -> search for the variable corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/variables/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<VariableDTO> searchVariables(@PathVariable String query) {
        return StreamSupport
            .stream(variableSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(variableMapper::variableToVariableDTO)
            .collect(Collectors.toList());
    }
}
