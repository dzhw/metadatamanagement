package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericShadowableDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * DataSet REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
public class DataSetResourceController
    extends GenericShadowableDomainObjectResourceController<DataSet, DataSetRepository> {

  @Autowired
  public DataSetResourceController(DataSetRepository dataSetRepository, ApplicationEventPublisher
      applicationEventPublisher) {
    super(dataSetRepository, applicationEventPublisher);
  }

  /**
   * Override default get by id since it does not set cache headers correctly.
   *
   * @param id a dataSet id
   * @return the dataSet or not found
   */
  @RequestMapping(method = RequestMethod.GET, value = "/data-sets/{id:.+}")
  public ResponseEntity<DataSet> findDataSet(@PathVariable String id) {
    return super.findDomainObject(id);
  }

  /**
   * Override default put by id to prevent updates on shadow copies.
   * @param id Data set id
   */
  @RequestMapping(method = RequestMethod.PUT, value = "/data-sets/{id:.+}")
  public ResponseEntity<?> update(@PathVariable String id, @RequestBody DataSet dataSet) {
    return super.putDomainObject(id, dataSet);
  }

  /**
   * Override default post to prevent creating shadow copies by a client.
   * @param dataSet DataSet to create
   */
  @RequestMapping(method = RequestMethod.POST, value = "/data-sets")
  public ResponseEntity<DataSet> create(@RequestBody DataSet dataSet) {
    return super.postDomainObject(dataSet);
  }

  /**
   * Override default delete to prevent deleting shadow copies.
   * @param id Id of the data set to delete
   */
  @RequestMapping(method = RequestMethod.DELETE, value = "/data-sets/{id:.+}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    return super.deleteDomainObject(id);
  }

  @Override
  protected URI buildLocationHeaderUri(DataSet domainObject) {
    return UriComponentsBuilder.fromPath("/api/data-sets/" + domainObject.getId()).build().toUri();
  }
}
