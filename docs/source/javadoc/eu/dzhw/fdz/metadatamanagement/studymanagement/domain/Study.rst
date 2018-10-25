.. java:import:: java.util List

.. java:import:: javax.validation Valid

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

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain Person

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringEntireNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringEntireNotEmptyOptional

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringMustNotContainComma

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain DataAcquisitionProject

.. java:import:: eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection StudySubDocumentProjection

.. java:import:: eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation ValidDataAvailability

.. java:import:: eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation ValidStudyId

.. java:import:: eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation ValidSurveyDesign

.. java:import:: io.searchbox.annotations JestId

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

Study
=====

.. java:package:: eu.dzhw.fdz.metadatamanagement.studymanagement.domain
   :noindex:

.. java:type:: @Entity @Document @ValidStudyId @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Study extends AbstractRdcDomainObject implements StudySubDocumentProjection

   The study domain object represents a study. A study can has more than one release. Every \ :java:ref:`DataAcquisitionProject`\  has exact one Study.

   :author: Daniel Katzberg

Fields
------
annotations
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString annotations
   :outertype: Study

authors
^^^^^^^

.. java:field:: @Valid @NotEmpty private List<Person> authors
   :outertype: Study

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Indexed @NotEmpty private String dataAcquisitionProjectId
   :outertype: Study

dataAvailability
^^^^^^^^^^^^^^^^

.. java:field:: @NotNull @ValidDataAvailability private I18nString dataAvailability
   :outertype: Study

description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringNotEmpty private I18nString description
   :outertype: Study

id
^^

.. java:field:: @Id @JestId @NotEmpty @Size @Pattern private String id
   :outertype: Study

institution
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringEntireNotEmpty private I18nString institution
   :outertype: Study

sponsor
^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringEntireNotEmpty private I18nString sponsor
   :outertype: Study

studySeries
^^^^^^^^^^^

.. java:field:: @I18nStringSize @I18nStringEntireNotEmptyOptional @I18nStringMustNotContainComma private I18nString studySeries
   :outertype: Study

surveyDesign
^^^^^^^^^^^^

.. java:field:: @NotNull @ValidSurveyDesign private I18nString surveyDesign
   :outertype: Study

title
^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringEntireNotEmpty private I18nString title
   :outertype: Study

