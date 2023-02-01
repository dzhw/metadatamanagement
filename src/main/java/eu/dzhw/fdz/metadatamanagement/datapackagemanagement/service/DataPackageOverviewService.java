package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.amazonaws.transform.MapEntry;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.web.Link;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Period;
import eu.dzhw.fdz.metadatamanagement.common.service.AbstractReportService;
import eu.dzhw.fdz.metadatamanagement.common.service.MarkdownHelper;
import eu.dzhw.fdz.metadatamanagement.common.service.TaskManagementService;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.SubDataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.filemanagement.service.FileService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.helper.DoiBuilder;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Population;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.AccessWays;

/**
 * This service generated the data package overview. It fill the tex template using freemarker.
 */
@Service
public class DataPackageOverviewService extends AbstractReportService {
  private final SurveyRepository surveyRepository;
  private final DataPackageRepository dataPackageRepository;
  private final DataSetRepository dataSetRepository;
  private final DoiBuilder doiBuilder;

  @Value("${metadatamanagement.server.context-root}")
  private String baseUrl;

  /**
   * Instantiate and autowire the service.
   */
  public DataPackageOverviewService(FileService fileService, TaskManagementService taskService,
      MarkdownHelper markdownHelper, SurveyRepository surveyRepository,
      DataPackageRepository dataPackageRepository, DataSetRepository dataSetRepository,
      DoiBuilder doiBuilder) {
    super(fileService, taskService, markdownHelper);
    this.surveyRepository = surveyRepository;
    this.dataPackageRepository = dataPackageRepository;
    this.dataSetRepository = dataSetRepository;
    this.doiBuilder = doiBuilder;
  }

  @Override
  protected Map<String, Object> loadDataForTemplateFilling(String dataPackageId, String version) {
    // Create Map for the template
    Map<String, Object> dataForTemplate = new HashMap<>();
    DataPackage dataPackage = dataPackageRepository.findById(dataPackageId).orElse(null);
    // reformat title
    dataPackage.reformatTitle();
    dataForTemplate.put("dataPackage", dataPackage);
    List<Survey> surveys =
        surveyRepository.findByDataPackageIdOrderBySerialNumberAscNumberAsc(dataPackageId);

    List<I18nString> surveyDataTypes =
        surveys.stream().map(Survey::getDataType).distinct().collect(Collectors.toList());
    dataForTemplate.put("surveyDataTypes", surveyDataTypes);

    // create deduplicated strings for displaying survey information
    Map<String, List<Integer>> surveyAnnotationsMapDe = new LinkedHashMap<>();
    Map<String, List<Integer>> surveyAnnotationsMapEn = new LinkedHashMap<>();
    Map<String, List<Integer>> surveySampleMapDe = new LinkedHashMap<>();
    Map<String, List<Integer>> surveySampleMapEn = new LinkedHashMap<>();
    Map<String, List<Integer>> surveyMethodMapDe = new LinkedHashMap<>();
    Map<String, List<Integer>> surveyMethodMapEn = new LinkedHashMap<>();
    Map<String, List<Integer>> populationDescriptionMapDe = new LinkedHashMap<>();
    Map<String, List<Integer>> populationDescriptionMapEn = new LinkedHashMap<>();
    Map<Period, List<Integer>> surveyPeriodMap = new LinkedHashMap<>();

    Map<Integer, Double> cleanedResponseRatesMap = new LinkedHashMap<>();

    int surveyIndex = 0;
    for (Survey survey : surveys) {
      surveyIndex++;
      fillMap(surveyAnnotationsMapDe, surveyIndex, survey, Survey::getAnnotations,
          I18nString::getDe);
      fillMap(surveyAnnotationsMapEn, surveyIndex, survey, Survey::getAnnotations,
          I18nString::getEn);
      fillMap(surveySampleMapDe, surveyIndex, survey, Survey::getSample, I18nString::getDe);
      fillMap(surveySampleMapEn, surveyIndex, survey, Survey::getSample, I18nString::getEn);
      fillMap(surveyMethodMapDe, surveyIndex, survey, Survey::getSurveyMethod, I18nString::getDe);
      fillMap(surveyMethodMapEn, surveyIndex, survey, Survey::getSurveyMethod, I18nString::getEn);
      fillMap(populationDescriptionMapDe, surveyIndex, survey.getPopulation(),
          Population::getDescription, I18nString::getDe);
      fillMap(populationDescriptionMapEn, surveyIndex, survey.getPopulation(),
          Population::getDescription, I18nString::getEn);
      fillMap(surveyPeriodMap, surveyIndex, survey, Survey::getFieldPeriod);

      if (survey.getResponseRate() != null && survey.getResponseRate() != 0.0) {
        cleanedResponseRatesMap.put(surveyIndex, survey.getResponseRate());
      }
    }

    // generate map for displaying access ways
    List<DataSet> dataSets = dataSetRepository.findByDataPackageIdOrderByNumber(dataPackageId);
    Map<String, List<String>> accessWaysMap = generateAccessWaysMap(dataSets);
    dataForTemplate.put("accessWaysMap", accessWaysMap);

    // generate quali and quanti datasets lists
    generateSeperateDataSetLists(dataSets, surveys, dataForTemplate);

    String doi = doiBuilder.buildDataOrAnalysisPackageDoi(dataPackage.getDataAcquisitionProjectId(),
        Release.builder().version(version).build());
    dataForTemplate.put("surveys", surveys);
    dataForTemplate.put("surveyAnnotationsMapDe", surveyAnnotationsMapDe);
    dataForTemplate.put("surveyAnnotationsMapEn", surveyAnnotationsMapEn);
    dataForTemplate.put("surveySampleMapDe", surveySampleMapDe);
    dataForTemplate.put("surveySampleMapEn", surveySampleMapEn);
    dataForTemplate.put("surveyMethodMapDe", surveyMethodMapDe);
    dataForTemplate.put("surveyMethodMapEn", surveyMethodMapEn);
    dataForTemplate.put("populationDescriptionMapDe", populationDescriptionMapDe);
    dataForTemplate.put("populationDescriptionMapEn", populationDescriptionMapEn);
    dataForTemplate.put("surveyPeriodMap", surveyPeriodMap);
    dataForTemplate.put("cleanedResponseRatesMap", cleanedResponseRatesMap);
    dataForTemplate.put("numberOfObservationsMap", createNumberOfObservationsMap(dataSets));
    dataForTemplate.put("version", version);
    dataForTemplate.put("baseUrl", baseUrl);
    dataForTemplate.put("doi", doi);

    return dataForTemplate;
  }

  private void generateSeperateDataSetLists(List<DataSet> dataSets, List<Survey> surveys,
      Map<String, Object> dataForTemplate) {
    Map<String, Survey> surveyMap =
        surveys.stream().collect(Collectors.toMap(Survey::getId, Function.identity()));
    List<DataSet> qualiDataSets = new ArrayList<>();
    List<DataSet> nonQualiDataSets = new ArrayList<>();
    for (DataSet dataSet : dataSets) {
      // reformat description
      dataSet.reformatDescription();
      boolean isQualiDataSet = true;
      for (String surveyId : dataSet.getSurveyIds()) {
        if (surveyMap.containsKey(surveyId)) {
          if (surveyMap.get(surveyId).getDataType().equals(DataTypes.QUANTITATIVE_DATA)) {
            isQualiDataSet = false;
          }
        } else {
          isQualiDataSet = false;
        }
      }
      if (isQualiDataSet) {
        qualiDataSets.add(dataSet);
      } else {
        nonQualiDataSets.add(dataSet);
      }
    }
    dataForTemplate.put("qualiDataSets", qualiDataSets);
    dataForTemplate.put("nonQualiDataSets", nonQualiDataSets);
  }

  private Map<String, List<String>> generateAccessWaysMap(List<DataSet> dataSets) {
    List<String> accessWays =
        dataSets.stream().map(DataSet::getSubDataSets).flatMap(Collection::stream)
            .map(SubDataSet::getAccessWay).distinct().collect(Collectors.toList());
    accessWays.sort(Comparator.comparing(accessWay -> AccessWays.ALL.indexOf(accessWay)));
    Map<String, List<String>> accessWayMap = new LinkedHashMap<>();
    for (String accessWay : accessWays) {
      String key = null;
      if (accessWay.endsWith("cuf")) {
        key = "CUF: ";
      }
      if (accessWay.endsWith("suf")) {
        key = "SUF: ";
      }
      if (key != null) {
        if (accessWayMap.containsKey(key)) {
          accessWayMap.get(key).add(AccessWays.displayAccessWay(accessWay).replace(key, ""));
        } else {
          accessWayMap.put(key,
              Lists.newArrayList(AccessWays.displayAccessWay(accessWay).replace(key, "")));
        }
      }
    }
    return accessWayMap;
  }

  @SuppressWarnings("unchecked")
  private Map<Integer, Map<String, List<Pair<String, Integer>>>> createNumberOfObservationsMap(
      List<DataSet> dataSets) {
    Map<Integer, Map<String, List<Pair<String, Integer>>>> result = new HashMap<>();
    for (DataSet dataSet : dataSets) {
      Map<String, List<Pair<String, Integer>>> dataProductToAccessWayMap = new LinkedHashMap<>();
      result.put(dataSet.getNumber(), dataProductToAccessWayMap);
      dataSet.getSubDataSets().sort(
          Comparator.comparing(subDataSet -> AccessWays.ALL.indexOf(subDataSet.getAccessWay())));
      for (SubDataSet subDataSet : dataSet.getSubDataSets()) {
        String key = null;
        if (subDataSet.getAccessWay().endsWith("cuf")) {
          key = "CUF: ";
        }
        if (subDataSet.getAccessWay().endsWith("suf")) {
          key = "SUF: ";
        }
        if (key != null) {
          if (dataProductToAccessWayMap.containsKey(key)) {
            dataProductToAccessWayMap.get(key)
                .add(new ImmutablePair<String, Integer>(
                    AccessWays.displayAccessWay(subDataSet.getAccessWay()).replace(key, ""),
                    subDataSet.getNumberOfObservations()));
          } else {
            dataProductToAccessWayMap.put(key,
                Lists.newArrayList(new ImmutablePair<String, Integer>(
                    AccessWays.displayAccessWay(subDataSet.getAccessWay()).replace(key, ""),
                    subDataSet.getNumberOfObservations())));
          }
        }
      }
    }
    return result;
  }

  private static <T> void fillMap(Map<String, List<Integer>> map, Integer surveyIndex, T baseObject,
      Function<T, I18nString> i18nStringGetter, Function<I18nString, String> languageGetter) {
    if (i18nStringGetter.apply(baseObject) != null
        && !StringUtils.isEmpty(languageGetter.apply(i18nStringGetter.apply(baseObject)))) {
      String property = languageGetter.apply(i18nStringGetter.apply(baseObject));
      if (map.containsKey(property)) {
        map.get(property).add(surveyIndex);
      } else {
        map.put(property, Lists.newArrayList(surveyIndex));
      }
    }
  }

  private static <T> void fillMap(Map<Period, List<Integer>> map, Integer surveyIndex, T baseObject,
      Function<T, Period> periodGetter) {
    if (periodGetter.apply(baseObject) != null) {
      Period property = periodGetter.apply(baseObject);
      if (map.containsKey(property)) {
        map.get(property).add(surveyIndex);
      } else {
        map.put(property, Lists.newArrayList(surveyIndex));
      }
    }
  }

  @Override
  protected List<String> getSimpleTemplateFiles() {
    return List.of("Main.tex");
  }

  @Override
  protected List<String> getComplexTemplateFiles() {
    return new ArrayList<>();
  }
}
