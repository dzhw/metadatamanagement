package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataProvider;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for creating shadow copies of surveys.
 */
@Service
public class SurveyShadowCopyDataProvider implements ShadowCopyDataProvider {

  private SurveyRepository surveyRepository;

  public SurveyShadowCopyDataProvider(SurveyRepository surveyRepository) {
    this.surveyRepository = surveyRepository;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<AbstractShadowableRdcDomainObject> getMasters(String dataAcquisitionProjectId) {
    return (List) surveyRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public AbstractShadowableRdcDomainObject createShadowCopy(
      AbstractShadowableRdcDomainObject source, String version) {
    if (!(source instanceof Survey)) {
      throw new IllegalArgumentException(this.getClass().getSimpleName() + " only accepts "
          + Survey.class.getName());
    }

    Survey survey = new Survey((Survey) source);
    survey.setId(survey.getId() + "-" + version);
    survey.setDataAcquisitionProjectId(survey.getDataAcquisitionProjectId() + "-" + version);
    survey.setShadow(true);
    return survey;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<AbstractShadowableRdcDomainObject> getLastShadowCopies(
      String dataAcquisitionProjectId) {
    return (List) surveyRepository
        .streamByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(
            dataAcquisitionProjectId);
  }

  @Override
  public void saveShadowCopies(List<? extends AbstractShadowableRdcDomainObject> shadowCopies) {
    List<Survey> surveyCopies = shadowCopies.stream().map(copy -> {
      if (copy instanceof Survey) {
        return (Survey) copy;
      } else {
        throw new IllegalArgumentException(this.getClass().getSimpleName()
            + " cannot save object of type " + copy.getClass());
      }
    })
        .collect(Collectors.toList());
    surveyRepository.saveAll(surveyCopies);
  }
}
