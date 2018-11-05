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

   Details of a customer who has ordered \ :java:ref:`Product`\ s.

Fields
------
email
^^^^^

.. java:field:: @Email @NotEmpty private String email
   :outertype: Customer

   Email address of the customer. Must be a valid email address and must not be empty.

name
^^^^

.. java:field:: @NotEmpty private String name
   :outertype: Customer

   Name of the customer as given in the shopping cart. Must not be empty.

