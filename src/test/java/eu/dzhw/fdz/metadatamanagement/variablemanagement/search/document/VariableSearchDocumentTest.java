/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.variablemanagement.search.document;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.domain.ScaleLevels;
import eu.dzhw.fdz.metadatamanagement.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.service.enums.ElasticsearchIndices;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders.VariableBuilder;

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
      .withLabel(new I18nStringBuilder().withDe("TestLabel").withEn("TestLabel").build())
      .withScaleLevel(ScaleLevels.ORDINAL)
      .withDataType(DataTypes.STRING)
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
      .withLabel(new I18nStringBuilder().withDe("TestLabel").withEn("TestLabel").build())
      .withScaleLevel(ScaleLevels.ORDINAL)
      .withDataType(DataTypes.STRING)
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
