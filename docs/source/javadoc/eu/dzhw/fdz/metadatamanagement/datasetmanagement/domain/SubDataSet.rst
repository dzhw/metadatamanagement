.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Size

.. java:import:: org.javers.core.metamodel.annotation ValueObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation ValidAccessWay

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain AccessWays

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

SubDataSet
==========

.. java:package:: eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder @ValueObject public class SubDataSet

   A subdataset is part of a \ :java:ref:`DataSet`\  and describes the concrete analyzable file which is accessible by a given access way.

Fields
------
accessWay
^^^^^^^^^

.. java:field:: @NotNull @ValidAccessWay private String accessWay
   :outertype: SubDataSet

   The access way of this subdataset. Describes how the user will be able to work with the data set. Must not be empty and be one of \ :java:ref:`AccessWays`\  but not \ :java:ref:`AccessWays.NOT_ACCESSIBLE`\ .

citationHint
^^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString citationHint
   :outertype: SubDataSet

   A hint telling how to cite this subdataset in publications. Must not contain more than 2048 characters.

description
^^^^^^^^^^^

.. java:field:: @I18nStringSize @I18nStringNotEmpty private I18nString description
   :outertype: SubDataSet

   A description for this subdataset. It must be specified in at least one language and it must not contain more than 512 characters.

name
^^^^

.. java:field:: @NotEmpty @Size private String name
   :outertype: SubDataSet

   The filename of the subdataset without extension. Must not be empty and must not contain more than 32 characters.

numberOfObservations
^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer numberOfObservations
   :outertype: SubDataSet

   The number of rows (observations or episodes) which are present in this subdataset. Must not be empty.

