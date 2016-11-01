package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;

/**
 * The Repository for the Instruments.
 * 
 * @author Daniel Katzberg
 *
 */
@RepositoryRestResource(path = "/instruments")
public interface InstrumentRepository
    extends MongoRepository<Instrument, String>, QueryDslPredicateExecutor<Instrument> {

  @RestResource(exported = false)
  List<Instrument> deleteByDataAcquisitionProjectId(String dataAcquisitionProjectId);
  
  @RestResource(exported = false)
  Slice<Instrument> findBy(Pageable pageable);
}
