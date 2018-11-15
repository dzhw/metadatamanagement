package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * If a data acquisition project has been released before, it can not be deleted by anyone.
 * @author Daniel Katzberg
 *
 */
@RepositoryRestController
@Slf4j
public class DataAcquisitionProjectResource 
    extends GenericDomainObjectResourceController<DataAcquisitionProject, 
    DataAcquisitionProjectRepository> {

  private DataAcquisitionProjectService dataAcquisitionProjectService;

  @Autowired
  public DataAcquisitionProjectResource(DataAcquisitionProjectRepository projectRepository,
      DataAcquisitionProjectService dataAcquisitionProjectService) {
    super(projectRepository);
    this.dataAcquisitionProjectService = dataAcquisitionProjectService;
  }

  /**
   * Override default get by id since it does not set cache headers correctly.
   * 
   * @param id a {@link DataAcquisitionProject} id
   * @return the {@link DataAcquisitionProject} or not found
   */
  @RequestMapping(method = RequestMethod.GET, value = "/data-acquisition-projects/{id:.+}")
  public ResponseEntity<DataAcquisitionProject> findProject(@PathVariable String id) {
    Optional<DataAcquisitionProject> project = dataAcquisitionProjectService
      .findDataAcquisitionProjectById(id);

    return project.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Overwriting the delete data acquisition proect api method from mongo db.
   * @param id The id of the data acquisition project.
   * @return Return a 200 (ok) if successfull deleted or a Bad Request,
   *     if it has been released before and deleting is forbidden.
   */
  @RequestMapping(value = "/data-acquisition-projects/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteDataAcquisitionProject(@PathVariable String id) {
    
    //load project
    DataAcquisitionProject dataAcquisitionProject = super.repository
        .findById(id).orElse(null);
    
    //project could not be found
    if (dataAcquisitionProject == null) {
      log.warn("Project could not be found and deleted!");
      return ResponseEntity.badRequest().build();
    }
    
    //Check project, if it has been released before
    if (this.dataAcquisitionProjectService.deleteDataAcquisitionProject(dataAcquisitionProject)) {
      log.info("Project has not been released before. Project is deleted.");
      return ResponseEntity.ok().build(); 
    } else {
      log.warn("Project has been released before. It is forbidden to delete it!");
      return ResponseEntity.badRequest().build();      
    }   
  }

  /**
   * Find projects by (partial) id.
   */
  @GetMapping("/data-acquisition-projects/search/findByIdLikeOrderByIdAsc")
  public ResponseEntity<List<DataAcquisitionProject>> findByIdLikeOrderByIdAsc(
    @RequestParam(value = "id", required = false, defaultValue = "") String id) {
    List<DataAcquisitionProject> projects = dataAcquisitionProjectService
      .findDataAcquisitionProjectListById(id);
    return ResponseEntity.ok(projects);
  }
}
