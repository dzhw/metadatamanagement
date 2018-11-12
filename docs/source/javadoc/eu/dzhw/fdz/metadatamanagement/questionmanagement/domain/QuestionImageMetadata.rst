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

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain DataAcquisitionProject

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

   The metadata for one question images. One question image displays the question in one language with one given resolution.

Fields
------
containsAnnotations
^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Boolean containsAnnotations
   :outertype: QuestionImageMetadata

   Flag indicating whether the image contains annotations which highlight parts that were only visible to specific participants. These annotations were not visible to the participants.

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private String dataAcquisitionProjectId
   :outertype: QuestionImageMetadata

   The id of the \ :java:ref:`DataAcquisitionProject`\  of the \ :java:ref:`Question`\  to which this image belongs. Must not be empty.

fileName
^^^^^^^^

.. java:field:: @NotEmpty @Pattern private String fileName
   :outertype: QuestionImageMetadata

   The name of the images file. Must not be empty and must only contain (german) alphanumeric characters and "_","-" and ".".

imageType
^^^^^^^^^

.. java:field:: @NotNull @ValidQuestionImageType private ImageType imageType
   :outertype: QuestionImageMetadata

   The type of this image. Must be one of \ :java:ref:`ImageType`\  and must not be empty.

indexInQuestion
^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer indexInQuestion
   :outertype: QuestionImageMetadata

   The index in the \ :java:ref:`Question`\  of this image. Used for sorting the images of this \ :java:ref:`Question`\ . Must not be empty.

language
^^^^^^^^

.. java:field:: @NotEmpty @Size @ValidIsoLanguage private String language
   :outertype: QuestionImageMetadata

   The language of the question text on this image. Must not be empty and must be a valid ISO 639 code.

questionId
^^^^^^^^^^

.. java:field:: @NotEmpty private String questionId
   :outertype: QuestionImageMetadata

   The id of the \ :java:ref:`Question`\  to which this image belongs. Must not be empty.

resolution
^^^^^^^^^^

.. java:field:: @Valid private Resolution resolution
   :outertype: QuestionImageMetadata

   The resolution of the image.

