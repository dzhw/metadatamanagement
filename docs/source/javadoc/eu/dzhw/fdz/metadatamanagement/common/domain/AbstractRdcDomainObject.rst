.. java:import:: java.time LocalDateTime

.. java:import:: org.springframework.data.annotation CreatedBy

.. java:import:: org.springframework.data.annotation CreatedDate

.. java:import:: org.springframework.data.annotation LastModifiedBy

.. java:import:: org.springframework.data.annotation LastModifiedDate

.. java:import:: org.springframework.data.annotation Version

.. java:import:: lombok Data

AbstractRdcDomainObject
=======================

.. java:package:: eu.dzhw.fdz.metadatamanagement.common.domain
   :noindex:

.. java:type:: @Data public abstract class AbstractRdcDomainObject

   Base class for all rdc domain objects.

   :author: René Reitmann

Fields
------
createdBy
^^^^^^^^^

.. java:field:: @CreatedBy private String createdBy
   :outertype: AbstractRdcDomainObject

   The name of the user which has created this object.

createdDate
^^^^^^^^^^^

.. java:field:: @CreatedDate private LocalDateTime createdDate
   :outertype: AbstractRdcDomainObject

   The date and time (in UTC) when this domain object was created.

lastModifiedBy
^^^^^^^^^^^^^^

.. java:field:: @LastModifiedBy private String lastModifiedBy
   :outertype: AbstractRdcDomainObject

   The name of the user who last saved this object.

lastModifiedDate
^^^^^^^^^^^^^^^^

.. java:field:: @LastModifiedDate private LocalDateTime lastModifiedDate
   :outertype: AbstractRdcDomainObject

   The date and time when this object was last saved.

version
^^^^^^^

.. java:field:: @Version private Long version
   :outertype: AbstractRdcDomainObject

   Number which is incremented on each save of this object.

