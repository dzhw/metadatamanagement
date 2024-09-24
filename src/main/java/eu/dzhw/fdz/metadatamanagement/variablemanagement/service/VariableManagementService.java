package eu.dzhw.fdz.metadatamanagement.variablemanagement.service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchIoException;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataAcquisitionProjectSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataPackageSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.VariableSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.AccessWays;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.service.helper.VariableCrudHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * Service for managing the domain object/aggregate {@link Variable}.
 *
 * @author René Reitmann
 */
@Service
@RepositoryEventHandler
@RequiredArgsConstructor
@Slf4j
public class VariableManagementService implements CrudService<Variable> {

  private final QuestionRepository questionRepository;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private final VariableRepository variableRepository;

  private final VariableCrudHelper crudHelper;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final RestHighLevelClient client;

  private final Gson gson;

  private static final String landingPageBaseUrl = "https://metadata.fdz.dzhw.eu/en/variables/";
  private static final String pidPrefix = "21.T11998/dzhw:";



  /**
   * Delete all variables when the dataAcquisitionProject was deleted.
   *
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteAllVariablesByProjectId(dataAcquisitionProject.getId());
  }

  /**
   * Update all variables of the project, when the project is released.
   *
   * @param dataAcquisitionProject the changed project
   */
  @HandleAfterSave
  public void onDataAcquisitionProjectUpdated(DataAcquisitionProject dataAcquisitionProject) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository
            .streamIdsByDataAcquisitionProjectId(dataAcquisitionProject.getId()),
        ElasticsearchType.variables);
  }

  /**
   * A service method for deletion of variables within a data acquisition project.
   *
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public void deleteAllVariablesByProjectId(String dataAcquisitionProjectId) {
    // TODO check access rights by project
    try (Stream<Variable> variables =
        variableRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      variables.forEach(variable -> {
        crudHelper.deleteMaster(variable);
      });
    }
  }

  /**
   * Enqueue update of variable search documents when the data set is changed.
   *
   * @param dataSet the updated, created or deleted data set.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataSetChanged(DataSet dataSet) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository.streamIdsByDataSetId(dataSet.getId()),
        ElasticsearchType.variables);
  }

  /**
   * Enqueue update of variable search documents when the dataPackage is changed.
   *
   * @param dataPackage the updated, created or deleted dataPackage.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataPackageChanged(DataPackage dataPackage) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository.streamIdsByDataPackageId(dataPackage.getId()),
        ElasticsearchType.variables);
  }

  /**
   * Enqueue update of variable search documents when the instrument is changed.
   *
   * @param instrument the updated, created or deleted instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onInstrumentChanged(Instrument instrument) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository.streamIdsByRelatedQuestionsInstrumentId(instrument.getId()),
        ElasticsearchType.variables);
  }

  /**
   * Enqueue update of variable search documents when the question is changed.
   *
   * @param question the updated, created or deleted question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onQuestionChanged(Question question) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository.streamIdsByRelatedQuestionsQuestionId(question.getId()),
        ElasticsearchType.variables);
  }

  /**
   * Enqueue update of variable search documents when the survey is updated.
   *
   * @param survey the updated, created or deleted survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onSurveyChanged(Survey survey) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> variableRepository.streamIdsBySurveyIdsContaining(survey.getId()),
        ElasticsearchType.variables);
  }

  /**
   * Enqueue update of variable search documents when the concept is changed.
   *
   * @param concept the updated, created or deleted concept.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onConceptChanged(Concept concept) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      Set<String> questionIds = questionRepository.streamIdsByConceptIdsContaining(concept.getId())
          .map(question -> question.getId()).collect(Collectors.toSet());
      return variableRepository.streamIdsByRelatedQuestionsQuestionIdIn(questionIds);
    }, ElasticsearchType.variables);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public Variable create(Variable variable) {
    // TODO check access rights by project
    return crudHelper.createMaster(variable);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public Variable save(Variable variable) {
    // TODO check access rights by project
    return crudHelper.saveMaster(variable);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public void delete(Variable variable) {
    // TODO check access rights by project
    crudHelper.deleteMaster(variable);
  }

  @Override
  public Optional<Variable> read(String id) {
    return crudHelper.read(id);
  }

  @Override
  public Optional<Variable> readSearchDocument(String id) {
    return crudHelper.readSearchDocument(id);
  }

  /**
   * Writes all variable data according to the PID metadata schema to a JSON file.
   * @return the JSON file
   * @throws IOException if elasticsearch fails
   */
  public ResponseEntity<?> exportVariablesAsJson() throws IOException {
    ArrayNode variableMetadata = this.getPidVariablesMetadata();
    ObjectNode node = objectMapper.createObjectNode();
    node.set("variables", variableMetadata);
    try {
      ByteArrayResource resource = new ByteArrayResource(objectMapper.writeValueAsBytes(node));
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Disposition", "attachment; filename=Variables_PID_MDM_Export.json");
      log.info("Finished variable export with " + variableMetadata.size() + " variables.");
      return ResponseEntity.ok()
        .headers(headers)
        .body(resource);
    } catch (IOException ex) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Mapping variable metadata to metadata schema needed for PID registration.
   * @return collected metadata as JsonNode
   */
  private ArrayNode getPidVariablesMetadata() {
    ArrayNode jsonNode = objectMapper.createArrayNode();
    SearchRequest projectsRequest = new SearchRequest();
    SearchSourceBuilder builderProjects = new SearchSourceBuilder();
    builderProjects.query(QueryBuilders.boolQuery()
      .must(new TermQueryBuilder("shadow", false))
      .must(new TermQueryBuilder("hidden", false))
      .must(new ExistsQueryBuilder("release"))
      .must(new TermQueryBuilder("release.isPreRelease", false)))
      .size(10000);
    projectsRequest.source(builderProjects);
    projectsRequest.indices("data_acquisition_projects");
    try {
      SearchResponse response =  client.search(projectsRequest, RequestOptions.DEFAULT);
      List<SearchHit> hits = Arrays.asList(response.getHits().getHits());
      log.info("Exporting variables for " + hits.size() + " projects");
      for (var hit : hits) {
        DataAcquisitionProjectSearchDocument project = gson.fromJson(
            hit.getSourceAsString(), DataAcquisitionProjectSearchDocument.class);
        SearchRequest dataPackagesRequest = new SearchRequest();
        SearchSourceBuilder builderDataPackages = new SearchSourceBuilder();
        builderDataPackages.query(QueryBuilders.boolQuery()
          .must(new TermQueryBuilder("shadow", true))
          .must(new TermQueryBuilder("dataAcquisitionProjectId", project.getId()
            + "-" + project.getRelease().getVersion())))
          .size(10);
        dataPackagesRequest.source(builderDataPackages);
        dataPackagesRequest.indices("data_packages");
        SearchResponse responseDataPackage =  client.search(dataPackagesRequest, RequestOptions.DEFAULT);
        List<SearchHit> hitsDataPackages = Arrays.asList(responseDataPackage.getHits().getHits());
        assert hitsDataPackages.size() == 1;

        SearchRequest variablesRequest = new SearchRequest();
        SearchSourceBuilder builderVariables = new SearchSourceBuilder();
        builderVariables.query(QueryBuilders.boolQuery()
          .must(new TermQueryBuilder("shadow", true))
          .must(new TermQueryBuilder("dataAcquisitionProjectId", project.getId()
            + "-" + project.getRelease().getVersion())))
          .size(10000);
        variablesRequest.source(builderVariables);
        variablesRequest.indices("variables");
        SearchResponse responseVariables =  client.search(variablesRequest, RequestOptions.DEFAULT);
        List<SearchHit> hitsVariables = Arrays.asList(responseVariables.getHits().getHits());
        for (var variable : hitsVariables) {
          VariableSearchDocument variableObj = gson.fromJson(
              variable.getSourceAsString(), VariableSearchDocument.class);
          DataPackageSearchDocument dataPackage = gson.fromJson(
              hitsDataPackages.get(0).getSourceAsString(), DataPackageSearchDocument.class);
          ObjectNode obj = this.getPidMetadataOfVariable(variableObj, project, dataPackage);
          if (obj != null) {
            jsonNode.add(obj);
          }
        }
      }
      return jsonNode;
    } catch (IOException e) {
      throw new ElasticsearchIoException(e);
    }
  }

  /**
   * Collects PID metadata of a variable.
   * @param variable the variable metadata is collected for
   * @param project the project this variable belongs to
   * @param dataPackage the data package this variable belongs to
   * @return the metadata as a json node
   */
  private ObjectNode getPidMetadataOfVariable(
      VariableSearchDocument variable,
      DataAcquisitionProject project,
      DataPackageSearchDocument dataPackage) {
    if (this.hasMissingFields(variable, project, dataPackage)) {
      return null;
    }
    ObjectNode variableObj = objectMapper.createObjectNode();
    variableObj.put("studyDOI", dataPackage.getDoi());
    variableObj.put("variableName", variable.getName());
    variableObj.put("variableLabel", variable.getLabel().getEn());
    variableObj.put("pidProposal", pidPrefix + project.getId() + "_" + variable.getName() + ":"
        + project.getRelease().getVersion());
    variableObj.put("landingPage", landingPageBaseUrl
        + variable.getMasterId().replace("$", "")
        + "?version=" + project.getRelease().getVersion());
    variableObj.put("resourceType", "Variable");
    variableObj.put("title", variable.getName() + ": " + variable.getLabel().getEn());
    variableObj.set("creators", this.compileCreators(dataPackage.getProjectContributors()));
    variableObj.put("publisher", "FDZ-DZHW");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedDateTime = project.getRelease().getLastDate().format(dateTimeFormatter);
    variableObj.put("publicationDate", formattedDateTime);
    variableObj.put("availability", this.compileAccessWays(variable.getAccessWays()));
    return variableObj;
  }

  /**
   * Checks the required fields for PID registration. Variables with missing required fields will not be exported.
   * @param variable  the variable for which a PID should be registered
   * @param project   the project this variable belongs to
   * @param dataPackage the data package this variable belongs to
   * @return true if required fields are missing, else false
   */
  private boolean hasMissingFields(VariableSearchDocument variable,
                                   DataAcquisitionProject project,
                                   DataPackageSearchDocument dataPackage) {
    if (variable.getName() == null || variable.getName().isEmpty()) {
      log.info(String.format("Unable to export variable with id '%s'. Required variable name is missing.",
          variable.getId()));
      return true;
    }
    if (variable.getLabel() == null) {
      log.info(String.format("Unable to export variable with id '%s'. Required variable label is missing.",
          variable.getId()));
      return true;
    } else if ((variable.getLabel().getDe() == null || variable.getLabel().getDe().trim().isEmpty())
        && (variable.getLabel().getEn() == null || variable.getLabel().getEn().trim().isEmpty())) {
      log.info(String.format("Unable to export variable with id '%s'. Required variable label is missing.",
          variable.getId()));
      return true;
    }
    if (dataPackage.getDoi() == null || dataPackage.getDoi().trim().isEmpty()) {
      log.info(String.format("Unable to export variable with id '%s'. Required datapackage DOIl is missing.",
          variable.getId()));
      return true;
    }
    if (dataPackage.getProjectContributors() == null || dataPackage.getProjectContributors().isEmpty()) {
      log.info(String.format("Unable to export variable with id '%s'. Datapackage has no contributors.",
          variable.getId()));
      return true;
    }
    if (project.getId() == null
        || project.getRelease() == null
        || project.getRelease().getVersion() == null
        || project.getRelease().getVersion().trim().isEmpty()
        || project.getRelease().getLastDate() == null) {
      log.info(String.format("Unable to export variable with id '%s'. Required project fields are missing.",
          variable.getId()));
      return true;
    }
    return false;
  }

  /**
   * Compiles the given list of project contributor entries into a PID metadata list.
   * @param projectContributors the list of contributors of this project
   * @return a list of contributors as needed for PID registration
   */
  private ArrayNode compileCreators(List<Person> projectContributors) {
    ArrayNode contributorList = objectMapper.createArrayNode();
    for (var person : projectContributors) {
      ObjectNode personNode = objectMapper.createObjectNode();
      personNode.put("firstName", person.getFirstName());
      if (person.getMiddleName() != null && !person.getMiddleName().isEmpty()) {
        personNode.put("middleName", person.getMiddleName());
      }
      personNode.put("lastName", person.getLastName());
      contributorList.add(personNode);
    }
    return contributorList;
  }

  /**
   * Selects the availablity type according to the available access ways.
   * All download, remote and onsite access ways are mapped to "delivery".
   * All not available access ways are mapped to "not available".
   * Every other access ways us mapped as "unknown".
   * @param accessWays list of access ways
   * @return the availability string
   */
  private String compileAccessWays(List<String> accessWays) {
    if (accessWays.contains(AccessWays.DOWNLOAD_CUF)
        || accessWays.contains(AccessWays.DOWNLOAD_SUF)
        || accessWays.contains(AccessWays.REMOTE_DESKTOP)
        || accessWays.contains(AccessWays.ONSITE_SUF)) {
      return "delivery";
    } else if (accessWays.contains(AccessWays.NOT_ACCESSIBLE)) {
      return "not available";
    } else {
      return "unknown";
    }
  }
}
