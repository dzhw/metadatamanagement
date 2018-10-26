.. java:import:: java.util List

.. java:import:: javax.validation Valid

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.index Indexed

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

Order
=====

.. java:package:: eu.dzhw.fdz.metadatamanagement.ordermanagement.domain
   :noindex:

.. java:type:: @Document @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Order extends AbstractRdcDomainObject

   Order (DTO) containing all relevant information of a customer and her data products.

   :author: René Reitmann

Fields
------
customer
^^^^^^^^

.. java:field:: @Valid @NotNull private Customer customer
   :outertype: Order

id
^^

.. java:field:: @Id private String id
   :outertype: Order

languageKey
^^^^^^^^^^^

.. java:field:: @NotEmpty private String languageKey
   :outertype: Order

products
^^^^^^^^

.. java:field:: @Valid @NotEmpty private List<Product> products
   :outertype: Order

state
^^^^^

.. java:field:: @Indexed private OrderState state
   :outertype: Order

