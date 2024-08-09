package eu.dzhw.fdz.metadatamanagement.variablemanagement.service;

import java.io.IOException;
import java.util.ArrayList;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchIoException;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.SurveySearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.VariableSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.CodeBook;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.service.helper.VariableCrudHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Code;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
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

  private final RestHighLevelClient client;

  private final Gson gson;

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
   * Exports all variables belonging to a given study according to the DDI Codebook standard.
   * @param surveyId the ID of the study
   * @return DDI metadata as XML
   */
  public ResponseEntity<?> exportDdiVariablesAsXML(String surveyId) {
    try {
      CodeBook variableMetadata = this.getDdiVariablesMetadata(surveyId);
      XmlMapper mapper = new XmlMapper();
      ByteArrayResource resource = new ByteArrayResource(mapper.writeValueAsBytes(variableMetadata));
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Disposition", "attachment; filename=Variables_PID_MDM_Export.xml");
      return ResponseEntity.ok()
        .headers(headers)
        .body(resource);
    } catch (IOException ex) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Collects metadata according to DDI Codebook standard.
   * @return the metadata
   */
  private CodeBook getDdiVariablesMetadata(String surveyId) throws JsonProcessingException {
    SearchRequest surveyRequest = new SearchRequest();
    SearchSourceBuilder builderSurveys = new SearchSourceBuilder();
    builderSurveys.query(QueryBuilders.termsQuery("id", surveyId));
    surveyRequest.source(builderSurveys);
    surveyRequest.indices("surveys");

    SearchRequest variablesRequest = new SearchRequest();
    SearchSourceBuilder builderVariables = new SearchSourceBuilder();
    builderVariables.query(QueryBuilders.termsQuery("surveyIds", surveyId))
      .size(10000);
    variablesRequest.source(builderVariables);
    variablesRequest.indices("variables");
    try {
      SearchResponse surveyResponse =  client.search(surveyRequest, RequestOptions.DEFAULT);
      List<SearchHit> surveyHits = Arrays.asList(surveyResponse.getHits().getHits());
      if (surveyHits.size() == 0) {
        throw new ElasticsearchException(String.format("Could not find survey with id '%s'", surveyId));
      }
      SurveySearchDocument surveyDoc = gson.fromJson(
        surveyHits.get(0).getSourceAsString(), SurveySearchDocument.class);
      // toDo: Decide on language usage
      CodeBook.StdyDscr.Citation.TitlStmt titlStmt = new CodeBook.StdyDscr.Citation.TitlStmt(surveyDoc.getTitle().getEn());
      CodeBook.StdyDscr.Citation citation = new CodeBook.StdyDscr.Citation(titlStmt);
      CodeBook.StdyDscr study = new CodeBook.StdyDscr(citation);
      SearchResponse variableResponse =  client.search(variablesRequest, RequestOptions.DEFAULT);
      List<SearchHit> hits = Arrays.asList(variableResponse.getHits().getHits());
      if (hits.size() > 0) {
        log.info(String.format("Found %d variables for survey '%s'", hits.size(),surveyId));
        List<CodeBook.DataDscr.Var> variableList = new ArrayList<>();
        for (var variable : hits) {
          VariableSearchDocument variableDoc = gson.fromJson(
            variable.getSourceAsString(), VariableSearchDocument.class);
          CodeBook.DataDscr.Var varMetadata = new CodeBook.DataDscr.Var(variableDoc.getName());
          variableList.add(varMetadata);
        }
        CodeBook.DataDscr dataDscr = new CodeBook.DataDscr(variableList);
        return new CodeBook(study, dataDscr);
      } else {
        log.info(String.format("No variables found for studyId '%s'", surveyId));
      }
    } catch (IOException e) {
      throw new ElasticsearchIoException(e);
    }
    return null;
  }
}
