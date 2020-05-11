.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: org.springframework.data.annotation Id

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractShadowableRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidIsoLanguage

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain DataAcquisitionProject

.. java:import:: lombok AccessLevel

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok Setter

.. java:import:: lombok ToString

SurveyResponseRateImageMetadata
===============================

.. java:package:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain
   :noindex:

.. java:type:: @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class SurveyResponseRateImageMetadata extends AbstractShadowableRdcDomainObject

   Metadata which will be stored with each response rate image of a \ :java:ref:`Survey`\ .

Fields
------
dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private String dataAcquisitionProjectId
   :outertype: SurveyResponseRateImageMetadata

   The id of the \ :java:ref:`DataAcquisitionProject`\  to which the \ :java:ref:`Survey`\  of this response rate image belongs. Must not be empty.

fileName
^^^^^^^^

.. java:field:: @NotEmpty @Pattern private String fileName
   :outertype: SurveyResponseRateImageMetadata

   The filename of the image. Must not be empty and must contain only (german) alphanumeric characters and "_" and "-" and ".".

id
^^

.. java:field:: @Id @Setter private String id
   :outertype: SurveyResponseRateImageMetadata

   The id of the response rate image. Holds the complete path which can be used to download the file.

language
^^^^^^^^

.. java:field:: @NotNull @ValidIsoLanguage private String language
   :outertype: SurveyResponseRateImageMetadata

   The language used in the response rate image. Must be either "de" or "en".

masterId
^^^^^^^^

.. java:field:: @Setter private String masterId
   :outertype: SurveyResponseRateImageMetadata

   The master id of the survey response rate image metadata.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: SurveyResponseRateImageMetadata

surveyId
^^^^^^^^

.. java:field:: @NotEmpty private String surveyId
   :outertype: SurveyResponseRateImageMetadata

   The id of the \ :java:ref:`Survey`\  to which this response rate image belongs. Must not be empty.

surveyNumber
^^^^^^^^^^^^

.. java:field:: @NotNull private Integer surveyNumber
   :outertype: SurveyResponseRateImageMetadata

   The number of the \ :java:ref:`Survey`\  to which this response rate image belongs. Must not be empty.

