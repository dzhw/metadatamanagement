package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.helper;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import lombok.RequiredArgsConstructor;

/**
 * Provides data for creating shadow copies of {@link AnalysisPackage}.
 */
@Component
@RequiredArgsConstructor
public class AnalysisPackageShadowCopyDataSource implements ShadowCopyDataSource<AnalysisPackage> {

  private final AnalysisPackageRepository analysisPackageRepository;

  private final AnalysisPackageCrudHelper crudHelper;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  @Override
  public Stream<AnalysisPackage> getMasters(String dataAcquisitionProjectId) {
    return analysisPackageRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public AnalysisPackage createShadowCopy(AnalysisPackage source, Release release) {
    String derivedId = source.getId() + "-" + release.getVersion();
    AnalysisPackage copy = crudHelper.read(derivedId).orElseGet(AnalysisPackage::new);
    BeanUtils.copyProperties(source, copy, "version");
    copy.setId(derivedId);
    copy.setDataAcquisitionProjectId(
        source.getDataAcquisitionProjectId() + "-" + release.getVersion());
    return copy;
  }

  @Override
  public Optional<AnalysisPackage> findPredecessorOfShadowCopy(AnalysisPackage shadowCopy,
      String previousVersion) {
    String shadowCopyId = shadowCopy.getMasterId() + "-" + previousVersion;
    if (shadowCopy.getId().equals(shadowCopyId)) {
      return Optional.empty();
    } else {
      return crudHelper.read(shadowCopyId);
    }
  }

  @Override
  public void updatePredecessor(AnalysisPackage predecessor) {
    crudHelper.saveShadow(predecessor);
  }

  @Override
  public void saveShadowCopy(AnalysisPackage shadowCopy) {
    crudHelper.saveShadow(shadowCopy);
  }

  @Override
  public Stream<AnalysisPackage> findShadowCopiesWithDeletedMasters(String projectId,
      String previousVersion) {
    String oldProjectId = projectId + "-" + previousVersion;
    return analysisPackageRepository.streamByDataAcquisitionProjectIdAndShadowIsTrue(oldProjectId)
        .filter(shadowCopy -> !analysisPackageRepository.existsById(shadowCopy.getMasterId()));
  }

  @Override
  public void deleteExistingShadowCopies(String projectId, String version) {
    String oldProjectId = projectId + "-" + version;
    try (Stream<AnalysisPackage> analysisPackages = analysisPackageRepository
        .findByDataAcquisitionProjectIdAndShadowIsTrue(oldProjectId)) {
      analysisPackages.forEach(crudHelper::deleteShadow);
    }
  }

  @Override
  public void updateElasticsearch(String dataAcquisitionProjectId, String releaseVersion,
      String previousVersion) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      return analysisPackageRepository
          .streamIdsByDataAcquisitionProjectId(dataAcquisitionProjectId + "-" + releaseVersion);
    }, ElasticsearchType.analysis_packages);
    if (!StringUtils.isEmpty(previousVersion)) {
      elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
        return analysisPackageRepository
            .streamIdsByDataAcquisitionProjectId(dataAcquisitionProjectId + "-" + previousVersion);
      }, ElasticsearchType.analysis_packages);
    }
  }

  @Override
  public void hideExistingShadowCopies(String projectId, String version) {
    setHiddenState(projectId, version, true);
  }

  private void setHiddenState(String projectId, String version, boolean hidden) {
    String shadowId = projectId + "-" + version;
    try (Stream<AnalysisPackage> analysisPackages =
        analysisPackageRepository.streamByDataAcquisitionProjectIdAndShadowIsTrue(shadowId)) {
      analysisPackages.forEach(shadow -> {
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
