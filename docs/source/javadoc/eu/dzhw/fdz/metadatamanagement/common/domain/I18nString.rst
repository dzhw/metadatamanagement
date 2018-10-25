.. java:import:: org.javers.core.metamodel.annotation ValueObject

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

I18nString
==========

.. java:package:: eu.dzhw.fdz.metadatamanagement.common.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder @ValueObject public class I18nString

   Strings that can be represented in English and German.

   :author: René Reitmann, Daniel Katzberg

Fields
------
de
^^

.. java:field:: private String de
   :outertype: I18nString

   The german version of this string.

en
^^

.. java:field:: private String en
   :outertype: I18nString

   The english version of this string.

