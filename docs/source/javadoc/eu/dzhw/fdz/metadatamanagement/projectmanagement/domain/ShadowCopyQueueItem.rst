.. java:import:: java.time LocalDateTime

.. java:import:: javax.validation.constraints NotNull

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

ShadowCopyQueueItem
===================

.. java:package:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain
   :noindex:

.. java:type:: @Document @AllArgsConstructor @NoArgsConstructor @Data @EqualsAndHashCode public class ShadowCopyQueueItem extends AbstractRdcDomainObject

   Represents a queued shadow copy task of a project.

Fields
------
action
^^^^^^

.. java:field:: @NotNull private Action action
   :outertype: ShadowCopyQueueItem

   The action which will be performed for the shadow copies.

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private String dataAcquisitionProjectId
   :outertype: ShadowCopyQueueItem

   Project id for which a shadow copy should be created or hidden or unhidden.

id
^^

.. java:field:: @Id private String id
   :outertype: ShadowCopyQueueItem

   Queue item id.

release
^^^^^^^

.. java:field:: @NotNull private Release release
   :outertype: ShadowCopyQueueItem

   The release object of the project which has been released.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: ShadowCopyQueueItem

updateStartedAt
^^^^^^^^^^^^^^^

.. java:field:: private LocalDateTime updateStartedAt
   :outertype: ShadowCopyQueueItem

   Start time of the copy process.

