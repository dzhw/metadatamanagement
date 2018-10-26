.. java:import:: javax.validation.constraints Size

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

Statistics
==========

.. java:package:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Statistics

   This statics class has statistical information about a variable. The information of this class will be represented for a boxplot on the frontend.

   :author: Daniel Katzberg

Fields
------
deviance
^^^^^^^^

.. java:field:: private Double deviance
   :outertype: Statistics

firstQuartile
^^^^^^^^^^^^^

.. java:field:: @Size private String firstQuartile
   :outertype: Statistics

highWhisker
^^^^^^^^^^^

.. java:field:: private Double highWhisker
   :outertype: Statistics

kurtosis
^^^^^^^^

.. java:field:: private Double kurtosis
   :outertype: Statistics

lowWhisker
^^^^^^^^^^

.. java:field:: private Double lowWhisker
   :outertype: Statistics

maximum
^^^^^^^

.. java:field:: @Size private String maximum
   :outertype: Statistics

meanDeviation
^^^^^^^^^^^^^

.. java:field:: private Double meanDeviation
   :outertype: Statistics

meanValue
^^^^^^^^^

.. java:field:: private Double meanValue
   :outertype: Statistics

median
^^^^^^

.. java:field:: @Size private String median
   :outertype: Statistics

minimum
^^^^^^^

.. java:field:: @Size private String minimum
   :outertype: Statistics

mode
^^^^

.. java:field:: private String mode
   :outertype: Statistics

skewness
^^^^^^^^

.. java:field:: private Double skewness
   :outertype: Statistics

standardDeviation
^^^^^^^^^^^^^^^^^

.. java:field:: private Double standardDeviation
   :outertype: Statistics

thirdQuartile
^^^^^^^^^^^^^

.. java:field:: @Size private String thirdQuartile
   :outertype: Statistics

