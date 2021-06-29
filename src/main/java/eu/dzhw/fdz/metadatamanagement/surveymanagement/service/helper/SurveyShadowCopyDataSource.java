package eu.dzhw.fdz.metadatamanagement.surveymanagement.service.helper;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;

/**
 * Provides data for creating shadow copies of {@link Survey}.
 */
@Component
@RequiredArgsConstructor
public class SurveyShadowCopyDataSource implements ShadowCopyDataSource<Survey> {

  private final SurveyRepository surveyRepository;

  private final SurveyCrudHelper crudHelper;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  @Override
  public Stream<Survey> getMasters(String dataAcquisitionProjectId) {
    return surveyRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public Survey createShadowCopy(Survey source, Release release) {
    String derivedId = source.getId() + "-" + release.getVersion();
    Survey copy = crudHelper.read(derivedId).orElseGet(Survey::new);
    BeanUtils.copyProperties(source, copy, "version");
    copy.setId(derivedId);
    copy.setDataAcquisitionProjectId(
        source.getDataAcquisitionProjectId() + "-" + release.getVersion());
    copy.setDataPackageId(source.getDataPackageId() + "-" + release.getVersion());
    return copy;
  }

  @Override
  public Optional<Survey> findPredecessorOfShadowCopy(Survey shadowCopy, String previousVersion) {
    String shadowCopyId = shadowCopy.getMasterId() + "-" + previousVersion;
    if (shadowCopy.getId().equals(shadowCopyId)) {
      return Optional.empty();
    } else {
      return crudHelper.read(shadowCopyId);
    }
  }

  @Override
  public void updatePredecessor(Survey predecessor) {
    crudHelper.saveShadow(predecessor);
  }

  @Override
  public void saveShadowCopy(Survey shadowCopy) {
    crudHelper.saveShadow(shadowCopy);
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
    try (Stream<Survey> surveys = surveyRepository
        .findByDataAcquisitionProjectIdAndShadowIsTrue(oldProjectId)) {
      surveys.forEach(crudHelper::deleteShadow);
    }
  }

  @Override
  public void updateElasticsearch(String dataAcquisitionProjectId, String releaseVersion,
      String previousVersion) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      return surveyRepository
          .streamIdsByDataAcquisitionProjectId(dataAcquisitionProjectId + "-" + releaseVersion);
    }, ElasticsearchType.surveys);
    if (!StringUtils.isEmpty(previousVersion)) {
      elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
        return surveyRepository
            .streamIdsByDataAcquisitionProjectId(dataAcquisitionProjectId + "-" + previousVersion);
      }, ElasticsearchType.surveys);
    }
  }
  
  @Override
  public void hideExistingShadowCopies(String projectId, String version) {
    setHiddenState(projectId, version, true);
  }

  private void setHiddenState(String projectId, String version, boolean hidden) {
    String shadowId = projectId + "-" + version;
    try (Stream<Survey> surveys =
        surveyRepository.streamByDataAcquisitionProjectIdAndShadowIsTrue(shadowId)) {
      surveys.forEach(shadow -> {
        shadow.setHidden(hidden);
        crudHelper.saveShadow(shadow);
      });
    }
  }

  @Override
  public void unhideExistingShadowCopies(String projectId, String version) {
    setHiddenState(projectId, version, false);
  }
}
