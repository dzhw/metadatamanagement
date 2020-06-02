.. java:import:: java.io Serializable

.. java:import:: java.util List

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringEntireNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain DataTypes

.. java:import:: io.swagger.v3.oas.annotations.media Schema

.. java:import:: lombok Data

OrderedStudy
============

.. java:package:: eu.dzhw.fdz.metadatamanagement.ordermanagement.domain
   :noindex:

.. java:type:: @Data @Schema public class OrderedStudy implements Serializable

   Partial \ :java:ref:`eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study`\  which is part of a \ :java:ref:`Product`\ . It is a copy of the \ :java:ref:`eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study`\  attributes which is made when the customer places the orders.

Fields
------
annotations
^^^^^^^^^^^

.. java:field:: private I18nString annotations
   :outertype: OrderedStudy

   The annotations of the \ :java:ref:`eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study`\ .

id
^^

.. java:field:: @NotEmpty private String id
   :outertype: OrderedStudy

   The id of the \ :java:ref:`eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study`\ . Must not be empty.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: OrderedStudy

studySeries
^^^^^^^^^^^

.. java:field:: private I18nString studySeries
   :outertype: OrderedStudy

   The name of the series of studies to which this study belongs. May be null.

surveyDataTypes
^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private List<I18nString> surveyDataTypes
   :outertype: OrderedStudy

   List of \ :java:ref:`DataTypes`\ . Must not be empty.

title
^^^^^

.. java:field:: @NotNull @I18nStringEntireNotEmpty private I18nString title
   :outertype: OrderedStudy

   The title of the \ :java:ref:`eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study`\ . Must not be empty neither in German nor in English.

