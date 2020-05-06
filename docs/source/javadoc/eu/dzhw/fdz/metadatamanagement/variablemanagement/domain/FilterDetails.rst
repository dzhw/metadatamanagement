.. java:import:: java.io Serializable

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints Size

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.questionmanagement.domain Question

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation ValidFilterExpressionLanguage

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

FilterDetails
=============

.. java:package:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder public class FilterDetails implements Serializable

   Filter details of a \ :java:ref:`Variable`\  describe the condition which must have evaluated to true before a participant was asked a \ :java:ref:`Question`\  resulting in this \ :java:ref:`Variable`\ . All participants for which the conditions evaluates to false will have a \ :java:ref:`Missing`\  in this \ :java:ref:`Variable`\ .

Fields
------
description
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString description
   :outertype: FilterDetails

   A description of this filter condition. Markdown is supported. Must not contain more than 2048 characters

expression
^^^^^^^^^^

.. java:field:: @NotEmpty @Size private String expression
   :outertype: FilterDetails

   A technical expression describing the condition which must have evaluated to true. The expression is given in the expressionLanguage. Must not be empty and must not contain more than 2048 characters.

expressionLanguage
^^^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty @ValidFilterExpressionLanguage private String expressionLanguage
   :outertype: FilterDetails

   The name of the language in which the expression was given. Must not be empty and must be one of \ :java:ref:`FilterExpressionLanguages`\ .

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: FilterDetails

