package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import eu.dzhw.fdz.metadatamanagement.common.rest.util.ZipUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.AbstractReportService;
import eu.dzhw.fdz.metadatamanagement.common.service.MarkdownHelper;
import eu.dzhw.fdz.metadatamanagement.common.service.TaskManagementService;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.filemanagement.service.FileService;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RelatedQuestion;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ValidResponse;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * This service fills tex templates with data and put it into the gridfs / mongodb.
 *
 * @author Daniel Katzberg
 */
@Service
public class DataSetReportService extends AbstractReportService {
  private final DataSetRepository dataSetRepository;

  private final VariableRepository variableRepository;

  private final QuestionRepository questionRepository;

  private final DataPackageRepository dataPackageRepository;

  private final InstrumentRepository instrumentRepository;

  /**
   * Instantiate and autowire the service.
   */
  public DataSetReportService(FileService fileService, TaskManagementService taskService,
      MarkdownHelper markdownHelper, DataSetRepository dataSetRepository,
      VariableRepository variableRepository, QuestionRepository questionRepository,
      DataPackageRepository dataPackageRepository, InstrumentRepository instrumentRepository) {
    super(fileService, taskService, markdownHelper);
    this.dataSetRepository = dataSetRepository;
    this.variableRepository = variableRepository;
    this.questionRepository = questionRepository;
    this.dataPackageRepository = dataPackageRepository;
    this.instrumentRepository = instrumentRepository;
  }

  @Override
  protected Map<String, Object> loadDataForTemplateFilling(String dataSetId, String version) {

    // Create Map for the template
    Map<String, Object> dataForTemplate = new HashMap<>();

    // Create Information for the latex template.
    dataForTemplate =
        this.addDataPackageAndDataSetAndLastRelease(dataForTemplate, dataSetId, version);
    dataForTemplate = this.createVariableDependingMaps(dataForTemplate);

    return dataForTemplate;
  }

  /**
   * This is a "fluent" method for the Map with created Objects of the latex template. Added data
   * set information to the map of objects for the template.
   *
   * @param dataForTemplate The map for the template with all added objects before this method.
   * @param dataSetId The id of the used data set; Root Element of the report.
   * @param version The version of the report as it is displayed in the title.
   * @return The map for the template as fluent result. Added some created elements within this
   *         method.
   */
  private Map<String, Object> addDataPackageAndDataSetAndLastRelease(
      Map<String, Object> dataForTemplate, String dataSetId, String version) {
    // Get DataSet and check the valid result
    DataSet dataSet = this.dataSetRepository.findById(dataSetId).orElse(null);
    // reformat description
    dataSet.reformatDescription();
    DataPackage dataPackage;

    if (dataSet != null) {
      dataPackage = this.dataPackageRepository.findById(dataSet.getDataPackageId()).orElse(null);
      // reformat title strings
      dataPackage.reformatTitle();
    } else {
      dataPackage = null;
    }

    dataForTemplate.put("dataPackage", dataPackage);
    dataForTemplate.put("dataSet", dataSet);
    dataForTemplate.put("version", version);

    return dataForTemplate;
  }

  /**
   * Fluent Method for creating Map Objects for the latex template. Creates the follow Maps:
   * questions : Map with all question, which are connected by variables of a given data set (key is
   * variable.questionId) isAMissingCounterMap: A map with counter, how many isAMissing Values a
   * variable has (key is variable.id) firstTenValues: The first ten isAMissing values for one tex
   * template layout. (key is variable.id) lastTenValues: The last ten isAMissing values for one tex
   * template layout. (key is variable.id)
   *
   * @param dataForTemplate The map for the template with all added objects before this method.
   * @return The map for the template as fluent result. Added some created elements within this
   *         method.
   */
  private Map<String, Object> createVariableDependingMaps(Map<String, Object> dataForTemplate) {
    // Create a Map of Variables
    DataSet dataSet = (DataSet) dataForTemplate.get("dataSet");
    String dataSetId = dataSet != null ? dataSet.getId() : null;
    List<Variable> variables;
    if (dataSetId != null) {
      variables = this.variableRepository.findByDataSetIdOrderByIndexInDataSetAsc(dataSetId);
    } else {
      variables = Collections.emptyList();
    }
    Map<String, Variable> variablesMap = Maps.uniqueIndex(variables, Variable::getId);
    dataForTemplate.put("variables", variablesMap);

    // Create different information from the variable
    Map<String, Question> questionsMap = new HashMap<>();
    Map<String, Instrument> instrumentMap = new HashMap<>();
    Map<String, List<ValidResponse>> firstTenValidResponses = new HashMap<>();
    Map<String, List<ValidResponse>> lastTenValidResponses = new HashMap<>();
    Map<String, List<VariableSubDocumentProjection>> repeatedMeasurementVariables = new HashMap<>();
    Map<String, List<VariableSubDocumentProjection>> derivedVariables = new HashMap<>();

    for (Variable variable : variables) {
      int sizeValidResponses = 0;
      if (variable.getDistribution() != null
          && variable.getDistribution().getValidResponses() != null) {
        sizeValidResponses = variable.getDistribution().getValidResponses().size();
      }

      // Create a Map with Questions
      if (variable.getRelatedQuestions() != null && !variable.getRelatedQuestions().isEmpty()) {
        for (RelatedQuestion relatedQuestion : variable.getRelatedQuestions()) {
          // question is unknown. add it to the question map.
          if (!questionsMap.containsKey(relatedQuestion.getQuestionId())) {
            this.questionRepository.findById(relatedQuestion.getQuestionId())
                .ifPresent(question -> questionsMap.put(relatedQuestion.getQuestionId(), question));
          }
        }
      }

      // Create a Map with Instruments
      if (!questionsMap.isEmpty()) {
        questionsMap.values().forEach(question -> {
          if (!instrumentMap.containsKey(question.getInstrumentId())) {
            this.instrumentRepository.findById(question.getInstrumentId())
                .ifPresent(instrument -> instrumentMap.put(question.getInstrumentId(), instrument));
          }
        });
      }

      // Create the first and last ten isAMissing Values to different list, if there are more
      // than 20.
      if (sizeValidResponses > 20) {
        firstTenValidResponses.put(variable.getId(),
            variable.getDistribution().getValidResponses().subList(0, 10));
        lastTenValidResponses.put(variable.getId(), variable.getDistribution().getValidResponses()
            .subList(sizeValidResponses - 10, sizeValidResponses));
      }

      if (!StringUtils.isEmpty(variable.getRepeatedMeasurementIdentifier())) {
        List<VariableSubDocumentProjection> otherVariablesInPanel =
            this.variableRepository.findAllByRepeatedMeasurementIdentifierAndDataSetIdAndIdNot(
                variable.getRepeatedMeasurementIdentifier(), variable.getDataSetId(),
                variable.getId());
        repeatedMeasurementVariables.put(variable.getId(), otherVariablesInPanel);
      }

      if (!StringUtils.isEmpty(variable.getDerivedVariablesIdentifier())) {
        List<VariableSubDocumentProjection> otherDerivedVariables =
            this.variableRepository.findAllByDerivedVariablesIdentifierAndDataSetIdAndIdNot(
                variable.getDerivedVariablesIdentifier(), variable.getDataSetId(),
                variable.getId());
        derivedVariables.put(variable.getId(), otherDerivedVariables);
      }
    }
    dataForTemplate.put("questions", questionsMap);
    dataForTemplate.put("instruments", instrumentMap);
    dataForTemplate.put("firstTenValidResponses", firstTenValidResponses);
    dataForTemplate.put("lastTenValidResponses", lastTenValidResponses);
    dataForTemplate.put("repeatedMeasurementVariables", repeatedMeasurementVariables);
    dataForTemplate.put("derivedVariables", derivedVariables);

    return dataForTemplate;

  }

  @Override
  protected List<String> getSimpleTemplateFiles() {
    return List.of("Main.tex", "Variablelist.tex");
  }

  @Override
  protected List<String> getComplexTemplateFiles() {
    return List.of("variables/Variable.tex");
  }

  @Override
  protected void handleComplexTemplateFile(String templateFilename, FileSystem zipFileSystem,
      Map<String, Object> dataForTemplate, Configuration templateConfiguration)
      throws IOException, TemplateException {
    @SuppressWarnings("unchecked")
    Map<String, Variable> variablesMap = (Map<String, Variable>) dataForTemplate.get("variables");
    Collection<Variable> variables = variablesMap.values();
    String template = ZipUtil.readFileFromZip(zipFileSystem.getPath(templateFilename));

    for (Variable variable : variables) {
      dataForTemplate.put("variable", variable);
      String filledVariablesFile =
          fillTemplate(template, templateConfiguration, dataForTemplate, templateFilename);
      Path pathOfVariable = Paths.get("variables/" + variable.getName() + ".tex");
      final Path root = zipFileSystem.getPath("/");
      final Path dest = zipFileSystem.getPath(root.toString(), pathOfVariable.toString());
      ZipUtil.writeFileToZip(dest, filledVariablesFile);
    }

    // Delete Variables.tex file from zip
    Files.delete(zipFileSystem.getPath(templateFilename));
  }
}
