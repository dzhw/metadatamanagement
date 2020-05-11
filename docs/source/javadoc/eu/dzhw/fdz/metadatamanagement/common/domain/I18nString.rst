.. java:import:: java.io Serializable

.. java:import:: org.javers.core.metamodel.annotation ValueObject

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

I18nString
==========

.. java:package:: eu.dzhw.fdz.metadatamanagement.common.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder @ValueObject public class I18nString implements Serializable

   Strings that can be represented in English and German.

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

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: I18nString

