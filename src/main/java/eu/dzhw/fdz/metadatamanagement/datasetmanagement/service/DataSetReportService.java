package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import eu.dzhw.fdz.metadatamanagement.common.rest.util.ZipUtil;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.exception.TemplateIncompleteException;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.filemanagement.service.FileService;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RelatedQuestion;
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

  @Autowired
  private FileService fileService;

  @Autowired
  private DataSetRepository dataSetRepository;
  
  @Autowired
  private VariableRepository variableRepository;

  @Autowired
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

  /**
   * Files which will be filled by the freemarker code.
   */
  public static final String KEY_INTRODUCTION = "Introduction.tex";
  public static final String KEY_VARIABLELIST = "Variablelist.tex";
  public static final String KEY_MAIN = "Main.tex";
  public static final String KEY_REFERENCES_BIB = "References.bib";
  public static final String KEY_VARIABLE = "variables/Variable.tex";
  
  /**
   * Files which will be copy into the download file.
   */
  public static final String KEY_DSREPORT_STY = "dsreport.sty";
  public static final String KEY_BLUE_BAR_EPS = "blauer_balken.eps";
  public static final String KEY_BLUE_BAR_PDF = "blauer_balken.pdf";
  public static final String KEY_BMBF_LOGO_EPS = "bmbf-logo_gef_vom.eps";
  public static final String KEY_BMBF_LOGO_PDF = "bmbf-logo_gef_vom.pdf";
  public static final String KEY_CREATIVE_COMMONS_EPS = "by-nc-sa_eu.eps";
  public static final String KEY_CREATIVE_COMMONS_PNG = "by-nc-sa_eu.png";
  public static final String KEY_FDZ_LOGO_EPS = "fdz-logo_mit-text.eps";
  public static final String KEY_FDZ_LOGO_PDF = "fdz-logo_mit-text.pdf";
  public static final String KEY_FDZ_LOGO_NO_TEXT_EPS = "fdz-logo_ohne-text.eps";
  public static final String KEY_FDZ_LOGO_NO_TEXT_PDF = "fdz-logo_ohne-text.pdf";
  
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
  public String generateReport(MultipartFile multiPartFile,
      String dataSetId) throws TemplateException, TemplateIncompleteException, IOException {

    // Configuration, based on Freemarker Version 2.3.23
    Configuration templateConfiguration = new Configuration(Configuration.VERSION_2_3_23);
    templateConfiguration.setDefaultEncoding(StandardCharsets.UTF_8.toString());
    templateConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    templateConfiguration.setNumberFormat("0.######");

    // Unzip the zip file
    Map<String, byte[]> texTemplates = ZipUtil.unzip(multiPartFile);
    
    List<String> missingTexFiles = this.validateDataSetReportStructure(texTemplates);
    if (!missingTexFiles.isEmpty()) {
      throw new TemplateIncompleteException("data-set-management.error"
          + ".files-in-template-zip-incomplete", missingTexFiles);      
    }
    
    Map<String, byte[]> filledTemplates = new HashMap<>();

    // Load data for template only once
    Map<String, Object> dataForTemplate = this.loadDataForTemplateFilling(dataSetId);

    // Zip the filled templates.
    String bibReferenceForTemplateStr =
        IOUtils.toString(texTemplates.get(KEY_REFERENCES_BIB), StandardCharsets.UTF_8.name());
    filledTemplates.put(KEY_REFERENCES_BIB,
        this.fillTemplate(bibReferenceForTemplateStr, templateConfiguration, dataForTemplate));
    String introductionForTemplateStr = 
        IOUtils.toString(texTemplates.get(KEY_INTRODUCTION), StandardCharsets.UTF_8.name());
    filledTemplates.put(KEY_INTRODUCTION,
        this.fillTemplate(introductionForTemplateStr, templateConfiguration, dataForTemplate));
    
    String variableListForTemplateStr = 
        IOUtils.toString(texTemplates.get(KEY_VARIABLELIST), StandardCharsets.UTF_8.name());
    filledTemplates.put(KEY_VARIABLELIST,
        this.fillTemplate(variableListForTemplateStr, templateConfiguration, dataForTemplate));
    
    String mainForTemplateStr = 
        IOUtils.toString(texTemplates.get(KEY_MAIN), StandardCharsets.UTF_8.name());
    filledTemplates.put(KEY_MAIN,
        this.fillTemplate(mainForTemplateStr, templateConfiguration, dataForTemplate));
    
    //Just pipe files into the download zip file    
    filledTemplates.put(KEY_DSREPORT_STY, texTemplates.get(KEY_DSREPORT_STY));    
    filledTemplates.put(KEY_BLUE_BAR_EPS, texTemplates.get(KEY_BLUE_BAR_EPS));
    filledTemplates.put(KEY_BLUE_BAR_PDF, texTemplates.get(KEY_BLUE_BAR_PDF));
    filledTemplates.put(KEY_BMBF_LOGO_EPS, texTemplates.get(KEY_BMBF_LOGO_EPS));
    filledTemplates.put(KEY_BMBF_LOGO_PDF, texTemplates.get(KEY_BMBF_LOGO_PDF));
    filledTemplates.put(KEY_CREATIVE_COMMONS_EPS, texTemplates.get(KEY_CREATIVE_COMMONS_EPS));
    filledTemplates.put(KEY_CREATIVE_COMMONS_PNG, texTemplates.get(KEY_CREATIVE_COMMONS_PNG));
    filledTemplates.put(KEY_FDZ_LOGO_EPS, texTemplates.get(KEY_FDZ_LOGO_EPS));
    filledTemplates.put(KEY_FDZ_LOGO_PDF, texTemplates.get(KEY_FDZ_LOGO_PDF));
    filledTemplates.put(KEY_FDZ_LOGO_NO_TEXT_EPS, texTemplates.get(KEY_FDZ_LOGO_NO_TEXT_EPS));
    filledTemplates.put(KEY_FDZ_LOGO_NO_TEXT_PDF, texTemplates.get(KEY_FDZ_LOGO_NO_TEXT_PDF));

    // Create Variables pages
    @SuppressWarnings("unchecked")
    Map<String, Variable> variablesMap = (Map<String, Variable>) dataForTemplate.get("variables");
    Collection<Variable> variables = variablesMap.values();
    String variableForTemplateStr = 
        IOUtils.toString(texTemplates.get(KEY_VARIABLE), StandardCharsets.UTF_8.name());
    for (Variable variable : variables) {
      
      //Check for null
      if (variable == null) {
        continue;
      }
      
      //check id field for null
      if (variable.getId() == null) {
        continue;
      }            
      
      dataForTemplate.put("variableId", variable.getId());      
      
      filledTemplates.put("variables/" + variable.getName() + ".tex",
          fillTemplate(variableForTemplateStr, templateConfiguration, dataForTemplate));
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
  private List<String> validateDataSetReportStructure(Map<String, byte[]> texTemplates) {
    List<String> missingTexFiles = new ArrayList<>();
    
    // NO Check for References.bib. This file is just optional has has to be added manually.
    
    if (!texTemplates.containsKey(KEY_INTRODUCTION)) {
      missingTexFiles.add(KEY_INTRODUCTION);
    }
    
    if (!texTemplates.containsKey(KEY_MAIN)) {
      missingTexFiles.add(KEY_MAIN);
    }
    
    if (!texTemplates.containsKey(KEY_VARIABLE)) {
      missingTexFiles.add(KEY_VARIABLE);
    }
    
    if (!texTemplates.containsKey(KEY_VARIABLELIST)) {
      missingTexFiles.add(KEY_VARIABLELIST);
    }
    
    if (!texTemplates.containsKey(KEY_REFERENCES_BIB)) {
      missingTexFiles.add(KEY_REFERENCES_BIB);
    }
    
    if (!texTemplates.containsKey(KEY_DSREPORT_STY)) {
      missingTexFiles.add(KEY_DSREPORT_STY);
    }
    
    if (!texTemplates.containsKey(KEY_BLUE_BAR_EPS)) {
      missingTexFiles.add(KEY_BLUE_BAR_EPS);
    }
    
    if (!texTemplates.containsKey(KEY_BLUE_BAR_PDF)) {
      missingTexFiles.add(KEY_BLUE_BAR_PDF);
    }
    
    if (!texTemplates.containsKey(KEY_BMBF_LOGO_EPS)) {
      missingTexFiles.add(KEY_BMBF_LOGO_EPS);
    }
    
    if (!texTemplates.containsKey(KEY_BMBF_LOGO_PDF)) {
      missingTexFiles.add(KEY_BMBF_LOGO_PDF);
    }
    
    if (!texTemplates.containsKey(KEY_CREATIVE_COMMONS_EPS)) {
      missingTexFiles.add(KEY_CREATIVE_COMMONS_EPS);
    }
    
    if (!texTemplates.containsKey(KEY_CREATIVE_COMMONS_PNG)) {
      missingTexFiles.add(KEY_CREATIVE_COMMONS_PNG);
    }
    
    if (!texTemplates.containsKey(KEY_FDZ_LOGO_EPS)) {
      missingTexFiles.add(KEY_FDZ_LOGO_EPS);
    }
    
    if (!texTemplates.containsKey(KEY_FDZ_LOGO_PDF)) {
      missingTexFiles.add(KEY_FDZ_LOGO_PDF);
    }
    
    if (!texTemplates.containsKey(KEY_FDZ_LOGO_NO_TEXT_EPS)) {
      missingTexFiles.add(KEY_FDZ_LOGO_NO_TEXT_EPS);
    }
    
    if (!texTemplates.containsKey(KEY_FDZ_LOGO_NO_TEXT_PDF)) {
      missingTexFiles.add(KEY_FDZ_LOGO_NO_TEXT_PDF);
    }
    
    return missingTexFiles;
  }

  /**
   * This method fills the tex templates.
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
   * @throws IOException thrown if a stream cannot be closed
   */
  private String saveCompleteTexTemplate(ByteArrayOutputStream byteArrayOutputStream,
      String fileName) throws IOException {
    try (OutputStream outputStream = byteArrayOutputStream) {
      // prepare additional information for tex file.
      ByteArrayInputStream byteArrayInputStream =
          new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
      
      // No Update by API, so we have to delete first.
      fileService.deleteTempFile(fileName);
      
      // Save tex file, based on the bytearray*streams
      return fileService.saveTempFile(byteArrayInputStream, fileName, CONTENT_TYPE_ZIP);      
    }
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
    String dataSetId = ((DataSet)dataForTemplate.get("dataSet")).getId();
    List<Variable> variables = this.variableRepository
        .findByDataSetIdOrderByIndexInDataSetAsc(dataSetId);
    Map<String, Variable> variablesMap = Maps.uniqueIndex(variables, new VariableFunction());
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

      // Create a Map with Questions
      if (variable.getRelatedQuestions() != null 
          && !variable.getRelatedQuestions().isEmpty()) {        
        for (RelatedQuestion relatedQuestion : variable.getRelatedQuestions()) {
          //question is unknown. add it to the question map.
          if (!questionsMap.containsKey(relatedQuestion.getQuestionId())) {
            Question question = this.questionRepository.findOne(relatedQuestion.getQuestionId());
            questionsMap.put(relatedQuestion.getQuestionId(), question);
          }          
        }
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
