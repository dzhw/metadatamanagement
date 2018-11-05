package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.javers.core.metamodel.annotation.Entity;
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
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation.UniqueDatasetNumberInProject;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation.UniqueSubDatasetAccessWayInDataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation.ValidDataSetIdName;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation.ValidDataSetType;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation.ValidFormat;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.AccessWays;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import io.searchbox.annotations.JestId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A dataset contains {@link Variable}s. It results from at least one {@link Survey}.
 */
@Entity
@Document(collection = "data_sets")
@ValidDataSetIdName(message = "data-set-management.error.data-set.id.valid-data-set-id-name")
@UniqueDatasetNumberInProject(
    message = "data-set-management.error.data-set.unique-data-set-number-in-project")
@CompoundIndex(def = "{number: 1, dataAcquisitionProjectId: 1}", unique = true)
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class DataSet extends AbstractRdcDomainObject {

  /**
   * The id of the dataset which uniquely identifies the dataset in this application.
   * 
   * The id must not be empty and must be of the form
   * dat-{{dataAcquisitionProjectId}}-ds{{number}}$. The id must not contain more than 512
   * characters.
   */
  @Id
  @JestId
  @NotEmpty(message = "data-set-management.error.data-set.id.not-empty")
  @Size(max = StringLengths.MEDIUM, message = "data-set-management.error.data-set.id.size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOLLAR,
      message = "data-set-management.error.data-set.id.pattern")
  private String id;

  /**
   * The id of the {@link DataAcquisitionProject} to which this dataset belongs.
   * 
   * The dataAcquisitionProjectId must not be empty.
   */
  @Indexed
  @NotEmpty(message = "data-set-management.error.data-set.data-acquisition-project.id.not-empty")
  private String dataAcquisitionProjectId;

  /**
   * A short description of the dataset.
   * 
   * It must be specified in at least one language and it must not contain more than 2048
   * characters.
   */
  @NotNull(message = "data-set-management.error.data-set.description.not-null")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "data-set-management.error.data-set.description.i18n-string-size")
  @I18nStringNotEmpty(
      message = "data-set-management.error.data-set.description.i18n-string-not-empty")
  private I18nString description;

  /**
   * The number of the dataset.
   * 
   * Must not be empty and must be unique within the {@link DataAcquisitionProject}.
   */
  @NotNull(message = "data-set-management.error.data-set.number.not-null")
  private Integer number;

  /**
   * The format of the dataset.
   * 
   * Must be one of {@link Format}.
   */
  @ValidFormat(message = "data-set-management.error.data-set.format.valid-format")
  private I18nString format;

  /**
   * The type of the dataset.
   * 
   * Must be one of {@link DataSetTypes} and must not be empty.
   */
  @NotNull(message = "data-set-management.error.data-set.type.not-null")
  @ValidDataSetType(message = "data-set-management.error.data-set.type.valid-type")
  private I18nString type;

  /**
   * Arbitrary additional text for the dataset.
   * 
   * Must not contain more than 2048 characters.
   */
  @I18nStringSize(max = StringLengths.LARGE,
      message = "data-set-management.error.variable.annotations.i18n-string-size")
  private I18nString annotations;

  /**
   * List of numbers of {@link Survey}s of this {@link DataAcquisitionProject}. The dataset contains
   * results from these {@link Survey}s.
   * 
   * Must contain at least one element.
   */
  @NotEmpty(message = "data-set-management.error.data-set.survey-numbers.not-empty")
  private List<Integer> surveyNumbers;

  /**
   * The id of the {@link Study} to which this dataset belongs.
   * 
   * Must not be empty.
   */
  @Indexed
  @NotEmpty(message = "data-set-management.error.data-set.study.id.not-empty")
  private String studyId;

  /**
   * List of ids of {@link Survey}s of this {@link DataAcquisitionProject}. The dataset contains
   * results from these {@link Survey}s.
   * 
   * Must contain at least one element.
   */
  @Indexed
  @NotEmpty(message = "data-set-management.error.data-set.survey.ids.not-empty")
  private List<String> surveyIds;

  /**
   * List of {@link SubDataSet}s (concrete accessible files) within this dataset.
   * 
   * Must contain at least one element. There must not be more than one {@link SubDataSet} per
   * {@link AccessWays}.
   */
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
