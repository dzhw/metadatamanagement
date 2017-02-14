package eu.dzhw.fdz.metadatamanagement.searchmanagement.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.ElasticsearchDao;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

/**
 * Service which sets up all indices.
 * 
 * @author René Reitmann
 */
@Service
public class ElasticsearchAdminService {
  @Autowired
  private ElasticsearchDao elasticsearchDao;

  @Autowired
  private VariableRepository variableRepository;
  
  @Autowired
  private SurveyRepository surveyRepository;
  
  @Autowired
  private DataSetRepository dataSetRepository;
  
  @Autowired
  private QuestionRepository questionRepository;
  
  @Autowired
  private RelatedPublicationRepository relatedPublicationRepository;
  
  @Autowired
  private InstrumentRepository instrumentRepository;
  
  @Autowired
  private StudyRepository studyRepository;
  
  @Autowired
  private ElasticsearchUpdateQueueService updateQueueService;

  @Autowired
  private ResourceLoader resourceLoader;

  private JsonParser jsonParser = new JsonParser();

  /**
   * Recreate the indices and all their mappings.
   */
  public void recreateAllIndices() {
    for (ElasticsearchType type : ElasticsearchType.values()) {
      recreateIndex(type);      
    }
    this.enqueueAllVariables();
    this.enqueueAllSurveys();
    this.enqueueAllDataSets();
    this.enqueueAllQuestions();
    this.enqueueAllRelatedPublications();
    this.enqueueAllInstruments();
    this.enqueueAllStudies();
    updateQueueService.processQueue();
  }
  
  private void enqueueAllStudies() {
    Pageable pageable = new PageRequest(0, 100);
    Slice<Study> studies = studyRepository.findBy(pageable);

    while (studies.hasContent()) {
      studies.forEach(instrument -> {
        updateQueueService.enqueue(
            instrument.getId(), 
            ElasticsearchType.studies, 
            ElasticsearchUpdateQueueAction.UPSERT);
      });
      pageable = pageable.next();
      studies = studyRepository.findBy(pageable);
    }
  }
  
  private void enqueueAllInstruments() {
    Pageable pageable = new PageRequest(0, 100);
    Slice<Instrument> instruments = instrumentRepository.findBy(pageable);

    while (instruments.hasContent()) {
      instruments.forEach(instrument -> {
        updateQueueService.enqueue(
            instrument.getId(), 
            ElasticsearchType.instruments, 
            ElasticsearchUpdateQueueAction.UPSERT);
      });
      pageable = pageable.next();
      instruments = instrumentRepository.findBy(pageable);
    }
  }

  /**
   * Load all variables from mongo and enqueue them for updating.
   */
  private void enqueueAllVariables() {
    Pageable pageable = new PageRequest(0, 100);
    Slice<Variable> variables = variableRepository.findBy(pageable);

    while (variables.hasContent()) {
      variables.forEach(variable -> {
        updateQueueService.enqueue(
            variable.getId(), 
            ElasticsearchType.variables, 
            ElasticsearchUpdateQueueAction.UPSERT);
      });
      pageable = pageable.next();
      variables = variableRepository.findBy(pageable);
    }
  }
  
  /**
   * Load all surveys from mongo and enqueue them for updating.
   */
  private void enqueueAllSurveys() {
    Pageable pageable = new PageRequest(0, 100);
    Slice<Survey> surveys = surveyRepository.findBy(pageable);

    while (surveys.hasContent()) {
      surveys.forEach(survey -> {
        updateQueueService.enqueue(
            survey.getId(), 
            ElasticsearchType.surveys, 
            ElasticsearchUpdateQueueAction.UPSERT);
      });
      pageable = pageable.next();
      surveys = surveyRepository.findBy(pageable);
    }
  }
  
  /**
   * Load all dataSets from mongo and enqueue them for updating.
   */
  private void enqueueAllDataSets() {
    Pageable pageable = new PageRequest(0, 100);
    Slice<DataSet> dataSets = dataSetRepository.findBy(pageable);

    while (dataSets.hasContent()) {
      dataSets.forEach(dataSet -> {
        updateQueueService.enqueue(
            dataSet.getId(), 
            ElasticsearchType.data_sets, 
            ElasticsearchUpdateQueueAction.UPSERT);
      });
      pageable = pageable.next();
      dataSets = dataSetRepository.findBy(pageable);
    }
  }
    
  /**
   * Load all questions from mongo and enqueue them for updating.
   */
  private void enqueueAllQuestions() {
    Pageable pageable = new PageRequest(0, 100);
    Slice<Question> questions = questionRepository.findBy(pageable);

    while (questions.hasContent()) {
      questions.forEach(question -> {
        updateQueueService.enqueue(
            question.getId(), 
            ElasticsearchType.questions, 
            ElasticsearchUpdateQueueAction.UPSERT);
      });
      pageable = pageable.next();
      questions = questionRepository.findBy(pageable);
    }
  }
  
  /**
   * Load all related publications from mongo and enqueue them for updating.
   */
  private void enqueueAllRelatedPublications() {
    Pageable pageable = new PageRequest(0, 100);
    Slice<RelatedPublication> relatedPublications = relatedPublicationRepository.findBy(pageable);

    while (relatedPublications.hasContent()) {
      relatedPublications.forEach(relatedPublication -> {
        updateQueueService.enqueue(
            relatedPublication.getId(), 
            ElasticsearchType.related_publications, 
            ElasticsearchUpdateQueueAction.UPSERT);
      });
      pageable = pageable.next();
      relatedPublications = relatedPublicationRepository.findBy(pageable);
    }
  }

  /**
   * Deletes and create an elasticsearch index.
   * @param type name of the index (equals type for us).
   */
  private void recreateIndex(ElasticsearchType type) {
    if (elasticsearchDao.exists(type.name())) {
      elasticsearchDao.delete(type.name());
      // deleting is asynchronous and thus searchly complains if we create the new index to early
      elasticsearchDao.refresh(Arrays.asList(type.name()));
    }
    elasticsearchDao.createIndex(type.name(), loadSettings());
    elasticsearchDao.putMapping(type.name(), type.name(), 
          loadMapping(type.name()));
  }
  
  /**
   * Load Elasticsearch Index Settings.
   * @return A JSON Representation of the Settings.
   */
  private JsonObject loadSettings() {
    try {
      Reader reader = new InputStreamReader(
          resourceLoader.getResource("classpath:elasticsearch/settings.json")
            .getInputStream(),
          "UTF-8");
      JsonObject settings = jsonParser.parse(reader)
          .getAsJsonObject();
      return settings;
    } catch (IOException e) {
      throw new RuntimeException("Unable to load settings!", e);
    }
  }

  /**
   * Load Elasticsearch Mapping of an index.
   * @param index An elasticsearch index
   * @param type An elasticsearch type of an index.
   * @return A Json Representation of a Mapping
   */
  private JsonObject loadMapping(String type) {
    try {
      Reader reader = new InputStreamReader(resourceLoader
          .getResource("classpath:elasticsearch/" + type + "/mapping.json")
          .getInputStream(), "UTF-8");
      JsonObject mapping = jsonParser.parse(reader)
          .getAsJsonObject();
      return mapping;
    } catch (IOException e) {
      throw new RuntimeException("Unable to load mapping for index " + type + " and type " + type,
          e);
    }
  }
  
  /**
   * Refresh all elasticsearch indices.
   */
  public void refreshAllIndices() {
    elasticsearchDao.refresh(Arrays.asList(
        ElasticsearchType.names()));
  }
  
  /**
   * 
   * @return An Double Value with the number of count documents.
   */
  public Double countAllDocuments() {
    return elasticsearchDao.countAllDocuments();
  }
}
