package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;

/**
 * 
 * @author Daniel Katzberg
 *
 */
public class RelatedPublicationResourceTest  extends AbstractTest {
  private static final String API_RELATED_PUBLICATION_URI = "/api/related-publications";
  
  @Autowired
  private WebApplicationContext wac;
  
  @Autowired
  private RelatedPublicationRepository publicationRepository;
  
  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    this.publicationRepository.deleteAll();
  }
  
  @Test
  public void testCreateStudy() throws IOException, Exception {
    
    RelatedPublication relatedPublication = UnitTestCreateDomainObjectUtils.buildRelatedPublication();
    
    // create the study with the given id
    MvcResult mvcResult = this.mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
      .content(TestUtil.convertObjectToJsonBytes(relatedPublication)))
      //.andExpect(status().isCreated());
      .andReturn();
    System.out.println(mvcResult.getResponse().getContentAsString());

    // read the study under the new url
    this.mockMvc.perform(get(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId()))
      .andExpect(status().isOk());
  }
}
