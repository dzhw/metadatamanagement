package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.projection.DataPackageSubDocumentProjection;

/**
 * Spring Data MongoDB repository for the data acquisitionProject entity.
 *
 * @author Daniel Katzberg
 */
@RepositoryRestResource(path = "data-packages", excerptProjection = IdAndVersionProjection.class)
@JaversSpringDataAuditable
public interface DataPackageRepository
    extends BaseRepository<DataPackage, String>, DataPackageRepositoryCustom {

  @RestResource(exported = false)
  IdAndVersionProjection findOneIdAndVersionById(String id);

  @RestResource(exported = false)
  DataPackage findOneByDataAcquisitionProjectId(String dataAcquisitionProjectId);

  @RestResource(exported = true)
  List<DataPackage> findByDataAcquisitionProjectId(
      @Param("dataAcquisitionProjectId") String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Stream<DataPackage> streamByDataAcquisitionProjectId(String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamAllIdAndVersionsBy();

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByIdIn(Collection<String> dataPackageIds);

  @RestResource(exported = false)
  List<DataPackageSubDocumentProjection> findSubDocumentsByIdIn(Collection<String> dataPackageIds);

  @RestResource(exported = false)
  DataPackageSubDocumentProjection findOneSubDocumentById(String dataPackageId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByDataAcquisitionProjectId(String id);

  @RestResource(exported = false)
  boolean existsByStudySeries(I18nString studySeries);

  @RestResource(exported = false)
  Stream<DataPackage> streamByDataAcquisitionProjectIdAndShadowIsFalse(
      String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Stream<DataPackage> streamByDataAcquisitionProjectIdAndSuccessorIdIsNullAndShadowIsTrue(
      String oldProjectId);

  @RestResource(exported = false)
  Stream<DataPackage> streamByDataAcquisitionProjectIdAndShadowIsTrue(String oldProjectId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByMasterIdInAndShadowIsTrueAndSuccessorIdIsNull(
      Collection<String> dataSetIds);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByMasterIdIn(Collection<String> dataSetIds);

  @RestResource(exported = false)
  Stream<DataPackage> findByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(
      String dataAcquisitionProjectId);
}
