package eu.dzhw.fdz.metadatamanagement.web.common;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.RelatedVariable;
import eu.dzhw.fdz.metadatamanagement.web.common.RelatedVariableResource;

/**
 * 
 * @author Amine limouri
 *
 */
public class RelatedVariableResourceTest {
  
  @Test
  public void testHashCode() {
   
    RelatedVariable relatedVariable1 = new RelatedVariable();
    RelatedVariable relatedVariable2 = new RelatedVariable();
  
    relatedVariable1.setId("id");
    relatedVariable2.setId("id2");
    
    RelatedVariableResource relatedVariableResource =
        new RelatedVariableResource(relatedVariable1);
    
    RelatedVariableResource relatedVariableResource2 =
        new RelatedVariableResource(relatedVariable2);

    int hashCode = relatedVariableResource.hashCode();
    int hashCode2 = relatedVariableResource2.hashCode();

    assertThat(hashCode, not(0));
    assertThat(hashCode2, not(0));
    assertThat(hashCode, not(hashCode2));
    assertThat(relatedVariableResource.getRelatedVariable().getId(), is("id"));
    assertThat(relatedVariableResource2.getRelatedVariable().getId(), is("id2"));
  }

  @Test
  public void testEquals() {
    
    RelatedVariable relatedVariable1 = new RelatedVariable();
    RelatedVariable relatedVariable2 = new RelatedVariable();
  
    relatedVariable1.setId("id");
    relatedVariable2.setId("id2");
    
    RelatedVariableResource relatedVariableResource =
        new RelatedVariableResource(relatedVariable1);
    
    RelatedVariableResource relatedVariableResource2 =
        new RelatedVariableResource(relatedVariable2);
   
    boolean checkSame = relatedVariableResource.equals(relatedVariableResource);
    boolean checkNull = relatedVariableResource.equals(null);
    boolean checkOtherClass = relatedVariableResource.equals(new Object());
    boolean checkPageOther = relatedVariableResource.equals(relatedVariableResource2);

    
    assertEquals(true, checkSame);
    assertEquals(false, checkNull);
    assertEquals(false, checkOtherClass);
    assertEquals(false, checkPageOther);
  }
}
