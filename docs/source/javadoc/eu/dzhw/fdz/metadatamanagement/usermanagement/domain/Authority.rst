.. java:import:: java.io Serializable

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Size

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.mapping Document

Authority
=========

.. java:package:: eu.dzhw.fdz.metadatamanagement.usermanagement.domain
   :noindex:

.. java:type:: @Document public class Authority implements Serializable

   An authority (a security role) used by Spring Security.

Fields
------
name
^^^^

.. java:field:: @NotNull @Size @Id private String name
   :outertype: Authority

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: Authority

