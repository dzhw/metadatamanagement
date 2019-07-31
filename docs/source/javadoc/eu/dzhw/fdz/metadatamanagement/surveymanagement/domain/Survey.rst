.. java:import:: javax.validation Valid

.. java:import:: javax.validation.constraints Max

.. java:import:: javax.validation.constraints Min

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

.. java:import:: org.javers.core.metamodel.annotation Entity

.. java:import:: org.springframework.beans BeanUtils

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.index Indexed

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractShadowableRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain Period

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringEntireNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidShadowId

.. java:import:: eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain DataSet

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain DataAcquisitionProject

.. java:import:: eu.dzhw.fdz.metadatamanagement.studymanagement.domain Study

.. java:import:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation ValidDataType

.. java:import:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation ValidSampleType

.. java:import:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation ValidSurveyIdName

.. java:import:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation ValidUniqueSurveyNumber

.. java:import:: io.searchbox.annotations JestId

.. java:import:: lombok AccessLevel

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok Setter

.. java:import:: lombok ToString

Survey
======

.. java:package:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain
   :noindex:

.. java:type:: @Entity @Document @ValidSurveyIdName @ValidUniqueSurveyNumber @ValidShadowId @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Survey extends AbstractShadowableRdcDomainObject

   A survey is conducted to examine a population on the basis of a sample. The resulting \ :java:ref:`DataSet`\ s can be used to make statements about the population.

Fields
------
annotations
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString annotations
   :outertype: Survey

   Arbitrary additional text for this survey. Must not contain more than 2048 characters.

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Indexed @NotEmpty private String dataAcquisitionProjectId
   :outertype: Survey

   The id of the \ :java:ref:`DataAcquisitionProject`\  to which this survey belongs. The dataAcquisitionProjectId must not be empty.

dataType
^^^^^^^^

.. java:field:: @NotNull @ValidDataType private I18nString dataType
   :outertype: Survey

   The type of data which the survey produced. Must be one of \ :java:ref:`DataTypes`\  and must not be empty.

fieldPeriod
^^^^^^^^^^^

.. java:field:: @NotNull @Valid private Period fieldPeriod
   :outertype: Survey

   The period during which the survey has been conducted or is expected to be conducted. Must not be empty.

grossSampleSize
^^^^^^^^^^^^^^^

.. java:field:: @Min private Integer grossSampleSize
   :outertype: Survey

   The gross sample size represents the number of participants which have been invited to take part in the \ :java:ref:`Survey`\ . Must not be negative.

id
^^

.. java:field:: @Id @JestId @Setter private String id
   :outertype: Survey

   The id of the survey which uniquely identifies the survey in this application.

masterId
^^^^^^^^

.. java:field:: @NotEmpty @Size @Pattern @Setter @Indexed private String masterId
   :outertype: Survey

   The master id of the survey. It must not be empty, must be of the form \ ``sur-{{dataAcquisitionProjectId}}-sy{{number}}$``\  and must not contain more than 512 characters.

number
^^^^^^

.. java:field:: @NotNull private Integer number
   :outertype: Survey

   The number of the instrument. Must not be empty and must be unique within the \ :java:ref:`DataAcquisitionProject`\ .

population
^^^^^^^^^^

.. java:field:: @Valid @NotNull private Population population
   :outertype: Survey

   Details about the \ :java:ref:`Population`\ . Must not be empty.

responseRate
^^^^^^^^^^^^

.. java:field:: @Min @Max private Double responseRate
   :outertype: Survey

   The response rate is the quotient of the gross sample size and the sample size. Must be between 0 and 100.

sample
^^^^^^

.. java:field:: @NotNull @ValidSampleType private I18nString sample
   :outertype: Survey

   The sampling method is the procedure for selecting sample members from a population. It must match the controlled vocabulary specified by VFDB.

   **See also:** \ `Catalog: GNERD: Sampling Procedure Educational Research (Version 1.0) <https://mdr.iqb.hu-berlin.de/#/catalog/1d791cc7-6d8d-dd35-b1ef-0eec9c31bbb5">`_\

sampleSize
^^^^^^^^^^

.. java:field:: @NotNull @Min private Integer sampleSize
   :outertype: Survey

   The sample size is the number of participant which took part in the survey. Must not be empty and must not be negative.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: Survey

studyId
^^^^^^^

.. java:field:: @Indexed @NotEmpty private String studyId
   :outertype: Survey

   The id of the \ :java:ref:`Study`\  to which this survey belongs. Must not be empty.

surveyMethod
^^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringEntireNotEmpty @I18nStringSize private I18nString surveyMethod
   :outertype: Survey

   The survey method briefly describes how the data were collected. It must be specified in German and English and it must not contain more than 2048 characters.

title
^^^^^

.. java:field:: @I18nStringSize @I18nStringEntireNotEmpty private I18nString title
   :outertype: Survey

   The title of the instrument. It must be specified in German and English and it must not contain more than 2048 characters.

wave
^^^^

.. java:field:: @NotNull @Min private Integer wave
   :outertype: Survey

   Number of the wave which this \ :java:ref:`Survey`\  represents. Will be ignored if the \ :java:ref:`Study`\  is not organized in waves. Must not be empty and must be greater than or equal to 1.

