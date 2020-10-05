.. java:import:: java.io Serializable

.. java:import:: java.util Set

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: org.javers.core.metamodel.annotation TypeName

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

Tags
====

.. java:package:: eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain
   :noindex:

.. java:type:: @Data @TypeName @NoArgsConstructor public class Tags implements Serializable

   Contains tags associated with a dataPackage.

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

