.. java:import:: java.time LocalDateTime

.. java:import:: javax.validation.constraints NotNull

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

ShadowCopyQueueItem.Action
==========================

.. java:package:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain
   :noindex:

.. java:type:: public enum Action
   :outertype: ShadowCopyQueueItem

   The action which will be performed for the shadows.

Enum Constants
--------------
CREATE
^^^^^^

.. java:field:: public static final ShadowCopyQueueItem.Action CREATE
   :outertype: ShadowCopyQueueItem.Action

HIDE
^^^^

.. java:field:: public static final ShadowCopyQueueItem.Action HIDE
   :outertype: ShadowCopyQueueItem.Action

UNHIDE
^^^^^^

.. java:field:: public static final ShadowCopyQueueItem.Action UNHIDE
   :outertype: ShadowCopyQueueItem.Action

