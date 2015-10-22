/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.common.document;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.RelatedVariable;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.RelatedVariableBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class RelatedVariableTest {

  @Test
  public void testEquals() {
    //Arrange
    RelatedVariable relatedVariable = new RelatedVariableBuilder().withDataType("numeric")
        .withId("id").withLabel("label").withName("name").withScaleLevel("nominal").build();
    RelatedVariable relatedVariable2 = new RelatedVariableBuilder().withDataType("numeric")
        .withId("id2").withLabel("label2").withName("name2").withScaleLevel("nominal").build();
    
    //Act
    boolean checkNull = relatedVariable.equals(null);
    boolean checkDifferentClass = relatedVariable.equals(new Object());
    boolean checkDifferentVariableDocument = relatedVariable.equals(relatedVariable2);
    boolean checkSame = relatedVariable.equals(relatedVariable);
    relatedVariable.setName("name");
    relatedVariable2.setName("name");
    boolean checkDifferentVariableDocumentSameName = relatedVariable.equals(relatedVariable2);
    relatedVariable.setLabel("label");
    relatedVariable2.setLabel("label");
    boolean checkDifferentVariableDocumentSameLabel = relatedVariable.equals(relatedVariable2);
    relatedVariable.setScaleLevel("nominal");
    relatedVariable2.setScaleLevel("nominal");
    boolean checkDifferentVariableDocumentSameScaleLevel = relatedVariable.equals(relatedVariable2);
    relatedVariable.setDataType("numeric");
    relatedVariable2.setDataType("numeric");
    boolean checkDifferentVariableDocumentSameDataType = relatedVariable.equals(relatedVariable2);
    relatedVariable.setId("id");
    relatedVariable2.setId("id");
    boolean checkDifferentVariableDocumentSameId = relatedVariable.equals(relatedVariable2);
    
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
    RelatedVariable relatedVariable = new RelatedVariableBuilder().withDataType("numeric")
        .withId("id").withLabel("label").withName("name").withScaleLevel("nominal").build();

    // Act

    // Assert
    assertThat(relatedVariable.toString(), is(
        "RelatedVariable{id=id, name=name, dataType=numeric, label=label, scaleLevel=nominal}"));
    assertThat(relatedVariable.getId(), is("id"));
    assertThat(relatedVariable.getLabel(), is("label"));
    assertThat(relatedVariable.getName(), is("name"));
    assertThat(relatedVariable.getScaleLevel(), is("nominal"));
    assertThat(relatedVariable.getDataType(), is("numeric"));
  }

  @Test
  public void testHashCode() {
    // Arrange
    RelatedVariable relatedVariable = new RelatedVariableBuilder().withDataType("numeric")
        .withId("id").withLabel("label").withName("name").withScaleLevel("nominal").build();
    RelatedVariable relatedVariable2 = new RelatedVariableBuilder().withDataType("numeric")
        .withId("id2").withLabel("label2").withName("name2").withScaleLevel("nominal").build();

    // Act

    // Assert
    assertThat(relatedVariable.hashCode(), not(0));
    assertThat(relatedVariable2.hashCode(), not(0));
    assertThat(relatedVariable2.hashCode(), not(relatedVariable.hashCode()));
  }

}
