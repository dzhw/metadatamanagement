package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringEntireNotEmpty;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.validation.ValidSurveyDataType;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Partial {@link eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study} which is part of a
 * {@link Product}. It is a copy of the
 * {@link eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study} attributes which is made when
 * the customer places the orders.
 */
@Data
@ApiModel(
    description = "Go <a href='https://metadatamanagement.readthedocs.io/de/stable/javadoc/eu/dzhw/"
    + "fdz/metadatamanagement/ordermanagement/domain/OrderedStudy.html'>here</a> "
    + "for further details.")
public class OrderedStudy implements Serializable {

  private static final long serialVersionUID = 5959657930630887807L;

  /**
   * The id of the {@link eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study}.
   *
   * Must not be empty.
   */
  @NotEmpty
  private String id;

  /**
   * The title of the {@link eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study}.
   *
   * Must not be empty neither in German nor in English.
   */
  @NotNull
  @I18nStringEntireNotEmpty
  private I18nString title;

  /**
   * The annotations of the {@link eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study}.
   */
  private I18nString annotations;

  /**
   * One of {@link eu.dzhw.fdz.metadatamanagement.studymanagement.domain.SurveyDataTypes}. Must not
   * be {@code null}.
   */
  @NotNull
  @ValidSurveyDataType
  private I18nString surveyDataType;
  
  /**
   * The name of the series of studies to which this study belongs. May be null.
   */
  private I18nString studySeries;
}
