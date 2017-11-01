package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain;

import java.util.List;

import javax.validation.Valid;
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
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation.UniqueDatasetNumberInProject;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation.UniqueSubDatasetAccessWayInDataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation.ValidDataSetIdName;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation.ValidDataSetType;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation.ValidFormat;
import io.searchbox.annotations.JestId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Data Set.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(collection = "data_sets")
@ValidDataSetIdName(message = "data-set-management.error.data-set.id.valid-data-set-id-name")
@UniqueDatasetNumberInProject(
    message = "data-set-management.error.data-set.unique-data-set-number-in-project")
@CompoundIndex(def = "{number: 1, dataAcquisitionProjectId: 1}", unique = true)
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class DataSet extends AbstractRdcDomainObject {
  
  @Id
  @JestId
  @NotEmpty(message = "data-set-management.error.data-set.id.not-empty")
  @Size(max = StringLengths.MEDIUM, message = "data-set-management.error.data-set.id.size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOLLAR,
      message = "data-set-management.error.data-set.id.pattern")
  private String id;
  
  @Indexed
  @NotEmpty(message = "data-set-management.error.data-set.data-acquisition-project.id.not-empty")
  private String dataAcquisitionProjectId;
  
  @I18nStringSize(max = StringLengths.LARGE,
      message = "data-set-management.error.data-set.description.i18n-string-size")
  private I18nString description;
  
  @NotNull(message = "data-set-management.error.data-set.number.not-null")
  private Integer number;
  
  @ValidFormat(message = "data-set-management.error.data-set.format.valid-format")
  private I18nString format;
  
  @NotNull(message = "data-set-management.error.data-set.type.not-null")
  @ValidDataSetType(message = "data-set-management.error.data-set.type.valid-type")
  private I18nString type;
  
  @I18nStringSize(max = StringLengths.LARGE,
      message = "data-set-management.error.variable.annotations.i18n-string-size")
  private I18nString annotations;
  
  @NotEmpty(message = "data-set-management.error.data-set.survey-numbers.not-empty")
  private List<Integer> surveyNumbers;
  
  /* Foreign Keys */
  @Indexed
  @NotEmpty(message = "data-set-management.error.data-set.study.id.not-empty")
  private String studyId;

  @Indexed
  @NotEmpty(message = "data-set-management.error.data-set.survey.ids.not-empty")
  private List<String> surveyIds;
  
  /* Nested Objects */
  @Valid
  @NotEmpty(message = "data-set-management.error.data-set.sub-data-sets.not-empty")
  @UniqueSubDatasetAccessWayInDataSet(message = "data-set-management.error.data-set."
          + "sub-data-sets.access-way-unique-within-data-set")
  private List<SubDataSet> subDataSets;

  public DataSet(DataSet dataSet) {
    super();
    BeanUtils.copyProperties(dataSet, this);
  }
}
