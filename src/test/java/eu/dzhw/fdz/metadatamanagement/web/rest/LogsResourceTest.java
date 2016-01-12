/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.rest;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.web.rest.dto.LoggerDto;

/**
 * @author Daniel Katzberg
 *
 */
public class LogsResourceTest extends AbstractTest {

  private MockMvc restUserMockMvc;

  @Before
  public void setup() {
    LogsResource logsResource = new LogsResource();
    this.restUserMockMvc = MockMvcBuilders.standaloneSetup(logsResource)
      .build();
  }

  @Test
  public void testGetList() throws Exception {

    // Arrange

    // Act
    MvcResult mvcResult =
        this.restUserMockMvc.perform(get("/api/logs").accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(content().contentType("application/json"))
          .andReturn();

    String content = mvcResult.getResponse()
      .getContentAsString();
    JSONArray jsonArray = new JSONArray(content);

    System.out.println(jsonArray);

    // Assert
    assertThat(content, not(nullValue()));
    assertThat(jsonArray.length(), greaterThan(0));
  }

  @Test
  public void testChangeLevel() throws Exception {

    // Arrange
    LoggerDto loggerDTO = new LoggerDto();
    loggerDTO.setLevel("INFO");
    loggerDTO.setName("ROOT");

    // Act

    // Assert
    this.restUserMockMvc.perform(put("/api/logs").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(loggerDTO)))
      .andExpect(status().is2xxSuccessful());
  }

}
