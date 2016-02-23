package eu.dzhw.fdz.metadatamanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import eu.dzhw.fdz.metadatamanagement.domain.Concept;

/**
 * Spring Data MongoDB repository for the Concept entity.
 * 
 * @author Daniel Katzberg
 *
 */
@RepositoryRestResource(path = "/concepts")
public interface ConceptRepository
    extends MongoRepository<Concept, String>, QueryDslPredicateExecutor<Concept> {

}
