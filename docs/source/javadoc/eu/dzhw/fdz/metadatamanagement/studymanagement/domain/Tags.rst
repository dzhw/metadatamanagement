.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: java.util List

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

.. java:field:: @NotEmpty private List<String> de
   :outertype: Tags

   German tags.

en
^^

.. java:field:: private List<String> en
   :outertype: Tags

   English tags.

