.. java:import:: javax.validation Valid

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain Resolution

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidIsoLanguage

.. java:import:: eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation ValidQuestionImageType

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

QuestionImageMetadata
=====================

.. java:package:: eu.dzhw.fdz.metadatamanagement.questionmanagement.domain
   :noindex:

.. java:type:: @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class QuestionImageMetadata extends AbstractRdcDomainObject

   The metadata for question images. An question image display the question in one language with one given resolution, how the user saw the question on his device.

   :author: Daniel Katzberg

Fields
------
containsAnnotations
^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Boolean containsAnnotations
   :outertype: QuestionImageMetadata

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private String dataAcquisitionProjectId
   :outertype: QuestionImageMetadata

fileName
^^^^^^^^

.. java:field:: @NotEmpty @Pattern private String fileName
   :outertype: QuestionImageMetadata

imageType
^^^^^^^^^

.. java:field:: @NotNull @ValidQuestionImageType private ImageType imageType
   :outertype: QuestionImageMetadata

indexInQuestion
^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer indexInQuestion
   :outertype: QuestionImageMetadata

language
^^^^^^^^

.. java:field:: @NotEmpty @Size @ValidIsoLanguage private String language
   :outertype: QuestionImageMetadata

questionId
^^^^^^^^^^

.. java:field:: @NotEmpty private String questionId
   :outertype: QuestionImageMetadata

resolution
^^^^^^^^^^

.. java:field:: @Valid private Resolution resolution
   :outertype: QuestionImageMetadata

