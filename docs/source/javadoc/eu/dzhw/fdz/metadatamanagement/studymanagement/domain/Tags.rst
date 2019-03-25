.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: java.util Set

Tags
====

.. java:package:: eu.dzhw.fdz.metadatamanagement.studymanagement.domain
   :noindex:

.. java:type:: @Data @NoArgsConstructor public class Tags

   Contains tags associated with a study.

Fields
------
de
^^

.. java:field:: @NotEmpty private Set<String> de
   :outertype: Tags

   German tags. At least one tag must be provided.

en
^^

.. java:field:: private Set<String> en
   :outertype: Tags

   English tags (optional).

