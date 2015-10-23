package eu.dzhw.fdz.metadatamanagement.web.common;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.RelatedVariable;
import eu.dzhw.fdz.metadatamanagement.web.common.RelatedVariableResource;
import eu.dzhw.fdz.metadatamanagement.web.common.RelatedVariableResourceAssembler;

/**
 * 
 * @author Amine limouri
 *
 */
public class RelatedVariableResourceAssemblerTest extends AbstractTest {

  @Autowired
  RelatedVariableResourceAssembler relatedVariableResourceAssembler;

  @Test
  public void testToResource() {
   
    RelatedVariable relatedVariable = new RelatedVariable();
    relatedVariable.setId("id");
    
    RelatedVariableResource relatedVariableResource = this.relatedVariableResourceAssembler
        .toResource(relatedVariable);

    assertThat(relatedVariableResource, not(nullValue()));
    assertThat(relatedVariableResource.getRelatedVariable().getId(), is("id"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToResourceException() {
    
    this.relatedVariableResourceAssembler.toResource(new RelatedVariable());
    
  }
  
  @Test
  public void testInistantiateResource() {
  
    RelatedVariable relatedVariable = new RelatedVariable();
    relatedVariable.setId("id");
    
    RelatedVariableResource relatedVariableResource = this.relatedVariableResourceAssembler.instantiateResource(relatedVariable);
    
    assertThat(relatedVariableResource, not(nullValue()));
    assertThat(relatedVariableResource.getRelatedVariable().getId(), is("id"));
  }

}
