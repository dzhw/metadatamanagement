package eu.dzhw.fdz.metadatamanagement.variablemanagement.repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;

/**
 * Spring Data MongoDB repository for the Variable entity.
 */
@RepositoryRestResource(path = "/variables")
public interface VariableRepository
    extends MongoRepository<Variable, String>, QueryDslPredicateExecutor<Variable> {
  @RestResource(exported = false)
  Stream<Variable> streamByDataAcquisitionProjectId(String dataAcquisitionProjectId);
  
  @RestResource(exported = false)
  List<Variable> findByDataAcquisitionProjectId(String dataAcquisitionProjectId);
  
  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsBySurveyIdsContaining(String surveyId);
  
  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByDataSetId(String dataSetId);
  
  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamAllIdAndVersionsBy();
  
  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByIdIn(Collection<String> ids);
  
  @RestResource(exported = false)
  List<VariableSubDocumentProjection> findSubDocumentsByIdIn(Collection<String> variableIds);
  
  @RestResource(exported = false)
  List<IdAndVersionProjection> findIdsByNameAndDataSetId(String name, String dataSetId);
  
  @RestResource(exported = false)
  List<IdAndVersionProjection> findIdsByIndexInDataSetAndDataSetId(
      Integer indexInDataSet, String dataSetId);
  
  @RestResource(exported = false)
  List<VariableSubDocumentProjection> findSubDocumentsByRelatedQuestionsQuestionId(
      String questionId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByStudyId(String studyId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByRelatedQuestionsInstrumentId(String instrumentId);

  @RestResource(exported = false)
  List<Variable> findByDataSetIdOrderByIndexInDataSetAsc(String dataSetId);

  @RestResource(exported = false)
  List<VariableSubDocumentProjection> findSubDocumentsBySurveyIdsContaining(String surveyId);

  @RestResource(exported = false)
  List<VariableSubDocumentProjection> findSubDocumentsByDataSetId(String dataSetId);

  @RestResource(exported = false)
  List<VariableSubDocumentProjection> findSubDocumentsByRelatedQuestionsInstrumentId(
      String instrumentId);

  @RestResource(exported = false)
  List<VariableSubDocumentProjection> findSubDocumentsByStudyId(String studyId);
}