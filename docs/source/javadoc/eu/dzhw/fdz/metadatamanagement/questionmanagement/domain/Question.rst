.. java:import:: java.util List

.. java:import:: javax.validation Valid

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

.. java:import:: org.springframework.beans BeanUtils

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.index CompoundIndex

.. java:import:: org.springframework.data.mongodb.core.index Indexed

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain Instrument

.. java:import:: eu.dzhw.fdz.metadatamanagement.ordermanagement.domain Study

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain DataAcquisitionProject

.. java:import:: eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation ValidQuestionIdName

.. java:import:: eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation ValidQuestionType

.. java:import:: eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation ValidUniqueQuestionNumber

.. java:import:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain Survey

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain Variable

.. java:import:: io.searchbox.annotations JestId

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

Question
========

.. java:package:: eu.dzhw.fdz.metadatamanagement.questionmanagement.domain
   :noindex:

.. java:type:: @Document @CompoundIndex @ValidUniqueQuestionNumber @ValidQuestionIdName @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Question extends AbstractRdcDomainObject

   A question is part of an \ :java:ref:`Instrument`\  which has been used in at least one \ :java:ref:`Survey`\ s. The responses to a question are stored in \ :java:ref:`Variable`\ s.

Fields
------
additionalQuestionText
^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString additionalQuestionText
   :outertype: Question

   Arbitrary additional question text which has been presented to the participant. Must not contain more than 1 MB characters.

annotations
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString annotations
   :outertype: Question

   Arbitrary annotations to this question. Must not contain more than 2048 characters.

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Indexed @NotEmpty private String dataAcquisitionProjectId
   :outertype: Question

   The id of the \ :java:ref:`DataAcquisitionProject`\  to which this question belongs. The dataAcquisitionProjectId must not be empty.

id
^^

.. java:field:: @Id @JestId @NotEmpty @Pattern @Size private String id
   :outertype: Question

   The id of the question which uniquely identifies the question in this application. The id must not be empty and must be of the form que-{{dataAcquisitionProjectId}}-ins{{instrumentNumber}}-{{number}}$. The id must not contain more than 512 characters.

indexInInstrument
^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer indexInInstrument
   :outertype: Question

   The index of the question in the \ :java:ref:`Instrument`\ . Used for sorting the questions.

instruction
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString instruction
   :outertype: Question

   The instruction for the participant which tells how to give the answers to this question. Must not contain more than 1 MB characters.

instrumentId
^^^^^^^^^^^^

.. java:field:: @NotEmpty @Indexed private String instrumentId
   :outertype: Question

   The id of the \ :java:ref:`Instrument`\  to which this question belongs. Must not be empty.

instrumentNumber
^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer instrumentNumber
   :outertype: Question

   The number of the \ :java:ref:`Instrument`\  to which this question belongs. Must not be empty.

introduction
^^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString introduction
   :outertype: Question

   The introduction of this question which gives more context to the participant before asking the question. Must not contain more than 2048 characters.

number
^^^^^^

.. java:field:: @NotEmpty @Size @Pattern private String number
   :outertype: Question

   The number of the question. Must not be empty and must be unique within the \ :java:ref:`Instrument`\ . Must contain only (german) alphanumeric characters and "_","-" and "." and must not contain more than 32 characters.

questionText
^^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringNotEmpty @I18nStringSize private I18nString questionText
   :outertype: Question

   The question the \ :java:ref:`Survey`\ s participant was asked. It must be specified in at least one language and it must not contain more than 2048 characters.

studyId
^^^^^^^

.. java:field:: @Indexed @NotEmpty private String studyId
   :outertype: Question

   The id of the \ :java:ref:`Study`\  to which this question belongs. Must not be empty.

successorNumbers
^^^^^^^^^^^^^^^^

.. java:field:: private List<String> successorNumbers
   :outertype: Question

   List of numbers of the \ :java:ref:`Question`\ s which directly follow this question in the \ :java:ref:`Instrument`\ .

successors
^^^^^^^^^^

.. java:field:: @Indexed private List<String> successors
   :outertype: Question

   List of ids of the \ :java:ref:`Question`\ s which directly follow this question in the \ :java:ref:`Instrument`\ .

technicalRepresentation
^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Valid private TechnicalRepresentation technicalRepresentation
   :outertype: Question

   A \ :java:ref:`TechnicalRepresentation`\  of this question. This is optional and can be used to add the source code of the question which was used to generate it.

topic
^^^^^

.. java:field:: @I18nStringSize private I18nString topic
   :outertype: Question

   The topic or section in the \ :java:ref:`Instrument`\  to which this question belongs. It must not contain more than 2048 characters.

type
^^^^

.. java:field:: @NotNull @ValidQuestionType private I18nString type
   :outertype: Question

   The type of the question. Must be one of QuestionTypes and must not be empty.

