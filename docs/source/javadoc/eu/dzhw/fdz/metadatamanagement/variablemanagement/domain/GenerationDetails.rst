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

.. java:type:: @NotEmptyGenerationDetailsDescriptionOrRule @RuleExpressionLanguageAndRuleFilledOrEmpty @NoArgsConstructor @Data @AllArgsConstructor @Builder public class GenerationDetails

   Generation Details.

   :author: Daniel Katzberg

Fields
------
description
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString description
   :outertype: GenerationDetails

rule
^^^^

.. java:field:: @Size private String rule
   :outertype: GenerationDetails

ruleExpressionLanguage
^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @ValidRuleExpressionLanguage private String ruleExpressionLanguage
   :outertype: GenerationDetails

