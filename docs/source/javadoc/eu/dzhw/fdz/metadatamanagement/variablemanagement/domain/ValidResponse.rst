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

.. java:type:: @Data @NoArgsConstructor @AllArgsConstructor @Builder public class ValidResponse

   The value includes the value itself, a label and frequencies. There are no calculations of the frequencies.

   :author: Daniel Katzberg

Fields
------
absoluteFrequency
^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer absoluteFrequency
   :outertype: ValidResponse

label
^^^^^

.. java:field:: @I18nStringSize private I18nString label
   :outertype: ValidResponse

relativeFrequency
^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Double relativeFrequency
   :outertype: ValidResponse

validRelativeFrequency
^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Double validRelativeFrequency
   :outertype: ValidResponse

value
^^^^^

.. java:field:: @NotEmpty @Size private String value
   :outertype: ValidResponse

