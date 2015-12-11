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

import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;
import eu.dzhw.fdz.metadatamanagement.service.FdzProjectService;
import eu.dzhw.fdz.metadatamanagement.web.rest.util.HeaderUtil;
import eu.dzhw.fdz.metadatamanagement.web.rest.util.PaginationUtil;

/**
 * REST controller for managing FdzProject.
 */
@RestController
@RequestMapping("/api")
public class FdzProjectResource {

  private final Logger log = LoggerFactory.getLogger(FdzProjectResource.class);

  @Inject
  private FdzProjectService fdzProjectService;

  /**
   * POST /fdzProjects -> Create a new fdzProject.
   */
  @RequestMapping(value = "/fdzProjects", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<FdzProject> createFdzProject(@Valid @RequestBody FdzProject fdzProject)
      throws URISyntaxException {
    log.debug("REST request to save FdzProject : {}", fdzProject);

    FdzProject result = fdzProjectService.createFdzProject(fdzProject);
    return ResponseEntity.created(new URI("/api/fdzProjects/" + result.getName()))
      .headers(HeaderUtil.createEntityCreationAlert("fdzProject", result.getName()))
      .body(result);
  }

  /**
   * PUT /fdzProjects -> Updates an existing fdzProject.
   */
  @RequestMapping(value = "/fdzProjects", method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<FdzProject> updateFdzProject(@Valid @RequestBody FdzProject fdzProject)
      throws URISyntaxException {
    log.debug("REST request to update FdzProject : {}", fdzProject);

    FdzProject result = fdzProjectService.updateFdzProject(fdzProject);
    return ResponseEntity.ok()
      .headers(HeaderUtil.createEntityUpdateAlert("fdzProject", fdzProject.getName()))
      .body(result);
  }

  /**
   * GET /fdzProjects -> get all the fdzProjects.
   */
  @RequestMapping(value = "/fdzProjects", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<List<FdzProject>> getAllFdzProjects(Pageable pageable,
      Optional<String> getAll) throws URISyntaxException {
    log.debug("REST request to get a page of FdzProjects");

    if (getAll.isPresent()) {
      List<FdzProject> allFdzProjects = fdzProjectService.findAll();
      return Optional.ofNullable(allFdzProjects)
        .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    } else {
      Page<FdzProject> page = fdzProjectService.findAll(pageable);
      HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fdzProjects");
      return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
  }

  /**
   * GET /fdzProjects/:name -> get the "name" fdzProject.
   */
  @RequestMapping(value = "/fdzProjects/{name}", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<FdzProject> getFdzProject(@PathVariable String name) {
    log.debug("REST request to get FdzProject : {}", name);
    FdzProject fdzProject = fdzProjectService.findOne(name);
    return Optional.ofNullable(fdzProject)
      .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * DELETE /fdzProjects/:name -> delete the "name" fdzProject.
   */
  @RequestMapping(value = "/fdzProjects/{name}", method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Void> deleteFdzProject(@PathVariable String name) {
    log.debug("REST request to delete FdzProject : {}", name);
    fdzProjectService.delete(name);
    return ResponseEntity.ok()
      .headers(HeaderUtil.createEntityDeletionAlert("fdzProject", name))
      .build();
  }
}
