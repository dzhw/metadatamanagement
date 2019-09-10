package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorDto;
import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorListDto;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowCopyQueueItem.Action;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowHidingNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowUnhidingNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectManagementService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyQueueItemService;
import lombok.RequiredArgsConstructor;

/**
 * REST resource for getting all shadow copies of a {@link DataAcquisitionProject}.
 * 
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataAcquisitionProjectShadowsResource {

  private final DataAcquisitionProjectManagementService dataAcquisitionProjectManagementService;

  private final ShadowCopyQueueItemService shadowCopyQueueItemService;

  /**
   * Get the previous 5 versions of the data acquisition project.
   * 
   * @param id The id of the project
   * @param pageable contains size and limit
   * 
   * @return A list of previous data acquisition project versions
   */
  @GetMapping("/data-acquisition-projects/{id}/shadows")
  public ResponseEntity<?> findShadows(@PathVariable String id, Pageable pageable) {
    Page<DataAcquisitionProject> page =
        dataAcquisitionProjectManagementService.findAllShadows(id, pageable);

    return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(page);
  }

  /**
   * Hide the shadow copies of the given project with the given version.
   * 
   * @param id The id of the project
   * @param version the version of the shadows to hide
   * 
   * @return ACCEPTED if the hiding task has been started
   * @throws ShadowHidingNotAllowedException Thrown if hiding the project version is not allowed.
   */
  @PostMapping("/data-acquisition-projects/{id}/shadows/{version}/hidden")
  public ResponseEntity<?> hideShadows(@PathVariable String id, @PathVariable String version)
      throws ShadowHidingNotAllowedException {
    String shadowId = id + "-" + version;
    Optional<DataAcquisitionProject> optional =
        dataAcquisitionProjectManagementService.read(shadowId);
    if (optional.isPresent()) {
      dataAcquisitionProjectManagementService.hideShadows(optional.get());
    } else {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.accepted().build();
  }

  /**
   * Unhide the shadow copies of the given project with the given version.
   * 
   * @param id The id of the project
   * @param version the version of the shadows to hide
   * 
   * @return ACCEPTED if the hiding task has been started
   * @throws ShadowUnhidingNotAllowedException Thrown if the given project version is already
   *         unhidden
   */
  @DeleteMapping("/data-acquisition-projects/{id}/shadows/{version}/hidden")
  public ResponseEntity<?> unhideShadows(@PathVariable String id, @PathVariable String version)
      throws ShadowUnhidingNotAllowedException {
    String shadowId = id + "-" + version;
    Optional<DataAcquisitionProject> optional =
        dataAcquisitionProjectManagementService.read(shadowId);
    if (optional.isPresent()) {
      dataAcquisitionProjectManagementService.unhideShadows(optional.get());
    } else {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.accepted().build();
  }

  /**
   * Get the current action which is currently performed for the given shadow.
   * 
   * @param id The master id of the project
   * @param version the version of the project
   * 
   * @return The current action, or empty response.
   */
  @GetMapping("/data-acquisition-projects/{id}/shadows/{version}/action")
  public ResponseEntity<Map<String, Action>> getCurrentAction(@PathVariable String id,
      @PathVariable String version) {
    Action action = shadowCopyQueueItemService.findCurrentAction(id, version);
    Map<String, Action> result = new HashMap<>();
    result.put("action", action);
    return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(result);
  }

  /**
   * Handle {@link ShadowHidingNotAllowedException} {@link ShadowUnhidingNotAllowedException} or
   * thrown by attempts to hide or unhide a project.
   */
  @ExceptionHandler({ShadowHidingNotAllowedException.class,
      ShadowUnhidingNotAllowedException.class})
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ErrorListDto handleShadowHidingExceptions(Exception ex) {
    ErrorDto errorDto = new ErrorDto(null, ex.getMessage(), null, null);
    ErrorListDto errorListDto = new ErrorListDto();
    errorListDto.add(errorDto);
    return errorListDto;
  }
}
