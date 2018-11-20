.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

.. java:import:: org.javers.core.metamodel.annotation ValueObject

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: java.util ArrayList

.. java:import:: java.util List

Configuration
=============

.. java:package:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain
   :noindex:

.. java:type:: @Data @NoArgsConstructor @ValueObject @AllArgsConstructor @Builder public class Configuration

   The project configuration describes which users are publishers or data providers for a project.

Fields
------
dataProviders
^^^^^^^^^^^^^

.. java:field:: private List<String> dataProviders
   :outertype: Configuration

   User names having the role of a data provider for a project. Must contain at least one user name.

publishers
^^^^^^^^^^

.. java:field:: @NotEmpty private List<String> publishers
   :outertype: Configuration

   User names having the role of a publisher for a project. Must contain at least one user name.

