/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.common.aggregations;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * @author Daniel Katzberg
 *
 */
public class ScaleLevelBucketTest extends AbstractWebTest {

  @Autowired
  private ScaleLevelProvider scaleLevelProvider;
  
  @Test
  public void testSetter() {
    // Arrange
    ScaleLevelBucket scaleLevelBucket = new ScaleLevelBucket("Key1", 0L, this.scaleLevelProvider);

    // Act
    scaleLevelBucket.setKey("Key2");
    scaleLevelBucket.setDocCount(1L);

    // Assert
    assertThat(scaleLevelBucket.getKey(), is("Key2"));
    assertThat(scaleLevelBucket.getDocCount(), is(1L));
  }

  @Test
  public void testHashCode() {
    // Arrange
    ScaleLevelBucket scaleLevelBucket = new ScaleLevelBucket("Key1", 0L, this.scaleLevelProvider);

    // Act

    // Assert
    assertThat(scaleLevelBucket.hashCode(), not(0));
  }

  @Test
  public void testEquals() {
    ScaleLevelBucket scaleLevelBucket = new ScaleLevelBucket("Key1", 0L, this.scaleLevelProvider);
    ScaleLevelBucket scaleLevelBucket2 = new ScaleLevelBucket("Key2", 1L, this.scaleLevelProvider);

    // Act
    boolean checkSame = scaleLevelBucket.equals(scaleLevelBucket);
    boolean checkNull = scaleLevelBucket.equals(null);
    boolean checkOtherClass = scaleLevelBucket.equals(new Object());
    boolean checkDifferent = scaleLevelBucket.equals(scaleLevelBucket2);

    // Assert
    assertEquals(true, checkSame);
    assertEquals(false, checkNull);
    assertEquals(false, checkOtherClass);
    assertEquals(false, checkDifferent);
  }

  @Test
  public void testEqualsWithSameKeyButDifferentDocCount() {
    ScaleLevelBucket bucket = new ScaleLevelBucket("Key1", 0L, this.scaleLevelProvider);
    ScaleLevelBucket bucket2 = new ScaleLevelBucket("Key1", 1L, this.scaleLevelProvider);

    // Act
    boolean checkSame = bucket.equals(bucket2);

    // Assert
    assertEquals(true, checkSame);
  }

  @Test
  public void testHashCodeWithSameKeyButDifferentDocCount() {
    ScaleLevelBucket bucket = new ScaleLevelBucket("Key1", 0L, this.scaleLevelProvider);
    ScaleLevelBucket bucket2 = new ScaleLevelBucket("Key1", 1L, this.scaleLevelProvider);

    // Act
    boolean checkSame = bucket.hashCode() == bucket2.hashCode();

    // Assert
    assertEquals(true, checkSame);
  }
}
