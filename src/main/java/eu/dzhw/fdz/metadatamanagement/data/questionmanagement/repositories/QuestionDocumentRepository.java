package eu.dzhw.fdz.metadatamanagement.data.questionmanagement.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionDocument;

/**
 * This defines the basic findBy methods for the question document repository.
 * 
 * @author Daniel Katzberg
 *
 */
public interface QuestionDocumentRepository
    extends ElasticsearchRepository<QuestionDocument, String>, QuestionDocumentRepositoryCustom {

}
