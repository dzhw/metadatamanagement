.. java:import:: lombok Data

.. java:import:: java.util Objects

AbstractShadowableRdcDomainObject
=================================

.. java:package:: eu.dzhw.fdz.metadatamanagement.common.domain
   :noindex:

.. java:type:: @Data public abstract class AbstractShadowableRdcDomainObject extends AbstractRdcDomainObject

   Base class for all rdc domain objects which can exist as multiple versions.

Fields
------
masterId
^^^^^^^^

.. java:field:: private String masterId
   :outertype: AbstractShadowableRdcDomainObject

shadow
^^^^^^

.. java:field:: private boolean shadow
   :outertype: AbstractShadowableRdcDomainObject

successorId
^^^^^^^^^^^

.. java:field:: private String successorId
   :outertype: AbstractShadowableRdcDomainObject

