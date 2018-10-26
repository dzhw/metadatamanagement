.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints Size

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation ValidFilterExpressionLanguage

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

FilterDetails
=============

.. java:package:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder public class FilterDetails

   FilterDetails of variable.

   :author: Daniel Katzberg

Fields
------
description
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString description
   :outertype: FilterDetails

expression
^^^^^^^^^^

.. java:field:: @NotEmpty @Size private String expression
   :outertype: FilterDetails

expressionLanguage
^^^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty @ValidFilterExpressionLanguage private String expressionLanguage
   :outertype: FilterDetails

