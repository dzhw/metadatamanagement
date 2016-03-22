package eu.dzhw.fdz.metadatamanagement.service.reporting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.domain.AtomicQuestion;
import eu.dzhw.fdz.metadatamanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.repository.AtomicQuestionRepository;
import eu.dzhw.fdz.metadatamanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.service.FileService;
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
public class VariableReportService {

  @Inject
  private FileService fileService;

  @Inject
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Inject
  private VariableRepository variableRepository;

  @Inject
  private AtomicQuestionRepository atomicQuestionRepository;

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

  public static final String CONTENT_TYPE_LATEX = "application/x-tex";

  /**
   * This service method will receive a tex template as a string and an id of a data acquision
   * project. With this id, the service will load the project for receiving all project information,
   * which are needed for filling of the tex template with data.
   * 
   * @param texTemplateStr An uploaded tex template by the user as a String.
   * @param fileName the name of the uploaded tex template.
   * @param dataAcquisitionProjectId An id of the data acquision project id.
   * @return The name of the saved tex template in the GridFS / MongoDB.
   * @throws TemplateException Handles templates exceptions.
   * @throws IOException Handles IO Exception for the template.
   */
  public String generateReport(String texTemplateStr, String fileName,
      String dataAcquisitionProjectId) throws TemplateException, IOException {

    // Configuration, based on Freemarker Version 2.3.23
    Configuration templateConfiguration = new Configuration(Configuration.VERSION_2_3_23);
    templateConfiguration.setDefaultEncoding("UTF-8");
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
    return this.saveCompleteTexTemplate(byteArrayOutputStream, fileName);
  }

  /**
   * This method save a latex file into GridFS/MongoDB based on a byteArrayOutputStream.
   * 
   * @param byteArrayOutputStream The latex file as byteArrayOutputStream
   * @param fileName The name of the file to be saved
   * @return return the file name of the saved latex template in the GridFS / MongoDB.
   */
  private String saveCompleteTexTemplate(ByteArrayOutputStream byteArrayOutputStream,
      String fileName) {

    // prepare additional information for tex file.
    ByteArrayInputStream byteArrayInputStream =
        new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

    // No Update by API, so we have to delete first.
    fileService.deleteTempFile(fileName);

    // Save tex file, based on the bytearray*streams
    return fileService.saveTempFile(byteArrayInputStream, fileName, CONTENT_TYPE_LATEX);
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

    List<AtomicQuestion> atomicQuestions =
        this.atomicQuestionRepository.findByDataAcquisitionProjectId(dataAcquisitionProjectId);

    // Change AtomicQuestion List to Map for joins in tex template
    Map<String, AtomicQuestion> atomicQuestionMap = new HashMap<>();
    for (AtomicQuestion atomicQuestion : atomicQuestions) {
      atomicQuestionMap.put(atomicQuestion.getId(), atomicQuestion);
    }

    List<Variable> variables =
        this.variableRepository.findByDataAcquisitionProjectId(dataAcquisitionProjectId);

    Map<String, Object> dataForTemplate = new HashMap<String, Object>();
    dataForTemplate.put("dataAcquisitionProject", dataAcquisitionProject);
    dataForTemplate.put("variables", variables);
    dataForTemplate.put("atomicQuestions", atomicQuestionMap);

    return dataForTemplate;
  }
}
