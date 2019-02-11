package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
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
    copy.setShadow(true);
    return copy;
  }

  @Override
  public Stream<DataAcquisitionProject> getLastShadowCopies(String dataAcquisitionProjectId) {
    return dataAcquisitionProjectRepository
        .streamByIdAndShadowIsTrueAndSuccessorIdIsNull(dataAcquisitionProjectId);
  }

  @Override
  public List<DataAcquisitionProject> saveShadowCopies(List<DataAcquisitionProject> shadowCopies) {
    return dataAcquisitionProjectRepository.saveAll(shadowCopies);
  }
}
