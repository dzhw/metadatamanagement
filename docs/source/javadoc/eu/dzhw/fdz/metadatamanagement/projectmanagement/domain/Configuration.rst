.. java:import:: java.io Serializable

.. java:import:: java.util ArrayList

.. java:import:: java.util List

.. java:import:: javax.validation Valid

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: org.javers.core.metamodel.annotation ValueObject

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

Configuration
=============

.. java:package:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain
   :noindex:

.. java:type:: @Data @NoArgsConstructor @ValueObject @AllArgsConstructor @Builder public class Configuration implements Serializable

   The project configuration describes which users are publishers or data providers for a project.

Fields
------
dataProviders
^^^^^^^^^^^^^

.. java:field:: @Builder.Default private List<String> dataProviders
   :outertype: Configuration

   User names having the role of a data provider for a project. Must contain at least one user name.

dataSetsState
^^^^^^^^^^^^^

.. java:field:: private ProjectState dataSetsState
   :outertype: Configuration

   The state of data sets.

instrumentsState
^^^^^^^^^^^^^^^^

.. java:field:: private ProjectState instrumentsState
   :outertype: Configuration

   The state of instruments.

publishers
^^^^^^^^^^

.. java:field:: @NotEmpty @Builder.Default private List<String> publishers
   :outertype: Configuration

   User names having the role of a publisher for a project. Must contain at least one user name.

questionsState
^^^^^^^^^^^^^^

.. java:field:: private ProjectState questionsState
   :outertype: Configuration

   The state of questions.

requirements
^^^^^^^^^^^^

.. java:field:: @Valid @NotNull @Builder.Default private Requirements requirements
   :outertype: Configuration

   Defines which object types are required before a project can be released.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: Configuration

studiesState
^^^^^^^^^^^^

.. java:field:: private ProjectState studiesState
   :outertype: Configuration

   The state of the study.

surveysState
^^^^^^^^^^^^

.. java:field:: private ProjectState surveysState
   :outertype: Configuration

   The State of surveys.

variablesState
^^^^^^^^^^^^^^

.. java:field:: private ProjectState variablesState
   :outertype: Configuration

   The state of variables.

