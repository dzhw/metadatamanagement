.. java:import:: java.util Arrays

.. java:import:: java.util Collections

.. java:import:: java.util HashSet

.. java:import:: java.util Set

.. java:import:: org.springframework.cglib.beans ImmutableBean

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

ScaleLevels
===========

.. java:package:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain
   :noindex:

.. java:type:: public class ScaleLevels

   The scale level (or level of measurement) classifies the nature of information within the values assigned to a \ :java:ref:`Variable`\  (\ :java:ref:`ValidResponse`\ s). It determines which mathematical operations can be performed with the values.

Fields
------
ALL
^^^

.. java:field:: public static final Set<I18nString> ALL
   :outertype: ScaleLevels

INTERVAL
^^^^^^^^

.. java:field:: public static final I18nString INTERVAL
   :outertype: ScaleLevels

NOMINAL
^^^^^^^

.. java:field:: public static final I18nString NOMINAL
   :outertype: ScaleLevels

ORDINAL
^^^^^^^

.. java:field:: public static final I18nString ORDINAL
   :outertype: ScaleLevels

RATIO
^^^^^

.. java:field:: public static final I18nString RATIO
   :outertype: ScaleLevels

