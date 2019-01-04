.. java:import:: java.net URI

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.rest.errors ErrorListDto

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

Task
====

.. java:package:: eu.dzhw.fdz.metadatamanagement.common.domain
   :noindex:

.. java:type:: @Document @EqualsAndHashCode @ToString @NoArgsConstructor @AllArgsConstructor @Data @Builder public class Task extends AbstractRdcDomainObject

   entity for task handling.

   :author: tgehrke

Fields
------
errorList
^^^^^^^^^

.. java:field:: private ErrorListDto errorList
   :outertype: Task

id
^^

.. java:field:: @Id private String id
   :outertype: Task

location
^^^^^^^^

.. java:field:: private URI location
   :outertype: Task

state
^^^^^

.. java:field:: private TaskState state
   :outertype: Task

type
^^^^

.. java:field:: private TaskType type
   :outertype: Task

