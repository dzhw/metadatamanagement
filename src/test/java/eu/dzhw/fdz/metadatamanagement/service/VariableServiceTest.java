/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.domain.builders.VariableBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.DataType;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.ScaleLevel;
import eu.dzhw.fdz.metadatamanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.service.exception.EntityExistsException;

/**
 * @author Daniel Katzberg
 *
 */
public class VariableServiceTest extends AbstractTest {

  @Inject
  private VariableRepository variableRepository;

  @Inject
  private VariableService variableService;

  private Variable variable1;
  private Variable variable2;

  @After
  public void afterEachTest() {
    if (this.variableRepository.exists(this.variable1.getId())) {
      this.variableService.deleteVariable(this.variable1.getId());
    }

    if (this.variableRepository.exists(this.variable2.getId())) {
      this.variableService.deleteVariable(this.variable2.getId());
    }
  }


  @Test(expected = EntityExistsException.class)
  public void testDoublicatedKeyById() {
    // Arrange
    this.variable1 = new VariableBuilder().withDataType(DataType.string)
      .withScaleLevel(ScaleLevel.ordinal)
      .withId("Id1")
      .withFdzProjectName("Project1")
      .withName("Name1")
      .withLabel("Label")
      .build();

    this.variable2 = new VariableBuilder().withDataType(DataType.string)
      .withScaleLevel(ScaleLevel.ordinal)
      .withId("Id1")
      .withFdzProjectName("Project1")
      .withName("Name2")
      .withLabel("Label")
      .build();

    // Act
    this.variableService.createVariable(this.variable1);
    this.variableService.createVariable(this.variable2);

    // Assert
  }

  @Test(expected = EntityExistsException.class)
  public void testDoublicatedKeyByCompoundIndex() {
    // Arrange
    this.variable1 = new VariableBuilder().withDataType(DataType.string)
      .withScaleLevel(ScaleLevel.ordinal)
      .withId("Id1")
      .withFdzProjectName("Project1")
      .withName("Name1")
      .withLabel("Label")
      .build();

    this.variable2 = new VariableBuilder().withDataType(DataType.string)
      .withScaleLevel(ScaleLevel.ordinal)
      .withId("Id2")
      .withFdzProjectName("Project1")
      .withName("Name1")
      .withLabel("Label")
      .build();

    // Act
    this.variableService.createVariable(this.variable1);
    this.variableService.createVariable(this.variable2);

    // Assert
  }
  
  @Test
  public void testSameNameDifferentProject() {
    // Arrange
    this.variable1 = new VariableBuilder().withDataType(DataType.string)
      .withScaleLevel(ScaleLevel.ordinal)
      .withId("Id1")
      .withFdzProjectName("Project1")
      .withName("Name1")
      .withLabel("Label")
      .build();

    this.variable2 = new VariableBuilder().withDataType(DataType.string)
      .withScaleLevel(ScaleLevel.ordinal)
      .withId("Id2")
      .withFdzProjectName("Project2")
      .withName("Name1")
      .withLabel("Label")
      .build();

    // Act
    this.variableService.createVariable(this.variable1);
    this.variableService.createVariable(this.variable2);

    // Assert
    assertThat(this.variableRepository.exists(this.variable1.getId()), is(true));
    assertThat(this.variableRepository.exists(this.variable2.getId()), is(true));
  }
}
