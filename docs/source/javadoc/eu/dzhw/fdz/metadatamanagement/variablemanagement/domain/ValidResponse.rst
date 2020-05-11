.. java:import:: java.io Serializable

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Size

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

ValidResponse
=============

.. java:package:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain
   :noindex:

.. java:type:: @Data @NoArgsConstructor @AllArgsConstructor @Builder public class ValidResponse implements Serializable

   A valid response represents one observation of a \ :java:ref:`Variable`\  and its frequency.

Fields
------
absoluteFrequency
^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer absoluteFrequency
   :outertype: ValidResponse

   The absolute number of occurrences of this observation. Must not be empty.

label
^^^^^

.. java:field:: @I18nStringSize private I18nString label
   :outertype: ValidResponse

   An optional label for the value of this observation.

relativeFrequency
^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Double relativeFrequency
   :outertype: ValidResponse

   The quotient from absoluteFrequency and \ :java:ref:`Distribution`\ .totalAbsoluteFrequency. Must not be empty.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: ValidResponse

validRelativeFrequency
^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Double validRelativeFrequency
   :outertype: ValidResponse

   The quotient from absoluteFrequency and \ :java:ref:`Distribution`\ .totalValidAbsoluteFrequency. Must not be empty.

value
^^^^^

.. java:field:: @NotEmpty @Size private String value
   :outertype: ValidResponse

   The value which has been observed (e.g. was responded by the participant). Must not be empty and must not contain more than 256 characters.

