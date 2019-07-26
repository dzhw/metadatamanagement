.. java:import:: java.io Serializable

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints Size

.. java:import:: org.javers.core.metamodel.annotation ValueObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain Instrument

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

TechnicalRepresentation
=======================

.. java:package:: eu.dzhw.fdz.metadatamanagement.questionmanagement.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder @ValueObject public class TechnicalRepresentation implements Serializable

   The technical representation of a \ :java:ref:`Question`\  which was used to generate the question for instance in an online \ :java:ref:`Instrument`\ .

Fields
------
language
^^^^^^^^

.. java:field:: @NotEmpty @Size private String language
   :outertype: TechnicalRepresentation

   The technical language of the source of this representation. E.g. "qml". Must not be empty and must not contain more than 32 characters.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: TechnicalRepresentation

source
^^^^^^

.. java:field:: @NotEmpty @Size private String source
   :outertype: TechnicalRepresentation

   The source code of the question. Must not be empty and must not contain more than 1 MB characters.

type
^^^^

.. java:field:: @NotEmpty @Size private String type
   :outertype: TechnicalRepresentation

   The type of the technical representation. E.g. "zofar". Must not be empty and must not contain more than 32 characters.

