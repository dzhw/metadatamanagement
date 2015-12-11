/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.service;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;
import eu.dzhw.fdz.metadatamanagement.domain.builders.FdzProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.repository.FdzProjectRepository;
import eu.dzhw.fdz.metadatamanagement.service.exception.EntityExistsException;
import eu.dzhw.fdz.metadatamanagement.service.exception.EntityNotFoundException;

/**
 * @author Daniel Katzberg
 *
 */
public class FdzProjectServiceTest extends AbstractTest {

  @Inject
  private FdzProjectRepository fdzProjectRepository;

  @Inject
  private FdzProjectService fdzProjectService;

  private FdzProject fdzProject;

  @After
  public void afterEachTest() {
    if (this.fdzProjectRepository.exists(this.fdzProject.getName())) {
      this.fdzProjectRepository.delete(this.fdzProject.getName());
    }
  }

  @Test(expected = EntityExistsException.class)
  public void testDuplicatedFdzProject() {
    // Arrange
    this.fdzProject = new FdzProjectBuilder().withName("Name")
      .withCufDoi("CufDoi")
      .withSufDoi("SufDoi")
      .build();

    // Act
    this.fdzProjectService.createFdzProject(this.fdzProject);
    this.fdzProjectService.createFdzProject(this.fdzProject);

    // Assert
  }

  @Test(expected=EntityNotFoundException.class)
  public void testNotUpdateableFdzProject() {
    // Arrange
    this.fdzProject = new FdzProjectBuilder().withName("Name")
      .withCufDoi("CufDoi")
      .withSufDoi("SufDoi")
      .build();

    // Act
    this.fdzProjectService.updateFdzProject(this.fdzProject);

    // Assert
  }

}
