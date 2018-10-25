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

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Distribution

   Domain object of the ValueSummary. Represent sums for all values of the variable.

   :author: Daniel Katzberg

Fields
------
maxNumberOfDecimalPlaces
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: private Integer maxNumberOfDecimalPlaces
   :outertype: Distribution

missings
^^^^^^^^

.. java:field:: @UniqueCode @Valid @Size private List<Missing> missings
   :outertype: Distribution

statistics
^^^^^^^^^^

.. java:field:: @Valid private Statistics statistics
   :outertype: Distribution

totalAbsoluteFrequency
^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer totalAbsoluteFrequency
   :outertype: Distribution

totalValidAbsoluteFrequency
^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer totalValidAbsoluteFrequency
   :outertype: Distribution

totalValidRelativeFrequency
^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Double totalValidRelativeFrequency
   :outertype: Distribution

validResponses
^^^^^^^^^^^^^^

.. java:field:: @UniqueValue @Valid @Size private List<ValidResponse> validResponses
   :outertype: Distribution

