package eu.dzhw.fdz.metadatamanagement.searchmanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchIndicesVersion;

/**
 * Spring Data MongoDB repository for the {@link ElasticsearchIndicesVersion}.
 */
@Repository
public interface ElasticsearchIndicesVersionRepository 
    extends MongoRepository<ElasticsearchIndicesVersion, String> {

}
