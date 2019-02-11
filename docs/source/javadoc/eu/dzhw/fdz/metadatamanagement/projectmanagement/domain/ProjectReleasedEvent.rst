.. java:import:: lombok Getter

.. java:import:: org.springframework.context ApplicationEvent

ProjectReleasedEvent
====================

.. java:package:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain
   :noindex:

.. java:type:: @Getter public class ProjectReleasedEvent extends ApplicationEvent

   Event emitted if a project release is detected. Contains the release version and projectId of \ :java:ref:`DataAcquisitionProject`\  as a reference.

Fields
------
dataAcquisitionProject
^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: private DataAcquisitionProject dataAcquisitionProject
   :outertype: ProjectReleasedEvent

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: ProjectReleasedEvent

