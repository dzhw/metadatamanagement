/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Daniel Katzberg
 *
 */
public class AnswerOptionsTest {
  
  @Test
  public void testHashCode(){
    //Arrange
    AnswerOption answerOption = new AnswerOption();
    answerOption.setCode(1);
    answerOption.setLabel("Label 1");
    
    //Act
        
    //Assert
    assertEquals(1601450437, answerOption.hashCode());
  }
  
  @Test
  public void testEquals(){
    //Arrange
    AnswerOption answerOption = new AnswerOption();
    AnswerOption answerOption2 = new AnswerOption();
    
    //Act
    boolean checkSame = answerOption.equals(answerOption);
    boolean checkNull = answerOption.equals(null);
    boolean checkOtherClass = answerOption.equals(new Object());
    answerOption2.setCode(2);
    boolean checkCodeOther = answerOption.equals(answerOption2);
    answerOption.setCode(1);
    boolean checkCodeBoth = answerOption.equals(answerOption2);
    answerOption.setCode(2);
    boolean checkCodeBothSame = answerOption.equals(answerOption2);
    answerOption.setCode(null);
    answerOption2.setCode(null);
    answerOption2.setLabel("Label 2");
    boolean checkLabelOther = answerOption.equals(answerOption2);
    answerOption.setLabel("Label 1");
    boolean checkLabelBoth = answerOption.equals(answerOption2);
    answerOption.setLabel("Label 2");
    boolean checkLabelBothSame = answerOption.equals(answerOption2);
    
    //Assert
    assertEquals(true, checkSame);
    assertEquals(false, checkNull);
    assertEquals(false, checkOtherClass);
    assertEquals(false, checkCodeOther);
    assertEquals(false, checkCodeBoth);
    assertEquals(true, checkCodeBothSame);
    assertEquals(false, checkLabelOther);
    assertEquals(false, checkLabelBoth);
    assertEquals(true, checkLabelBothSame);
    
    
  }

}
