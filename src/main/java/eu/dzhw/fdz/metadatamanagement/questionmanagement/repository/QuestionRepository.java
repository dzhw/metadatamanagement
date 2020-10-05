package eu.dzhw.fdz.metadatamanagement.questionmanagement.repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.IdAndVersionAndInstrumentProjection;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;

/**
 * The Repository for {@link Question} domain object. The data will be insert with a REST API and
 * save in a mongo db.
 */
@RepositoryRestResource(path = "questions", excerptProjection = IdAndVersionProjection.class)
public interface QuestionRepository extends BaseRepository<Question, String> {

  @RestResource(exported = false)
  Stream<Question> streamByDataAcquisitionProjectId(String dataAcquisitionProjectId);

  @RestResource(exported = true)
  List<Question> findByDataAcquisitionProjectId(
      @Param("dataAcquisitionProjectId") String dataAcquisitionProjectId);

  @RestResource(exported = false)
  List<IdAndVersionProjection> findIdsByInstrumentIdAndNumber(String instrumentId, String number);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamAllIdAndVersionsBy();

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByInstrumentId(String instrumentId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByIdIn(List<String> ids);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByDataPackageId(String dataPackageId);

  @RestResource(exported = false)
  Stream<IdAndVersionAndInstrumentProjection> streamIdsByConceptIdsContaining(String conceptId);

  @RestResource(exported = false)
  List<QuestionSubDocumentProjection> findSubDocumentsByInstrumentIdIn(
      Collection<String> instrumentIds);

  @RestResource(exported = false)
  List<QuestionSubDocumentProjection> findSubDocumentsByDataPackageId(String dataPackageId);

  @RestResource(exported = false)
  List<QuestionSubDocumentProjection> findSubDocumentsByIdIn(Collection<String> questionIds);

  @RestResource(exported = false)
  List<QuestionSubDocumentProjection> findSubDocumentsByInstrumentId(String instrumentId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByDataAcquisitionProjectId(String projectId);

  @RestResource(exported = false)
  Stream<Question> streamByDataAcquisitionProjectIdAndShadowIsFalse(
      String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Stream<Question> streamByDataAcquisitionProjectIdAndSuccessorIdIsNullAndShadowIsTrue(
      String oldProjectId);

  @RestResource(exported = false)
  Stream<Question> streamByDataAcquisitionProjectIdAndShadowIsTrue(String oldProjectId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByMasterIdInAndShadowIsTrueAndSuccessorIdIsNull(
      Collection<String> questionIds);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByMasterIdIn(Collection<String> questionIds);

  @RestResource(exported = false)
  List<QuestionSubDocumentProjection> findSubDocumentsByConceptIdsContaining(String id);

  @RestResource(exported = false)
  Stream<Question> findByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(
      String dataAcquisitionProjectId);
}
