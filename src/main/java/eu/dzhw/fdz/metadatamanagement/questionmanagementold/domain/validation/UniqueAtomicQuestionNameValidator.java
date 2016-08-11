package eu.dzhw.fdz.metadatamanagement.questionmanagementold.domain.validation;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.questionmanagementold.domain.AtomicQuestion;
import eu.dzhw.fdz.metadatamanagement.questionmanagementold.repository.AtomicQuestionRepository;

/**
 * Validates the name of a atomicquestion. The name have to be unique within a questionnaire.
 * 
 * @author dkatzberg
 *
 */
public class UniqueAtomicQuestionNameValidator
    implements ConstraintValidator<UniqueAtomicQuestionName, AtomicQuestion> {

  @Inject
  private AtomicQuestionRepository atomicQuestionRepository;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(UniqueAtomicQuestionName constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(AtomicQuestion atomicQuestion,
      ConstraintValidatorContext context) {

    // The name has to be unique within a questionnaire
    int numberFoundResults = this.atomicQuestionRepository
        .findByNameAndQuestionnaireId(atomicQuestion.getName(), atomicQuestion.getQuestionnaireId())
        .size();

    return numberFoundResults <= 1;
  }

}
