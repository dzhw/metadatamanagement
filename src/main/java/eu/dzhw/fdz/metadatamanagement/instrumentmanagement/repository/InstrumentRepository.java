package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.IdAndNumberInstrumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.IdAndVersionAndSurveyIdsProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;

/**
 * The Repository for the Instruments.
 * 
 * @author Daniel Katzberg
 *
 */
@RepositoryRestResource(path = "instruments", excerptProjection = IdAndVersionProjection.class)
@JaversSpringDataAuditable
public interface InstrumentRepository extends BaseRepository<Instrument, String> {

  @RestResource(exported = false)
  Stream<Instrument> streamByDataAcquisitionProjectId(String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamAllIdAndVersionsBy();

  @RestResource(exported = true)
  List<Instrument> findByDataAcquisitionProjectId(
      @Param("dataAcquisitionProjectId") String dataAcquisitionProjectId);

  @RestResource(exported = false)
  List<IdAndVersionProjection> findIdsByNumberAndDataAcquisitionProjectId(Integer number,
      String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsBySurveyIdsContaining(String surveyId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByDataPackageId(String dataPackageId);

  @RestResource(exported = false)
  Stream<IdAndVersionAndSurveyIdsProjection> streamIdsByIdIn(Collection<String> instrumentIds);

  @RestResource(exported = false)
  IdAndVersionProjection findOneIdAndVersionById(String id);

  @RestResource(exported = false)
  List<InstrumentSubDocumentProjection> findSubDocumentsByDataPackageId(String dataPackageId);

  @RestResource(exported = false)
  List<InstrumentSubDocumentProjection> findSubDocumentsByIdIn(Collection<String> instrumentIds);

  @RestResource(exported = false)
  List<InstrumentSubDocumentProjection> findSubDocumentsBySurveyIdsContaining(String surveyId);

  @RestResource(exported = false)
  InstrumentSubDocumentProjection findOneSubDocumentById(String instrumentId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByDataAcquisitionProjectId(String projectId);

  @RestResource(exported = false)
  List<IdAndNumberInstrumentProjection> findInstrumentNumbersByDataAcquisitionProjectId(
      String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Stream<Instrument> streamByDataAcquisitionProjectIdAndShadowIsFalse(
      String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Stream<Instrument> streamByDataAcquisitionProjectIdAndSuccessorIdIsNullAndShadowIsTrue(
      String oldProjectId);

  @RestResource(exported = false)
  Stream<Instrument> streamByDataAcquisitionProjectIdAndShadowIsTrue(String oldProjectId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByMasterIdInAndShadowIsTrueAndSuccessorIdIsNull(
      Collection<String> instrumentIds);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByMasterIdIn(Collection<String> instrumentIds);

  @RestResource(exported = false)
  boolean existsByDataAcquisitionProjectId(String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Stream<IdAndVersionAndSurveyIdsProjection> streamIdsByConceptIdsContaining(String id);

  @RestResource(exported = false)
  List<IdAndVersionProjection> findIdsByConceptIdsContaining(String id);

  @RestResource(exported = false)
  Stream<Instrument> findByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(
      String dataAcquisitionProjectId);
}
