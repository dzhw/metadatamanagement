.. java:import:: java.util Objects

.. java:import:: org.springframework.data.mongodb.core.index Indexed

.. java:import:: lombok AccessLevel

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok Setter

AbstractShadowableRdcDomainObject
=================================

.. java:package:: eu.dzhw.fdz.metadatamanagement.common.domain
   :noindex:

.. java:type:: @Data @EqualsAndHashCode public abstract class AbstractShadowableRdcDomainObject extends AbstractRdcDomainObject

   Base class for all rdc domain objects which can exist as multiple versions.

Fields
------
masterId
^^^^^^^^

.. java:field:: private String masterId
   :outertype: AbstractShadowableRdcDomainObject

   The id shared between all shadow copies of a domain object. It points to the most recent version of the domain object.

shadow
^^^^^^

.. java:field:: @Setter @Indexed private boolean shadow
   :outertype: AbstractShadowableRdcDomainObject

   Determines whether this document is a shadow copy.

successorId
^^^^^^^^^^^

.. java:field:: private String successorId
   :outertype: AbstractShadowableRdcDomainObject

   The document id which is the successor to this shadow copy.

