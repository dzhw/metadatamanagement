package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.helper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import lombok.RequiredArgsConstructor;

/**
 * Provides data for creating shadow copies of {@link Instrument}.
 */
@Component
@RequiredArgsConstructor
public class InstrumentShadowCopyDataSource implements ShadowCopyDataSource<Instrument> {

  private final InstrumentRepository instrumentRepository;

  private final InstrumentCrudHelper crudHelper;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  @Override
  public Stream<Instrument> getMasters(String dataAcquisitionProjectId) {
    return instrumentRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public Instrument createShadowCopy(Instrument source, Release release) {
    String derivedId = source.getId() + "-" + release.getVersion();
    Instrument copy = crudHelper.read(derivedId).orElseGet(Instrument::new);
    BeanUtils.copyProperties(source, copy, "version");
    copy.setId(derivedId);
    copy.setDataAcquisitionProjectId(
        source.getDataAcquisitionProjectId() + "-" + release.getVersion());
    copy.setDataPackageId(source.getDataPackageId() + "-" + release.getVersion());
    copy.setSurveyIds(createDerivedSurveyIds(source.getSurveyIds(), release.getVersion()));
    return copy;
  }

  @Override
  public Optional<Instrument> findPredecessorOfShadowCopy(Instrument shadowCopy,
      String previousVersion) {
    String shadowCopyId = shadowCopy.getMasterId() + "-" + previousVersion;
    if (shadowCopy.getId().equals(shadowCopyId)) {
      return Optional.empty();
    } else {
      return crudHelper.read(shadowCopyId);
    }
  }

  @Override
  public void updatePredecessor(Instrument predecessor) {
    crudHelper.saveShadow(predecessor);
  }

  @Override
  public void saveShadowCopy(Instrument shadowCopy) {
    crudHelper.saveShadow(shadowCopy);
  }

  @Override
  public Stream<Instrument> findShadowCopiesWithDeletedMasters(String projectId,
      String previousVersion) {
    String oldProjectId = projectId + "-" + previousVersion;
    return instrumentRepository.streamByDataAcquisitionProjectIdAndShadowIsTrue(oldProjectId)
        .filter(shadowCopy -> !instrumentRepository.existsById(shadowCopy.getMasterId()));
  }

  private static List<String> createDerivedSurveyIds(List<String> surveyIds, String version) {
    return surveyIds.stream().map(surveyId -> surveyId + "-" + version)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteExistingShadowCopies(String projectId, String version) {
    String oldProjectId = projectId + "-" + version;
    try (Stream<Instrument> instruments = instrumentRepository
        .findByDataAcquisitionProjectIdAndShadowIsTrue(oldProjectId)) {
      instruments.forEach(crudHelper::deleteShadow);
    }
  }

  @Override
  public void updateElasticsearch(String dataAcquisitionProjectId, String releaseVersion,
      String previousVersion) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      return instrumentRepository
          .streamIdsByDataAcquisitionProjectId(dataAcquisitionProjectId + "-" + releaseVersion);
    }, ElasticsearchType.instruments);
    if (!StringUtils.isEmpty(previousVersion)) {
      elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
        return instrumentRepository
            .streamIdsByDataAcquisitionProjectId(dataAcquisitionProjectId + "-" + previousVersion);
      }, ElasticsearchType.instruments);
    }
  }
  
  @Override
  public void hideExistingShadowCopies(String projectId, String version) {
    setHiddenState(projectId, version, true);
  }
  
  private void setHiddenState(String projectId, String version, boolean hidden) {
    String shadowId = projectId + "-" + version;
    try (Stream<Instrument> instruments =
        instrumentRepository.streamByDataAcquisitionProjectIdAndShadowIsTrue(shadowId)) {
      instruments.forEach(shadow -> {
        shadow.setHidden(hidden);
        crudHelper.saveShadow(shadow);
      });
    }
  }
  
  @Override
  public void unhideExistingShadowCopies(String projectId, String version) {
    setHiddenState(projectId, version, false);
  }
}
