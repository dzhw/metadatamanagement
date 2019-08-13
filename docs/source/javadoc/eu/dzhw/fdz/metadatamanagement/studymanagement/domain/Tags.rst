.. java:import:: java.io Serializable

.. java:import:: java.util Set

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

Tags
====

.. java:package:: eu.dzhw.fdz.metadatamanagement.studymanagement.domain
   :noindex:

.. java:type:: @Data @NoArgsConstructor public class Tags implements Serializable

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

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: Tags

