.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractShadowableRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidIsoLanguage

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain DataAcquisitionProject

.. java:import:: eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation ValidStudyAttachmentType

.. java:import:: lombok AccessLevel

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok Setter

.. java:import:: lombok ToString

.. java:import:: org.javers.core.metamodel.annotation Entity

.. java:import:: org.springframework.data.annotation Id

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

StudyAttachmentMetadata
=======================

.. java:package:: eu.dzhw.fdz.metadatamanagement.studymanagement.domain
   :noindex:

.. java:type:: @Entity @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class StudyAttachmentMetadata extends AbstractShadowableRdcDomainObject

   Metadata which will be stored with each attachment of a \ :java:ref:`Study`\ .

Fields
------
dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private String dataAcquisitionProjectId
   :outertype: StudyAttachmentMetadata

   The id of the \ :java:ref:`DataAcquisitionProject`\  to which the \ :java:ref:`Study`\  of this attachment belongs. Must not be empty.

description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringNotEmpty private I18nString description
   :outertype: StudyAttachmentMetadata

   A description for this attachment. It must be specified in at least one language and it must not contain more than 512 characters.

fileName
^^^^^^^^

.. java:field:: @NotEmpty @Pattern private String fileName
   :outertype: StudyAttachmentMetadata

   The filename of the attachment. Must not be empty and must contain only (german) alphanumeric characters and "_" and "-" and ".".

id
^^

.. java:field:: @Id @Setter private String id
   :outertype: StudyAttachmentMetadata

   The id of the attachment. Holds the complete path which can be used to download the file.

indexInStudy
^^^^^^^^^^^^

.. java:field:: @NotNull private Integer indexInStudy
   :outertype: StudyAttachmentMetadata

   The index in the \ :java:ref:`Study`\  of this attachment. Used for sorting the attachments of this \ :java:ref:`Study`\ . Must not be empty.

language
^^^^^^^^

.. java:field:: @NotNull @ValidIsoLanguage private String language
   :outertype: StudyAttachmentMetadata

   The language of the attachments content. Must not be empty and must be specified as ISO 639 language code.

studyId
^^^^^^^

.. java:field:: @NotEmpty private String studyId
   :outertype: StudyAttachmentMetadata

   The id of the \ :java:ref:`Study`\  to which this attachment belongs. Must not be empty.

title
^^^^^

.. java:field:: @Size private String title
   :outertype: StudyAttachmentMetadata

   An optional title of this attachment in the attachments' language. It must not contain more than 2048 characters.

type
^^^^

.. java:field:: @NotNull @I18nStringSize @ValidStudyAttachmentType private I18nString type
   :outertype: StudyAttachmentMetadata

   The type of the attachment. Must be one of \ :java:ref:`StudyAttachmentTypes`\  and must not be empty.

