.. java:import:: javax.validation Valid

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

Product
=======

.. java:package:: eu.dzhw.fdz.metadatamanagement.ordermanagement.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Product

   Data Product which can be ordered by a customer.

   :author: René Reitmann

Fields
------
accessWay
^^^^^^^^^

.. java:field:: @NotEmpty private String accessWay
   :outertype: Product

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private String dataAcquisitionProjectId
   :outertype: Product

study
^^^^^

.. java:field:: @NotNull @Valid private Study study
   :outertype: Product

version
^^^^^^^

.. java:field:: @NotEmpty private String version
   :outertype: Product

