.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

Counter
=======

.. java:package:: eu.dzhw.fdz.metadatamanagement.common.domain
   :noindex:

.. java:type:: @Document @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Counter

   Counter document which can be used to get an incremented sequence number per document id.

   :author: René Reitmann

Fields
------
id
^^

.. java:field:: @Id private String id
   :outertype: Counter

   The id of the counter, e.g. "orders".

seq
^^^

.. java:field:: private long seq
   :outertype: Counter

   The current sequence number.

