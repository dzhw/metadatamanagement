/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.variablemanagement.search.document;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.common.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.VariableSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchIndices;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ScaleLevels;
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
    List<String> surveyTitles = new ArrayList<String>();
    surveyTitles.add("SurveyTitleTest");
    
    searchDocument.setId("idTest");
    searchDocument.setName("name");
    searchDocument.setDataAcquisitionProjectId("AnotherRDCID");
    searchDocument.setLabel("ALabelTest");
    searchDocument.setDataType("numeric");
    searchDocument.setScaleLevel("norminal");
    searchDocument.setSurveyTitles(surveyTitles);


    // Assert
    assertThat(searchDocument.getId(), is("idTest"));
    assertThat(searchDocument.getName(), is("name"));
    assertThat(searchDocument.getDataAcquisitionProjectId(), is("AnotherRDCID"));
    assertThat(searchDocument.getLabel(), is("ALabelTest"));
    assertThat(searchDocument.getDataType(), is("numeric"));
    assertThat(searchDocument.getScaleLevel(), is("norminal"));
    assertThat(searchDocument.getSurveyTitles(), hasItem("SurveyTitleTest"));

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
    List<String> surveyTitles = new ArrayList<String>();
    surveyTitles.add("SurveyTitleTest");
    
    searchDocument.setId("idTest");
    searchDocument.setName("name");
    searchDocument.setDataAcquisitionProjectId("AnotherRDCID");
    searchDocument.setLabel("ALabelTest");
    searchDocument.setDataType("numeric");
    searchDocument.setScaleLevel("nominal");
    searchDocument.setSurveyTitles(surveyTitles);


    // Assert
    assertThat(searchDocument.getId(), is("idTest"));
    assertThat(searchDocument.getName(), is("name"));
    assertThat(searchDocument.getDataAcquisitionProjectId(), is("AnotherRDCID"));
    assertThat(searchDocument.getLabel(), is("ALabelTest"));
    assertThat(searchDocument.getDataType(), is("numeric"));
    assertThat(searchDocument.getScaleLevel(), is("nominal"));
    assertThat(searchDocument.getSurveyTitles(), hasItem("SurveyTitleTest"));

  }

}
