package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.helper;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import lombok.RequiredArgsConstructor;

/**
 * Provides data for creating shadow copies of {@link DataPackage}.
 */
@Component
@RequiredArgsConstructor
public class DataPackageShadowCopyDataSource implements ShadowCopyDataSource<DataPackage> {

  private final DataPackageRepository dataPackageRepository;

  private final DataPackageCrudHelper crudHelper;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  @Override
  public Stream<DataPackage> getMasters(String dataAcquisitionProjectId) {
    return dataPackageRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public DataPackage createShadowCopy(DataPackage source, Release release) {
    String derivedId = source.getId() + "-" + release.getVersion();
    DataPackage copy = crudHelper.read(derivedId).orElseGet(DataPackage::new);
    BeanUtils.copyProperties(source, copy, "version");
    copy.setId(derivedId);
    copy.setDataAcquisitionProjectId(
        source.getDataAcquisitionProjectId() + "-" + release.getVersion());
    return copy;
  }

  @Override
  public Optional<DataPackage> findPredecessorOfShadowCopy(DataPackage shadowCopy,
      String previousVersion) {
    String shadowCopyId = shadowCopy.getMasterId() + "-" + previousVersion;
    if (shadowCopy.getId().equals(shadowCopyId)) {
      return Optional.empty();
    } else {
      return crudHelper.read(shadowCopyId);
    }
  }

  @Override
  public void updatePredecessor(DataPackage predecessor) {
    crudHelper.saveShadow(predecessor);
  }

  @Override
  public void saveShadowCopy(DataPackage shadowCopy) {
    crudHelper.saveShadow(shadowCopy);
  }

  @Override
  public Stream<DataPackage> findShadowCopiesWithDeletedMasters(String projectId,
      String previousVersion) {
    String oldProjectId = projectId + "-" + previousVersion;
    return dataPackageRepository.streamByDataAcquisitionProjectIdAndShadowIsTrue(oldProjectId)
        .filter(shadowCopy -> !dataPackageRepository.existsById(shadowCopy.getMasterId()));
  }

  @Override
  public void deleteExistingShadowCopies(String projectId, String version) {
    String oldProjectId = projectId + "-" + version;
    try (Stream<DataPackage> dataPackages = dataPackageRepository
        .findByDataAcquisitionProjectIdAndShadowIsTrue(oldProjectId)) {
      dataPackages.forEach(crudHelper::deleteShadow);
    }
  }

  @Override
  public void updateElasticsearch(String dataAcquisitionProjectId, String releaseVersion,
      String previousVersion) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      return dataPackageRepository
          .streamIdsByDataAcquisitionProjectId(dataAcquisitionProjectId + "-" + releaseVersion);
    }, ElasticsearchType.data_packages);
    if (!StringUtils.isEmpty(previousVersion)) {
      elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
        return dataPackageRepository
            .streamIdsByDataAcquisitionProjectId(dataAcquisitionProjectId + "-" + previousVersion);
      }, ElasticsearchType.data_packages);
    }
  }

  @Override
  public void hideExistingShadowCopies(String projectId, String version) {
    setHiddenState(projectId, version, true);
  }

  private void setHiddenState(String projectId, String version, boolean hidden) {
    String shadowId = projectId + "-" + version;
    try (Stream<DataPackage> dataPackages =
        dataPackageRepository.streamByDataAcquisitionProjectIdAndShadowIsTrue(shadowId)) {
      dataPackages.forEach(shadow -> {
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
