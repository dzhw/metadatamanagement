package eu.dzhw.fdz.metadatamanagement.surveymanagement.repository;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.repository.BaseRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.IdAndNumberSurveyProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * Spring Data MongoDB repository for the Survey entity.
 * 
 * @author Daniel Katzberg
 */
@RepositoryRestResource(path = "/surveys")
@JaversSpringDataAuditable
public interface SurveyRepository
    extends BaseRepository<Survey, String> {

  @RestResource(exported = false)
  Stream<Survey> streamByDataAcquisitionProjectId(String dataAcquisitionProjectId);
  
  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamAllIdAndVersionsBy();
  
  @RestResource(exported = true)
  List<Survey> findByDataAcquisitionProjectId(
      @Param("dataAcquisitionProjectId") String dataAcquisitionProjectId);
  
  @RestResource(exported = false)
  List<Survey> findByDataAcquisitionProjectIdOrderByNumber(String dataAcquisitionProjectId);
  
  @RestResource(exported = false)
  List<IdAndVersionProjection> findIdsByNumberAndDataAcquisitionProjectId(Integer number, 
      String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByStudyId(String studyId);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByIdIn(Collection<String> surveyIds);

  @RestResource(exported = false)
  List<SurveySubDocumentProjection> findSubDocumentByIdIn(List<String> surveyIds);

  @RestResource(exported = false)
  List<SurveySubDocumentProjection> findSubDocumentByStudyId(String id);

  @RestResource(exported = false)
  Stream<IdAndVersionProjection> streamIdsByDataAcquisitionProjectId(String projectId);
  
  @RestResource(exported = false)
  List<IdAndNumberSurveyProjection> findSurveyNumbersByDataAcquisitionProjectId(
      @Param("dataAcquisitionProjectId") String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Stream<Survey> streamByDataAcquisitionProjectIdAndShadowIsFalse(
      @Param("dataAcquisitionProjectId") String dataAcquisitionProjectId);

  @RestResource(exported = false)
  Stream<Survey> streamByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(
      @Param("dataAcquisitionProjectId") String dataAcquisitionProjectId
  );
}
