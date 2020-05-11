.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

.. java:import:: org.javers.core.metamodel.annotation Entity

.. java:import:: org.springframework.data.annotation Id

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidIsoLanguage

.. java:import:: eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.validation ValidConceptAttachmentType

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

ConceptAttachmentMetadata
=========================

.. java:package:: eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain
   :noindex:

.. java:type:: @Entity @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class ConceptAttachmentMetadata extends AbstractRdcDomainObject

   Metadata which will be stored with each attachment of a \ :java:ref:`Concept`\ .

Fields
------
conceptId
^^^^^^^^^

.. java:field:: @NotEmpty private String conceptId
   :outertype: ConceptAttachmentMetadata

   The id of the \ :java:ref:`Concept`\  to which this attachment belongs. Must not be empty.

description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringNotEmpty private I18nString description
   :outertype: ConceptAttachmentMetadata

   A description for this attachment. It must be specified in at least one language and it must not contain more than 512 characters.

fileName
^^^^^^^^

.. java:field:: @NotEmpty @Pattern private String fileName
   :outertype: ConceptAttachmentMetadata

   The filename of the attachment. Must not be empty and must contain only (german) alphanumeric characters and "_" and "-" and ".".

id
^^

.. java:field:: @Id private String id
   :outertype: ConceptAttachmentMetadata

   The id of the attachment. Holds the complete path which can be used to download the file.

indexInConcept
^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer indexInConcept
   :outertype: ConceptAttachmentMetadata

   The index in the \ :java:ref:`Concept`\  of this attachment. Used for sorting the attachments of this \ :java:ref:`Concept`\ . Must not be empty.

language
^^^^^^^^

.. java:field:: @NotNull @ValidIsoLanguage private String language
   :outertype: ConceptAttachmentMetadata

   The language of the attachments content. Must not be empty and must be specified as ISO 639 language code.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: ConceptAttachmentMetadata

title
^^^^^

.. java:field:: @NotEmpty @Size private String title
   :outertype: ConceptAttachmentMetadata

   An optional title of this attachment in the attachments' language. It must not contain more than 2048 characters.

type
^^^^

.. java:field:: @NotNull @I18nStringSize @ValidConceptAttachmentType private I18nString type
   :outertype: ConceptAttachmentMetadata

   The type of the attachment. Must be one of \ :java:ref:`ConceptAttachmentTypes`\  and must not be empty.

