package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataProvider;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;

/**
 * Provides data for creating shadow copies of {@link Survey}.
 */
@Service
public class SurveyShadowCopyDataProvider implements ShadowCopyDataProvider<Survey> {

  private SurveyRepository surveyRepository;

  public SurveyShadowCopyDataProvider(SurveyRepository surveyRepository) {
    this.surveyRepository = surveyRepository;
  }

  @Override
  public Stream<Survey> getMasters(String dataAcquisitionProjectId) {
    return surveyRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public Survey createShadowCopy(Survey source, String version) {
    Survey survey = new Survey(source);
    survey.setId(survey.getId() + "-" + version);
    survey.setDataAcquisitionProjectId(survey.getDataAcquisitionProjectId() + "-" + version);
    return survey;
  }

  @Override
  public Stream<Survey> getLastShadowCopies(String dataAcquisitionProjectId) {
    return surveyRepository.streamByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(
        dataAcquisitionProjectId);
  }

  @Override
  public void saveShadowCopies(List<Survey> shadowCopies) {
    surveyRepository.saveAll(shadowCopies);
  }
}
