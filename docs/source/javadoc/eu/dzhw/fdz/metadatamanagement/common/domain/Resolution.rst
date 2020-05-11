.. java:import:: java.io Serializable

.. java:import:: javax.validation.constraints NotNull

.. java:import:: org.javers.core.metamodel.annotation ValueObject

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

Resolution
==========

.. java:package:: eu.dzhw.fdz.metadatamanagement.common.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder @ValueObject public class Resolution implements Serializable

   Representation of the resolution of images.

Fields
------
heightY
^^^^^^^

.. java:field:: @NotNull private Integer heightY
   :outertype: Resolution

   The height in pixel.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: Resolution

widthX
^^^^^^

.. java:field:: @NotNull private Integer widthX
   :outertype: Resolution

   The width in pixel.

