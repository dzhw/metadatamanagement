package eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.projections.ConceptSubDocumentProjection;

/**
 * Concept Repository.
 */
@RepositoryRestResource(path = "/concepts", excerptProjection = IdAndVersionProjection.class)
@JaversSpringDataAuditable
public interface ConceptRepository 
    extends BaseRepository<Concept, String>, ConceptRepositoryCustom {
  @RestResource(exported = false)
  List<ConceptSubDocumentProjection> findSubDocumentsByIdIn(Collection<String> conceptIds);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamAllIdAndVersionsBy();

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByIdIn(Collection<String> conceptIds);
}
