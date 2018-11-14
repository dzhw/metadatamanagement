package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;


import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Used for searching {@link DataAcquisitionProject} items.
 */
@RestController
@RequestMapping("/api/data-acquisition-projects/search")
public class DataAcquisitionProjectRestController {

  private final DataAcquisitionProjectService dataAcquisitionProjectService;

  /**
   * Creates a new controller.
   */
  public DataAcquisitionProjectRestController(DataAcquisitionProjectService service) {
    this.dataAcquisitionProjectService = service;
  }

  /**
   * Find projects by (partial) id.
   */
  @GetMapping("/findByIdLikeOrderByIdAsc")
  public ResponseEntity<List<DataAcquisitionProject>> findByIdLikeOrderByIdAsc(
      @RequestParam("id") String id) {
    List<DataAcquisitionProject> projects = dataAcquisitionProjectService
        .findDataAcquisitionProjectListById(id);
    return ResponseEntity.ok(projects);
  }
}
