package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;

/**
 * Related Publication Repository.
 * 
 * @author Daniel Katzberg
 *
 */
@RepositoryRestResource(path = "related-publications",
    excerptProjection = IdAndVersionProjection.class)
public interface RelatedPublicationRepository extends BaseRepository<RelatedPublication, String> {
  @RestResource(exported = false)
  Slice<RelatedPublication> findBy(Pageable pageable);

  @RestResource(exported = false)
  List<RelatedPublication> findByDataPackageIdsContaining(String dataPackageId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByDataPackageIdsContaining(String dataPackageId);
  
  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByAnalysisPackageIdsContaining(String analysisPackageId);
  
  @RestResource(exported = false)
  List<IdAndVersionProjection> findIdsByAnalysisPackageIdsContaining(String analysisPackageId);

  @RestResource(exported = false)
  List<RelatedPublicationSubDocumentProjection> findSubDocumentsByDataPackageIdsContaining(
      String dataPackageId);
  
  @RestResource(exported = false)
  List<RelatedPublicationSubDocumentProjection> findSubDocumentsByAnalysisPackageIdsContaining(
      String analysisPackageId);
  
  @RestResource(exported = false)
  boolean existsByAnalysisPackageIdsContaining(String analysisPackageId);
  
  @RestResource(exported = false)
  long countByAnalysisPackageIdsContaining(String analysisPackageId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamAllIdAndVersionsBy();

  @RestResource(exported = true)
  List<RelatedPublication> findAllBy();
}
