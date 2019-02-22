.. java:import:: javax.validation.constraints NotNull

.. java:import:: org.javers.core.metamodel.annotation ValueObject

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

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder @ValueObject public class Population

   Details of the population of a \ :java:ref:`Survey`\ .

Fields
------
description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringNotEmpty @I18nStringSize private I18nString description
   :outertype: Population

   A description of the population. It must be specified in at least one language and it must not contain more than 2048 characters.

title
^^^^^

.. java:field:: @NotNull @I18nStringNotEmpty @I18nStringSize private I18nString title
   :outertype: Population

   A short title for the population. It must be specified in at least one language and it must not contain more than 512 characters.

