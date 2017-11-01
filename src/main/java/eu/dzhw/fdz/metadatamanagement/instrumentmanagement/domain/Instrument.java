package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidInstrumentIdPattern;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidInstrumentType;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidUniqueInstrumentNumber;
import io.searchbox.annotations.JestId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A Instrument.
 *
 * @author Daniel Katzberg
 *
 */
@Document(collection = "instruments")
@ValidInstrumentIdPattern(message = "instrument-management.error"
    + ".instrument.valid-instrument-id-pattern")
@ValidUniqueInstrumentNumber(message = "instrument-management.error"
    + ".instrument.unique-instrument-number")
@CompoundIndex(def = "{number: 1, dataAcquisitionProjectId: 1}", unique = true)
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Instrument extends AbstractRdcDomainObject {

  @Id
  @JestId
  @NotEmpty(message = "instrument-management.error.instrument.id.not-empty")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOLLAR,
      message = "instrument-management.error.instrument.id.pattern")
  @Size(max = StringLengths.MEDIUM, message = "instrument-management.error.instrument.id.size")
  private String id;

  @Indexed
  @NotEmpty(message =
      "instrument-management.error.instrument.data-acquisition-project-id.not-empty")
  private String dataAcquisitionProjectId;

  @NotNull(message = "instrument-management.error.instrument.title.not-null")
  @I18nStringSize(max = StringLengths.LARGE, message = "instrument-"
      + "management.error.instrument.title.i18n-string-size")
  @I18nStringNotEmpty(message = "instrument-management.error.instrument.title."
      + "i18n-string-not-empty")
  private I18nString title;
  
  @I18nStringSize(max = StringLengths.LARGE, message = "instrument-"
      + "management.error.instrument.subtitle.i18n-string-size")
  private I18nString subtitle;

  @NotNull(message = "instrument-management.error.instrument.description.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM, message = "instrument-"
      + "management.error.instrument.description.i18n-string-size")
  @I18nStringNotEmpty(message = "instrument-management.error.instrument.description."
      + "i18n-string-not-empty")
  private I18nString description;

  @NotNull(message = "instrument-management.error.instrument.number.not-null")
  private Integer number;

  @NotEmpty(message = "instrument-management.error.instrument.type.not-empty")
  @ValidInstrumentType(message = "instrument-management.error.instrument.type.valid")
  private String type;

  @I18nStringSize(max = StringLengths.LARGE,
      message = "instrument-management.error.instrument.annotations.i18n-string-size")
  private I18nString annotations;

  @Indexed
  private List<String> surveyIds;

  @NotEmpty(message = "instrument-management.error.instrument.survey-numbers.not-empty")
  private List<Integer> surveyNumbers;

  @Indexed
  @NotEmpty(message = "instrument-management.error.instrument.study-id.not-empty")
  private String studyId;

  public Instrument(Instrument instrument) {
    super();
    BeanUtils.copyProperties(instrument, this);
  }
}
