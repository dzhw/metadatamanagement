.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: javax.validation.constraints NotNull

.. java:import:: java.time LocalDateTime

ShadowCopyQueueItem
===================

.. java:package:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain
   :noindex:

.. java:type:: @Document @AllArgsConstructor @NoArgsConstructor @Data @EqualsAndHashCode public class ShadowCopyQueueItem extends AbstractRdcDomainObject

   Represents a queued shadow copy task of a project.

Fields
------
dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private String dataAcquisitionProjectId
   :outertype: ShadowCopyQueueItem

   Project id for which a shadow copy should be created.

id
^^

.. java:field:: @Id private String id
   :outertype: ShadowCopyQueueItem

   Queue item id.

shadowCopyVersion
^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private String shadowCopyVersion
   :outertype: ShadowCopyQueueItem

   The version that should be created.

updateStartedAt
^^^^^^^^^^^^^^^

.. java:field:: private LocalDateTime updateStartedAt
   :outertype: ShadowCopyQueueItem

   Start time of the copy process.

