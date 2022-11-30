package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation.ValidDataAcquisitionProjectSave;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectManagementService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;

/**
 * Variable REST Controller which overrides default spring data rest methods.
 *
 * @author René Reitmann
 */
@RepositoryRestController
@Validated
public class DataAcquisitionProjectResourceController extends
    GenericDomainObjectResourceController
    <DataAcquisitionProject, CrudService<DataAcquisitionProject>> {

  private final DataAcquisitionProjectManagementService projectManagementService;

  public DataAcquisitionProjectResourceController(
      DataAcquisitionProjectManagementService projectManagementService,
      UserInformationProvider userInformationProvider) {
    super(projectManagementService, userInformationProvider);
    this.projectManagementService = projectManagementService;
  }

  /**
   * Project saving currently uses a special validator therefore we cannot directly override
   * {@link GenericDomainObjectResourceController#putDomainObject(DataAcquisitionProject)}.
   *
   * @param project The {@link DataAcquisitionProject} to be created.
   * @return The saved {@link DataAcquisitionProject}.
   */
  @PutMapping("/data-acquisition-projects/{id:.+}")
  public ResponseEntity<?> putProject(
      @ValidDataAcquisitionProjectSave @RequestBody DataAcquisitionProject project) {
    return super.putDomainObject(project);
  }

  /**
   * Project saving currently uses a special validator therefore we cannot directly override
   * {@link GenericDomainObjectResourceController#postDomainObject(DataAcquisitionProject)}.
   *
   * @param project The {@link DataAcquisitionProject} to be created.
   * @return The created {@link DataAcquisitionProject}.
   */
  @PostMapping("/data-acquisition-projects")
  public ResponseEntity<?> postProject(@RequestBody DataAcquisitionProject project) {
    return super.postDomainObject(project);
  }


  @Override
  @GetMapping("/data-acquisition-projects/{id:.+}")
  public ResponseEntity<DataAcquisitionProject> getDomainObject(@PathVariable String id) {
    return super.getDomainObject(id);
  }

  @Override
  @DeleteMapping("/data-acquisition-projects/{id:.+}")
  public ResponseEntity<?> deleteDomainObject(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  /**
   * Find projects by (partial) id.
   */
  @GetMapping("/data-acquisition-projects/search/findByIdLikeOrderByIdAsc")
  public ResponseEntity<Map<String,Object>> findByIdLikeOrderByIdAsc(
    @RequestParam(value = "id", required = false, defaultValue = "") String id,
    @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
    @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {

    Pageable pageable = PageRequest.of(page,size);
    Page<DataAcquisitionProject> pageProjects = projectManagementService.findByIdLikeOrderByIdAsc(id, pageable);
    List<DataAcquisitionProject> projects = pageProjects.getContent();

    Map<String, Object> response = new HashMap<>();
    Map<String, Object> pageMap = new HashMap<>();

    pageMap.put("size", pageProjects.getSize());
    pageMap.put("totalElements", pageProjects.getTotalElements());  // TODO: wrong value?
    pageMap.put("totalPages", pageProjects.getTotalPages());
    pageMap.put("number", pageProjects.getNumber());

    response.put("dataAcquisitionProjects", projects);
    response.put("page", pageMap);

    return ResponseEntity.ok(response);
  }

  @Override
  protected URI buildLocationHeaderUri(DataAcquisitionProject domainObject) {
    return UriComponentsBuilder.fromPath("/api/data-acquisition-projects/" + domainObject.getId())
        .build().toUri();
  }
}
