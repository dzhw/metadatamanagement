package eu.dzhw.fdz.metadatamanagement.common.rest;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;

public class I18nResourceControllerTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  public void testGetCountryCodes() throws IOException, Exception {
    // get the country codes
    mockMvc
        .perform(get("/api/i18n/country-codes")).andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", greaterThan(0)));
  }

  @Test
  public void testGetLanguages() throws IOException, Exception {
    // get the language codes
    mockMvc.perform(get("/api/i18n/languages")).andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", greaterThan(0)));
  }
}
