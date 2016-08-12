package eu.dzhw.fdz.metadatamanagement.citationmanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import eu.dzhw.fdz.metadatamanagement.citationmanagement.domain.Citation;

/**
 * Spring Data MongoDB repository for the citations entity.
 *
 * @author Daniel Katzberg
 *
 */
@RepositoryRestResource(path = "/citations")
public interface CitationRepository
    extends MongoRepository<Citation, String>,
    QueryDslPredicateExecutor<Citation> {

}
