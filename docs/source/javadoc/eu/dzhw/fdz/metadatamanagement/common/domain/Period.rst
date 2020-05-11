.. java:import:: java.io Serializable

.. java:import:: java.time LocalDate

.. java:import:: org.javers.core.metamodel.annotation ValueObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidPeriod

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

Period
======

.. java:package:: eu.dzhw.fdz.metadatamanagement.common.domain
   :noindex:

.. java:type:: @ValidPeriod @NoArgsConstructor @Data @AllArgsConstructor @Builder @ValueObject public class Period implements Serializable

   Objects representing periods in time. All periods must have a start date and an end date and the start date must be before or equal to the end date.

Fields
------
end
^^^

.. java:field:: private LocalDate end
   :outertype: Period

   The end date of the period. Mandatory and must not be before start date.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: Period

start
^^^^^

.. java:field:: private LocalDate start
   :outertype: Period

   The start date of the period. Mandatory and must not be after end date.

