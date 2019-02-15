package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;

/**
 * Provides data for creating shadow copies of {@link Instrument}.
 */
@Service
public class InstrumentShadowCopyDataSource implements ShadowCopyDataSource<Instrument> {

  private InstrumentRepository instrumentRepository;

  public InstrumentShadowCopyDataSource(InstrumentRepository instrumentRepository) {
    this.instrumentRepository = instrumentRepository;
  }

  @Override
  public Stream<Instrument> getMasters(String dataAcquisitionProjectId) {
    return instrumentRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public Instrument createShadowCopy(Instrument source, String version) {
    String derivedId = source.getId() + "-" + version;
    Instrument copy = instrumentRepository.findById(derivedId).orElseGet(Instrument::new);
    BeanUtils.copyProperties(source, copy, "version");
    copy.setId(derivedId);
    copy.setDataAcquisitionProjectId(source.getDataAcquisitionProjectId() + "-" + version);
    copy.setStudyId(source.getStudyId() + "-" + version);
    copy.setSurveyIds(createDerivedSurveyIds(source.getSurveyIds(), version));
    return copy;
  }

  @Override
  public Optional<Instrument> findPredecessorOfShadowCopy(Instrument shadowCopy, String previousVersion) {
    String shadowCopyId = shadowCopy + "-" + previousVersion;
    return instrumentRepository.findById(shadowCopyId);
  }

  @Override
  public void updatePredecessor(Instrument predecessor) {
    instrumentRepository.save(predecessor);
  }

  @Override
  public void saveShadowCopy(Instrument shadowCopy) {
    instrumentRepository.save(shadowCopy);
  }

  @Override
  public Stream<Instrument> findShadowCopiesWithDeletedMasters(String projectId,
      String lastVersion) {
    String oldProjectId = projectId + "-" + lastVersion;
    return instrumentRepository
        .streamByDataAcquisitionProjectIdAndSuccessorIdIsNullAndShadowIsTrue(oldProjectId)
        .filter(shadowCopy -> !instrumentRepository.existsById(shadowCopy.getMasterId()));
  }

  private static List<String> createDerivedSurveyIds(List<String> surveyIds, String version) {
    return surveyIds.stream().map(surveyId -> surveyId + "-" + version)
        .collect(Collectors.toList());
  }
}
