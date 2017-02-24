package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
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
    extends MongoRepository<Instrument, String>, QueryDslPredicateExecutor<Instrument> {

  @RestResource(exported = false)
  Stream<Instrument> streamByDataAcquisitionProjectId(String dataAcquisitionProjectId);
  
  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamAllIdAndVersionsBy();

  @RestResource(exported = false)
  List<Instrument> findByDataAcquisitionProjectId(String dataAcquisitionProjectId);
  
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
}
