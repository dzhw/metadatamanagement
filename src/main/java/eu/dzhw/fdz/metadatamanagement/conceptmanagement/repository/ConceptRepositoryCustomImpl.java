package eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Default implementation of {@link ConceptRepositoryCustom}.
 */
@Component
public class ConceptRepositoryCustomImpl implements ConceptRepositoryCustom {

  private MongoTemplate mongoTemplate;

  @Autowired
  public ConceptRepositoryCustomImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Set<String> findQuestionIdsByConceptId(String conceptId) {
    Query query = createIdsByConceptIdQuery(conceptId);
    List<Question> questions = mongoTemplate.find(query, Question.class);
    return questions.stream().map(Question::getId).collect(Collectors.toSet());
  }

  @Override
  public Set<String> findInstrumentIdsByConceptId(String conceptId) {
    Query query = createIdsByConceptIdQuery(conceptId);
    List<Instrument> instruments = mongoTemplate.find(query, Instrument.class);
    return instruments.stream().map(Instrument::getId).collect(Collectors.toSet());
  }

  private Query createIdsByConceptIdQuery(String conceptId) {
    Criteria criteria = where("conceptIds").is(conceptId);
    Query query = new Query(criteria);
    query.fields().include("_id");
    return query;
  }
}
