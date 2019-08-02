.. java:import:: java.util Set

.. java:import:: lombok Getter

ConceptInUseException
=====================

.. java:package:: eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain
   :noindex:

.. java:type:: public class ConceptInUseException extends RuntimeException

   Thrown if a delete attempt was made while the Concept is referenced by an instance of \ :java:ref:`eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument`\  or \ :java:ref:`eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question`\ .

Fields
------
instrumentIds
^^^^^^^^^^^^^

.. java:field:: @Getter private final Set<String> instrumentIds
   :outertype: ConceptInUseException

questionIds
^^^^^^^^^^^

.. java:field:: @Getter private final Set<String> questionIds
   :outertype: ConceptInUseException

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: ConceptInUseException

