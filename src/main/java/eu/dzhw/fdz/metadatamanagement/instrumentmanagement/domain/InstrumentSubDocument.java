package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain;

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
import io.searchbox.annotations.JestId;

/**
 * Subset of instrument attributes which can be used in other search documents
 * as sub document.
 * @author René Reitmann
 */
public class InstrumentSubDocument extends AbstractRdcDomainObject {

  @Id
  @JestId
  @NotEmpty(message = "instrument-management.error.instrument.id.not-empty")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_EXCLAMATIONMARK,
      message = "instrument-management.error.instrument.id.pattern")
  @Size(max = StringLengths.MEDIUM, message = "instrument-management.error.instrument.id.size")
  protected String id;
  @NotNull(message = "instrument-management.error.instrument.title.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM, min = 1,
      message = "instrument-management.error.instrument.title.i18n-string-size")
  protected I18nString title;
  @NotNull(message = "instrument-management.error.instrument.description.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM, min = 1,
      message = "instrument-management.error.instrument.description.i18n-string-size")
  protected I18nString description;
  @NotNull(message = "instrument-management.error.instrument.number.not-null")
  protected Integer number;

  public InstrumentSubDocument() {
    super();
  }
  
  public InstrumentSubDocument(Instrument instrument) {
    super();
    BeanUtils.copyProperties(instrument, this);
  }

  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public I18nString getTitle() {
    return title;
  }

  public void setTitle(I18nString title) {
    this.title = title;
  }

  public I18nString getDescription() {
    return description;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

}
