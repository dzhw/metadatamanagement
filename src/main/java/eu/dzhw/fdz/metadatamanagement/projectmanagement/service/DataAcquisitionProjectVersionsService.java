package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.javers.core.Javers;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.springframework.stereotype.Service;

import com.github.zafarkhaja.semver.Version;
import com.querydsl.core.types.Predicate;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.common.service.GenericDomainObjectVersionsService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.QDataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;

/**
 * Service responsible for retrieving an initializing the data acquisition project history.
 *
 * @author Daniel Katzberg
 *
 */
@Service
public class DataAcquisitionProjectVersionsService extends
    GenericDomainObjectVersionsService<DataAcquisitionProject, DataAcquisitionProjectRepository> {

  private final UserInformationProvider userInformationProvider;

  private static final Predicate projectNotHidden =
      QDataAcquisitionProject.dataAcquisitionProject.hidden.isNull()
          .or(QDataAcquisitionProject.dataAcquisitionProject.hidden.isFalse());

  /**
   * Construct the service.
   */
  public DataAcquisitionProjectVersionsService(Javers javers,
      DataAcquisitionProjectRepository dataAcquisitionProjectRepository,
      MetadataManagementProperties metadataManagementProperties,
      UserInformationProvider userInformationProvider) {
    super(DataAcquisitionProject.class, javers, dataAcquisitionProjectRepository,
        metadataManagementProperties);
    this.userInformationProvider = userInformationProvider;
  }

  /**
   * Init Javers with all current projects if there are no project commits in Javers yet.
   */
  @PostConstruct
  public void initJaversForDataAcquisitionProjects() {
    super.initJaversWithCurrentVersions();
  }

  /**
   * Get the last saved release for the given project id.
   *
   * @param id the id of the data acquisition project.
   * @return the last saved release or null
   */
  public Release findLastRelease(String id) {
    return findPreviousRelease(id, null);
  }

  /**
   * Get the previous release of a data acquisition project. The release before currentRelease.
   *
   * @param id the id of the data acquisition project.
   * @param currentRelease get the release saved before this release, if null will return the
   *        current release
   * @return the previous release or null
   */
  public Release findPreviousRelease(String id, Release currentRelease) {
    try (Stream<Shadow<Release>> shadows = javers.findShadowsAndStream(
        QueryBuilder.byValueObjectId(id, DataAcquisitionProject.class, "release")
          .withChangedProperty("version")
          .withChangedProperty("isPreRelease")
          .build());) {
      if (currentRelease == null) {
        Optional<Shadow<Release>> optional = shadows.findFirst();
        if (optional.isPresent()) {
          return optional.get().get();
        } else {
          return null;
        }
      } else {
        final AtomicReference<Release> previousRelease = new AtomicReference<>();
        shadows.takeWhile(shadow -> previousRelease.get() == null).forEach(shadow -> {
          Release release = shadow.get();
          if (Version.valueOf(currentRelease.getVersion())
              .greaterThan(Version.valueOf(release.getVersion()))) {
            previousRelease.set(release);
          }
        });
        return previousRelease.get();
      }
    }
  }

  /**
   * Find all release stamps (limited to 100 results) for the given project id.
   *
   * @param id the project id
   * @param noBeta boolean indicating if beta release shall be skipped or not
   * @return List of all releases (max 100 entries).
   */
  public List<Release> findAllReleases(String id, boolean noBeta, boolean onlyNotHiddenVersions) {
    // Find all version changes
    List<Shadow<Release>> shadows =
        javers.findShadows(QueryBuilder.byValueObjectId(id, DataAcquisitionProject.class, "release")
            .withChangedProperty("version").limit(100).build());

    if (shadows.isEmpty()) {
      return new ArrayList<>();
    } else {
      if (onlyNotHiddenVersions) {
        // return only not-hidden versions (for all users)
        return shadows.stream().map(shadow -> shadow.get())
          .filter(release -> checkNotHidden(id, release) && (!noBeta || Version
            .valueOf(release.getVersion()).greaterThanOrEqualTo(Version.valueOf("1.0.0"))))
          .collect(Collectors.toList());
      }

      // return all hidden and not-hidden versions for authenticated users and
      // only not-hidden versions for public users
      return shadows.stream().map(shadow -> shadow.get())
        .filter(release -> isAvailable(id, release) && (!noBeta || Version
          .valueOf(release.getVersion()).greaterThanOrEqualTo(Version.valueOf("1.0.0"))))
        .collect(Collectors.toList());

    }
  }

  /**
   * Check if the released shadow has not been hidden for public users.
   *
   * @param id masterId of the project
   * @param release the release containing the version of the shadow
   * @return false if the shadow is hidden and the current user is a public user
   */
  private boolean isAvailable(String id, Release release) {
    if (userInformationProvider.isUserAnonymous()) {
      return super.repository.exists(QDataAcquisitionProject.dataAcquisitionProject.id
          .eq(id + "-" + release.getVersion()).and(projectNotHidden));
    }
    return true;
  }

  /**
   * Check if the released shadow is hidden. Only return not hidden shadows.
   *
   * @param id masterId of the project
   * @param release the release containing the version of the shadow
   * @return false if the shadow is hidden
   */
  private boolean checkNotHidden(String id, Release release) {
    return super.repository.exists(QDataAcquisitionProject.dataAcquisitionProject.id
      .eq(id + "-" + release.getVersion()).and(projectNotHidden));
  }
}
