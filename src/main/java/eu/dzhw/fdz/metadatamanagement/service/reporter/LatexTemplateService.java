package eu.dzhw.fdz.metadatamanagement.service.reporter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mongodb.gridfs.GridFSFile;

import eu.dzhw.fdz.metadatamanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.repository.VariableRepository;
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
public class LatexTemplateService {

  @Inject
  private GridFsOperations operations;

  @Inject
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Inject
  private VariableRepository variableRepository;

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
   * @return The name of the saved tex template in the GridFS / MongoDB.
   * @throws TemplateException Handles templates exceptions.
   * @throws IOException Handles IO Exception for the template.
   */
  public String generateReport(String texTemplateStr, String dataAcquisitionProjectId)
      throws TemplateException, IOException {

    // Configuration, based on Freemarker Version 2.3.23
    Configuration templateConfiguration = new Configuration(Configuration.VERSION_2_3_23);
    templateConfiguration.setDefaultEncoding("UTF-8");
    templateConfiguration.setLocale(Locale.GERMAN);
    templateConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

    // Read Template and escape elements
    Template texTemplate = new Template("texTemplate",
        (ESCAPE_PREFIX + texTemplateStr + ESCAPE_SUFFIX), templateConfiguration);

    // Write output to output stream
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    Writer fileWriter = new OutputStreamWriter(byteArrayOutputStream, Charset.defaultCharset());
    Map<String, Object> dataForTemplate = this.loadDataForTemplateFilling(dataAcquisitionProjectId);
    texTemplate.process(dataForTemplate, fileWriter);

    // Save into MongoDB / GridFS
    return this.saveCompleteTexTemplate(byteArrayOutputStream, dataAcquisitionProjectId);
  }

  /**
   * This method save a latex file into GridFS/MongoDB based on a byteArrayOutputStream.
   * 
   * @param byteArrayOutputStream The latex file as byteArrayOutputStream
   * @param dataAcquisitionProjectId An id of a data acquision project id.
   * @return return the file name of the saved latex template in the GridFS / MongoDB.
   */
  public String saveCompleteTexTemplate(ByteArrayOutputStream byteArrayOutputStream,
      String dataAcquisitionProjectId) {

    // prepare additional information for tex file.
    String contentType = "application/x-tex";
    ByteArrayInputStream byteArrayInputStream =
        new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    String fileName = dataAcquisitionProjectId + "_texTemplate.tex";

    // No Update by API, so we have to delete first.
    this.deleteTexTemplateByName(fileName);

    // Save tex file, based on the bytearray*streams
    GridFSFile texGridFsFile = this.operations.store(byteArrayInputStream, fileName, contentType);
    texGridFsFile.validate();

    return fileName;
  }

  /**
   * Delete all tex templates from the GridFS / MongoDB. Cron Meaning: Every Day at 3 am.
   */
  @Scheduled(cron = "0 0 3 * * ?")
  public void deleteTexTemplates() {
    // Regular Expression with $in Operator. Checks for all files with ends with .tex
    Query query = new Query(GridFsCriteria.whereContentType()
        .is("application/x-tex"));

    this.operations.delete(query);

  }

  private void deleteTexTemplateByName(String fileName) {
    // Regular Expression with $in Operator. Checks for all files with ends with .tex
    Query query = new Query(GridFsCriteria.whereFilename()
        .is(fileName));

    this.operations.delete(query);
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

    List<Variable> variables =
        this.variableRepository.findByDataAcquisitionProjectId(dataAcquisitionProjectId);

    // Check for found data acquisition project
    if (dataAcquisitionProject == null) {
      throw new IllegalArgumentException(
          "No Data Acquisition Project found with given id: " + dataAcquisitionProjectId);
    }

    Map<String, Object> dataForTemplate = new HashMap<String, Object>();
    dataForTemplate.put("dataAcquisitionProject", dataAcquisitionProject);
    dataForTemplate.put("variables", variables);

    return dataForTemplate;
  }


}
