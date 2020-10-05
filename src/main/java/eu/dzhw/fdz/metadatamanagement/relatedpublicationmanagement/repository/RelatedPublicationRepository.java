package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
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
  Stream<IdAndVersionProjection> streamIdsByDataSetIdsContaining(String dataSetId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsBySurveyIdsContaining(String surveyId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByVariableIdsContaining(String variableId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByInstrumentIdsContaining(String instrumentId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByQuestionIdsContaining(String questionId);

  @RestResource(exported = false)
  List<RelatedPublicationSubDocumentProjection> findSubDocumentsByDataPackageIdsContaining(
      String dataPackageId);

  @RestResource(exported = false)
  List<RelatedPublicationSubDocumentProjection> findSubDocumentsByQuestionIdsContaining(
      String questionId);

  @RestResource(exported = false)
  List<RelatedPublicationSubDocumentProjection> findSubDocumentsByVariableIdsContaining(
      String variableId);

  @RestResource(exported = false)
  List<RelatedPublicationSubDocumentProjection> findSubDocumentsBySurveyIdsContaining(
      String surveyId);

  @RestResource(exported = false)
  List<RelatedPublicationSubDocumentProjection> findSubDocumentsByDataSetIdsContaining(
      String dataSetId);

  @RestResource(exported = false)
  List<RelatedPublicationSubDocumentProjection> findSubDocumentsByInstrumentIdsContaining(
      String instrumentId);

  @RestResource(exported = false)
  List<RelatedPublicationSubDocumentProjection> findSubDocumentsByStudySeriesesContaining(
      I18nString studySeries);

  @RestResource(exported = false)
  Stream<RelatedPublication> streamByStudySeriesesContaining(I18nString studySeries);

  @RestResource(exported = false)
  Stream<RelatedPublication> streamByDataPackageIdsContaining(String dataPackageId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamAllIdAndVersionsBy();

  @RestResource(exported = true)
  List<RelatedPublication> findAllBy();
}
