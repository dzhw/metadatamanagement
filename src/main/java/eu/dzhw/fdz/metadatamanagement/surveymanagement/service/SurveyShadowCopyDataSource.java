package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;

/**
 * Provides data for creating shadow copies of {@link Survey}.
 */
@Service
public class SurveyShadowCopyDataSource implements ShadowCopyDataSource<Survey> {

  private SurveyRepository surveyRepository;

  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  public SurveyShadowCopyDataSource(SurveyRepository surveyRepository,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService) {
    this.surveyRepository = surveyRepository;
    this.elasticsearchUpdateQueueService = elasticsearchUpdateQueueService;
  }

  @Override
  public Stream<Survey> getMasters(String dataAcquisitionProjectId) {
    return surveyRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public Survey createShadowCopy(Survey source, String version) {
    String derivedId = source.getId() + "-" + version;
    Survey copy = surveyRepository.findById(derivedId).orElseGet(Survey::new);
    BeanUtils.copyProperties(source, copy, "version");
    copy.setId(derivedId);
    copy.setDataAcquisitionProjectId(source.getDataAcquisitionProjectId() + "-" + version);
    copy.setStudyId(source.getStudyId() + "-" + version);
    return copy;
  }

  @Override
  public Optional<Survey> findPredecessorOfShadowCopy(Survey shadowCopy, String previousVersion) {
    String shadowCopyId = shadowCopy.getMasterId() + "-" + previousVersion;
    if (shadowCopy.getId().equals(shadowCopyId)) {
      return Optional.empty();
    } else {
      return surveyRepository.findById(shadowCopyId);
    }
  }

  @Override
  public void updatePredecessor(Survey predecessor) {
    surveyRepository.save(predecessor);
  }

  @Override
  public void saveShadowCopy(Survey shadowCopy) {
    surveyRepository.save(shadowCopy);
  }

  @Override
  public Stream<Survey> findShadowCopiesWithDeletedMasters(String projectId,
      String previousVersion) {
    String oldProjectId = projectId + "-" + previousVersion;
    return surveyRepository.streamByDataAcquisitionProjectIdAndShadowIsTrue(oldProjectId)
        .filter(shadowCopy -> !surveyRepository.existsById(shadowCopy.getMasterId()));
  }

  @Override
  public void deleteExistingShadowCopies(String projectId, String version) {
    String oldProjectId = projectId + "-" + version;
    List<IdAndVersionProjection> deletedIds = surveyRepository
        .deleteByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(oldProjectId);
    for (IdAndVersionProjection survey : deletedIds) {
      elasticsearchUpdateQueueService.enqueue(survey.getId(), ElasticsearchType.surveys,
          ElasticsearchUpdateQueueAction.DELETE);
    }
  }

}
