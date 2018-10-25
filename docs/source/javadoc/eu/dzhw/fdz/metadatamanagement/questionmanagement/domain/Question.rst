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

.. java:import:: eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation ValidQuestionIdName

.. java:import:: eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation ValidQuestionType

.. java:import:: eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation ValidUniqueQuestionNumber

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

   Question.

   :author: Daniel Katzberg

Fields
------
additionalQuestionText
^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString additionalQuestionText
   :outertype: Question

annotations
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString annotations
   :outertype: Question

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Indexed @NotEmpty private String dataAcquisitionProjectId
   :outertype: Question

id
^^

.. java:field:: @Id @JestId @NotEmpty @Pattern @Size private String id
   :outertype: Question

indexInInstrument
^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer indexInInstrument
   :outertype: Question

instruction
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString instruction
   :outertype: Question

instrumentId
^^^^^^^^^^^^

.. java:field:: @NotEmpty @Indexed private String instrumentId
   :outertype: Question

instrumentNumber
^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer instrumentNumber
   :outertype: Question

introduction
^^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString introduction
   :outertype: Question

number
^^^^^^

.. java:field:: @NotEmpty @Size private String number
   :outertype: Question

questionText
^^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringNotEmpty @I18nStringSize private I18nString questionText
   :outertype: Question

studyId
^^^^^^^

.. java:field:: @Indexed @NotEmpty private String studyId
   :outertype: Question

successorNumbers
^^^^^^^^^^^^^^^^

.. java:field:: private List<String> successorNumbers
   :outertype: Question

successors
^^^^^^^^^^

.. java:field:: @Indexed private List<String> successors
   :outertype: Question

technicalRepresentation
^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Valid private TechnicalRepresentation technicalRepresentation
   :outertype: Question

topic
^^^^^

.. java:field:: @I18nStringSize private I18nString topic
   :outertype: Question

type
^^^^

.. java:field:: @NotNull @ValidQuestionType private I18nString type
   :outertype: Question

