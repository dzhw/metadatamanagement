.. java:import:: org.javers.core.metamodel.annotation ValueObject

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

ProjectState
============

.. java:package:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain
   :noindex:

.. java:type:: @ValueObject @Data @AllArgsConstructor @NoArgsConstructor @Builder public class ProjectState

   State of a data acquisition project. Used for all metadata

   :author: tgehrke

Fields
------
isDataProviderReady
^^^^^^^^^^^^^^^^^^^

.. java:field:: @Builder.Default private boolean isDataProviderReady
   :outertype: ProjectState

   indicates if the data providers marked it's metadata as ready.

isPublisherReady
^^^^^^^^^^^^^^^^

.. java:field:: @Builder.Default private boolean isPublisherReady
   :outertype: ProjectState

   indicates if the publisher marked the metadata as ready.

