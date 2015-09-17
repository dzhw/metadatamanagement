package eu.dzhw.fdz.metadatamanagement.web;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Locale;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bitplan.w3ccheck.W3CValidator;
import com.bitplan.w3ccheck.W3CValidator.Body.ValidationResponse.Errors;

import eu.dzhw.fdz.metadatamanagement.MetaDataManagementApplication;
import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableDocumentRepository;

/**
 * Base class for all MVC Controller tests. Sets up the application context and initializes the mvc
 * mock and enables the test profile.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MetaDataManagementApplication.class)
@ActiveProfiles("test")
@WebAppConfiguration
public abstract class AbstractWebTest {
  @Autowired
  private WebApplicationContext wac;

  protected MockMvc mockMvc;

  @Autowired
  private VariableDocumentRepository variableRepository;

  /*
   * A slf4j logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(AbstractWebTest.class);

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @After
  public void ensureEmptyRepositories() {
    for (Locale locale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      LocaleContextHolder.setLocale(locale);
      if (variableRepository.findAll(new PageRequest(0, 10)).hasContent()) {
        LOG.error("Found variables in '" + locale.getLanguage() + "'-index.");
        throw new IllegalStateException("Found variables in '" + locale.getLanguage() + "'-index.");
      }
    }
  }


  /**
   * This method send a post to the official w3c validation. The validation url is:
   * {@linkplain http://validator.w3.org/check}
   * 
   * @param htmlResult The html page as string
   * @param methodeName the method, who call this validation (for debugging)
   * @throws JAXBException catch umarshell exception by jaxb and other exception by jaxb.
   */
  protected void checkHtmlValidation(String htmlResult, String methodeName) throws JAXBException {
    // Validate
    final String validationUrlW3C = "http://validator.w3.org/check";
    W3CValidator checkResult = W3CValidator.check(validationUrlW3C, htmlResult);

    // Check for Errors and put it to the log
    Errors errors = checkResult.body.response.errors;
    LOG.info(methodeName + ": Number of Errors: " + errors.errorcount);
    if (errors.errorcount > 0) {
      LOG.error("Content: " + htmlResult);
      errors.errorlist.forEach(e -> {
        LOG.error(methodeName + ": Validation Error: (Line: " + e.line + ", Col.: " + e.col + ") "
            + e.message + "");
      });
    }

    // Assert
    assertThat(checkResult.body.response.validity, is(true));
  }
}
