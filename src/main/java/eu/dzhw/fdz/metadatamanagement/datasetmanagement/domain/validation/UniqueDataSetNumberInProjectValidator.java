package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;

/**
 * This validator checks the number of a data set within a project. The data set number has to be 
 * unique within the project.
 *
 * @author Daniel Katzberg
 *
 */
public class UniqueDataSetNumberInProjectValidator
    implements ConstraintValidator<UniqueDatasetNumberInProject, DataSet> {

  @Autowired
  private DataSetRepository dataSetRepository;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(UniqueDatasetNumberInProject constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(DataSet dataSet, ConstraintValidatorContext context) {

    // search for data sets with the same project id and number (not id!)
    List<IdAndVersionProjection> dataSetWithNumberAndProject =
        this.dataSetRepository.findIdsByDataAcquisitionProjectIdAndNumber(
        dataSet.getDataAcquisitionProjectId(), dataSet.getNumber());
    
    // ignore the same object (for an update)
    List<IdAndVersionProjection> dataSetWithoutSameDataSet = new ArrayList<>();
    for (IdAndVersionProjection dataSetFromList : dataSetWithNumberAndProject) {
      if (!dataSetFromList.getId()
          .equals(dataSet.getId())) {
        dataSetWithoutSameDataSet.add(dataSetFromList);
      }
    }
    
    //check for data set with same number and project id
    // if no other found .-> good!
    return dataSetWithoutSameDataSet.size() == 0;
  }

}
