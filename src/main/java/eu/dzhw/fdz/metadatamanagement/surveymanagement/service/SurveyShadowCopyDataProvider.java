package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import eu.dzhw.fdz.metadatamanagement.common.service.AbstractShadowCopyDataProvider;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

/**
 * Provides data for creating shadow copies of {@link Survey}.
 */
@Service
public class SurveyShadowCopyDataProvider extends AbstractShadowCopyDataProvider<Survey> {

  private SurveyRepository surveyRepository;

  public SurveyShadowCopyDataProvider(SurveyRepository surveyRepository) {
    super(Survey.class);
    this.surveyRepository = surveyRepository;
  }

  @Override
  protected Survey internalCopy(Survey source, String version) {
    Survey survey = new Survey(source);
    survey.setId(survey.getId() + "-" + version);
    survey.setDataAcquisitionProjectId(survey.getDataAcquisitionProjectId() + "-" + version);
    return survey;
  }

  @Override
  protected void internalSave(List<Survey> shadowCopies) {
    surveyRepository.saveAll(shadowCopies);
  }

  @Override
  protected Stream<Survey> internalGetMasters(String dataAcquisitionProjectId) {
    return surveyRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  protected Stream<Survey> internalGetLastShadowCopies(String dataAcquisitionProjectId) {
    return surveyRepository.streamByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(
        dataAcquisitionProjectId);
  }
}
