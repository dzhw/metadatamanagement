package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import lombok.RequiredArgsConstructor;

/**
 * Validator which ensures that there is a dataPackage with the given id.
 * 
 * @author René Reitmann
 */
@RequiredArgsConstructor
public class DataPackageExistsValidator implements ConstraintValidator<DataPackageExists, String> {

  private final DataPackageRepository dataPackageRepository;
  
  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(DataPackageExists constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String dataPackageId, ConstraintValidatorContext context) {   
    return dataPackageRepository.existsById(dataPackageId);
  }
}
