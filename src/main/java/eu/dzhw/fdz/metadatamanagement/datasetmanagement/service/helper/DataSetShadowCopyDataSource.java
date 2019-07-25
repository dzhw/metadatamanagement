package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.helper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyDataSource;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import lombok.RequiredArgsConstructor;

/**
 * Provides data for creating shadow copies of {@link DataSet}.
 */
@Component
@RequiredArgsConstructor
public class DataSetShadowCopyDataSource implements ShadowCopyDataSource<DataSet> {
  private final DataSetRepository dataSetRepository;
  
  private final DataSetCrudHelper crudHelper;

  @Override
  public Stream<DataSet> getMasters(String dataAcquisitionProjectId) {
    return dataSetRepository
        .streamByDataAcquisitionProjectIdAndShadowIsFalse(dataAcquisitionProjectId);
  }

  @Override
  public DataSet createShadowCopy(DataSet source, String version) {
    String derivedId = source.getId() + "-" + version;
    DataSet copy = crudHelper.read(derivedId).orElseGet(DataSet::new);
    BeanUtils.copyProperties(source, copy, "version");
    copy.setId(derivedId);
    copy.setDataAcquisitionProjectId(source.getDataAcquisitionProjectId() + "-" + version);
    copy.setStudyId(source.getStudyId() + "-" + version);
    copy.setSurveyIds(createDerivedSurveyIds(source.getSurveyIds(), version));
    return copy;
  }

  @Override
  public Optional<DataSet> findPredecessorOfShadowCopy(DataSet shadowCopy, String previousVersion) {
    String shadowCopyId = shadowCopy.getMasterId() + "-" + previousVersion;
    if (shadowCopy.getId().equals(shadowCopyId)) {
      return Optional.empty();
    } else {
      return crudHelper.read(shadowCopyId);
    }
  }

  @Override
  public void updatePredecessor(DataSet predecessor) {
    crudHelper.saveShadow(predecessor);
  }

  @Override
  public void saveShadowCopy(DataSet shadowCopy) {
    crudHelper.saveShadow(shadowCopy);
  }

  @Override
  public Stream<DataSet> findShadowCopiesWithDeletedMasters(String projectId,
      String previousVersion) {
    String previousProjectId = projectId + "-" + previousVersion;
    return dataSetRepository.streamByDataAcquisitionProjectIdAndShadowIsTrue(previousProjectId)
        .filter(shadowCopy -> !dataSetRepository.existsById(shadowCopy.getMasterId()));
  }

  private static List<String> createDerivedSurveyIds(List<String> surveyIds, String version) {
    return surveyIds.stream().map(surveyId -> surveyId + "-" + version)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteExistingShadowCopies(String projectId, String version) {
    String oldProjectId = projectId + "-" + version;
    try (Stream<DataSet> dataSets = dataSetRepository
        .findByDataAcquisitionProjectIdAndShadowIsTrueAndSuccessorIdIsNull(oldProjectId)) {
      dataSets.forEach(crudHelper::delete); 
    }
  }
}
