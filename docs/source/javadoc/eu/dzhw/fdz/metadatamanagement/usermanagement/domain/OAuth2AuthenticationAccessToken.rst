.. java:import:: java.util Objects

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.annotation PersistenceConstructor

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: edu.umd.cs.findbugs.annotations SuppressFBWarnings

OAuth2AuthenticationAccessToken
===============================

.. java:package:: eu.dzhw.fdz.metadatamanagement.usermanagement.domain
   :noindex:

.. java:type:: @Document @SuppressFBWarnings public class OAuth2AuthenticationAccessToken

   The OAuth2 AccessToken.

Fields
------
authentication
^^^^^^^^^^^^^^

.. java:field:: private byte[] authentication
   :outertype: OAuth2AuthenticationAccessToken

authenticationId
^^^^^^^^^^^^^^^^

.. java:field:: private String authenticationId
   :outertype: OAuth2AuthenticationAccessToken

clientId
^^^^^^^^

.. java:field:: private String clientId
   :outertype: OAuth2AuthenticationAccessToken

refreshToken
^^^^^^^^^^^^

.. java:field:: private String refreshToken
   :outertype: OAuth2AuthenticationAccessToken

token
^^^^^

.. java:field:: private byte[] token
   :outertype: OAuth2AuthenticationAccessToken

tokenId
^^^^^^^

.. java:field:: @Id private String tokenId
   :outertype: OAuth2AuthenticationAccessToken

username
^^^^^^^^

.. java:field:: private String username
   :outertype: OAuth2AuthenticationAccessToken

