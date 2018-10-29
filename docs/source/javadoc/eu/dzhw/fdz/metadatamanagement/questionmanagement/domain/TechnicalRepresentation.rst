.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints Size

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

TechnicalRepresentation
=======================

.. java:package:: eu.dzhw.fdz.metadatamanagement.questionmanagement.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder public class TechnicalRepresentation

   Technical Representation of a Question. It contains the type, the language (e.g. XML) and the Source itself.

   :author: Daniel Katzberg

Fields
------
language
^^^^^^^^

.. java:field:: @NotEmpty @Size private String language
   :outertype: TechnicalRepresentation

source
^^^^^^

.. java:field:: @NotEmpty @Size private String source
   :outertype: TechnicalRepresentation

type
^^^^

.. java:field:: @NotEmpty @Size private String type
   :outertype: TechnicalRepresentation

