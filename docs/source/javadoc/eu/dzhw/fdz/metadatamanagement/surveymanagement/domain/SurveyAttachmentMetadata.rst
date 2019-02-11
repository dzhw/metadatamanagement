.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractShadowableRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidIsoLanguage

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain DataAcquisitionProject

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

.. java:import:: org.springframework.data.annotation Id

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

SurveyAttachmentMetadata
========================

.. java:package:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain
   :noindex:

.. java:type:: @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class SurveyAttachmentMetadata extends AbstractShadowableRdcDomainObject

   Metadata which will be stored with each attachment of a \ :java:ref:`Survey`\ .

Fields
------
dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private String dataAcquisitionProjectId
   :outertype: SurveyAttachmentMetadata

   The id of the \ :java:ref:`DataAcquisitionProject`\  to which the \ :java:ref:`Survey`\  of this attachment belongs. Must not be empty.

description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringNotEmpty private I18nString description
   :outertype: SurveyAttachmentMetadata

   A description for this attachment. It must be specified in at least one language and it must not contain more than 512 characters.

fileName
^^^^^^^^

.. java:field:: @NotEmpty @Pattern private String fileName
   :outertype: SurveyAttachmentMetadata

   The filename of the attachment. Must not be empty and must contain only (german) alphanumeric characters and "_" and "-" and ".".

id
^^

.. java:field:: @Id private String id
   :outertype: SurveyAttachmentMetadata

   The id of the attachment. Holds the complete path which can be used to download the file.

indexInSurvey
^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer indexInSurvey
   :outertype: SurveyAttachmentMetadata

   The index in the \ :java:ref:`Survey`\  of this attachment. Used for sorting the attachments of this \ :java:ref:`Survey`\ . Must not be empty.

language
^^^^^^^^

.. java:field:: @NotNull @ValidIsoLanguage private String language
   :outertype: SurveyAttachmentMetadata

   The language of the attachments content. Must not be empty and must be specified as ISO 639 language code.

surveyId
^^^^^^^^

.. java:field:: @NotEmpty private String surveyId
   :outertype: SurveyAttachmentMetadata

   The id of the \ :java:ref:`Survey`\  to which this attachment belongs. Must not be empty.

surveyNumber
^^^^^^^^^^^^

.. java:field:: @NotNull private Integer surveyNumber
   :outertype: SurveyAttachmentMetadata

   The number of the \ :java:ref:`Survey`\  to which this attachment belongs. Must not be empty.

title
^^^^^

.. java:field:: @NotNull @Size private String title
   :outertype: SurveyAttachmentMetadata

   A title of this attachment in the attachments' language. It must not contain more than 2048 characters.

