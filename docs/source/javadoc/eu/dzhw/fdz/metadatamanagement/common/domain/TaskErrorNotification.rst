.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain Task.TaskType

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

TaskErrorNotification
=====================

.. java:package:: eu.dzhw.fdz.metadatamanagement.common.domain
   :noindex:

.. java:type:: @EqualsAndHashCode @ToString @NoArgsConstructor @AllArgsConstructor @Data @Builder public class TaskErrorNotification

   DTO holding all information required for sending notifications to users in case an error occurred during task execution.

   :author: René Reitmann

Fields
------
domainObjectId
^^^^^^^^^^^^^^

.. java:field:: private String domainObjectId
   :outertype: TaskErrorNotification

   An id of a domainObject. May be empty.

errorMessage
^^^^^^^^^^^^

.. java:field:: @NotEmpty private String errorMessage
   :outertype: TaskErrorNotification

   An error message indicating the reason of the error. Must not be empty.

onBehalfOf
^^^^^^^^^^

.. java:field:: private String onBehalfOf
   :outertype: TaskErrorNotification

   The name of the user for which the task has been executed. May be empty.

taskType
^^^^^^^^

.. java:field:: @NotNull private TaskType taskType
   :outertype: TaskErrorNotification

   The type of the task which has been executed. Must not be empty.

