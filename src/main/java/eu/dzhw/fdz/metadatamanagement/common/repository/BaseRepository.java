package eu.dzhw.fdz.metadatamanagement.common.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.security.access.annotation.Secured;

import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * Base repository from which all repository inherit access rights to modifying
 * methods.
 * 
 * @author René Reitmann
 *
 * @param <T> the entitiy
 * @param <ID> the id of the entity
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable>
    extends MongoRepository<T, ID> {
  @Override
  @Secured(AuthoritiesConstants.PUBLISHER)
  void delete(T entity);
  
  @Override
  @Secured(AuthoritiesConstants.PUBLISHER)
  void delete(ID id);
  
  @Override
  @Secured(AuthoritiesConstants.PUBLISHER)
  void delete(Iterable<? extends T> entities);
  
  @Override
  @Secured(AuthoritiesConstants.PUBLISHER)
  void deleteAll();
  
  @Override
  @Secured(AuthoritiesConstants.PUBLISHER)
  <S extends T> List<S> save(Iterable<S> entites);
  
  @Override
  @Secured(AuthoritiesConstants.PUBLISHER)
  <S extends T> S save(S entity);
  
  @Override
  @Secured(AuthoritiesConstants.PUBLISHER)
  <S extends T> List<S> insert(Iterable<S> entities);
  
  @Override
  @Secured(AuthoritiesConstants.PUBLISHER)
  <S extends T> S insert(S entity);
}
