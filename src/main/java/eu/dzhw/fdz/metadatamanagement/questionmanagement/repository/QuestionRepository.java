package eu.dzhw.fdz.metadatamanagement.questionmanagement.repository;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;

/**
 * The Repository for {@link Question} domain object. The data will be insert with a REST API and
 * save in a mongo db.
 */
@RepositoryRestResource(path = "/questions")
public interface QuestionRepository
    extends MongoRepository<Question, String>, QueryDslPredicateExecutor<Question> {

  @RestResource(exported = false)
  Stream<Question> streamByDataAcquisitionProjectId(String dataAcquisitionProjectId);

  @RestResource(exported = false)
  List<Question> findByDataAcquisitionProjectId(String dataAcquisitionProjectId);
  
  @RestResource(exported = false)
  List<IdAndVersionProjection> findIdsByInstrumentIdAndNumber(String instrumentId, String number);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamAllIdAndVersionsBy();
  
  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByInstrumentId(String instrumentId);
  
  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByIdIn(List<String> ids);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByStudyId(String studyId);

  @RestResource(exported = false)
  List<QuestionSubDocumentProjection> findSubDocumentsByInstrumentIdIn(List<String> instrumentIds);

  @RestResource(exported = false)
  List<QuestionSubDocumentProjection> findSubDocumentsByStudyId(String studyId);

  @RestResource(exported = false)
  List<QuestionSubDocumentProjection> findSubDocumentsByIdIn(List<String> questionIds);

  @RestResource(exported = false)
  List<QuestionSubDocumentProjection> findSubDocumentsByInstrumentId(
      String instrumentId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByDataAcquisitionProjectId(String projectId);
}
