package eu.dzhw.fdz.metadatamanagement.common.repository;

import java.io.Serializable;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;

/**
 * Base repository for all {@link AbstractRdcDomainObject}s.
 * 
 * @author René Reitmann
 *
 * @param <T> the entity
 * @param <ID> the id of the entity
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable>
    extends MongoRepository<T, ID>, CrudRepository<T, ID>, QuerydslPredicateExecutor<T> {
  @RestResource(exported = false)
  <S extends T> Stream<S> streamAllBy();
}
