.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

.. java:import:: org.springframework.data.annotation CreatedDate

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: javax.validation.constraints NotNull

.. java:import:: java.time LocalDateTime

ShadowCopyQueueItem
===================

.. java:package:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain
   :noindex:

.. java:type:: @Document @AllArgsConstructor @NoArgsConstructor @Data public class ShadowCopyQueueItem

   Represents a queued shadow copy task of a project.

Fields
------
createdDate
^^^^^^^^^^^

.. java:field:: @CreatedDate private LocalDateTime createdDate
   :outertype: ShadowCopyQueueItem

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private String dataAcquisitionProjectId
   :outertype: ShadowCopyQueueItem

id
^^

.. java:field:: @Id private String id
   :outertype: ShadowCopyQueueItem

shadowCopyVersion
^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private String shadowCopyVersion
   :outertype: ShadowCopyQueueItem

updateStartedAt
^^^^^^^^^^^^^^^

.. java:field:: private LocalDateTime updateStartedAt
   :outertype: ShadowCopyQueueItem

username
^^^^^^^^

.. java:field:: @NotNull private String username
   :outertype: ShadowCopyQueueItem

