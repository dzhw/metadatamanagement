.. java:import:: java.time LocalDateTime

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.index CompoundIndex

.. java:import:: org.springframework.data.mongodb.core.index CompoundIndexes

.. java:import:: org.springframework.data.mongodb.core.index Indexed

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

DaraUpdateQueueItem
===================

.. java:package:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain
   :noindex:

.. java:type:: @Document @CompoundIndexes @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class DaraUpdateQueueItem extends AbstractRdcDomainObject

   Dara entries will be updated asynchronously this using these items.

   :author: Daniel Katzberg

Fields
------
id
^^

.. java:field:: @Id private String id
   :outertype: DaraUpdateQueueItem

projectId
^^^^^^^^^

.. java:field:: @NotEmpty @Indexed private String projectId
   :outertype: DaraUpdateQueueItem

updateStartedAt
^^^^^^^^^^^^^^^

.. java:field:: private LocalDateTime updateStartedAt
   :outertype: DaraUpdateQueueItem

updateStartedBy
^^^^^^^^^^^^^^^

.. java:field:: private String updateStartedBy
   :outertype: DaraUpdateQueueItem

