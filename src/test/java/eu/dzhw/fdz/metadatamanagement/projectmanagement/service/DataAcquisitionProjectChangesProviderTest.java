package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Configuration;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertTrue;

public class DataAcquisitionProjectChangesProviderTest {

  private DataAcquisitionProjectChangesProvider provider;
  private String projectId;

  @Before
  public void setup() {
    List<String> oldPublishers = Arrays.asList("10", "20", "30");
    List<String> oldDataProviders = Arrays.asList("a", "b", "c");
    DataAcquisitionProject projectA = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    Configuration oldConfig = UnitTestCreateDomainObjectUtils
        .buildDataAcquisitionProjectConfiguration(oldPublishers, oldDataProviders);
    projectA.setConfiguration(oldConfig);

    List<String> newPublishers = Arrays.asList("10", "20", "40");
    List<String> newDataProviders = Arrays.asList("a", "b", "d");
    DataAcquisitionProject projectB = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    Configuration newConfig = UnitTestCreateDomainObjectUtils
        .buildDataAcquisitionProjectConfiguration(newPublishers, newDataProviders);
    projectB.setConfiguration(newConfig);

    assert (Objects.equals(projectA.getId(), projectB.getId()));

    projectId = projectA.getId();
    provider = new DataAcquisitionProjectChangesProvider();
    provider.put(projectA, projectB);
  }

  @Test
  public void getAddedPublisherUserNamesList() {
    List<String> result = provider.getAddedPublisherUserNamesList(projectId);
    assertTrue("Expected list to contain value '40'", result.contains("40"));
  }

  @Test
  public void getRemovedPublisherUserNamesList() {
    List<String> result = provider.getRemovedPublisherUserNamesList(projectId);
    assertTrue("Expected list to contain value '30'", result.contains("30"));
  }

  @Test
  public void getAddedDataProviderUserNamesList() {
    List<String> result = provider.getAddedDataProviderUserNamesList(projectId);
    assertTrue("Expected list to contain value 'd'", result.contains("d"));
  }

  @Test
  public void getRemovedDataProviderUserNamesList() {
    List<String> result = provider.getRemovedDataProviderUserNamesList(projectId);
    assertTrue("Expected list to contain value 'c", result.contains("c"));
  }


}