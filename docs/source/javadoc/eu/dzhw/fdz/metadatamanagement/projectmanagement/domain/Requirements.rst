.. java:import:: java.io Serializable

.. java:import:: javax.validation.constraints AssertTrue

.. java:import:: org.javers.core.metamodel.annotation ValueObject

.. java:import:: lombok AccessLevel

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok Setter

Requirements
============

.. java:package:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain
   :noindex:

.. java:type:: @Data @NoArgsConstructor @AllArgsConstructor @ValueObject @EqualsAndHashCode @Builder public class Requirements implements Serializable

   This configuration defines which object types have to be delivered before a project can be released.

Fields
------
isDataPackagesRequired
^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @AssertTrue @Setter @Builder.Default private boolean isDataPackagesRequired
   :outertype: Requirements

   Defines if dataPackage data is required for a release (this object type is mandatory and this setting is therefore always \ ``true``\ .

isDataSetsRequired
^^^^^^^^^^^^^^^^^^

.. java:field:: private boolean isDataSetsRequired
   :outertype: Requirements

   Defines if data set data is required for a release.

isInstrumentsRequired
^^^^^^^^^^^^^^^^^^^^^

.. java:field:: private boolean isInstrumentsRequired
   :outertype: Requirements

   Defines if instrument data is required for a release.

isPublicationsRequired
^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: private boolean isPublicationsRequired
   :outertype: Requirements

   Defines if publication data is required for a release.

isQuestionsRequired
^^^^^^^^^^^^^^^^^^^

.. java:field:: private boolean isQuestionsRequired
   :outertype: Requirements

   Defines if question data is required for a release.

isSurveysRequired
^^^^^^^^^^^^^^^^^

.. java:field:: private boolean isSurveysRequired
   :outertype: Requirements

   Defines if survey data is required for a release.

isVariablesRequired
^^^^^^^^^^^^^^^^^^^

.. java:field:: private boolean isVariablesRequired
   :outertype: Requirements

   Defines if variable data is required for a release.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: Requirements

