package eu.dzhw.fdz.metadatamanagement.common.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Generic Service for initing and retrieving the history or our domain objects.
 *
 * @author René Reitmann
 *
 * @param <T> The domain object type
 * @param <S> The corresponding repository type
 */
@Slf4j
public abstract class GenericDomainObjectVersionsService<T extends AbstractRdcDomainObject,
    S extends BaseRepository<T, String>> {
  protected Javers javers;
  private S repository;
  private MetadataManagementProperties metadataManagementProperties;
  private Class<T> domainObjectClass;

  /**
   * Create the service.
   *
   * @param domainObjectClass The class of the domain objects being versioned.
   * @param javers The javers bean.
   * @param repository The repository managing the domain objects being versioned.
   * @param metadataManagementProperties The configuration properties of this app.
   */
  public GenericDomainObjectVersionsService(Class<T> domainObjectClass, Javers javers, S repository,
      MetadataManagementProperties metadataManagementProperties) {
    this.javers = javers;
    this.repository = repository;
    this.metadataManagementProperties = metadataManagementProperties;
    this.domainObjectClass = domainObjectClass;
  }

  protected void initJaversWithCurrentVersions() {
    if (!metadataManagementProperties.getServer().getInstanceIndex().equals(0)) {
      log.debug("This is server instance {} therefore skipping javers init for studies.",
          metadataManagementProperties.getServer().getInstanceIndex());
      return;
    }
    List<CdoSnapshot> snapshots =
        javers.findSnapshots(QueryBuilder.byClass(domainObjectClass).limit(1).build());
    // only init if there are no versions yet
    if (snapshots.isEmpty()) {
      log.debug(
          "Going to init javers with all current versions of " + domainObjectClass.getSimpleName());
      repository.streamAllBy().forEach(currentVersion -> {
        javers.commit(currentVersion.getLastModifiedBy(), currentVersion);
      });
    }
  }

  /**
   * Get the previous 10 versions of the domain object.
   *
   * @param id this id of the domain object
   * @param limit like page size
   * @param skip for skipping n versions
   *
   * @return A list of previous versions or null if no domain found
   */
  public List<T> findPreviousVersions(String id, int limit, int skip) {
    QueryBuilder snapshotQuery = QueryBuilder.byInstanceId(id, domainObjectClass);
    
    Stream<Shadow<T>> shadows = javers.findShadowsAndStream(snapshotQuery.build());
    shadows = shadows.skip(skip).limit(limit);
   
    return shadows.map(shadow -> {
      T domainObjectVersion = shadow.get();
      if (domainObjectVersion.getId() == null) {
        // deleted shadow
        domainObjectVersion.setLastModifiedBy(shadow.getCommitMetadata().getAuthor());
        domainObjectVersion.setLastModifiedDate(shadow.getCommitMetadata().getCommitDate());
      }
      return domainObjectVersion;
    }).collect(Collectors.toList());
  }
}
