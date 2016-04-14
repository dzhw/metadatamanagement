package eu.dzhw.fdz.metadatamanagement.bibliographicalreferencemanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import eu.dzhw.fdz.metadatamanagement.bibliographicalreferencemanagement.domain.BibliographicalReference;

/**
 * Spring Data MongoDB repository for the bibliographical reference entity.
 * 
 * @author Daniel Katzberg
 *
 */
@RepositoryRestResource(path = "/bibliographical-references")
public interface BibliographicalReferenceRepository
    extends MongoRepository<BibliographicalReference, String>,
    QueryDslPredicateExecutor<BibliographicalReference> {

}
