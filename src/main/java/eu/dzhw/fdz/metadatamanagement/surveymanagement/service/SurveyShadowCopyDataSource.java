package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

/**
 * Provides data for creating shadow copies of {@link Survey}.
 */
@Service
public class SurveyShadowCopyDataSource implements ShadowCopyDataSource<Survey> {

  private SurveyRepository surveyRepository;

  public SurveyShadowCopyDataSource(SurveyRepository surveyRepository) {
    this.surveyRepository = surveyRepository;
  }

  @Override
  public Stream<Survey> getMasters(String dataAcquisitionProjectId) {
    return surveyRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public Survey createShadowCopy(Survey source, String version) {
    String derivedId = source.getId() + "-" + version;
    Survey copy = surveyRepository.findById(derivedId).orElseGet(Survey::new);
    BeanUtils.copyProperties(source, copy, "version");
    copy.setId(derivedId);
    copy.setDataAcquisitionProjectId(copy.getDataAcquisitionProjectId() + "-" + version);
    copy.setShadow(true);
    return copy;
  }

  @Override
  public Stream<Survey> getLastShadowCopies(String dataAcquisitionProjectId) {
    return surveyRepository.streamByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(
        dataAcquisitionProjectId);
  }

  @Override
  public List<Survey> saveShadowCopies(List<Survey> shadowCopies) {
    return surveyRepository.saveAll(shadowCopies);
  }
}
