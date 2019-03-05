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

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain DataAcquisitionProject

.. java:import:: eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection StudySubDocumentProjection

.. java:import:: eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation ValidDataAvailability

.. java:import:: eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation ValidStudyId

.. java:import:: eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation ValidSurveyDesign

.. java:import:: io.searchbox.annotations JestId

.. java:import:: io.swagger.annotations ApiModel

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

.. java:type:: @Entity @Document @ValidStudyId @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder @ApiModel public class Study extends AbstractRdcDomainObject implements StudySubDocumentProjection

   A study contains all metadata of a \ :java:ref:`DataAcquisitionProject`\ . It will get a DOI (Digital Object Identifier) when the \ :java:ref:`DataAcquisitionProject`\  is released.

Fields
------
annotations
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString annotations
   :outertype: Study

   Arbitrary additional text for this instrument. Must not contain more than 2048 characters.

authors
^^^^^^^

.. java:field:: @Valid @NotEmpty private List<Person> authors
   :outertype: Study

   List of \ :java:ref:`Person`\ s which have performed this study. Must not be empty.

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Indexed @NotEmpty private String dataAcquisitionProjectId
   :outertype: Study

   The id of the \ :java:ref:`DataAcquisitionProject`\  to which this study belongs. The dataAcquisitionProjectId must not be empty.

dataAvailability
^^^^^^^^^^^^^^^^

.. java:field:: @NotNull @ValidDataAvailability private I18nString dataAvailability
   :outertype: Study

   The current state of the data's availability. Must be one of \ :java:ref:`DataAvailabilities`\  and must not be empty.

description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringEntireNotEmpty private I18nString description
   :outertype: Study

   A description of the study. It must be specified in German and English and it must not contain more than 2048 characters.

id
^^

.. java:field:: @Id @JestId @NotEmpty @Size @Pattern private String id
   :outertype: Study

   The id of the study which uniquely identifies the study in this application. The id must not be empty and must be of the form stu-{{dataAcquisitionProjectId}}$. The id must not contain more than 512 characters.

institution
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringEntireNotEmpty private I18nString institution
   :outertype: Study

   The name of the institution which has performed this study. It must be specified in German and English and it must not contain more than 512 characters.

sponsor
^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringEntireNotEmpty private I18nString sponsor
   :outertype: Study

   The name of the sponsor who which has sponsored this study. It must be specified in German and English and it must not contain more than 512 characters.

studySeries
^^^^^^^^^^^

.. java:field:: @I18nStringSize @I18nStringEntireNotEmptyOptional @I18nStringMustNotContainComma private I18nString studySeries
   :outertype: Study

   The name of the series of studies to which this study belongs.. If specified it must be specified in German and English. It must not contain more than 512 characters and must not contain ",".

surveyDesign
^^^^^^^^^^^^

.. java:field:: @NotNull @ValidSurveyDesign private I18nString surveyDesign
   :outertype: Study

   The survey design of this \ :java:ref:`Study`\ . Must be one of \ :java:ref:`SurveyDesigns`\  and must not be empty.

title
^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringEntireNotEmpty private I18nString title
   :outertype: Study

   The title of the study. It must be specified in German and English and it must not contain more than 2048 characters.

