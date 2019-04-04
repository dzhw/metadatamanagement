package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidShadowId;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidInstrumentIdPattern;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidInstrumentType;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidUniqueInstrumentNumber;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderedStudy;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import io.searchbox.annotations.JestId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.javers.core.metamodel.annotation.Entity;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * An instrument (e.g. a questionnaire) which was used in at least one {@link Survey}.
 */
@Entity
@Document(collection = "instruments")
@ValidInstrumentIdPattern(message = "instrument-management.error"
    + ".instrument.valid-instrument-id-pattern")
@ValidUniqueInstrumentNumber(message = "instrument-management.error"
    + ".instrument.unique-instrument-number")
@CompoundIndex(def = "{number: 1, dataAcquisitionProjectId: 1}", unique = true)
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ValidShadowId(message = "instrument-management.error.instrument.derived-id.pattern")
public class Instrument extends AbstractShadowableRdcDomainObject {

  /**
   * The id of the instrument which uniquely identifies the instrument in this application.
   */
  @Id
  @JestId
  @NotEmpty(message = "instrument-management.error.instrument.id.not-empty")
  @Setter(AccessLevel.NONE)
  private String id;

  /**
   * The instrument's master id. It must not be empty, must be of the form
   * {@code ins-{{dataAcquisitionProjectId}}-ins{{number}}$} and must not contain more than
   * 512 characters.
   */
  @NotEmpty(message = "instrument-management.error.instrument.master-id.not-empty")
  @Size(max = StringLengths.MEDIUM, message = "instrument-management.error.instrument"
      + ".master-id.size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOLLAR,
      message = "instrument-management.error.instrument.master-id.pattern")
  @Setter(AccessLevel.NONE)
  private String masterId;


  /**
   * The id of the {@link DataAcquisitionProject} to which this instrument belongs.
   * 
   * The dataAcquisitionProjectId must not be empty.
   */
  @Indexed
  @NotEmpty(message =
      "instrument-management.error.instrument.data-acquisition-project-id.not-empty")
  private String dataAcquisitionProjectId;

  /**
   * The title of the instrument.
   * 
   * It must be specified in at least one language and it must not contain more than 2048
   * characters.
   */
  @NotNull(message = "instrument-management.error.instrument.title.not-null")
  @I18nStringSize(max = StringLengths.LARGE, message = "instrument-"
      + "management.error.instrument.title.i18n-string-size")
  @I18nStringNotEmpty(message = "instrument-management.error.instrument.title."
      + "i18n-string-not-empty")
  private I18nString title;

  /**
   * An optional subtitle of the instrument.
   * 
   * It must not contain more than 2048 characters.
   */
  @I18nStringSize(max = StringLengths.LARGE, message = "instrument-"
      + "management.error.instrument.subtitle.i18n-string-size")
  private I18nString subtitle;

  /**
   * A short description of the instrument.
   * 
   * It must be specified in at least one language and it must not contain more than 512 characters.
   */
  @NotNull(message = "instrument-management.error.instrument.description.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM, message = "instrument-"
      + "management.error.instrument.description.i18n-string-size")
  @I18nStringNotEmpty(message = "instrument-management.error.instrument.description."
      + "i18n-string-not-empty")
  private I18nString description;

  /**
   * The number of the instrument.
   * 
   * Must not be empty and must be unique within the {@link DataAcquisitionProject}.
   */
  @NotNull(message = "instrument-management.error.instrument.number.not-null")
  private Integer number;

  /**
   * The type of this instrument.
   * 
   * Must be one of {@link InstrumentTypes} and must not be empty.
   */
  @NotEmpty(message = "instrument-management.error.instrument.type.not-empty")
  @ValidInstrumentType(message = "instrument-management.error.instrument.type.valid")
  private String type;

  /**
   * Arbitrary additional text for this instrument.
   * 
   * Must not contain more than 2048 characters.
   */
  @I18nStringSize(max = StringLengths.LARGE,
      message = "instrument-management.error.instrument.annotations.i18n-string-size")
  private I18nString annotations;

  /**
   * List of ids of {@link Survey}s of this {@link DataAcquisitionProject}. The instrument has been
   * used in these {@link Survey}s.
   * 
   * Must contain at least one element.
   */
  @Indexed
  @NotEmpty(message = "instrument-management.error.instrument.survey-ids.not-empty")
  private List<String> surveyIds;

  /**
   * List of numbers of {@link Survey}s of this {@link DataAcquisitionProject}. The instrument has
   * been used in these {@link Survey}s.
   * 
   * Must contain at least one element.
   */
  @NotEmpty(message = "instrument-management.error.instrument.survey-numbers.not-empty")
  private List<Integer> surveyNumbers;

  /**
   * The id of the {@link OrderedStudy} to which this instrument belongs.
   * 
   * Must not be empty.
   */
  @Indexed
  @NotEmpty(message = "instrument-management.error.instrument.study-id.not-empty")
  private String studyId;

  public Instrument(Instrument instrument) {
    super();
    BeanUtils.copyProperties(instrument, this);
  }

  @Override
  protected void setMasterIdInternal(String masterId) {
    this.masterId = masterId;
  }

  @Override
  protected void setIdInternal(String id) {
    this.id = id;
  }
}
