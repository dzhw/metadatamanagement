package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.dzhw.fdz.metadatamanagement.common.rest.GenericDomainObjectResourceController;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;

/**
 * DataSet REST Controller which overrides default spring data rest methods.
 * 
 * @author René Reitmann
 */
@RepositoryRestController
public class DataSetResourceController
    extends GenericDomainObjectResourceController<DataSet, DataSetRepository> {

  @Autowired
  public DataSetResourceController(DataSetRepository dataSetRepository) {
    super(dataSetRepository);
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
}
