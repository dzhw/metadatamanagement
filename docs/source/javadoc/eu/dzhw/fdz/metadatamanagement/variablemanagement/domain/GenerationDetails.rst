.. java:import:: java.io Serializable

.. java:import:: javax.validation.constraints Size

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation NotEmptyGenerationDetailsDescriptionOrRule

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation RuleExpressionLanguageAndRuleFilledOrEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation ValidRuleExpressionLanguage

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

GenerationDetails
=================

.. java:package:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain
   :noindex:

.. java:type:: @NotEmptyGenerationDetailsDescriptionOrRule @RuleExpressionLanguageAndRuleFilledOrEmpty @NoArgsConstructor @Data @AllArgsConstructor @Builder public class GenerationDetails implements Serializable

   Generation details describe how a \ :java:ref:`Variable`\  was generated from one or more input \ :java:ref:`Variable`\ s.

Fields
------
description
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString description
   :outertype: GenerationDetails

   A description of this generation rule. Markdown is supported. Must not contain more than 2048 characters

rule
^^^^

.. java:field:: @Size private String rule
   :outertype: GenerationDetails

   The computation rule in the ruleExpressionLanguage which was used to generate this \ :java:ref:`Variable`\ . Must not contain more than 1 MB characters.

ruleExpressionLanguage
^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @ValidRuleExpressionLanguage private String ruleExpressionLanguage
   :outertype: GenerationDetails

   The language which was used to describe this rule. Must be one of \ :java:ref:`RuleExpressionLanguages`\ .

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: GenerationDetails

