.. java:import:: java.io Serializable

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: org.javers.core.metamodel.annotation ValueObject

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

Person
======

.. java:package:: eu.dzhw.fdz.metadatamanagement.common.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder @ValueObject public class Person implements Serializable

   A representation of a person.

Fields
------
firstName
^^^^^^^^^

.. java:field:: @NotEmpty private String firstName
   :outertype: Person

   The first name of the person. Must not be empty.

lastName
^^^^^^^^

.. java:field:: @NotEmpty private String lastName
   :outertype: Person

   The last name of the person. Must not be empty.

middleName
^^^^^^^^^^

.. java:field:: private String middleName
   :outertype: Person

   The middle name of the person.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: Person

