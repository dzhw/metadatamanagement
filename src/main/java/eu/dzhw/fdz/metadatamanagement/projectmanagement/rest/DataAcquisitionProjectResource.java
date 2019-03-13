package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyCreateNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyDeleteNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyUpdateNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.rest.GenericShadowableDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation.ValidDataAcquisitionProjectSave;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * If a data acquisition project has been released before, it can not be deleted by anyone.
 * 
 * @author Daniel Katzberg
 */
@RepositoryRestController
@Slf4j
@Validated
public class DataAcquisitionProjectResource extends
    GenericShadowableDomainObjectResourceController<DataAcquisitionProject,
            DataAcquisitionProjectRepository> {

  private DataAcquisitionProjectService dataAcquisitionProjectService;

  @Autowired
  public DataAcquisitionProjectResource(DataAcquisitionProjectRepository projectRepository,
                                        DataAcquisitionProjectService service,
                                        ApplicationEventPublisher applicationEventPublisher) {
    super(projectRepository, applicationEventPublisher);
    this.dataAcquisitionProjectService = service;
  }

  /**
   * Override default put to validate authorization and append configuration data.
   */
  @RequestMapping(method = RequestMethod.PUT, value = "/data-acquisition-projects/{id:.+}")
  @Secured(value = {AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.PUBLISHER,
      AuthoritiesConstants.ADMIN})
  public ResponseEntity<?> saveProject(@PathVariable String id,
      @RequestBody @ValidDataAcquisitionProjectSave @Valid DataAcquisitionProject newDataProject) {

    Optional<DataAcquisitionProject> dataAcquisitionProject = repository.findById(id);
    DataAcquisitionProject projectToSave;

    if (dataAcquisitionProject.isPresent()) {
      projectToSave = dataAcquisitionProject.get();
      if (projectToSave.isShadow()) {
        throw new ShadowCopyUpdateNotAllowedException();
      }
    } else if (newDataProject.isShadow()) {
      throw new ShadowCopyCreateNotAllowedException();
    }

    projectToSave = dataAcquisitionProject.orElseGet(DataAcquisitionProject::new);

    BeanUtils.copyProperties(newDataProject, projectToSave, "version");
    DataAcquisitionProject savedProject =
        dataAcquisitionProjectService.saveDataAcquisitionProject(projectToSave);

    if (dataAcquisitionProject.isPresent()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.created(buildLocationHeaderUri(savedProject)).body(savedProject);
    }
  }

  /**
   * Override default post to prevent clients from creating shadow copies.
   */
  @RequestMapping(method = RequestMethod.POST, value = "/data-acquisition-projects")
  @Secured(value = {AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.PUBLISHER,
      AuthoritiesConstants.ADMIN})
  public ResponseEntity<?> postDataAcquisitionProject(@Valid @RequestBody DataAcquisitionProject
                                                          dataAcquisitionProject) {
    if (dataAcquisitionProject.isShadow()) {
      throw new ShadowCopyCreateNotAllowedException();
    }

    DataAcquisitionProject savedProject = dataAcquisitionProjectService
        .saveDataAcquisitionProject(dataAcquisitionProject);

    return ResponseEntity.created(buildLocationHeaderUri(savedProject)).build();
  }

  /**
   * Override default get by id since it does not set cache headers correctly.
   * 
   * @param id a {@link DataAcquisitionProject} id
   * @return the {@link DataAcquisitionProject} or not found
   */
  @RequestMapping(method = RequestMethod.GET, value = "/data-acquisition-projects/{id:.+}")
  public ResponseEntity<DataAcquisitionProject> findProject(@PathVariable String id) {
    Optional<DataAcquisitionProject> project =
        dataAcquisitionProjectService.findDataAcquisitionProjectById(id);

    return project.map(this::wrapInResponseEntity)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Overwriting the delete data acquisition project api method from mongo db.
   * 
   * @param id The id of the data acquisition project.
   * @return Return a 200 (ok) if successful deleted or a Bad Request, if it has been released
   *         before and deleting is forbidden.
   */
  @RequestMapping(value = "/data-acquisition-projects/{id:.+}", method = RequestMethod.DELETE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.ADMIN})
  public ResponseEntity<?> deleteDataAcquisitionProject(@PathVariable String id) {

    // load project
    DataAcquisitionProject dataAcquisitionProject = super.repository.findById(id).orElse(null);

    // project could not be found
    if (dataAcquisitionProject == null) {
      log.warn("Project could not be found and deleted!");
      return ResponseEntity.badRequest().build();
    } else if (dataAcquisitionProject.isShadow()) {
      throw new ShadowCopyDeleteNotAllowedException();
    }

    // Check project, if it has been released before
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
    List<DataAcquisitionProject> projects =
        dataAcquisitionProjectService.findByIdLikeOrderByIdAsc(id);
    return ResponseEntity.ok(projects);
  }

  @Override
  protected URI buildLocationHeaderUri(DataAcquisitionProject domainObject) {
    return UriComponentsBuilder.fromPath("/api/data-acquisition-projects/"
        + domainObject.getId()).build().toUri();
  }
}
