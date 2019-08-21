package eu.dzhw.fdz.metadatamanagement.projectmanagement.service.helper;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import lombok.RequiredArgsConstructor;

/**
 * Provides data for creating shadow copies of {@link DataAcquisitionProject}.
 */
@Component
@RequiredArgsConstructor
public class DataAcquisitionProjectShadowCopyDataSource
    implements ShadowCopyDataSource<DataAcquisitionProject> {

  private final DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  private final DataAcquisitionProjectCrudHelper crudHelper;

  @Override
  public Stream<DataAcquisitionProject> getMasters(String dataAcquisitionProjectId) {
    return dataAcquisitionProjectRepository.streamByIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public DataAcquisitionProject createShadowCopy(DataAcquisitionProject source, Release release) {
    String derivedId = source.getId() + "-" + release.getVersion();
    DataAcquisitionProject copy = crudHelper.read(derivedId)
        .orElseGet(DataAcquisitionProject::new);
    BeanUtils.copyProperties(source, copy, "version");
    copy.setId(derivedId);
    copy.setRelease(release);
    return copy;
  }

  @Override
  public Optional<DataAcquisitionProject> findPredecessorOfShadowCopy(
      DataAcquisitionProject shadowCopy, String previousVersion) {
    String shadowCopyId = shadowCopy.getMasterId() + "-" + previousVersion;
    if (shadowCopy.getId().equals(shadowCopyId)) {
      return Optional.empty();
    } else {
      return crudHelper.read(shadowCopyId);

    }
  }

  @Override
  public void updatePredecessor(DataAcquisitionProject predecessor) {
    crudHelper.saveShadow(predecessor);
  }

  @Override
  public void saveShadowCopy(DataAcquisitionProject shadowCopy) {
    crudHelper.saveShadow(shadowCopy);
  }

  @Override
  public Stream<DataAcquisitionProject> findShadowCopiesWithDeletedMasters(String projectId,
      String previousVersion) {
    String previousProjectId = projectId + "-" + previousVersion;
    return dataAcquisitionProjectRepository.streamByIdAndShadowIsTrue(previousProjectId).filter(
        shadowCopy -> !dataAcquisitionProjectRepository.existsById(shadowCopy.getMasterId()));
  }

  @Override
  public void deleteExistingShadowCopies(String projectId, String version) {
    String oldProjectId = projectId + "-" + version;
    try (Stream<DataAcquisitionProject> projects = dataAcquisitionProjectRepository
        .findByIdAndShadowIsTrueAndSuccessorIdIsNull(oldProjectId)) {
      projects.forEach(crudHelper::deleteShadow);
    }
  }

  @Override
  public void updateElasticsearch(String dataAcquisitionProjectId, String releaseVersion,
      String previousVersion) {
    throw new IllegalAccessError(
        "DataAcquisitionProjects are currently not indexed in elasticsearch");
  }
}
