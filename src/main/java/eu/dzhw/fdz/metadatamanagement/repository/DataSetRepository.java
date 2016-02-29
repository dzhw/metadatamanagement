package eu.dzhw.fdz.metadatamanagement.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.domain.DataSet;

/**
 * The Repository for {@link DataSet} domain object. The data will be insert with a REST API and
 * save in a mongo db.
 * 
 * @author Daniel Katzberg *
 */
@RepositoryRestResource(path = "/data_sets")
public interface DataSetRepository
    extends MongoRepository<DataSet, String>, QueryDslPredicateExecutor<DataSet> {

  @RestResource(exported = false)
  List<DataSet> deleteByDataAcquisitionProjectId(String dataAcquisitionProjectId);
  
}
