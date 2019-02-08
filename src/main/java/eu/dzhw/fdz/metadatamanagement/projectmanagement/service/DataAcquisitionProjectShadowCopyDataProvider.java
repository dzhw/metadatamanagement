package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataProvider;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;

/**
 * Provides data for creating shadow copies of {@link DataAcquisitionProject}.
 */
@Service
public class DataAcquisitionProjectShadowCopyDataProvider
    implements ShadowCopyDataProvider<DataAcquisitionProject> {

  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  public DataAcquisitionProjectShadowCopyDataProvider(
      DataAcquisitionProjectRepository dataAcquisitionProjectRepository) {
    this.dataAcquisitionProjectRepository = dataAcquisitionProjectRepository;
  }

  @Override
  public Stream<DataAcquisitionProject> getMasters(String dataAcquisitionProjectId) {
    return dataAcquisitionProjectRepository.streamByIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public DataAcquisitionProject createShadowCopy(DataAcquisitionProject source, String version) {
    DataAcquisitionProject copy = new DataAcquisitionProject(source);
    copy.setId(source.getId() + "-" + version);
    return copy;
  }

  @Override
  public Stream<DataAcquisitionProject> getLastShadowCopies(String dataAcquisitionProjectId) {
    return dataAcquisitionProjectRepository
        .streamByIdAndShadowIsTrueAndSuccessorIdIsNull(dataAcquisitionProjectId);
  }

  @Override
  public void saveShadowCopies(List<DataAcquisitionProject> shadowCopies) {
    dataAcquisitionProjectRepository.saveAll(shadowCopies);
    
  }
}
