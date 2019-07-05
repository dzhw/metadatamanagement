package eu.dzhw.fdz.metadatamanagement.common.repository;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.annotation.Secured;

import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * Base repository from which all repository inherit access rights to modifying methods.
 * 
 * @author René Reitmann
 *
 * @param <T> the entity
 * @param <ID> the id of the entity
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable>
    extends MongoRepository<T, ID>, CrudRepository<T, ID>, QuerydslPredicateExecutor<T> {
  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.TASK_USER})
  void delete(T entity);

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.TASK_USER})
  void deleteById(ID id);

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.TASK_USER})
  void deleteAll(Iterable<? extends T> entities);

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.TASK_USER})
  void deleteAll();

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.TASK_USER})
  <S extends T> List<S> saveAll(Iterable<S> entites);

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.TASK_USER})
  <S extends T> S save(S entity);

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.TASK_USER})
  <S extends T> List<S> insert(Iterable<S> entities);

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER,
      AuthoritiesConstants.TASK_USER})
  <S extends T> S insert(S entity);

  @RestResource(exported = false)
  <S extends T> Stream<S> streamAllBy();
}
