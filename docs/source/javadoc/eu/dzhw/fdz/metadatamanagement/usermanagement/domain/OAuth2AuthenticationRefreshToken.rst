.. java:import:: org.apache.commons.lang3.builder EqualsBuilder

.. java:import:: org.apache.commons.lang3.builder HashCodeBuilder

.. java:import:: org.apache.commons.lang3.builder ToStringBuilder

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.annotation PersistenceConstructor

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: edu.umd.cs.findbugs.annotations SuppressFBWarnings

OAuth2AuthenticationRefreshToken
================================

.. java:package:: eu.dzhw.fdz.metadatamanagement.usermanagement.domain
   :noindex:

.. java:type:: @Document @SuppressFBWarnings public class OAuth2AuthenticationRefreshToken

   The oauth2 refresh token.

Fields
------
authentication
^^^^^^^^^^^^^^

.. java:field:: private byte[] authentication
   :outertype: OAuth2AuthenticationRefreshToken

token
^^^^^

.. java:field:: private byte[] token
   :outertype: OAuth2AuthenticationRefreshToken

tokenId
^^^^^^^

.. java:field:: @Id private String tokenId
   :outertype: OAuth2AuthenticationRefreshToken

