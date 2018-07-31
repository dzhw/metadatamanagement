package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;

/**
 * Ensure that a related publication is linked to the study of a instrument.
 * 
 * @author René Reitmann
 */
public class ValidStudyIdsForInstrumentIdsValidator implements 
    ConstraintValidator<ValidStudyIdsForInstrumentIds, RelatedPublication> {

  @Autowired
  private InstrumentRepository instrumentRepository;
  
  private String messageKey;
  
  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidStudyIdsForInstrumentIds constraintAnnotation) {
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
    if ( relatedPublication.getStudyIds() == null
        || relatedPublication.getStudyIds().isEmpty()) {
      reportCustomViolation(relatedPublication, context, relatedPublication.getInstrumentIds());
      return false;
    }
    
    List<String> invalidIds = relatedPublication.getInstrumentIds().stream()
        .filter(instrumentId -> {
          Instrument instrument = instrumentRepository.findById(instrumentId).orElse(null);
          if (instrument == null 
              || relatedPublication.getStudyIds().contains(instrument.getStudyId())) {
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
