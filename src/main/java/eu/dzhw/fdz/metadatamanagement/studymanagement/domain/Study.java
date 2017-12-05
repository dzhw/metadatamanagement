package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.javers.core.metamodel.annotation.Entity;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringEntireNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation.ValidDataAvailability;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation.ValidStudyId;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation.ValidSurveyDesign;
import io.searchbox.annotations.JestId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The study domain object represents a study. A study can has more than one release. 
 * Every {@link DataAcquisitionProject} has exact one Study.
 * 
 * @author Daniel Katzberg
 *
 */
@Entity
@Document(collection = "studies")
@ValidStudyId(message = "study-management.error.study.id.not-valid-id")
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Study extends AbstractRdcDomainObject {
  
  @Id
  @JestId
  @NotEmpty(message = "study-management.error.study.id.not-empty")
  @Size(max = StringLengths.MEDIUM, message = "study-management.error.study.id.size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOLLAR,
      message = "study-management.error.study.id.pattern")
  private String id;
  
  @Indexed
  @NotEmpty(message = "study-management.error.study.data-acquisition-project.id.not-empty")
  private String dataAcquisitionProjectId;
  
  @NotNull(message = "study-management.error.study.title.not-null")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "study-management.error.study.title.i18n-string-size")
  @I18nStringEntireNotEmpty(
      message = "study-management.error.study.title.i18n-string-entire-not-empty")
  private I18nString title;
  
  @NotNull(message = "study-management.error.study.description.not-null")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "study-management.error.study.description.i18n-string-size")
  @I18nStringNotEmpty(message = "study-management.error.study.description.i18n-string-not-empty")
  private I18nString description;
  
  @NotNull(message = "study-management.error.study.institution.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "study-management.error.study.institution.i18n-string-size")
  @I18nStringNotEmpty(message = "study-management.error.study.institution.i18n-string-not-empty")
  private I18nString institution;
  
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "study-management.error.study.survey-series.i18n-string-size")
  private I18nString surveySeries;
  
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "study-management.error.study.sponsor.i18n-string-size")
  private I18nString sponsor;
  
  @Valid
  @NotEmpty(message = "study-management.error.study.authors.not-empty")
  private List<Person> authors;  
  
  @NotNull(message = "study-management.error.study.data-availability.not-null")
  @ValidDataAvailability(
      message = "study-management.error.study.data-availability.valid-data-availability")
  private I18nString dataAvailability;
  
  @NotNull(message = "study-management.error.study.survey-design.not-null")
  @ValidSurveyDesign(
      message = "study-management.error.study.survey-design.valid-survey-design")
  private I18nString surveyDesign;
  
  @NotEmpty(message = "study-management.error.study.doi.not-empty")
  private String doi;
  
  @I18nStringSize(max = StringLengths.LARGE,
      message = "study-management.error.study.annotations.i18n-string-size")
  private I18nString annotations;
  
  public Study(Study study) {
    super();
    BeanUtils.copyProperties(study, this);
  }
}
