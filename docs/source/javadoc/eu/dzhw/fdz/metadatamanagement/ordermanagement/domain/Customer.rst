.. java:import:: javax.validation.constraints Email

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

Customer
========

.. java:package:: eu.dzhw.fdz.metadatamanagement.ordermanagement.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Customer

   Details of a customer who orders data products.

   :author: René Reitmann

Fields
------
email
^^^^^

.. java:field:: @Email @NotEmpty private String email
   :outertype: Customer

name
^^^^

.. java:field:: @NotEmpty private String name
   :outertype: Customer

   Name of the customer as given in the shopping cart.

