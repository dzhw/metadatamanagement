package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.mailmanagement.service.MailService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraPidClientService.RegistrationException;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraPidClientService.RegistrationResponse;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.helper.DoiBuilder;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.Authority;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.AccessWays;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraPidClientService.*;
import static eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraPidClientService.VariableStatusType.*;

/**
 * A Service for registering PIDs for survey variables with da|ra.
 */
@Service
@Slf4j
@AllArgsConstructor
public class DaraPidRegistrationService {

  public static final String LANDING_PAGE_BASE_PATH = "/en/variables/";
  public static final String PID_PREFIX = "21.T11998/dzhw:";

  private final MetadataManagementProperties config;
  private final DoiBuilder doiBuilder;
  private final DataPackageRepository dataPackageRepository;
  private final VariableRepository variableRepository;
  private final DaraPidClientService daraPidClientService;
  private final MailService mailService;
  private final UserRepository userRepository;

  /**
   * Checks the given project for variables and whether they have been registered before.
   * @param project the data acquisition project to check
   * @return the status of variables and PID registrations
   */
  public VariablesCheckResult performVariablesCheckIn(DataAcquisitionProject project) {
    final var variables = this.variableRepository
      .findByDataAcquisitionProjectId(project.getId());
    return new VariablesCheckResult(
      !variables.isEmpty(),
      variables.parallelStream().anyMatch(v -> v.getPid() != null && !v.getPid().isBlank())
    );
  }

  /**
   * Performs the registration process asynchronous for all variables included in a data acquisition project.
   * @param project the data acquisition project
   * @param user the user that triggered the process by releasing a new project version
   */
  @Async
  public void register(DataAcquisitionProject project, User user) {

    final var dataPackages = this.dataPackageRepository.findByDataAcquisitionProjectId(project.getId());
    if (dataPackages.isEmpty()) {
      throw new RuntimeException("This project has no data package linked to it");
    } else if (dataPackages.size() > 1) {
      throw new RuntimeException("This project has more than one data package linked to it");
    }
    final var dataPackage = dataPackages.get(0);
    final var allVariables = this.variableRepository.findByDataAcquisitionProjectId(project.getId());
    final var variables = allVariables.subList(0, Math.min(10, allVariables.size())); // for testing purposes limit number of vars to 10 for now

    // not all projects have surveys and variables
    if (variables.isEmpty()) {
      log.debug("No variables found in data acquisition project '{}'", project.getId());
      return;
    }

    final var variableMetadata = variables.stream()
      .map(variable -> toVariableMetadata(project, dataPackage, variable))
      .toList();

    log.debug("Registering {} variable(s) for '{}'", variableMetadata.size(), dataPackage.getTitle().getEn());

    // cc'd error mail admin users
    final var admins = this.userRepository.findAllByAuthoritiesContaining(new Authority(AuthoritiesConstants.ADMIN));

    RegistrationResponse response;
    try {
      response = this.daraPidClientService.register(variableMetadata);
      log.debug("The registration process has been started with the following job ID: {}", response.jobId());
    } catch (RegistrationException e) {
      this.mailService.sendDaraPidRegistrationErrorEmail(user, admins, project, dataPackage, e);
      return;
    }

    // check job status and save PIDs if and
    // only if all registrations were successful
    final var startTime = System.currentTimeMillis();
    try {
      var result = PENDING;
      while (result == PENDING) {
        // fetch job status entries for variables
        VariableStatus[] entries;
        try {
          entries = this.daraPidClientService.getJobStatus(response.jobId());
        } catch (JobStatusException e) {
          this.mailService.sendDaraPidRegistrationErrorEmail(user, admins, project, dataPackage, e);
          return;
        }
        // check for pending registrations and continue -- even if some of them have failed already
        var pending = Arrays.stream(entries).filter(entry -> entry.status() == PENDING).findAny();
        if (pending.isPresent()) {
          final var now = System.currentTimeMillis();
          if (now - startTime > this.config.getDaraPid().getAbortAfterMinutesElapsed() * 60 * 1000) {
            this.mailService.sendDaraPidRegistrationErrorEmail(user, admins, project, dataPackage,
              new JobStatusException(String.format(
                "Registration of variables exceeded %d minutes. Aborting the process.",
                this.config.getDaraPid().getAbortAfterMinutesElapsed())));
            return;
          }
          Thread.sleep(this.config.getDaraPid().getStatusPollIntervalInSeconds() * 1000);
          continue;
        }
        // check for failed registrations and abort if any are found
        var failed = Arrays.stream(entries).filter(entry -> entry.status() == FAILED).toList();
        if (!failed.isEmpty()) {
          this.mailService.sendDaraPidRegistrationErrorEmail(user, admins, project, dataPackage,
            new RegistrationFailedException(failed));
          return;
        }
        // only entries with status FINISHED should be left at this point
        result = FINISHED;
      }
    } catch (InterruptedException e) {
      this.mailService.sendDaraPidRegistrationErrorEmail(user, admins, project, dataPackage,
        new JobStatusException("Polling the job status got interrupted", e));
      return;
    }

    // save PIDs in database
    for (var i = 0; i < variableMetadata.size(); i++) {
      variables.get(i).setPid(variableMetadata.get(i).pidProposal());
    }
    this.variableRepository.saveAll(variables);
    this.mailService.sendDaraPidRegistrationCompleteEmail(user, project, dataPackage);
  }

  /**
   * Maps variable datasets to the required metadata schema
   * @param project the data acquisition project the variables are linked to
   * @param dataPackage the data package the variables are linked to
   * @param variable the variable dataset being mapped
   * @return the mapped metadata object
   */
  VariableMetadata toVariableMetadata(DataAcquisitionProject project,
                                              DataPackage dataPackage,
                                              Variable variable)
  {
    final var doi = this.doiBuilder.buildDataOrAnalysisPackageDoiForDataCite(project.getId(), project.getRelease());
    final var varLabel = variable.getLabel().getEn() == null || variable.getLabel().getEn().isEmpty()
      ? variable.getLabel().getDe()
      : variable.getLabel().getEn();
    final var pidProposal = String.format("%s%s_%s:%s", PID_PREFIX,
      project.getId(),
      variable.getName(),
      project.getRelease().getVersion());
    final var landingPageUrl = String.format("%s%s%s?version=%s",
      this.config.getServer().getContextRoot(),
      LANDING_PAGE_BASE_PATH,
      variable.getMasterId().replace("$", ""),
      project.getRelease().getVersion());
    final var varTitle = String.format("%s: %s", variable.getName(), varLabel);
    final var creators = dataPackage.getProjectContributors().stream()
      .map(p -> new StudyCreator(p.getFirstName(), p.getMiddleName(), p.getLastName()))
      .toList();
    final var publicationDate = project.getRelease().getLastDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    return new VariableMetadata(
      doi,
      variable.getName(),
      varLabel,
      pidProposal,
      landingPageUrl,
      "Variable",
      varTitle,
      creators,
      "FDZ-DZHW",
      publicationDate,
      VariableAvailability.fromAccessWays(variable.getAccessWays())
    );
  }

  /**
   * The result type for checking the status of variables in a given data acquisition project.
   * @param hasVariables the project includes variables at all
   * @param hasRegistrations the variables have already been registered (found PIDs)
   */
  public record VariablesCheckResult(
    boolean hasVariables,
    boolean hasRegistrations
  ) {}

  /**
   * The metadata schema used for PID registration.
   */
  public record VariableMetadata(
    String studyDOI,
    String variableName,
    String variableLabel,
    String pidProposal,
    String landingPage,
    String resourceType,
    String title,
    List<StudyCreator> creators,
    String publisher,
    String publicationDate,
    VariableAvailability availability
  ) {}

  /**
   * The metadata schema used for the authors of a study.
   */
  @JsonInclude(NON_EMPTY)
  public record StudyCreator(
    String firstName,
    String middleName,
    String lastName
  ) {}

  /**
   * The types of availability for a variable used at da|ra (PID).
   * @see <a href="https://labs.da-ra.de/nfdi/api/swagger-ui/index.html#/Registration/verifyVariables">Metadata Verification Endpoint</a>
   */
  public enum VariableAvailability {

    DELIVERY("Delivery"),
    NOT_AVAILABLE("NotAvailable"),
    UNKNOWN("Unknown");

    @JsonValue
    public final String value;

    VariableAvailability(String value) {
      this.value = value;
    }

    public static VariableAvailability fromAccessWays(List<String> accessWays) {
      if (accessWays.contains(AccessWays.DOWNLOAD_CUF) ||
          accessWays.contains(AccessWays.DOWNLOAD_SUF) ||
          accessWays.contains(AccessWays.REMOTE_DESKTOP) ||
          accessWays.contains(AccessWays.ONSITE_SUF)
      ) {
        return DELIVERY;
      } else if (accessWays.contains(AccessWays.NOT_ACCESSIBLE)) {
        return NOT_AVAILABLE;
      } else {
        return UNKNOWN;
      }
    }
  }

  /**
   * This exception is thrown when at least one variable could not be registered with da|ra.
   */
  @Getter
  public static class RegistrationFailedException extends Exception implements DaraPidApiException {
    private final ArrayList<VariableStatus> entries;
    public RegistrationFailedException(List<VariableStatus> entries) {
      super("At least a subset of the variables could not be registered");
      this.entries = new ArrayList<>(entries);
    }
  }
}
