package eu.dzhw.fdz.metadatamanagement.common.rest;

import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;

/**
 * RestController which overrides Spring Datas default caching headers.
 *
 * @author René Reitmann
 *
 * @param <T> The domainObject's class
 * @param <S> The corresponding repository
 */
public abstract class OldGenericDomainObjectResourceController<T extends AbstractRdcDomainObject,
    S extends BaseRepository<T, String>> {

  protected S repository;

  /**
   * Create resource controller.
   *
   * @param repository The repository managing the domain objects being versioned.
   */
  public OldGenericDomainObjectResourceController(S repository) {
    this.repository = repository;
  }

  /**
   * Find the domain object and return it with the right caching directives.
   *
   * @param id The id of the domain object
   *
   * @return The http response
   */
  protected ResponseEntity<T> findDomainObject(String id) {
    Optional<T> optional = repository.findById(id);

    if (!optional.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    return wrapInResponseEntity(optional.get());
  }

  protected ResponseEntity<T> wrapInResponseEntity(T entity) {
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(0, TimeUnit.DAYS).mustRevalidate().cachePublic())
        .eTag(entity.getVersion().toString())
        .lastModified(
            entity.getLastModifiedDate().atZone(ZoneId.of("GMT")).toInstant().toEpochMilli())
        .body(entity);
  }
}
