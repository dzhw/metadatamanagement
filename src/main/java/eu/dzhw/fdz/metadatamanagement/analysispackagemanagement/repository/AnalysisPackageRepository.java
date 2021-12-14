package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.projection.AnalysisPackageSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;

/**
 * Spring Data MongoDB repository for the analysis package document.
 */
@RepositoryRestResource(path = "analysis-packages",
    excerptProjection = IdAndVersionProjection.class)
@JaversSpringDataAuditable
public interface AnalysisPackageRepository extends BaseRepository<AnalysisPackage, String> {

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByDataAcquisitionProjectId(String id);

  @RestResource(exported = false)
  Stream<AnalysisPackage> streamByDataAcquisitionProjectId(String dataAcquisitionProjectId);

  @RestResource(exported = false)
  AnalysisPackage findOneByDataAcquisitionProjectId(String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamAllIdAndVersionsBy();

  @RestResource(exported = false)
  Stream<AnalysisPackage> streamByDataAcquisitionProjectIdAndShadowIsFalse(
      String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Stream<AnalysisPackage> streamByDataAcquisitionProjectIdAndShadowIsTrue(String projectId);

  @RestResource(exported = false)
  Stream<AnalysisPackage> findByDataAcquisitionProjectIdAndShadowIsTrue(
      String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByMasterIdIn(Collection<String> dataSetIds);

  @RestResource(exported = false)
  List<AnalysisPackageSubDocumentProjection> findSubDocumentsByIdIn(
      Collection<String> analysisPackageIds);

  @RestResource(exported = false)
  @Query("{ 'analysisDataPackages.dataPackageMasterId' : ?0, 'analysisDataPackages.version' : ?1 }")
  Stream<IdAndVersionProjection> streamIdsByDataPackageMasterIdAndVersion(String masterId,
      String version);

  @RestResource(exported = false)
  @Query("{ 'analysisDataPackages.dataPackageMasterId' : ?0, 'analysisDataPackages.version' : ?1 }")
  List<AnalysisPackageSubDocumentProjection> findByDataPackageMasterIdAndVersion(String masterId,
      String version);
}
