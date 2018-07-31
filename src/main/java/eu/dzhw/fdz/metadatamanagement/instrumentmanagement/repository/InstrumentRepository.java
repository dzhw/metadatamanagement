package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;

/**
 * The Repository for the Instruments.
 * 
 * @author Daniel Katzberg
 *
 */
@RepositoryRestResource(path = "/instruments")
public interface InstrumentRepository
    extends BaseRepository<Instrument, String> {

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
  Stream<IdAndVersionProjection> streamIdsByStudyId(String studyId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByIdIn(Collection<String> instrumentIds);

  @RestResource(exported = false)    
  IdAndVersionProjection findOneIdAndVersionById(String id);

  @RestResource(exported = false)
  List<InstrumentSubDocumentProjection> findSubDocumentsByStudyId(String studyId);

  @RestResource(exported = false)
  List<InstrumentSubDocumentProjection> findSubDocumentsByIdIn(Collection<String> instrumentIds);

  @RestResource(exported = false)
  List<InstrumentSubDocumentProjection> findSubDocumentsBySurveyIdsContaining(String surveyId);

  @RestResource(exported = false)
  InstrumentSubDocumentProjection findOneSubDocumentById(String instrumentId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByDataAcquisitionProjectId(String projectId);
}
