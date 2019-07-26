.. java:import:: java.io Serializable

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation ValidCountryCode

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

GeographicCoverage
==================

.. java:package:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain
   :noindex:

.. java:type:: @AllArgsConstructor @NoArgsConstructor @Data @Builder public class GeographicCoverage implements Serializable

   Contains data regarding the location where survey data was collected.

Fields
------
country
^^^^^^^

.. java:field:: @ValidCountryCode private String country
   :outertype: GeographicCoverage

   ISO 3166-1 alpha-2 country code.

description
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString description
   :outertype: GeographicCoverage

   Free text description for additional information regarding the location.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: GeographicCoverage

