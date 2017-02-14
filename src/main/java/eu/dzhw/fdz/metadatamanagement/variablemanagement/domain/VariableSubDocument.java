package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import io.searchbox.annotations.JestId;

/**
 * Subset of variable attributes which can be used in other search documents
 * as sub document.
 * @author René Reitmann
 */
public class VariableSubDocument extends AbstractRdcDomainObject {

  @Id
  @JestId
  @NotEmpty(message = "variable-management.error.variable.id.not-empty")
  @Size(max = StringLengths.MEDIUM,
      message = "variable-management.error.variable.id.size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_EXCLAMATIONMARK,
      message = "variable-management.error.variable.id.pattern")
  protected String id;
  @NotEmpty(message = "variable-management.error.variable.name.not-empty")
  @Size(max = StringLengths.SMALL, message = "variable-management.error.variable.name.size")
  @Pattern(regexp = Patterns.ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN,
      message = "variable-management.error.variable.name.pattern")
  protected String name;
  @NotNull(message = "variable-management.error.variable.label.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "variable-management.error.variable.label.i18n-string-size")
  @I18nStringNotEmpty(message = "variable-management.error.variable.label.i18n-string-not-empty")
  protected I18nString label;
  @I18nStringSize(max = StringLengths.LARGE,
      message = "variable-management.error.variable.annotations.i18n-string-size")
  protected I18nString annotations;

  public VariableSubDocument() {
    super();
  }
  
  public VariableSubDocument(Variable variable) {
    super();
    BeanUtils.copyProperties(variable, this);
  }

  @Override
  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public I18nString getLabel() {
    return label;
  }

  public void setLabel(I18nString label) {
    this.label = label;
  }

  public I18nString getAnnotations() {
    return annotations;
  }

  public void setAnnotations(I18nString annotations) {
    this.annotations = annotations;
  }

  public void setId(String id) {
    this.id = id;
  }
}
