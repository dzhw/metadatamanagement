.. java:import:: java.util Set

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

Tags
====

.. java:package:: eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain
   :noindex:

.. java:type:: @Data @NoArgsConstructor public class Tags

   Contains tags associated with a concept.

Fields
------
de
^^

.. java:field:: @NotEmpty private Set<String> de
   :outertype: Tags

   German tags. At least one tag must be provided.

en
^^

.. java:field:: @NotEmpty private Set<String> en
   :outertype: Tags

   English tags. At least one tag must be provided.

