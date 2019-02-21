package eu.dzhw.fdz.metadatamanagement.common.rest;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyCreateNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyDeleteNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyUpdateNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.event.AfterCreateEvent;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.data.rest.core.event.BeforeCreateEvent;
import org.springframework.data.rest.core.event.BeforeDeleteEvent;
import org.springframework.data.rest.core.event.BeforeSaveEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Optional;

/**
 * REST Controller for handling {@link AbstractShadowableRdcDomainObject} implementations.
 * @param <T> {@link AbstractShadowableRdcDomainObject} type
 * @param <S> Repository for saving/updating
 */
public abstract class GenericShadowableDomainObjectResourceController
    <T extends AbstractShadowableRdcDomainObject, S extends BaseRepository<T, String>>
    extends GenericDomainObjectResourceController<T, S> {

  private ApplicationEventPublisher applicationEventPublisher;

  public GenericShadowableDomainObjectResourceController(S repository, ApplicationEventPublisher
      applicationEventPublisher) {
    super(repository);
    this.applicationEventPublisher = applicationEventPublisher;
  }

  protected ResponseEntity<?> putDomainObject(String id, T domainObject) {
    Optional<T> opt = repository.findById(id);
    if (opt.isPresent()) {
      T persistedDomainObject = opt.get();
      if (persistedDomainObject.isShadow()) {
        throw new ShadowCopyUpdateNotAllowedException();
      } else {
        saveDomainObject(domainObject);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
      }
    } else if (domainObject.isShadow()) {
      throw new ShadowCopyCreateNotAllowedException();
    } else {
      T persistedDomainObject = createDomainObject(domainObject);
      return ResponseEntity.created(buildLocationHeaderUri(persistedDomainObject)).build();
    }
  }

  protected ResponseEntity<T> postDomainObject(T domainObject) {
    if (domainObject.isShadow()) {
      throw new ShadowCopyCreateNotAllowedException();
    } else {
      T persisted = createDomainObject(domainObject);
      return ResponseEntity.created(buildLocationHeaderUri(persisted)).build();
    }
  }

  protected ResponseEntity<Void> deleteDomainObject(String id) {
    Optional<T> opt = repository.findById(id);

    if (opt.isPresent()) {
      T domainObject = opt.get();
      if (domainObject.isShadow()) {
        throw new ShadowCopyDeleteNotAllowedException();
      } else {
        applicationEventPublisher.publishEvent(new BeforeDeleteEvent(domainObject));
        repository.delete(domainObject);
        applicationEventPublisher.publishEvent(new AfterDeleteEvent(domainObject));
        return ResponseEntity.noContent().build();
      }
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  protected abstract URI buildLocationHeaderUri(T domainObject);

  private void saveDomainObject(T domainObject) {
    applicationEventPublisher.publishEvent(new BeforeSaveEvent(domainObject));
    T persisted = repository.save(domainObject);
    applicationEventPublisher.publishEvent(new AfterSaveEvent(persisted));
  }

  private T createDomainObject(T domainObject) {
    applicationEventPublisher.publishEvent(new BeforeCreateEvent(domainObject));
    T persisted = repository.save(domainObject);
    applicationEventPublisher.publishEvent(new AfterCreateEvent(persisted));
    return persisted;
  }
}
