package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * This defines the basic findBy methods for the variables document repository.
 * 
 * @author Daniel Katzberg
 *
 */
public interface VariableRepository
    extends ElasticsearchRepository<VariableDocument, String>, VariableRepositoryCustom {

}
