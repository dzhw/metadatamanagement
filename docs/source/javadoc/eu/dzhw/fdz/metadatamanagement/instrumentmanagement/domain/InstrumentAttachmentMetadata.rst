.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: org.javers.core.metamodel.annotation Entity

.. java:import:: org.springframework.data.annotation Id

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidIsoLanguage

.. java:import:: eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation ValidInstrumentAttachmentType

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain DataAcquisitionProject

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

InstrumentAttachmentMetadata
============================

.. java:package:: eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain
   :noindex:

.. java:type:: @Entity @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class InstrumentAttachmentMetadata extends AbstractRdcDomainObject

   Metadata which will be stored with each attachment of a \ :java:ref:`Instrument`\ .

Fields
------
dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private String dataAcquisitionProjectId
   :outertype: InstrumentAttachmentMetadata

   The id of the \ :java:ref:`DataAcquisitionProject`\  to which the \ :java:ref:`Instrument`\  of this attachment belongs. Must not be empty.

description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringNotEmpty private I18nString description
   :outertype: InstrumentAttachmentMetadata

   A description for this attachment. It must be specified in at least one language and it must not contain more than 512 characters.

fileName
^^^^^^^^

.. java:field:: @NotEmpty @Pattern private String fileName
   :outertype: InstrumentAttachmentMetadata

   The filename of the attachment. Must not be empty and must contain only (german) alphanumeric characters and "_" and "-" and ".".

id
^^

.. java:field:: @Id private String id
   :outertype: InstrumentAttachmentMetadata

   The id of the attachment. Holds the complete path which can be used to download the file.

indexInInstrument
^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer indexInInstrument
   :outertype: InstrumentAttachmentMetadata

   The index in the \ :java:ref:`Instrument`\  of this attachment. Used for sorting the attachments of this \ :java:ref:`Instrument`\ . Must not be empty.

instrumentId
^^^^^^^^^^^^

.. java:field:: @NotEmpty private String instrumentId
   :outertype: InstrumentAttachmentMetadata

   The id of the \ :java:ref:`Instrument`\  to which this attachment belongs. Must not be empty.

instrumentNumber
^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer instrumentNumber
   :outertype: InstrumentAttachmentMetadata

   The number of the \ :java:ref:`Instrument`\  to which this attachment belongs. Must not be empty.

language
^^^^^^^^

.. java:field:: @NotNull @ValidIsoLanguage private String language
   :outertype: InstrumentAttachmentMetadata

   The language of the attachments content. Must not be empty and must be specified as ISO 639 language code.

type
^^^^

.. java:field:: @NotNull @I18nStringSize @ValidInstrumentAttachmentType private I18nString type
   :outertype: InstrumentAttachmentMetadata

   The type of this attachment. Must not be empty and must be one of \ :java:ref:`InstrumentAttachmentTypes`\ .

