.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

.. java:import:: org.javers.core.metamodel.annotation Entity

.. java:import:: org.springframework.data.annotation Id

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidIsoLanguage

.. java:import:: eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation ValidStudyAttachmentType

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

StudyAttachmentMetadata
=======================

.. java:package:: eu.dzhw.fdz.metadatamanagement.studymanagement.domain
   :noindex:

.. java:type:: @Entity @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class StudyAttachmentMetadata extends AbstractRdcDomainObject

   Metadata which will be stored in GridFS with each attachment for studies.

Fields
------
dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private String dataAcquisitionProjectId
   :outertype: StudyAttachmentMetadata

description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringNotEmpty private I18nString description
   :outertype: StudyAttachmentMetadata

fileName
^^^^^^^^

.. java:field:: @NotEmpty @Pattern private String fileName
   :outertype: StudyAttachmentMetadata

id
^^

.. java:field:: @Id private String id
   :outertype: StudyAttachmentMetadata

indexInStudy
^^^^^^^^^^^^

.. java:field:: @NotNull private Integer indexInStudy
   :outertype: StudyAttachmentMetadata

language
^^^^^^^^

.. java:field:: @NotNull @ValidIsoLanguage private String language
   :outertype: StudyAttachmentMetadata

studyId
^^^^^^^

.. java:field:: @NotEmpty private String studyId
   :outertype: StudyAttachmentMetadata

title
^^^^^

.. java:field:: @Size private String title
   :outertype: StudyAttachmentMetadata

type
^^^^

.. java:field:: @NotNull @I18nStringSize @ValidStudyAttachmentType private I18nString type
   :outertype: StudyAttachmentMetadata

