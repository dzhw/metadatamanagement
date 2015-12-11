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

/**
 * @author Daniel Katzberg
 *
 */
public class FdZProjectServiceTest extends AbstractTest {

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
    this.fdzProjectRepository.insert(this.fdzProject);
  }

  @After
  public void afterTest() {
    this.fdzProjectRepository.delete(this.fdzProject);
  }

  @Test
  public void testFindAll() {
    // Arrange

    // Act
    List<FdzProject> allFdzProjects = this.fdzProjectService.findAll();

    // Assert
    assertThat(allFdzProjects.size(), is(1));
    assertThat(allFdzProjects.get(0), is(this.fdzProject));

  }

}
