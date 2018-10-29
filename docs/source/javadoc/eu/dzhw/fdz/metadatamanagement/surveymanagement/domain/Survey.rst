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

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain Period

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringEntireNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation ValidDataType

.. java:import:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation ValidSurveyIdName

.. java:import:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation ValidUniqueSurveyNumber

.. java:import:: io.searchbox.annotations JestId

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

Survey
======

.. java:package:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain
   :noindex:

.. java:type:: @Entity @Document @ValidSurveyIdName @ValidUniqueSurveyNumber @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Survey extends AbstractRdcDomainObject

   A Survey.

   :author: Daniel Katzberg

Fields
------
annotations
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString annotations
   :outertype: Survey

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Indexed @NotEmpty private String dataAcquisitionProjectId
   :outertype: Survey

dataType
^^^^^^^^

.. java:field:: @NotNull @ValidDataType private I18nString dataType
   :outertype: Survey

fieldPeriod
^^^^^^^^^^^

.. java:field:: @NotNull @Valid private Period fieldPeriod
   :outertype: Survey

grossSampleSize
^^^^^^^^^^^^^^^

.. java:field:: @Min private Integer grossSampleSize
   :outertype: Survey

id
^^

.. java:field:: @Id @JestId @NotEmpty @Size @Pattern private String id
   :outertype: Survey

number
^^^^^^

.. java:field:: @NotNull private Integer number
   :outertype: Survey

population
^^^^^^^^^^

.. java:field:: @NotNull private Population population
   :outertype: Survey

responseRate
^^^^^^^^^^^^

.. java:field:: @Min @Max private Double responseRate
   :outertype: Survey

sample
^^^^^^

.. java:field:: @NotNull @I18nStringNotEmpty @I18nStringSize private I18nString sample
   :outertype: Survey

sampleSize
^^^^^^^^^^

.. java:field:: @NotNull @Min private Integer sampleSize
   :outertype: Survey

studyId
^^^^^^^

.. java:field:: @Indexed @NotEmpty private String studyId
   :outertype: Survey

surveyMethod
^^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringEntireNotEmpty @I18nStringSize private I18nString surveyMethod
   :outertype: Survey

title
^^^^^

.. java:field:: @I18nStringSize @I18nStringEntireNotEmpty private I18nString title
   :outertype: Survey

wave
^^^^

.. java:field:: @NotNull @Min private Integer wave
   :outertype: Survey

