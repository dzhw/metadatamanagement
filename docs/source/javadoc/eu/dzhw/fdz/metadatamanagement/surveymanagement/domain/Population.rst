.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringEntireNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

.. java:import:: org.javers.core.metamodel.annotation ValueObject

.. java:import:: javax.validation Valid

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: java.util List

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

.. java:field:: @NotNull @I18nStringEntireNotEmpty @I18nStringSize private I18nString description
   :outertype: Population

   A description of the population. It must be specified in all languages and it must not contain more than 2048 characters.

geographicCoverages
^^^^^^^^^^^^^^^^^^^

.. java:field:: @Valid @NotEmpty private List<GeographicCoverage> geographicCoverages
   :outertype: Population

   A list of geographic coverages. Must contain at least one entry.

