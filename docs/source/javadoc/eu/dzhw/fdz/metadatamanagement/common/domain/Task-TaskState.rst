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

Task.TaskState
==============

.. java:package:: eu.dzhw.fdz.metadatamanagement.common.domain
   :noindex:

.. java:type:: public enum TaskState
   :outertype: Task

   State of tasks.

   :author: tgehrke

Enum Constants
--------------
DONE
^^^^

.. java:field:: public static final Task.TaskState DONE
   :outertype: Task.TaskState

FAILURE
^^^^^^^

.. java:field:: public static final Task.TaskState FAILURE
   :outertype: Task.TaskState

RUNNING
^^^^^^^

.. java:field:: public static final Task.TaskState RUNNING
   :outertype: Task.TaskState

