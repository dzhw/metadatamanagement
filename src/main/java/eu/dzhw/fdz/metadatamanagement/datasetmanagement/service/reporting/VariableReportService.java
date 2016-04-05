package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.reporting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import eu.dzhw.fdz.metadatamanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.filemanagement.service.FileService;
import eu.dzhw.fdz.metadatamanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
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
  private DataSetRepository dataSetRepository;

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

  public static final String CONTENT_TYPE_LATEX = "application/x-tex";

  /**
   * This service method will receive a tex template as a string and an id of a data set. With this
   * id, the service will load the data set for receiving all depending information, which are
   * needed for filling of the tex template with data.
   * 
   * @param texTemplateStr An uploaded tex template by the user as a String.
   * @param fileName the name of the uploaded tex template.
   * @param dataSetId An id of the data set.
   * @return The name of the saved tex template in the GridFS / MongoDB.
   * @throws TemplateException Handles templates exceptions.
   * @throws IOException Handles IO Exception for the template.
   */
  public String generateReport(String texTemplateStr, String fileName,
      String dataSetId) throws TemplateException, IOException {

    // Configuration, based on Freemarker Version 2.3.23
    Configuration templateConfiguration = new Configuration(Configuration.VERSION_2_3_23);
    templateConfiguration.setDefaultEncoding(StandardCharsets.UTF_8.toString());
    templateConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

    // Read Template and escape elements
    Template texTemplate = new Template("texTemplate",
        (ESCAPE_PREFIX + texTemplateStr + ESCAPE_SUFFIX), templateConfiguration);

    // Write output to output stream
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    Writer fileWriter = new OutputStreamWriter(byteArrayOutputStream, "UTF-8");
    Map<String, Object> dataForTemplate = this.loadDataForTemplateFilling(dataSetId);
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
   * @param dataSetId An id of the data acquision project id.
   * @return A HashMap with all data for the template filling. The Key is the name of the Object,
   *         which is used in the template.
   */
  private Map<String, Object> loadDataForTemplateFilling(String dataSetId) {

    // Create Map for the template
    Map<String, Object> dataForTemplate = new HashMap<String, Object>();

    // Get DataSet and check the valid result
    DataSet dataSet = this.dataSetRepository.findOne(dataSetId);
    if (dataSet == null) {
      throw new IllegalArgumentException(
          "No Data Set was found with given id: " + dataSetId);
    }
    dataForTemplate.put("dataSet", dataSet);

    // Create a Map of Variables
    List<Variable> variables =
        this.variableRepository.findByDataSetIdsContaining(dataSetId);
    Map<String, Variable> variablesMap =
        Maps.uniqueIndex(variables, new VariableFunction());
    dataForTemplate.put("variables", variablesMap);

    return dataForTemplate;
  }

  /**
   * Inner class for get the variable ids as index for the variables hashmap.
   * 
   * @author dkatzberg
   *
   */
  static class VariableFunction implements Function<Variable, String> {
    /*
     * (non-Javadoc)
     * 
     * @see com.google.common.base.Function#apply(java.lang.Object)
     */
    @Override
    public String apply(Variable variable) {
      if (variable == null) {
        return null;
      }

      return variable.getId();
    }
  }
}
