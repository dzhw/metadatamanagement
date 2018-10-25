.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: org.springframework.data.annotation Id

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidIsoLanguage

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

SurveyResponseRateImageMetadata
===============================

.. java:package:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain
   :noindex:

.. java:type:: @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class SurveyResponseRateImageMetadata extends AbstractRdcDomainObject

   Metadata which will be stored in GridFS with each response rate image for surveys.

Fields
------
dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private String dataAcquisitionProjectId
   :outertype: SurveyResponseRateImageMetadata

fileName
^^^^^^^^

.. java:field:: @NotEmpty @Pattern private String fileName
   :outertype: SurveyResponseRateImageMetadata

id
^^

.. java:field:: @Id private String id
   :outertype: SurveyResponseRateImageMetadata

language
^^^^^^^^

.. java:field:: @NotNull @ValidIsoLanguage private String language
   :outertype: SurveyResponseRateImageMetadata

surveyId
^^^^^^^^

.. java:field:: @NotEmpty private String surveyId
   :outertype: SurveyResponseRateImageMetadata

surveyNumber
^^^^^^^^^^^^

.. java:field:: @NotNull private Integer surveyNumber
   :outertype: SurveyResponseRateImageMetadata

