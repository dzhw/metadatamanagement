.. java:import:: java.io Serializable

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

Missing
=======

.. java:package:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Missing implements Serializable

   A missing or missing value is a value in a \ :java:ref:`Variable`\  which represents a reason why no observation (\ :java:ref:`ValidResponse`\ ) has been stored. It also contains its frequency.

Fields
------
absoluteFrequency
^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer absoluteFrequency
   :outertype: Missing

   The absolute number of occurrences of this missing. Must not be empty.

code
^^^^

.. java:field:: @NotEmpty private String code
   :outertype: Missing

   A (unique in this \ :java:ref:`Variable`\ ) code for this missing. Must not be empty.

label
^^^^^

.. java:field:: @I18nStringSize private I18nString label
   :outertype: Missing

   A label describing this missing. Must not contain more than 512 characters.

relativeFrequency
^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Double relativeFrequency
   :outertype: Missing

   The quotient from absoluteFrequency and \ :java:ref:`Distribution`\ .totalAbsoluteFrequency. Must not be empty.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: Missing

