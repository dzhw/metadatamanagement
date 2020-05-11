.. java:import:: java.io Serializable

.. java:import:: java.util List

.. java:import:: javax.validation Valid

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Size

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation UniqueCode

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation UniqueValue

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

Distribution
============

.. java:package:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Distribution implements Serializable

   A distribution contains the descriptives of a \ :java:ref:`Variable`\  meaning its \ :java:ref:`ValidResponse`\ s, \ :java:ref:`Missing`\ s and \ :java:ref:`Statistics`\ .

Fields
------
maxNumberOfDecimalPlaces
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: private Integer maxNumberOfDecimalPlaces
   :outertype: Distribution

   Integer used for rounding the values of this \ :java:ref:`Variable`\  when displaying it. It is computed during the import of the \ :java:ref:`Variable`\  by finding the maximum number of decimal places in the list of \ :java:ref:`ValidResponse`\ s.

missings
^^^^^^^^

.. java:field:: @UniqueCode @Valid @Size private List<Missing> missings
   :outertype: Distribution

   List of \ :java:ref:`Missing`\ s of this \ :java:ref:`Variable`\ . Must not contain more than 7000 entries and the code of the \ :java:ref:`Missing`\ s must be unique.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: Distribution

statistics
^^^^^^^^^^

.. java:field:: @Valid private Statistics statistics
   :outertype: Distribution

   Descriptive metrics of this \ :java:ref:`Variable`\ .

totalAbsoluteFrequency
^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer totalAbsoluteFrequency
   :outertype: Distribution

   The total absolute number of \ :java:ref:`ValidResponse`\ s and \ :java:ref:`Missing`\ s. Must not be empty.

totalValidAbsoluteFrequency
^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer totalValidAbsoluteFrequency
   :outertype: Distribution

   The total absolute number of \ :java:ref:`ValidResponse`\ s. Must not be empty.

totalValidRelativeFrequency
^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Double totalValidRelativeFrequency
   :outertype: Distribution

   The quotient from totalValidAbsoluteFrequency and totalAbsoluteFrequency. Must not be empty.

validResponses
^^^^^^^^^^^^^^

.. java:field:: @UniqueValue @Valid @Size private List<ValidResponse> validResponses
   :outertype: Distribution

   List of \ :java:ref:`ValidResponse`\ s of this variable. Must not contain more than 7000 entries and the value of the \ :java:ref:`ValidResponse`\ s must be unique.

