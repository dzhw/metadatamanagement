package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

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
  
  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;
  
  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;
  
  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    this.publicationRepository.deleteAll();
    this.elasticsearchUpdateQueueService.clearQueue();
    this.elasticsearchAdminService.recreateAllIndices();
  }
  
  @Test
  public void testCreateRelatedPublications() throws IOException, Exception {
    //ARRANGE
    RelatedPublication relatedPublication = UnitTestCreateDomainObjectUtils.buildRelatedPublication();
    
    //ACT
    // create the related publication with the given id
    this.mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
      .content(TestUtil.convertObjectToJsonBytes(relatedPublication)))
      .andExpect(status().isCreated());

    //ASSERT
    // read the related publication under the new url
    this.mockMvc.perform(get(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId()))
      .andExpect(status().isOk());
    
    elasticsearchUpdateQueueService.processQueue();

    // check that there are two data set documents plus two surveys
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(2.0));
  }
  

  @Test
  public void testUpdateRelatedPublications() throws IOException, Exception {
    //ARRANGE
    RelatedPublication relatedPublication = UnitTestCreateDomainObjectUtils.buildRelatedPublication();
    
    //ACT
    // create the related publication with the given id
    this.mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
      .content(TestUtil.convertObjectToJsonBytes(relatedPublication)))
      .andExpect(status().isCreated());
    
    relatedPublication.setDoi("Another DOI");
    
    //update the related publication with the given id
    mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
      .content(TestUtil.convertObjectToJsonBytes(relatedPublication)))
      .andExpect(status().is2xxSuccessful());

    //ASSERT
    // read the related publication under the new url
    mockMvc.perform(get(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(relatedPublication.getId())))
      .andExpect(jsonPath("$.doi", is("Another DOI")));
    
    elasticsearchUpdateQueueService.processQueue();

    // check that there are two data set documents plus two surveys
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(2.0));
  }
  
  @Test
  public void testDeleteRelatedPublications() throws IOException, Exception {
    //ARRANGE
    RelatedPublication relatedPublication = UnitTestCreateDomainObjectUtils.buildRelatedPublication();
    
    //ACT
    // create the related publication with the given id
    this.mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
      .content(TestUtil.convertObjectToJsonBytes(relatedPublication)))
      .andExpect(status().isCreated());
    
    // delete the related publication under the new url
    mockMvc.perform(delete(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId()))
      .andExpect(status().is2xxSuccessful());

    // ensure it is really deleted
    mockMvc.perform(get(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId()))
      .andExpect(status().isNotFound());
    
    elasticsearchUpdateQueueService.processQueue();

    // check that there are two data set documents plus two surveys
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(0.0));
  }
  
  @Test
  public void testUpdateStudyWithInvalidReleaseDoi() throws IOException, Exception {
  //ARRANGE
    RelatedPublication relatedPublication = UnitTestCreateDomainObjectUtils.buildRelatedPublication();
    
    
    relatedPublication.setDoi("ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong." + 
        "ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.");
    
    //ACT and ASSERT
    // create the related publication with the given id
    this.mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
      .content(TestUtil.convertObjectToJsonBytes(relatedPublication)))
      .andExpect(status().is4xxClientError());
  }
}
