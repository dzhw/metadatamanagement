package eu.dzhw.fdz.metadatamanagement.searchmanagement.service;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataSetSubDocument;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;

public class CopyPropertiesTest extends AbstractTest {

  @Autowired
  private DataSetRepository dataSetRepository;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private JaversService javersService;

  @After
  public void cleanUp() {
    this.dataAcquisitionProjectRepository.deleteAll();
    this.dataSetRepository.deleteAll();
    this.javersService.deleteAll();
  }

  @Test
  public void testCollectionPropertiesAreCopied() {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());

    DataSet dataSet =
        UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(), 1);

    dataSetRepository.save(dataSet);

    DataSetSubDocumentProjection projection =
        dataSetRepository.findOneSubDocumentById(dataSet.getId());
    // in spring boot 2.4.x BeanUtils.copyProperties does not copy collection properties
    DataSetSubDocument copy = new DataSetSubDocument(projection);

    assertNotNull("SubDataSets have not been copied!", copy.getSubDataSets());
  }
}
