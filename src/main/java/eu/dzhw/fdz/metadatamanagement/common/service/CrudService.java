package eu.dzhw.fdz.metadatamanagement.common.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;

@Validated
public interface CrudService<T extends AbstractRdcDomainObject> {
  
  public Optional<T> read(String id);
  
  public void delete(T domainObject);
  
  public T save(@Valid T domainObject);
  
  public T create(@Valid T domainObject);
}
