.. java:import:: javax.validation.constraints NotNull

.. java:import:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation ValidUnitValue

.. java:import:: org.javers.core.metamodel.annotation ValueObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringEntireNotEmpty

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

.. java:field:: @NotNull @I18nStringEntireNotEmpty @I18nStringSize private I18nString description
   :outertype: Population

   A description of the population. It must be specified in all languages and it must not contain more than 2048 characters.

unit
^^^^

.. java:field:: @NotNull @ValidUnitValue private I18nString unit
   :outertype: Population

   Unit type. Mandatory field which only allows values specified by VFDB.

   **See also:** \ `GNERD: Survey Unit Educational Research (Version 1.0) <https://mdr.iqb.hu-berlin.de/#/catalog/94d1ae4f-a441-c728-4a03-adb0eb4604af>`_\

