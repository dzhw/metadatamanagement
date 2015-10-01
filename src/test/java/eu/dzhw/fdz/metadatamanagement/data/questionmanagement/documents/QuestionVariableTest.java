/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.builders.QuestionVariableBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class QuestionVariableTest {

  @Test
  public void testEquals() {
    //Arrange
    QuestionVariable questionVariable = new QuestionVariableBuilder().withDataType("numeric")
        .withId("id").withLabel("label").withName("name").withScaleLevel("nominal").build();
    QuestionVariable questionVariable2 = new QuestionVariableBuilder().withDataType("numeric")
        .withId("id2").withLabel("label2").withName("name2").withScaleLevel("nominal").build();
    
    //Act
    boolean checkNull = questionVariable.equals(null);
    boolean checkDifferentClass = questionVariable.equals(new Object());
    boolean checkDifferentVariableDocument = questionVariable.equals(questionVariable2);
    boolean checkSame = questionVariable.equals(questionVariable);
    questionVariable.setName("name");
    questionVariable2.setName("name");
    boolean checkDifferentVariableDocumentSameName = questionVariable.equals(questionVariable2);
    questionVariable.setLabel("label");
    questionVariable2.setLabel("label");
    boolean checkDifferentVariableDocumentSameLabel = questionVariable.equals(questionVariable2);
    questionVariable.setScaleLevel("nominal");
    questionVariable2.setScaleLevel("nominal");
    boolean checkDifferentVariableDocumentSameScaleLevel = questionVariable.equals(questionVariable2);
    questionVariable.setDataType("numeric");
    questionVariable2.setDataType("numeric");
    boolean checkDifferentVariableDocumentSameDataType = questionVariable.equals(questionVariable2);
    questionVariable.setId("id");
    questionVariable2.setId("id");
    boolean checkDifferentVariableDocumentSameId = questionVariable.equals(questionVariable2);
    
    //Assert
    assertEquals(false, checkNull);
    assertEquals(false, checkDifferentClass);
    assertEquals(false, checkDifferentVariableDocument);
    assertEquals(true, checkSame);
    assertEquals(false, checkDifferentVariableDocumentSameName);
    assertEquals(false, checkDifferentVariableDocumentSameLabel);
    assertEquals(false, checkDifferentVariableDocumentSameScaleLevel);
    assertEquals(false, checkDifferentVariableDocumentSameDataType);
    assertEquals(true, checkDifferentVariableDocumentSameId);

  }

  @Test
  public void testToString() {
    // Arrange
    QuestionVariable questionVariable = new QuestionVariableBuilder().withDataType("numeric")
        .withId("id").withLabel("label").withName("name").withScaleLevel("nominal").build();

    // Act

    // Assert
    assertThat(questionVariable.toString(), is(
        "QuestionVariable{id=id, name=name, dataType=numeric, label=label, scaleLevel=nominal}"));
    assertThat(questionVariable.getId(), is("id"));
    assertThat(questionVariable.getLabel(), is("label"));
    assertThat(questionVariable.getName(), is("name"));
    assertThat(questionVariable.getScaleLevel(), is("nominal"));
    assertThat(questionVariable.getDataType(), is("numeric"));
  }

  @Test
  public void testHashCode() {
    // Arrange
    QuestionVariable questionVariable = new QuestionVariableBuilder().withDataType("numeric")
        .withId("id").withLabel("label").withName("name").withScaleLevel("nominal").build();
    QuestionVariable questionVariable2 = new QuestionVariableBuilder().withDataType("numeric")
        .withId("id2").withLabel("label2").withName("name2").withScaleLevel("nominal").build();

    // Act

    // Assert
    assertThat(questionVariable.hashCode(), not(0));
    assertThat(questionVariable2.hashCode(), not(0));
    assertThat(questionVariable2.hashCode(), not(questionVariable.hashCode()));
  }

}
