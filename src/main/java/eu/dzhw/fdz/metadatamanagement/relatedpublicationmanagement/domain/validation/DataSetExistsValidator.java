package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import lombok.RequiredArgsConstructor;

/**
 * Validator which ensures that there is a data set with the given id.
 * 
 * @author René Reitmann
 */
@RequiredArgsConstructor
public class DataSetExistsValidator implements ConstraintValidator<DataSetExists, String> {

  private final DataSetRepository dataSetRepository;
  
  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(DataSetExists constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String dataSetId, ConstraintValidatorContext context) {   
    return dataSetRepository.existsById(dataSetId);
  }
}