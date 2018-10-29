.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

.. java:import:: org.springframework.data.annotation Id

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidIsoLanguage

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

SurveyAttachmentMetadata
========================

.. java:package:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain
   :noindex:

.. java:type:: @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class SurveyAttachmentMetadata extends AbstractRdcDomainObject

   Metadata which will be stored in GridFS with each attachment for surveys.

Fields
------
dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private String dataAcquisitionProjectId
   :outertype: SurveyAttachmentMetadata

description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringNotEmpty private I18nString description
   :outertype: SurveyAttachmentMetadata

fileName
^^^^^^^^

.. java:field:: @NotEmpty @Pattern private String fileName
   :outertype: SurveyAttachmentMetadata

id
^^

.. java:field:: @Id private String id
   :outertype: SurveyAttachmentMetadata

indexInSurvey
^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer indexInSurvey
   :outertype: SurveyAttachmentMetadata

language
^^^^^^^^

.. java:field:: @NotNull @ValidIsoLanguage private String language
   :outertype: SurveyAttachmentMetadata

surveyId
^^^^^^^^

.. java:field:: @NotEmpty private String surveyId
   :outertype: SurveyAttachmentMetadata

surveyNumber
^^^^^^^^^^^^

.. java:field:: @NotNull private Integer surveyNumber
   :outertype: SurveyAttachmentMetadata

title
^^^^^

.. java:field:: @NotNull @Size private String title
   :outertype: SurveyAttachmentMetadata

