/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.common.aggregations;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Daniel Katzberg
 *
 */
public class BucketTest {
  
  @Test
  public void testSetter(){
    //Arrange
    Bucket bucket = new Bucket("Key1", 0L);
    
    //Act
    bucket.setKey("Key2");
    bucket.setDocCount(1L);
    
    //Assert
    assertThat(bucket.getKey(), is("Key2"));
    assertThat(bucket.getDocCount(), is(1L));
  }
  
  @Test
  public void testHashCode(){
    //Arrange
    Bucket bucket = new Bucket("Key1", 0L);
    
    //Act
    
    //Assert
    assertThat(bucket.hashCode(), not(0));
  }
  
  @Test
  public void testEquals(){
    Bucket bucket = new Bucket("Key1", 0L);
    Bucket bucket2 = new Bucket("Key2", 1L);
    
    //Act
    boolean checkSame = bucket.equals(bucket);
    boolean checkNull = bucket.equals(null);
    boolean checkOtherClass = bucket.equals(new Object());
    boolean checkDifferent = bucket.equals(bucket2);
    
    //Assert
    assertEquals(true, checkSame);
    assertEquals(false, checkNull);
    assertEquals(false, checkOtherClass);
    assertEquals(false, checkDifferent);
  }
}
