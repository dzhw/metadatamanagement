package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.service.AbstractShadowCopyDataProvider;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

/**
 * Provides data for creating shadow copies of {@link DataAcquisitionProject}.
 */
@Service
public class DataAcquisitionProjectShadowCopyDataProvider
    extends AbstractShadowCopyDataProvider<DataAcquisitionProject> {

  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  public DataAcquisitionProjectShadowCopyDataProvider(
      DataAcquisitionProjectRepository dataAcquisitionProjectRepository) {
    super(DataAcquisitionProject.class);
    this.dataAcquisitionProjectRepository = dataAcquisitionProjectRepository;
  }

  @Override
  protected void internalSave(List<DataAcquisitionProject> shadowCopies) {
    dataAcquisitionProjectRepository.saveAll(shadowCopies);
  }

  @Override
  protected AbstractShadowableRdcDomainObject internalCopy(
      DataAcquisitionProject shadowableRdcDomainObject, String version) {

    DataAcquisitionProject copy = new DataAcquisitionProject(shadowableRdcDomainObject);
    copy.setId(shadowableRdcDomainObject.getId() + "-" + version);
    return copy;
  }

  @Override
  protected Stream<DataAcquisitionProject> internalGetMasters(String dataAcquisitionProjectId) {
    return dataAcquisitionProjectRepository.streamByIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  protected Stream<DataAcquisitionProject> internalGetLastShadowCopies(
      String dataAcquisitionProjectId) {

    return dataAcquisitionProjectRepository
        .streamByIdAndShadowIsTrueAndSuccessorIdIsNull(dataAcquisitionProjectId);
  }
}
