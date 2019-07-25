package eu.dzhw.fdz.metadatamanagement.surveymanagement.service.helper;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;

/**
 * Provides data for creating shadow copies of {@link Survey}.
 */
@Component
@RequiredArgsConstructor
public class SurveyShadowCopyDataSource implements ShadowCopyDataSource<Survey> {

  private final SurveyRepository surveyRepository;
  
  private final SurveyCrudHelper crudHelper;

  @Override
  public Stream<Survey> getMasters(String dataAcquisitionProjectId) {
    return surveyRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public Survey createShadowCopy(Survey source, String version) {
    String derivedId = source.getId() + "-" + version;
    Survey copy = crudHelper.read(derivedId).orElseGet(Survey::new);
    BeanUtils.copyProperties(source, copy, "version");
    copy.setId(derivedId);
    copy.setDataAcquisitionProjectId(source.getDataAcquisitionProjectId() + "-" + version);
    copy.setStudyId(source.getStudyId() + "-" + version);
    return copy;
  }

  @Override
  public Optional<Survey> findPredecessorOfShadowCopy(Survey shadowCopy, String previousVersion) {
    String shadowCopyId = shadowCopy.getMasterId() + "-" + previousVersion;
    if (shadowCopy.getId().equals(shadowCopyId)) {
      return Optional.empty();
    } else {
      return crudHelper.read(shadowCopyId);
    }
  }

  @Override
  public void updatePredecessor(Survey predecessor) {
    crudHelper.saveShadow(predecessor);
  }

  @Override
  public void saveShadowCopy(Survey shadowCopy) {
    crudHelper.saveShadow(shadowCopy);
  }

  @Override
  public Stream<Survey> findShadowCopiesWithDeletedMasters(String projectId,
      String previousVersion) {
    String oldProjectId = projectId + "-" + previousVersion;
    return surveyRepository.streamByDataAcquisitionProjectIdAndShadowIsTrue(oldProjectId)
        .filter(shadowCopy -> !surveyRepository.existsById(shadowCopy.getMasterId()));
  }

  @Override
  public void deleteExistingShadowCopies(String projectId, String version) {
    String oldProjectId = projectId + "-" + version;
    try (Stream<Survey> surveys = surveyRepository
        .findByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(oldProjectId)) {
      surveys.forEach(crudHelper::delete); 
    }
  }
}
