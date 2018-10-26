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

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Missing

   The missing includes a code, a label and frequencies. There are no calculations of the frequencies. This represent a missing and not a valid response. Use \ ``ValidResponse``\  for the representation of valid responses.

   :author: Daniel Katzberg

Fields
------
absoluteFrequency
^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer absoluteFrequency
   :outertype: Missing

code
^^^^

.. java:field:: @NotEmpty private String code
   :outertype: Missing

label
^^^^^

.. java:field:: @I18nStringSize private I18nString label
   :outertype: Missing

relativeFrequency
^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Double relativeFrequency
   :outertype: Missing

