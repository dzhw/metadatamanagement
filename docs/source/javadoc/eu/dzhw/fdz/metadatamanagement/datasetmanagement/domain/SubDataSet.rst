.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Size

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation ValidAccessWay

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

SubDataSet
==========

.. java:package:: eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder public class SubDataSet

   Domain object of the SubDataSet.

Fields
------
accessWay
^^^^^^^^^

.. java:field:: @NotNull @ValidAccessWay private String accessWay
   :outertype: SubDataSet

citationHint
^^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString citationHint
   :outertype: SubDataSet

description
^^^^^^^^^^^

.. java:field:: @I18nStringSize @I18nStringNotEmpty private I18nString description
   :outertype: SubDataSet

name
^^^^

.. java:field:: @NotEmpty @Size private String name
   :outertype: SubDataSet

numberOfObservations
^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer numberOfObservations
   :outertype: SubDataSet

