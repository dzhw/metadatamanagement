package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Provides data for creating shadow copies of {@link DataAcquisitionProject}.
 */
@Service
public class DataAcquisitionProjectShadowCopyDataSource
    implements ShadowCopyDataSource<DataAcquisitionProject> {

  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  public DataAcquisitionProjectShadowCopyDataSource(
      DataAcquisitionProjectRepository dataAcquisitionProjectRepository) {
    this.dataAcquisitionProjectRepository = dataAcquisitionProjectRepository;
  }

  @Override
  public Stream<DataAcquisitionProject> getMasters(String dataAcquisitionProjectId) {
    return dataAcquisitionProjectRepository.streamByIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public DataAcquisitionProject createShadowCopy(DataAcquisitionProject source, String version) {
    String derivedId = source.getId() + "-" + version;
    DataAcquisitionProject copy = dataAcquisitionProjectRepository.findById(derivedId)
        .orElseGet(DataAcquisitionProject::new);
    BeanUtils.copyProperties(source, copy, "version");
    copy.setId(derivedId);
    return copy;
  }

  @Override
  public Optional<DataAcquisitionProject> findPredecessorOfShadowCopy(DataAcquisitionProject shadowCopy,
                                                                      String previousVersion) {
    String shadowCopyId = shadowCopy + "-" + previousVersion;
    return dataAcquisitionProjectRepository.findById(shadowCopyId);
  }

  @Override
  public void updatePredecessor(DataAcquisitionProject predecessor) {
    dataAcquisitionProjectRepository.save(predecessor);
  }

  @Override
  public void saveShadowCopy(DataAcquisitionProject shadowCopy) {
    dataAcquisitionProjectRepository.save(shadowCopy);
  }

  @Override
  public Stream<DataAcquisitionProject> findShadowCopiesWithDeletedMasters(String projectId,
      String lastVersion) {
    String previousProjectId = projectId + "-" + lastVersion;
    return dataAcquisitionProjectRepository
        .streamByIdAndShadowIsTrueAndSuccessorIdIsNull(previousProjectId)
        .filter(shadowCopy -> !dataAcquisitionProjectRepository
            .existsById(shadowCopy.getMasterId()));
  }
}
