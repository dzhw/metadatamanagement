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

   Task entity holding the current state of a long running task.

   :author: tgehrke

Fields
------
errorList
^^^^^^^^^

.. java:field:: private ErrorListDto errorList
   :outertype: Task

   The list of errors which occurred during execution of the task.

id
^^

.. java:field:: @Id private String id
   :outertype: Task

   The id or task number of the task.

location
^^^^^^^^

.. java:field:: private String location
   :outertype: Task

   The location URI of the result of the task.

state
^^^^^

.. java:field:: private TaskState state
   :outertype: Task

   The current state of the task.

type
^^^^

.. java:field:: private TaskType type
   :outertype: Task

   The type of the task.

