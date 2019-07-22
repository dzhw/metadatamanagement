package eu.dzhw.fdz.metadatamanagement.common.rest;

import java.net.URI;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;

import com.mongodb.client.MongoDatabase;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller managing CRUD access to our {@link AbstractRdcDomainObject} stored in
 * {@link MongoDatabase}.
 * 
 * @author René Reitmann
 *
 * @param <T> The concrete type of the domain object.
 * @param <S> The CRUD service implementation of the domain object.
 */
@RequiredArgsConstructor
public abstract class GenericDomainObjectResourceController<T extends AbstractRdcDomainObject, S extends CrudService<T>> {

  private final CrudService<T> crudService;

  /**
   * Retrieve the {@link AbstractRdcDomainObject} and set the cache header.
   * 
   * @param id the id of the {@link AbstractRdcDomainObject}.
   * @return the {@link AbstractRdcDomainObject} or 404
   */
  public ResponseEntity<T> getDomainObject(String id) {
    Optional<T> optional = crudService.read(id);
    if (!optional.isPresent()) {
      return ResponseEntity.notFound().build();
    } else {
      T domainObject = optional.get();
      return ResponseEntity.ok()
          .cacheControl(CacheControl.maxAge(0, TimeUnit.DAYS).mustRevalidate().cachePublic())
          .eTag(domainObject.getVersion().toString()).lastModified(domainObject
              .getLastModifiedDate().atZone(ZoneId.of("GMT")).toInstant().toEpochMilli())
          .body(domainObject);
    }
  }

  /**
   * Create the given {@link AbstractRdcDomainObject}. For {@link AbstractShadowableRdcDomainObject}
   * only masters can be created.
   * 
   * @param domainObject The {@link AbstractRdcDomainObject} to be created.
   */
  public ResponseEntity<?> postDomainObject(T domainObject) {
    T saved = crudService.create(domainObject);
    return ResponseEntity.created(buildLocationHeaderUri(saved)).build();
  }

  /**
   * Save or create the given {@link AbstractRdcDomainObject}.
   * {@link AbstractShadowableRdcDomainObject} may only be saved if they are masters.
   * 
   * @param domainObject The {@link AbstractRdcDomainObject} to be saved.
   */
  public ResponseEntity<?> putDomainObject(T domainObject) {
    crudService.save(domainObject);
    return ResponseEntity.noContent().build();
  }

  /**
   * Delete the {@link AbstractRdcDomainObject} under the given id.
   * {@link AbstractShadowableRdcDomainObject} may only be deleted if they are masters.
   * 
   * @param id The id of the {@link AbstractRdcDomainObject}
   */
  public ResponseEntity<?> deleteDomainObject(String id) {
    Optional<T> optional = crudService.read(id);
    if (optional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    crudService.delete(optional.get());
    return ResponseEntity.noContent().build();
  }

  protected abstract URI buildLocationHeaderUri(T domainObject);
}
