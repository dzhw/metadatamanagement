package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraPidClientService.RegistrationException;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraPidRegistrationService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraPidRegistrationService.VariablesCheckResult;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataCiteMetadataException;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataCiteService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorDto;
import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorListDto;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowCopyDoiRegistrationNotAllowed;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;

/**
 * A Resource Class for handling releasing and un-releasing of data acquisition projects to DataCite (previously Dara).
 * This Resource can register a DOI to DataCite, updates information of a DataCite doi or set to not
 * available if a project will be unreleased.
 *
 * @author Daniel Katzberg
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DaraReleaseResource {

  private final MetadataManagementProperties config;
  private final DaraPidRegistrationService daraPidRegistrationService;
  private final DataAcquisitionProjectRepository projectRepository;
  private final UserRepository userRepository;
  private final DataCiteService dataCiteService;

  /**
   * Release a project to dara (or update it).
   * @throws TemplateException
   * @throws IOException
   */
  /**
   * Release (or update) a project to DataCite.
   * @param id the project id
   * @param project the project dataset
   * @param registerVars whether variable PIDs should be registered or not
   * @param authentication the authentification
   * @return the response from DataCite
   * @throws IOException IO Exception for the XML Freemarker Process.
   * @throws TemplateException Template Errors of the XML Freemarker Process.
   * @throws RegistrationException When errors occurr while registering variable PIDs.
   * @throws DataCiteMetadataException When metadata could not be compiled.
   */
  @RequestMapping(value = "/data-acquisition-projects/{id}/release",
      method = RequestMethod.POST)
  @Secured(value = {AuthoritiesConstants.PUBLISHER})
  public ResponseEntity<?> release(@PathVariable String id,
                                   @RequestBody @Valid DataAcquisitionProject project,
                                   @RequestParam(required = false, defaultValue = "false") boolean registerVars,
                                   Authentication authentication
  ) throws IOException, TemplateException, RegistrationException, DataCiteMetadataException {
    if (project.isShadow()) {
      throw new ShadowCopyDoiRegistrationNotAllowed();
    }
    if (this.config.getDaraPid().isEnabled() && registerVars) {
      // only attempt registration if bridge is enabled and the
      // user checked the relevant box on the release dialog
      final var user = this.userRepository.findOneByLogin(authentication.getName())
        .orElseThrow(() -> new RuntimeException("Unable to find user with login name: " + authentication.getName()));
      this.daraPidRegistrationService.register(project, user);
    }
    HttpStatus dataCiteStatus = this.dataCiteService.registerOrUpdateProjectToDataCite(project);
    return ResponseEntity.status(dataCiteStatus).build();
  }

  /**
   * Update all (non-shadow and non beta-release) projects in DataCite.
   * @return List of errors that occurred during update. Empty list if no errors occurred.
   */
  @RequestMapping(value = "/data-acquisition-projects/update-dara",
      method = RequestMethod.PUT)
  @Secured(value = {AuthoritiesConstants.ADMIN})
  public List<String> updateAllProjectsAtDataCite() {
    List<String> templateExceptionMessages = new ArrayList<>();
    for (DataAcquisitionProject project : this.projectRepository.findAllByShadowIsFalse()) {
      if (project.getRelease() != null
          && !project.getRelease().getVersion().startsWith("0")) { // ignore beta releases (0.X.X)
        try {
          this.dataCiteService.registerOrUpdateProjectToDataCite(project);
        } catch (Exception e) {
          templateExceptionMessages.add("Error in project: " + project.getId() + "\n" + e.getMessage());
        }
      }
    }
    return templateExceptionMessages;
  }

  /**
   * Pre-Release a project to DataCite (or update it).
   * @param id the project id
   * @param project the project dataset
   * @return the response from DataCite
   * @throws DataCiteMetadataException when metadata could not be compiled
   */
  @RequestMapping(value = "/data-acquisition-projects/{id}/pre-release",
      method = RequestMethod.POST)
  @Secured(value = {AuthoritiesConstants.PUBLISHER})
  public ResponseEntity<?> preRelease(@PathVariable String id,
      @RequestBody @Valid DataAcquisitionProject project) throws DataCiteMetadataException {
    if (project.isShadow()) {
      throw new ShadowCopyDoiRegistrationNotAllowed();
    }
    HttpStatus dataCiteStatus = this.dataCiteService.registerOrUpdateProjectToDataCite(project);
    return ResponseEntity.status(dataCiteStatus).build();
  }

  /**
   * Checks a data acquisition project for variables and their registration status.
   * @param id the data acquisition project's ID
   * @return the status of the variables
   */
  @GetMapping(path = "/data-acquisition-projects/{id}/variables-check")
  @Secured(value = {AuthoritiesConstants.ADMIN, AuthoritiesConstants.PUBLISHER})
  VariablesCheckResult performVariablesCheck(@PathVariable String id) {
    var project = this.projectRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Unable to find data acquisition project with ID: " + id));
    return this.daraPidRegistrationService.performVariablesCheckIn(project);
  }

  /**
   * Registers PIDs for variables linked to the given data acquisition project.
   * @param id the data acqusition project ID
   * @param project the data acquisition project
   * @param authentication the security context user object
   */
  @PostMapping(path = "/data-acquisition-projects/{id}/variables-register")
  @Secured(value = {AuthoritiesConstants.ADMIN})
  void startVariablesRegistration(@PathVariable String id,
                                  @RequestBody @Valid DataAcquisitionProject project,
                                  Authentication authentication
  ) {
    var user = this.userRepository.findOneByLogin(authentication.getName())
      .orElseThrow(() -> new RuntimeException("Unable to find user with login: " + authentication.getName()));
    this.daraPidRegistrationService.register(project, user);
  }

  @ExceptionHandler(ShadowCopyDoiRegistrationNotAllowed.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ErrorListDto handleShadowCopyReleaseToDaraNotAllowed() {
    ErrorDto errorDto = new ErrorDto(null, "project-management.error."
        + "shadow-copy-release-to-dara-not-allowed", null, null);
    ErrorListDto errorListDto = new ErrorListDto();
    errorListDto.add(errorDto);
    return errorListDto;
  }
}
