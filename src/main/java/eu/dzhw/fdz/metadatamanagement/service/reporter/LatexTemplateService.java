package eu.dzhw.fdz.metadatamanagement.service.reporter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.repository.DataAcquisitionProjectRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * This service fill tex templates with data and put it into the gridfs / mongodb.
 * 
 * @author Daniel Katzberg
 *
 */
@Service
// TODO ATTENTION! EARLY DEVELOPMENT VERSION!! DO NOT USE!! DKatzberg
public class LatexTemplateService {

  @Inject
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  /**
   * The Escape Prefix handles the escaping of special latex signs within data information. This
   * Prefix will be copied before the template source code.
   */
  public static final String ESCAPE_PREFIX =
      "<#escape x as x?replace(\"\\\\\", \"\\\\textbackslash\")"
          + "?replace(\"{\", \"\\\\{\")?replace(\"}\", \"\\\\}\")"
          + "?replace(\"#\", \"\\\\#\")?replace(\"$\", \"\\\\$\")"
          + "?replace(\"%\", \"\\\\%\")?replace(\"&\", \"\\\\&\")"
          + "?replace(\"^\", \"\\\\textasciicircum\")?replace(\"_\", \"\\\\_\")"
          + "?replace(\"~\", \"\\\\textasciitilde\")>";

  /**
   * The Escape Suffix closes the escaping prefix. This Prefix will be copied after the template
   * source code.
   */
  public static final String ESCAPE_SUFFIX = "</#escape>";

  /**
   * This service method will receive a tex template as a string and an id of a data acquision
   * project. With this id, the service will load the project for receiving all project information,
   * which are needed for filling of the tex template with data.
   * 
   * @param texTemplateStr An uploaded tex template by the user as a String.
   * @param dataAcquisitionProjectId An id of the data acquision project id.
   * @throws TemplateException Handles templates exceptions.
   * @throws IOException Handles IO Exception for the template.
   */
  public void fillLatexTemplateWithData(String texTemplateStr, String dataAcquisitionProjectId)
      throws TemplateException, IOException {

    // Configuration, based on Freemarker Version 2.3.23
    Configuration templateConfiguration = new Configuration(Configuration.VERSION_2_3_23);
    templateConfiguration.setDefaultEncoding("UTF-8");
    templateConfiguration.setLocale(Locale.GERMAN);
    templateConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

    // Read Template and escape elements
    Template texTemplate = new Template("texTemplate",
        (ESCAPE_PREFIX + texTemplateStr + ESCAPE_SUFFIX), templateConfiguration);

    // Write output to console and file
    // TODO Use another Writer. This is only for testing.
    Writer consoleWriter = new OutputStreamWriter(System.out, Charset.defaultCharset());
    Map<String, Object> dataForTemplate = this.loadDataForTemplateFilling(dataAcquisitionProjectId);
    texTemplate.process(dataForTemplate, consoleWriter);
  }

  public void saveCompleteTexTemplate() {
    // TODO New Method Save Template into mongodb /gridfs
  }

  public void downloadTexTemplate() {
    // TODO New Method download Template from mongodb /gridfs
  }

  /**
   * This method load all needed objects from the db for filling the tex template.
   * 
   * @param dataAcquisitionProjectId An id of the data acquision project id.
   * @return A HashMap with all data for the template filling. The Key is the name of the Object,
   *         which is used in the template.
   */
  private Map<String, Object> loadDataForTemplateFilling(String dataAcquisitionProjectId) {

    // Get Project by Id
    DataAcquisitionProject dataAcquisitionProject =
        this.dataAcquisitionProjectRepository.findOne(dataAcquisitionProjectId);

    // Check for found data acquisition project
    if (dataAcquisitionProject == null) {
      throw new IllegalArgumentException(
          "No Data Acquisition Project found with given id: " + dataAcquisitionProjectId);
    }

    Map<String, Object> dataForTemplate = new HashMap<String, Object>();
    dataForTemplate.put("dataAcquisitionProject", dataAcquisitionProject);

    return dataForTemplate;
  }
}
