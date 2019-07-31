.. java:import:: java.io Serializable

.. java:import:: javax.validation Valid

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain DataFormat

.. java:import:: eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain DataSet

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain DataAcquisitionProject

.. java:import:: io.swagger.annotations ApiModel

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

.. java:import:: java.util Set

Product
=======

.. java:package:: eu.dzhw.fdz.metadatamanagement.ordermanagement.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder @ApiModel public class Product implements Serializable

   Data Product which can be ordered by a customer.

   :author: René Reitmann

Fields
------
accessWay
^^^^^^^^^

.. java:field:: @NotEmpty private String accessWay
   :outertype: Product

   The access way to the \ :java:ref:`DataSet`\ s which the customer wants to have.

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private String dataAcquisitionProjectId
   :outertype: Product

   The id of the \ :java:ref:`DataAcquisitionProject`\  in which this product was generated. Must not be empty.

dataFormats
^^^^^^^^^^^

.. java:field:: @NotEmpty private Set<DataFormat> dataFormats
   :outertype: Product

   The available data formats of the study. It must not be empty.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: Product

study
^^^^^

.. java:field:: @NotNull @Valid private OrderedStudy study
   :outertype: Product

   The (partial) \ :java:ref:`OrderedStudy`\  of this product. Must not be empty.

version
^^^^^^^

.. java:field:: @NotEmpty private String version
   :outertype: Product

   The version of the \ :java:ref:`DataSet`\ s which the customer wants to have.

