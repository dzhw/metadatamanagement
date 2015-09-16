package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableDocumentRepository;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details.VariableResourceAssembler;

/**
 * A custom spring validator for the variable document. It uses the default JSR-303 validator and
 * additionally checks if the scale level is valid depending on the current datatype.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public abstract class VariableDocumentValidator implements Validator {

  public static final String MANDATORY_SCALE_LEVEL_MESSAGE_CODE =
      "MandatoryScaleLevelOnNumericDataType.variabledocument.scaleLevel";

  public static final String MANDATORY_VARIABLE_SURVEY_VARIABLEALIAS_MESSAGE_CODE =
      "UniqueVariableAlias.variabledocument.variablesurvey.variablealias";

  private SmartValidator jsrValidator;

  private DataTypesProvider dataTypesProvider;

  protected VariableDocumentRepository variableRepository;

  protected VariableResourceAssembler variableResourceAssembler;



  /**
   * The variable document validator is abstract validator which defines the default implementation
   * of the edit and create validators.
   * 
   * @param jsrValidator A given JSR 303 Validator
   * @param dataTypesProvider The Datatype provider holds language depended fields of the valid data
   *        types of the variable management.
   */
  public VariableDocumentValidator(Validator jsrValidator, DataTypesProvider dataTypesProvider,
      VariableDocumentRepository variableRepository,
      VariableResourceAssembler variableResourceAssembler) {
    if (jsrValidator instanceof SmartValidator) {
      this.jsrValidator = (SmartValidator) jsrValidator;
    } else {
      throw new IllegalStateException(
          "Cast not successful at validators... (should be a smart validator.)");
    }
    this.dataTypesProvider = dataTypesProvider;
    this.variableRepository = variableRepository;
    this.variableResourceAssembler = variableResourceAssembler;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.validation.Validator#supports(java.lang.Class)
   */
  @Override
  public boolean supports(Class<?> clazz) {
    return clazz.equals(VariableDocument.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.validation.Validator#validate(java.lang.Object,
   * org.springframework.validation.Errors)
   */
  @Override
  public void validate(Object target, Errors errors) {
    // first execute JSR-303 validation
    this.jsrValidator.validate(target, errors, this.getValidationHints());

    VariableDocument variableDocument = (VariableDocument) target;

    // check if the scale level is correct
    this.validateMandatoryScaleLevelOnNumericDataType(variableDocument, errors);


    // check if variable alias is unique
    this.validateUniqueVariableAlias(variableDocument, errors);
  }

  /**
   * This method is a validation on the scale level field from the {@link VariableDocument}. If the
   * data type is numeric, the scale level is a not blank field.
   * 
   * @param variableDocument the given variable document for validation.
   * @param errors An errors object for collecting errors by the validation.
   */
  private void validateMandatoryScaleLevelOnNumericDataType(VariableDocument variableDocument,
      Errors errors) {
    // check for null
    if (variableDocument.getDataType() == null) {
      return;
    }

    // register an error if the datatype is numeric and scale level is not set
    if (variableDocument.getDataType().equals(this.dataTypesProvider.getNumericValueByLocale())
        && variableDocument.getScaleLevel() == null) {
      errors.rejectValue(VariableDocument.SCALE_LEVEL_FIELD.getPath(),
          MANDATORY_SCALE_LEVEL_MESSAGE_CODE);
      return;
    }
  }

  /**
   * This method is abstract for compensation of validation groups. Every implementation of this
   * methods is equivalent to depending to a validation group, because a validator depending on a
   * given web controller, which uses a validation class.
   * 
   * @param variableDocument A representation of a variable.
   * @param errors An list object, with countains validation errors.
   */
  protected abstract void validateUniqueVariableAlias(VariableDocument variableDocument,
      Errors errors);

  protected void rejectDuplicateAliasIfNecessary(VariableDocument variableDocument, Errors errors) {
    List<VariableDocument> variablesWithSameAlias = this.variableRepository
        .filterBySurveyIdAndVariableAlias(variableDocument.getVariableSurvey().getSurveyId(),
            variableDocument.getVariableSurvey().getVariableAlias())
        .getContent();
    // no elements found
    if (variablesWithSameAlias == null || variablesWithSameAlias.isEmpty()) {
      return;
      // found elements -> alias is used and not okay
    } else {
      VariableDocument existingVariableWithSameAlias = variablesWithSameAlias.get(0);
      String detailPageUrlExistingVariable = this.variableResourceAssembler
          .toResource(existingVariableWithSameAlias).getId().getHref();
      errors.rejectValue(
          VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD.getLeafSubFieldPath(),
          MANDATORY_VARIABLE_SURVEY_VARIABLEALIAS_MESSAGE_CODE,
          new Object[] {existingVariableWithSameAlias.getId(), detailPageUrlExistingVariable},
          "Invalid variable alias!");
      return;
    }
  }

  /**
   * Create the validtion hints used by the JSR validator.
   * 
   * @return An array of {@link VariableValidationGroups} classes
   */
  protected abstract Object[] getValidationHints();
}
