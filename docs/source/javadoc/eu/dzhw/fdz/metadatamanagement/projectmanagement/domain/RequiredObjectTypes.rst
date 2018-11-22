.. java:import:: lombok AccessLevel

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok Setter

.. java:import:: org.javers.core.metamodel.annotation ValueObject

.. java:import:: javax.validation.constraints AssertTrue

RequiredObjectTypes
===================

.. java:package:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain
   :noindex:

.. java:type:: @Data @NoArgsConstructor @AllArgsConstructor @ValueObject @Builder public class RequiredObjectTypes

   This configuration defines which object types have to be delivered before a project can be released.

Fields
------
isDataSetsRequired
^^^^^^^^^^^^^^^^^^

.. java:field:: private boolean isDataSetsRequired
   :outertype: RequiredObjectTypes

   Defines if data set data is required for a release.

isInstrumentsRequired
^^^^^^^^^^^^^^^^^^^^^

.. java:field:: private boolean isInstrumentsRequired
   :outertype: RequiredObjectTypes

   Defines if instrument data is required for a release.

isQuestionsRequired
^^^^^^^^^^^^^^^^^^^

.. java:field:: private boolean isQuestionsRequired
   :outertype: RequiredObjectTypes

   Defines if question data is required for a release.

isStudyRequired
^^^^^^^^^^^^^^^

.. java:field:: @AssertTrue @Setter private boolean isStudyRequired
   :outertype: RequiredObjectTypes

   Defines if study data is required for a release (this object type is mandatory and this setting is therefore always \ ``true``\ .

isSurveysRequired
^^^^^^^^^^^^^^^^^

.. java:field:: private boolean isSurveysRequired
   :outertype: RequiredObjectTypes

   Defines if survey data is required for a release.

isVariablesRequired
^^^^^^^^^^^^^^^^^^^

.. java:field:: private boolean isVariablesRequired
   :outertype: RequiredObjectTypes

   Defines if variable data is required for a release.

