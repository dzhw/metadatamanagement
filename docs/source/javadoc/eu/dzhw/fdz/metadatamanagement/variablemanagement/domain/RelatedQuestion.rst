.. java:import:: java.io Serializable

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints Size

.. java:import:: org.springframework.data.mongodb.core.index Indexed

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain Instrument

.. java:import:: eu.dzhw.fdz.metadatamanagement.questionmanagement.domain Question

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

RelatedQuestion
===============

.. java:package:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder public class RelatedQuestion implements Serializable

   A related question is a \ :java:ref:`Question`\  which has been asked to generate the values of a \ :java:ref:`Variable`\ . It contains the ids of the \ :java:ref:`Instrument`\  and the \ :java:ref:`Question`\  as well as all Strings of the \ :java:ref:`Question`\  which are related to this \ :java:ref:`Variable`\ .

Fields
------
instrumentId
^^^^^^^^^^^^

.. java:field:: @Indexed private String instrumentId
   :outertype: RelatedQuestion

   The id of the \ :java:ref:`Instrument`\  of this \ :java:ref:`Question`\ . Must not be empty.

instrumentNumber
^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private String instrumentNumber
   :outertype: RelatedQuestion

   The number of the \ :java:ref:`Instrument`\  of this \ :java:ref:`Question`\ . Must not be empty.

questionId
^^^^^^^^^^

.. java:field:: @Indexed private String questionId
   :outertype: RelatedQuestion

   The id of the corresponding \ :java:ref:`Question`\ . Must not be empty.

questionNumber
^^^^^^^^^^^^^^

.. java:field:: @Size @NotEmpty private String questionNumber
   :outertype: RelatedQuestion

   The number of the corresponding \ :java:ref:`Question`\ . Must not be empty.

relatedQuestionStrings
^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString relatedQuestionStrings
   :outertype: RelatedQuestion

   All Strings (concatenated) of this \ :java:ref:`Question`\  which "belong" to this \ :java:ref:`Variable`\ . These Strings typically overlap with String from other \ :java:ref:`Variable`\ s of the same \ :java:ref:`Question`\ .

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: RelatedQuestion

