package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.javers.core.Javers;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.springframework.stereotype.Service;

import com.github.zafarkhaja.semver.Version;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.common.service.GenericDomainObjectVersionsService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;

/**
 * Service responsible for retrieving an initializing the data acquisition project history.
 *
 * @author Daniel Katzberg
 *
 */
@Service
public class DataAcquisitionProjectVersionsService extends
    GenericDomainObjectVersionsService<DataAcquisitionProject, DataAcquisitionProjectRepository> {
  /**
   * Construct the service.
   */
  public DataAcquisitionProjectVersionsService(Javers javers,
      DataAcquisitionProjectRepository dataAcquisitionProjectRepository,
      MetadataManagementProperties metadataManagementProperties) {
    super(DataAcquisitionProject.class, javers, dataAcquisitionProjectRepository,
        metadataManagementProperties);
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
   * @param currentRelease get the release saved before this release
   * @return the previous release or null
   */
  public Release findPreviousRelease(String id, Release currentRelease) {
    // Find last changes
    List<Shadow<Release>> shadows =
        javers.findShadows(QueryBuilder.byValueObjectId(id, DataAcquisitionProject.class, "release")
            .withChangedProperty("version").limit(2).build());

    if (shadows.isEmpty()) {
      return null;
    } else {
      if (currentRelease == null) {
        return shadows.get(0).get();
      }
      for (Shadow<Release> shadow : shadows) {
        if (!shadow.get().getVersion().equals(currentRelease.getVersion())) {
          return shadow.get();
        }
      }
      return null;
    }
  }

  /**
   * Find all release stamps (limited to 100 results) for the given project id.
   *
   * @param id the project id
   * @return List of all releases (max 100 entries).
   */
  public List<Release> findAllReleases(String id, Boolean noBeta) {
    // Find all version changes
    List<Shadow<Release>> shadows =
        javers.findShadows(QueryBuilder.byValueObjectId(id, DataAcquisitionProject.class, "release")
            .withChangedProperty("version").limit(100).build());

    if (shadows.isEmpty()) {
      return new ArrayList<>();
    } else {
      return shadows
          .stream().map(shadow -> shadow.get()).filter(release -> !noBeta || Version
              .valueOf(release.getVersion()).greaterThanOrEqualTo(Version.valueOf("1.0.0")))
          .collect(Collectors.toList());
    }
  }
}
