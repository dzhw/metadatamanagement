.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints Size

.. java:import:: org.springframework.data.mongodb.core.index Indexed

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

RelatedQuestion
===============

.. java:package:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder public class RelatedQuestion

   The related Question includes the references to the question and instrument. This is a sub element by the variable.

   :author: Daniel Katzberg

Fields
------
instrumentId
^^^^^^^^^^^^

.. java:field:: @Indexed private String instrumentId
   :outertype: RelatedQuestion

instrumentNumber
^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private String instrumentNumber
   :outertype: RelatedQuestion

questionId
^^^^^^^^^^

.. java:field:: @Indexed private String questionId
   :outertype: RelatedQuestion

questionNumber
^^^^^^^^^^^^^^

.. java:field:: @Size @NotEmpty private String questionNumber
   :outertype: RelatedQuestion

relatedQuestionStrings
^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString relatedQuestionStrings
   :outertype: RelatedQuestion

