package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

/**
 * Provides data for creating shadow copies of {@link Study}.
 */
@Service
public class StudyShadowCopyDataSource implements ShadowCopyDataSource<Study> {

  private StudyRepository studyRepository;

  public StudyShadowCopyDataSource(StudyRepository studyRepository) {
    this.studyRepository = studyRepository;
  }

  @Override
  public Stream<Study> getMasters(String dataAcquisitionProjectId) {
    return studyRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public Study createShadowCopy(Study source, String version) {
    String derivedId = source.getId() + "-" + version;
    Study copy = studyRepository.findById(derivedId).orElseGet(Study::new);
    BeanUtils.copyProperties(source, copy, "version");
    copy.setId(derivedId);
    copy.setShadow(true);
    copy.setDataAcquisitionProjectId(source.getDataAcquisitionProjectId() + "-" + version);
    return copy;
  }

  @Override
  public Stream<Study> getLastShadowCopies(String dataAcquisitionProjectId) {
    return studyRepository.streamByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(
        dataAcquisitionProjectId);
  }

  @Override
  public List<Study> saveShadowCopies(List<Study> shadowCopies) {
    return studyRepository.saveAll(shadowCopies);
  }
}
