.. java:import:: lombok AccessLevel

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok Setter

.. java:import:: org.javers.core.metamodel.annotation ValueObject

.. java:import:: javax.validation.constraints AssertTrue

Requirements
============

.. java:package:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain
   :noindex:

.. java:type:: @Data @NoArgsConstructor @AllArgsConstructor @ValueObject @EqualsAndHashCode @Builder public class Requirements

   This configuration defines which object types have to be delivered before a project can be released.

Fields
------
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

isQuestionsRequired
^^^^^^^^^^^^^^^^^^^

.. java:field:: private boolean isQuestionsRequired
   :outertype: Requirements

   Defines if question data is required for a release.

isStudiesRequired
^^^^^^^^^^^^^^^^^

.. java:field:: @AssertTrue @Setter private boolean isStudiesRequired
   :outertype: Requirements

   Defines if study data is required for a release (this object type is mandatory and this setting is therefore always \ ``true``\ .

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

