.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Data

Language
========

.. java:package:: eu.dzhw.fdz.metadatamanagement.common.domain
   :noindex:

.. java:type:: @Data @AllArgsConstructor public class Language implements Comparable<Language>

   Wrapper for a language code and it's respective display name.

Fields
------
displayName
^^^^^^^^^^^

.. java:field:: private final String displayName
   :outertype: Language

   Display name.

languageCode
^^^^^^^^^^^^

.. java:field:: private final String languageCode
   :outertype: Language

   Language code.

