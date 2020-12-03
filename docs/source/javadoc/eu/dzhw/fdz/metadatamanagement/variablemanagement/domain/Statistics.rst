.. java:import:: java.io Serializable

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

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Statistics implements Serializable

   Descriptive metrics of this \ :java:ref:`Variable`\ .

Fields
------
deviance
^^^^^^^^

.. java:field:: private Double deviance
   :outertype: Statistics

   See \ `Deviance (Wikipedia) <https://en.wikipedia.org/wiki/Deviance_(statistics)>`_\ .

firstQuartile
^^^^^^^^^^^^^

.. java:field:: @Size private String firstQuartile
   :outertype: Statistics

   Splits off the lowest 25% of the values (\ :java:ref:`ValidResponse`\ s) of this \ :java:ref:`Variable`\  from the highest 75%. Must not contain more than 32 characters.

kurtosis
^^^^^^^^

.. java:field:: private Double kurtosis
   :outertype: Statistics

   See \ `Kurtosis (Wikipedia) <https://en.wikipedia.org/wiki/Kurtosis>`_\ .

maximum
^^^^^^^

.. java:field:: @Size private String maximum
   :outertype: Statistics

   The maximum of the values (\ :java:ref:`ValidResponse`\ s) of this \ :java:ref:`Variable`\ . Must not contain more than 32 characters.

meanDeviation
^^^^^^^^^^^^^

.. java:field:: private Double meanDeviation
   :outertype: Statistics

   See \ `Mean Absolute Deviation (Wikipedia) <https://en.wikipedia.org/wiki/Average_absolute_deviation>`_\ .

meanValue
^^^^^^^^^

.. java:field:: private Double meanValue
   :outertype: Statistics

   The arithmetic mean of the values (\ :java:ref:`ValidResponse`\ s) of this \ :java:ref:`Variable`\ .

median
^^^^^^

.. java:field:: @Size private String median
   :outertype: Statistics

   The median is the value separating the higher half from the lower half of the values (\ :java:ref:`ValidResponse`\ s) of this \ :java:ref:`Variable`\ . Must not contain more than 32 characters.

minimum
^^^^^^^

.. java:field:: @Size private String minimum
   :outertype: Statistics

   The minimum of the values (\ :java:ref:`ValidResponse`\ s) of this \ :java:ref:`Variable`\ . Must not contain more than 32 characters.

mode
^^^^

.. java:field:: private String mode
   :outertype: Statistics

   The mode is the value (\ :java:ref:`ValidResponse`\ ) that appears most often.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: Statistics

skewness
^^^^^^^^

.. java:field:: private Double skewness
   :outertype: Statistics

   See \ `Skewness (Wikipedia) <https://en.wikipedia.org/wiki/Skewness>`_\ .

standardDeviation
^^^^^^^^^^^^^^^^^

.. java:field:: private Double standardDeviation
   :outertype: Statistics

   Measure that is used to quantify the amount of variation of the values (\ :java:ref:`ValidResponse`\ s) of this \ :java:ref:`Variable`\ .

thirdQuartile
^^^^^^^^^^^^^

.. java:field:: @Size private String thirdQuartile
   :outertype: Statistics

   Splits off the highest 25% of the values (\ :java:ref:`ValidResponse`\ s) of this \ :java:ref:`Variable`\  from the lowest 75%. Must not contain more than 32 characters.

