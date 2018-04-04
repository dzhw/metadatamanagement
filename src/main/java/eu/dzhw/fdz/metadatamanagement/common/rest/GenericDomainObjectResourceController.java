package eu.dzhw.fdz.metadatamanagement.common.rest;

import java.time.ZoneId;
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
public abstract class GenericDomainObjectResourceController<T extends AbstractRdcDomainObject,
    S extends BaseRepository<T, String>> {

  protected S repository;

  /**
   * Create resource controller.
   * 
   * @param repository The repository managing the domain objects being versioned.
   */
  public GenericDomainObjectResourceController(S repository) {
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
    T domainObject = repository.findOne(id);

    if (domainObject == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(0, TimeUnit.DAYS).mustRevalidate().cachePublic())
        .eTag(domainObject.getVersion().toString())
        .lastModified(
            domainObject.getLastModifiedDate().atZone(ZoneId.of("GMT")).toInstant().toEpochMilli())
        .body(domainObject);
  }
}
