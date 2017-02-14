package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation.ValidFormat;
import io.searchbox.annotations.JestId;

/**
 * Subset of data set attributes which can be used in other search documents
 * as sub document.
 * @author René Reitmann
 */
public class DataSetSubDocument extends AbstractRdcDomainObject {

  @Id
  @JestId
  @NotEmpty(message = "data-set-management.error.data-set.id.not-empty")
  @Size(max = StringLengths.MEDIUM, message = "data-set-management.error.data-set.id.size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_EXCLAMATIONMARK,
      message = "data-set-management.error.data-set.id.pattern")
  protected String id;
  @I18nStringSize(max = StringLengths.LARGE,
      message = "data-set-management.error.data-set.description.i18n-string-size")
  protected I18nString description;
  @NotNull(message = "data-set-management.error.data-set.number.not-null")
  protected Integer number;
  @ValidFormat(message = "data-set-management.error.data-set.format.valid-format")
  protected I18nString format;

  public DataSetSubDocument() {
    super();
  }
  
  public DataSetSubDocument(DataSet dataSet) {
    super();
    BeanUtils.copyProperties(dataSet, this);
  }

  @Override
  public String getId() {
    return this.id;
  }

  public I18nString getDescription() {
    return description;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public I18nString getFormat() {
    return format;
  }

  public void setFormat(I18nString format) {
    this.format = format;
  }

}
