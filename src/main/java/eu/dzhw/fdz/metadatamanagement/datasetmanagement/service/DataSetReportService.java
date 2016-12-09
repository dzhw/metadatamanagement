package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import eu.dzhw.fdz.metadatamanagement.common.rest.util.ZipUtil;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.filemanagement.service.FileService;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ValidResponse;
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
public class DataSetReportService {

  @Inject
  private FileService fileService;

  @Inject
  private DataSetRepository dataSetRepository;

  @Inject
  private VariableRepository variableRepository;

  @Inject
  private QuestionRepository questionRepository;

  /**
   * The Escape Prefix handles the escaping of special latex signs within data information. This
   * Prefix will be copied before the template source code.
   */
  public static final String ESCAPE_PREFIX =
      "<#escape x as x?replace(\"\\\\\", \"\\\\textbackslash{}\")"
          + "?replace(\"{\", \"\\\\{\")?replace(\"}\", \"\\\\}\")"
          + "?replace(\"#\", \"\\\\#\")?replace(\"$\", \"\\\\$\")"
          + "?replace(\"%\", \"\\\\%\")?replace(\"&\", \"\\\\&\")"
          + "?replace(\"^\", \"\\\\textasciicircum{}\")?replace(\"_\", \"\\\\_\")"
          + "?replace(\">\", \"\\\\textgreater{}\")?replace(\"<\", \"\\\\textless{}\")"
          + "?replace(\"~\", \"\\\\textasciitilde{}\")>";

  /**
   * The Escape Suffix closes the escaping prefix. This Prefix will be copied after the template
   * source code.
   */
  public static final String ESCAPE_SUFFIX = "</#escape>";

  /**
   * Latex Mime Content Type.
   */
  public static final String CONTENT_TYPE_LATEX = "application/x-tex";

  /**
   * Zip Mime Content Type.
   */
  public static final String CONTENT_TYPE_ZIP = "application/zip";

  public static final String KEY_INTRODUCTION = "Introduction.tex";
  public static final String KEY_MAIN = "Main.tex";
  public static final String KEY_REFERENCES = "References.bib";
  public static final String KEY_VARIABLE = "variables/Variable.tex";
  
  /**
   * List of missing tex files.
   */
  private List<String> missingTexFiles;

  /**
   * This service method will receive a tex template as a string and an id of a data set. With this
   * id, the service will load the data set for receiving all depending information, which are
   * needed for filling of the tex template with data.
   *
   * @param multiPartFile The uploaded zip file
   * @param dataSetId An id of the data set.
   * @return The name of the saved tex template in the GridFS / MongoDB.
   * @throws TemplateException Handles templates exceptions.
   * @throws IOException Handles IO Exception for the template.
   */
  @SuppressWarnings("unchecked")
  public String generateReport(MultipartFile multiPartFile,
      String dataSetId) throws TemplateException, IOException {

    // Configuration, based on Freemarker Version 2.3.23
    Configuration templateConfiguration = new Configuration(Configuration.VERSION_2_3_23);
    templateConfiguration.setDefaultEncoding(StandardCharsets.UTF_8.toString());
    templateConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    templateConfiguration.setNumberFormat("0.######");

    // Unzip the zip file
    Map<String, String> texTemplates = ZipUtil.unzip(multiPartFile);
    
    if (!this.validateDataSetReportStructure(texTemplates)) {
      return null; 
    }
    
    Map<String, byte[]> filledTemplates = new HashMap<>();

    // Load data for template only once
    Map<String, Object> dataForTemplate = this.loadDataForTemplateFilling(dataSetId);

    // Zip the filled templates.
    filledTemplates.put(KEY_REFERENCES,
        this.fillTemplate(texTemplates.get(KEY_REFERENCES), templateConfiguration,
            dataForTemplate));
    filledTemplates.put(KEY_INTRODUCTION,
        this.fillTemplate(texTemplates.get(KEY_INTRODUCTION), templateConfiguration,
            dataForTemplate));
    filledTemplates.put(KEY_MAIN,
        this.fillTemplate(texTemplates.get(KEY_MAIN), templateConfiguration,
            dataForTemplate));

    // Create Variables pages
    List<String> variableIds = ((DataSet) dataForTemplate.get("dataSet")).getVariableIds();
    Map<String, Variable> variablesMap = (Map<String, Variable>) dataForTemplate.get("variables");
    for (String variableId : variableIds) {
      Variable variable = variablesMap.get(variableId);
      dataForTemplate.put("variableId", variableId);
      if (variable != null) {
        filledTemplates.put("variables/" + variable.getName() + ".tex",
            fillTemplate(texTemplates.get(KEY_VARIABLE), templateConfiguration, dataForTemplate));
      }
    }
    // Save into MongoDB / GridFS
    ByteArrayOutputStream byteArrayOutputStreamArchive = ZipUtil.zip(filledTemplates);
    return this.saveCompleteTexTemplate(byteArrayOutputStreamArchive, multiPartFile.getName());
  }
  
  /**
   * Checks for all files which are included for the tex template.
   * @param texTemplates All uploaded texTemplates 
   * @return True if all files are included. False min one file is missing.
   */
  private boolean validateDataSetReportStructure(Map<String, String> texTemplates) {
    this.missingTexFiles = new ArrayList<>();
    
    // NO Check for References.bib. This file is just optional has has to be added manually.
    
    if (!texTemplates.containsKey(KEY_INTRODUCTION)) {
      this.missingTexFiles.add(KEY_INTRODUCTION);
    }
    
    if (!texTemplates.containsKey(KEY_MAIN)) {
      this.missingTexFiles.add(KEY_MAIN);
    }
    
    if (!texTemplates.containsKey(KEY_VARIABLE)) {
      this.missingTexFiles.add(KEY_VARIABLE);
    }
    
    return this.missingTexFiles.isEmpty();
  }

  /**
   * @return The missing files from the last validate data set report structure.
   */
  public List<String> getMissingFiles() {
    return this.missingTexFiles;
  }

  /**
   * This method filles the tex templates.
   *
   * @param templateContent The content of a tex template.
   * @param templateConfiguration The configuration for freemarker.
   * @param dataForTemplateThe data for a tex template. All variables, questions and dataset
   *        information.
   * @return The filled tex templates as byte array.
   * @throws IOException Handles IO Exception.
   * @throws TemplateException Handles template Exceptions.
   */
  private byte[] fillTemplate(String templateContent,
      Configuration templateConfiguration, Map<String, Object> dataForTemplate)
      throws IOException, TemplateException {
    // Read Template and escape elements
    Template texTemplate = new Template("texTemplate",
        (ESCAPE_PREFIX + templateContent + ESCAPE_SUFFIX), templateConfiguration);

    // Write output to output stream
    ByteArrayOutputStream byteArrayOutputStreamFile = new ByteArrayOutputStream();
    Writer fileWriter = new OutputStreamWriter(byteArrayOutputStreamFile, "UTF-8");
    texTemplate.process(dataForTemplate, fileWriter);

    // Put translated element to tar archive
    return byteArrayOutputStreamFile.toByteArray();
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
    return fileService.saveTempFile(byteArrayInputStream, fileName, CONTENT_TYPE_ZIP);
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
    Map<String, Object> dataForTemplate = new HashMap<>();

    // Create Information for the latex template.
    dataForTemplate = this.createDataSetMap(dataForTemplate, dataSetId);
    dataForTemplate = this.createVariableDependingMaps(dataForTemplate);

    return dataForTemplate;
  }

  /**
   * This is a "fluent" method for the Map with created Objects of the latex template. Added data
   * set information to the map of objects for the template.
   *
   * @param dataForTemplate The map for the template with all added objects before this method.
   * @param dataSetId The id of the used data set; Root Element of the report.
   * @return The map for the template as fluent result. Added some created elements within this
   *         method.
   */
  private Map<String, Object> createDataSetMap(Map<String, Object> dataForTemplate,
      String dataSetId) {
    // Get DataSet and check the valid result
    DataSet dataSet = this.dataSetRepository.findOne(dataSetId);
    if (dataSet == null) {
      throw new IllegalArgumentException(
          "No Data Set was found with given id: " + dataSetId);
    }
    dataForTemplate.put("dataSet", dataSet);

    return dataForTemplate;
  }

  /**
   * Fluent Method for creating Map Objects for the latex template. Creates the follow Maps:
   * questions : Map with all question, which are connected by variables of a given data set (key is
   * variable.questionId) 
   * isAMissingCounterMap: A map with counter, how many isAMissing Values a variable has
   *    (key is variable.id)
   * firstTenValues: The first ten isAMissing values for one tex template layout.
   *    (key is variable.id)
   * lastTenValues: The last ten isAMissing values for one tex template layout.
   *    (key is variable.id)
   * @param dataForTemplate The map for the template with all added objects before this method.
   * @return The map for the template as fluent result. Added some created elements within this
   *     method.
   */
  private Map<String, Object> createVariableDependingMaps(Map<String, Object> dataForTemplate) {

    // Create a Map of Variables
    List<Variable> variables =
        this.variableRepository.findByIdIn(
            ((DataSet)dataForTemplate.get("dataSet")).getVariableIds());
    Map<String, Variable> variablesMap =
        Maps.uniqueIndex(variables, new VariableFunction());
    dataForTemplate.put("variables", variablesMap);

    // Create different information from the variable
    Map<String, Question> questionsMap = new HashMap<>();
    Map<String, List<ValidResponse>> firstTenValidResponses = new HashMap<>();
    Map<String, List<ValidResponse>> lastTenValidResponses = new HashMap<>();


    for (Variable variable : variables) {

      int sizeValidResponses = 0;
      if (variable.getDistribution() != null && variable.getDistribution()
          .getValidResponses() != null) {
        sizeValidResponses = variable.getDistribution()
          .getValidResponses()
          .size();
      }

      // Create a Map with Atomic Questions
      if (variable.getQuestionId() != null) {
        Question question =
            this.questionRepository.findOne(variable.getQuestionId());
        questionsMap.put(variable.getQuestionId(), question);
      }

      // Create the first and last ten isAMissing Values to different list, if there are more
      // than 20.

      if (sizeValidResponses > 20) {
        firstTenValidResponses.put(variable.getId(),
            variable.getDistribution().getValidResponses().subList(0, 9));
        lastTenValidResponses.put(variable.getId(),
            variable.getDistribution()
              .getValidResponses()
              .subList(sizeValidResponses - 10, sizeValidResponses - 1));
      }
    }
    dataForTemplate.put("questions", questionsMap);
    dataForTemplate.put("firstTenValidResponses", firstTenValidResponses);
    dataForTemplate.put("lastTenValidResponses", lastTenValidResponses);

    return dataForTemplate;

  }

  /**
   * Inner class for get the variable ids as index for the variables hashmap.
   *
   * @author Daniel Katzberg
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
