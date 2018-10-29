.. java:import:: javax.validation.constraints NotNull

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

Population
==========

.. java:package:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Population

   The representation of the population for the survey.

   :author: Daniel Katzberg

Fields
------
description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringNotEmpty @I18nStringSize private I18nString description
   :outertype: Population

title
^^^^^

.. java:field:: @NotNull @I18nStringNotEmpty @I18nStringSize private I18nString title
   :outertype: Population

