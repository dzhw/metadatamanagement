.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringEntireNotEmpty

.. java:import:: lombok Data

Study
=====

.. java:package:: eu.dzhw.fdz.metadatamanagement.ordermanagement.domain
   :noindex:

.. java:type:: @Data public class Study

   Study which is part of a \ :java:ref:`Product`\ .

   :author: René Reitmann

Fields
------
annotations
^^^^^^^^^^^

.. java:field:: private I18nString annotations
   :outertype: Study

id
^^

.. java:field:: @NotEmpty private String id
   :outertype: Study

title
^^^^^

.. java:field:: @NotNull @I18nStringEntireNotEmpty private I18nString title
   :outertype: Study

