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

   Partial \ :java:ref:`eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study`\  which is part of a \ :java:ref:`Product`\ . It is a copy of the \ :java:ref:`eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study`\  attributes which is made when the \ :java:ref:`Customer`\  places the orders.

Fields
------
annotations
^^^^^^^^^^^

.. java:field:: private I18nString annotations
   :outertype: Study

   The annotations of the \ :java:ref:`eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study`\ .

id
^^

.. java:field:: @NotEmpty private String id
   :outertype: Study

   The id of the \ :java:ref:`eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study`\ . Must not be empty.

title
^^^^^

.. java:field:: @NotNull @I18nStringEntireNotEmpty private I18nString title
   :outertype: Study

   The title of the \ :java:ref:`eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study`\ . Must not be empty neither in German nor in English.

