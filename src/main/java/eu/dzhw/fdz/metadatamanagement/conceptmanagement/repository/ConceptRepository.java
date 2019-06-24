package eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.annotation.Secured;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.projections.ConceptSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * Concept Repository.
 */
@RepositoryRestResource(path = "/concepts")
@JaversSpringDataAuditable
public interface ConceptRepository 
    extends BaseRepository<Concept, String>, ConceptRepositoryCustom {
  @Override
  @Secured(value = AuthoritiesConstants.PUBLISHER)
  void delete(Concept entity);
  
  @Override
  @Secured(value = AuthoritiesConstants.PUBLISHER)
  void deleteById(String id);
  
  @Override
  @Secured(value = AuthoritiesConstants.PUBLISHER)
  void deleteAll(Iterable<? extends Concept> entities);
  
  @Override
  @Secured(value = AuthoritiesConstants.PUBLISHER)
  void deleteAll();
  
  @Override
  @Secured(value = AuthoritiesConstants.PUBLISHER)
  <S extends Concept> List<S> saveAll(Iterable<S> entites);
  
  @Override
  @Secured(value = AuthoritiesConstants.PUBLISHER)
  <S extends Concept> S save(S entity);
  
  @Override
  @Secured(value = AuthoritiesConstants.PUBLISHER)
  <S extends Concept> List<S> insert(Iterable<S> entities);
  
  @Override
  @Secured(value = AuthoritiesConstants.PUBLISHER)
  <S extends Concept> S insert(S entity);
  
  @RestResource(exported = false)
  List<ConceptSubDocumentProjection> findSubDocumentsByIdIn(Collection<String> conceptIds);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamAllIdAndVersionsBy();

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByIdIn(Collection<String> conceptIds);
}
