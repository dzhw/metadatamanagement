.. java:import:: java.util Objects

.. java:import:: org.springframework.data.mongodb.core.index Indexed

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidHiddenShadow

.. java:import:: lombok AccessLevel

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok Setter

AbstractShadowableRdcDomainObject
=================================

.. java:package:: eu.dzhw.fdz.metadatamanagement.common.domain
   :noindex:

.. java:type:: @Data @EqualsAndHashCode @ValidHiddenShadow public abstract class AbstractShadowableRdcDomainObject extends AbstractRdcDomainObject

   Base class for all rdc domain objects which can exist as multiple versions (shadows).

Fields
------
hidden
^^^^^^

.. java:field:: private boolean hidden
   :outertype: AbstractShadowableRdcDomainObject

   True if and only if the shadow copy must not be available for the public user. Only shadow copies which have a successor may be hidden.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: AbstractShadowableRdcDomainObject

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

