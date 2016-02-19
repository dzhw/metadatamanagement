/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.search.document;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.domain.builders.VariableBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.DataType;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.ScaleLevel;
import eu.dzhw.fdz.metadatamanagement.service.enums.ElasticsearchIndices;

/**
 * @author Daniel Katzberg
 *
 */
public class VariableSearchDocumentTest {

  @Test
  public void testGetterAndSetterForDe() {

    // Arrange
    Variable variable = new VariableBuilder().withId("TestId")
      .withName("WithTestName")
      .withDataAcquisitionProjectId("ProjectIdTest")
      .withLabel("TestLabel")
      .withScaleLevel(ScaleLevel.ordinal)
      .withDataType(DataType.string)
      .build();
    VariableSearchDocument searchDocument =
        new VariableSearchDocument(variable, null, ElasticsearchIndices.METADATA_EN);

    // Act
    searchDocument.setId("idTest");
    searchDocument.setName("name");
    searchDocument.setDataAcquisitionProjectId("AnotherRDCID");
    searchDocument.setLabel("ALabelTest");
    searchDocument.setDataType("numeric");
    searchDocument.setScaleLevel("norminal");
    searchDocument.setSurveyTitle("SurveyTitleTest");


    // Assert
    assertThat(searchDocument.getId(), is("idTest"));
    assertThat(searchDocument.getName(), is("name"));
    assertThat(searchDocument.getDataAcquisitionProjectId(), is("AnotherRDCID"));
    assertThat(searchDocument.getLabel(), is("ALabelTest"));
    assertThat(searchDocument.getDataType(), is("numeric"));
    assertThat(searchDocument.getScaleLevel(), is("norminal"));
    assertThat(searchDocument.getSurveyTitle(), is("SurveyTitleTest"));

  }

  @Test
  public void testGetterAndSetterForEn() {

    // Arrange
    Variable variable = new VariableBuilder().withId("TestId")
      .withName("WithTestName")
      .withDataAcquisitionProjectId("ProjectIdTest")
      .withLabel("TestLabel")
      .withScaleLevel(ScaleLevel.ordinal)
      .withDataType(DataType.string)
      .build();
    VariableSearchDocument searchDocument =
        new VariableSearchDocument(variable, null, ElasticsearchIndices.METADATA_DE);

    // Act
    searchDocument.setId("idTest");
    searchDocument.setName("name");
    searchDocument.setDataAcquisitionProjectId("AnotherRDCID");
    searchDocument.setLabel("ALabelTest");
    searchDocument.setDataType("numeric");
    searchDocument.setScaleLevel("nominal");
    searchDocument.setSurveyTitle("SurveyTitleTest");


    // Assert
    assertThat(searchDocument.getId(), is("idTest"));
    assertThat(searchDocument.getName(), is("name"));
    assertThat(searchDocument.getDataAcquisitionProjectId(), is("AnotherRDCID"));
    assertThat(searchDocument.getLabel(), is("ALabelTest"));
    assertThat(searchDocument.getDataType(), is("numeric"));
    assertThat(searchDocument.getScaleLevel(), is("nominal"));
    assertThat(searchDocument.getSurveyTitle(), is("SurveyTitleTest"));

  }

}
