/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
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

  @Before
  public void beforeTest() {
    this.fdzProject = new FdzProjectBuilder().withCufDoi("CufDoi")
      .withName("Name")
      .withSufDoi("SufDoi")
      .build();
  }

  @After
  public void afterEachTest() {
    this.fdzProjectRepository.delete(this.fdzProject.getName());
  }

  @Test(expected = EntityExistsException.class)
  public void testDuplicatedFdzProject() {
    // Arrange

    // Act
    this.fdzProjectService.createFdzProject(this.fdzProject);
    this.fdzProjectService.createFdzProject(this.fdzProject);

    // Assert
  }

  @Test(expected = EntityNotFoundException.class)
  public void testNotUpdateableFdzProject() {
    // Arrange

    // Act
    this.fdzProjectService.updateFdzProject(this.fdzProject);

    // Assert
  }

  @Test
  public void testFindAll() {
    // Arrange
    this.fdzProjectRepository.insert(this.fdzProject);
    
    // Act
    List<FdzProject> allFdzProjects = this.fdzProjectService.findAll();

    // Assert
    assertThat(allFdzProjects.size(), is(1));
    assertThat(allFdzProjects.get(0), is(this.fdzProject));

  }

}
