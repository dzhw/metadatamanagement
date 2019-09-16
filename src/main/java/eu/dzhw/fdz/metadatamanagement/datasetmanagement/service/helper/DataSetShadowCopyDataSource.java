package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.helper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import lombok.RequiredArgsConstructor;

/**
 * Provides data for creating shadow copies of {@link DataSet}.
 */
@Component
@RequiredArgsConstructor
public class DataSetShadowCopyDataSource implements ShadowCopyDataSource<DataSet> {
  private final DataSetRepository dataSetRepository;

  private final DataSetCrudHelper crudHelper;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  @Override
  public Stream<DataSet> getMasters(String dataAcquisitionProjectId) {
    return dataSetRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public DataSet createShadowCopy(DataSet source, Release release) {
    String derivedId = source.getId() + "-" + release.getVersion();
    DataSet copy = crudHelper.read(derivedId).orElseGet(DataSet::new);
    BeanUtils.copyProperties(source, copy, "version");
    copy.setId(derivedId);
    copy.setDataAcquisitionProjectId(
        source.getDataAcquisitionProjectId() + "-" + release.getVersion());
    copy.setStudyId(source.getStudyId() + "-" + release.getVersion());
    copy.setSurveyIds(createDerivedSurveyIds(source.getSurveyIds(), release.getVersion()));
    return copy;
  }

  @Override
  public Optional<DataSet> findPredecessorOfShadowCopy(DataSet shadowCopy, String previousVersion) {
    String shadowCopyId = shadowCopy.getMasterId() + "-" + previousVersion;
    if (shadowCopy.getId().equals(shadowCopyId)) {
      return Optional.empty();
    } else {
      return crudHelper.read(shadowCopyId);
    }
  }

  @Override
  public void updatePredecessor(DataSet predecessor) {
    crudHelper.saveShadow(predecessor);
  }

  @Override
  public void saveShadowCopy(DataSet shadowCopy) {
    crudHelper.saveShadow(shadowCopy);
  }

  @Override
  public Stream<DataSet> findShadowCopiesWithDeletedMasters(String projectId,
      String previousVersion) {
    String previousProjectId = projectId + "-" + previousVersion;
    return dataSetRepository.streamByDataAcquisitionProjectIdAndShadowIsTrue(previousProjectId)
        .filter(shadowCopy -> !dataSetRepository.existsById(shadowCopy.getMasterId()));
  }

  private static List<String> createDerivedSurveyIds(List<String> surveyIds, String version) {
    return surveyIds.stream().map(surveyId -> surveyId + "-" + version)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteExistingShadowCopies(String projectId, String version) {
    String oldProjectId = projectId + "-" + version;
    try (Stream<DataSet> dataSets = dataSetRepository
        .findByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(oldProjectId)) {
      dataSets.forEach(crudHelper::deleteShadow);
    }
  }

  @Override
  public void updateElasticsearch(String dataAcquisitionProjectId, String releaseVersion,
      String previousVersion) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      return dataSetRepository
          .streamIdsByDataAcquisitionProjectId(dataAcquisitionProjectId + "-" + releaseVersion);
    }, ElasticsearchType.data_sets);
    if (!StringUtils.isEmpty(previousVersion)) {
      elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
        return dataSetRepository
            .streamIdsByDataAcquisitionProjectId(dataAcquisitionProjectId + "-" + previousVersion);
      }, ElasticsearchType.data_sets);
    }
  }
  
  @Override
  public void hideExistingShadowCopies(String projectId, String version) {
    setHiddenState(projectId, version, true);
  }
  
  private void setHiddenState(String projectId, String version, boolean hidden) {
    String shadowId = projectId + "-" + version;
    try (Stream<DataSet> dataSets =
        dataSetRepository.streamByDataAcquisitionProjectIdAndShadowIsTrue(shadowId)) {
      dataSets.forEach(shadow -> {
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
