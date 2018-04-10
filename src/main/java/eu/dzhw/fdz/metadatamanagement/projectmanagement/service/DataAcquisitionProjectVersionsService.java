package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class DataAcquisitionProjectVersionsService 
      extends GenericDomainObjectVersionsService<DataAcquisitionProject, 
      DataAcquisitionProjectRepository> {
  /**
   * Construct the service.
   */
  @Autowired
  public DataAcquisitionProjectVersionsService(Javers javers,
      DataAcquisitionProjectRepository dataAcquisitionProjectRepository,
      MetadataManagementProperties metadataManagementProperties) {
    super(DataAcquisitionProject.class, javers, 
        dataAcquisitionProjectRepository, metadataManagementProperties);
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
   * @param id the id of the data acquisition project.
   * @return the last saved release or null
   */
  public Release findLastRelease(String id) {
    return findPreviousRelease(id, null);
  }
  
  /**
   * Get the previous release of a data acquisition project. The release before currentRelease.
   * @param id the id of the data acquisition project.
   * @param currentRelease get the release saved before this release
   * @return the previous release or null
   */
  public Release findPreviousRelease(String id, Release currentRelease) {
    //Find last changes
    QueryBuilder jqlQuery = QueryBuilder
        .byValueObjectId(id, DataAcquisitionProject.class, "release")
        .withChangedProperty("version")
        .limit(2);
    List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
    if (snapshots.isEmpty()) {
      return null;
    }
    List<BigDecimal> commitIds = snapshots.stream()
        .map(snapshot -> snapshot.getCommitId().valueAsNumber())
        .collect(Collectors.toList());
    List<Shadow<Release>> shadows = javers.findShadows(
        QueryBuilder.byValueObjectId(id, DataAcquisitionProject.class, "release")
        .withCommitIds(commitIds).build());    
    
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
}
