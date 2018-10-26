.. java:import:: java.io Serializable

.. java:import:: java.time LocalDateTime

.. java:import:: java.util Set

.. java:import:: javax.validation.constraints Email

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.index Indexed

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: org.springframework.data.mongodb.core.mapping Field

.. java:import:: com.fasterxml.jackson.annotation JsonIgnore

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

User
====

.. java:package:: eu.dzhw.fdz.metadatamanagement.usermanagement.domain
   :noindex:

.. java:type:: @Document @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class User extends AbstractRdcDomainObject implements Serializable

   A user.

Fields
------
activated
^^^^^^^^^

.. java:field:: @Builder.Default private boolean activated
   :outertype: User

activationKey
^^^^^^^^^^^^^

.. java:field:: @Size @Field @JsonIgnore private String activationKey
   :outertype: User

authorities
^^^^^^^^^^^

.. java:field:: @JsonIgnore private Set<Authority> authorities
   :outertype: User

email
^^^^^

.. java:field:: @Email @Size @Indexed private String email
   :outertype: User

firstName
^^^^^^^^^

.. java:field:: @Size @Field private String firstName
   :outertype: User

id
^^

.. java:field:: @Id private String id
   :outertype: User

langKey
^^^^^^^

.. java:field:: @Size @Field private String langKey
   :outertype: User

lastName
^^^^^^^^

.. java:field:: @Size @Field private String lastName
   :outertype: User

login
^^^^^

.. java:field:: @NotNull @Pattern @Size @Indexed private String login
   :outertype: User

password
^^^^^^^^

.. java:field:: @JsonIgnore @NotNull @Size private String password
   :outertype: User

resetDate
^^^^^^^^^

.. java:field:: @Field @Builder.Default private LocalDateTime resetDate
   :outertype: User

resetKey
^^^^^^^^

.. java:field:: @Size @Field private String resetKey
   :outertype: User

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: User

