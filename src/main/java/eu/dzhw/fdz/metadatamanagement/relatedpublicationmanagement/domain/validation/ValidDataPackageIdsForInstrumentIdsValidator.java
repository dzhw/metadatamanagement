package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import lombok.RequiredArgsConstructor;

/**
 * Ensure that a related publication is linked to the dataPackage of a instrument.
 * 
 * @author René Reitmann
 */
@RequiredArgsConstructor
public class ValidDataPackageIdsForInstrumentIdsValidator implements 
    ConstraintValidator<ValidDataPackageIdsForInstrumentIds, RelatedPublication> {

  private final InstrumentRepository instrumentRepository;
  
  private String messageKey;
  
  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidDataPackageIdsForInstrumentIds constraintAnnotation) {
    messageKey = constraintAnnotation.message();
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(RelatedPublication relatedPublication, 
      ConstraintValidatorContext context) {
    if (relatedPublication.getInstrumentIds() == null 
        || relatedPublication.getInstrumentIds().isEmpty()) {
      return true;
    }
    if ( relatedPublication.getDataPackageIds() == null
        || relatedPublication.getDataPackageIds().isEmpty()) {
      reportCustomViolation(relatedPublication, context, relatedPublication.getInstrumentIds());
      return false;
    }
    
    List<String> invalidIds = relatedPublication.getInstrumentIds().stream()
        .filter(instrumentId -> {
          Instrument instrument = instrumentRepository.findById(instrumentId).orElse(null);
          if (instrument == null 
              || relatedPublication.getDataPackageIds().contains(instrument.getDataPackageId())) {
            return false;
          }
          return true;
        }).collect(Collectors.toList());
    
    if ( !invalidIds.isEmpty() ) {
      reportCustomViolation(relatedPublication, context, invalidIds);
    }
    
    return invalidIds.isEmpty();
  }
  
  private void reportCustomViolation(RelatedPublication relatedPublication, 
      ConstraintValidatorContext context, List<String> invalidIds) {
    context.disableDefaultConstraintViolation();
    invalidIds.forEach(invalidId -> {      
      int instrumentIdIndex = relatedPublication.getInstrumentIds().indexOf(invalidId);
      context.buildConstraintViolationWithTemplate(messageKey)
        .addPropertyNode("instrumentIds[" + instrumentIdIndex + "]")
        .addConstraintViolation();
    });
  }
}
