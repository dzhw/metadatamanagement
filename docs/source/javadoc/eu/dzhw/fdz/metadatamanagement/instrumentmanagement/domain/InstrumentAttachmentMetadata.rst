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

   Metadata which will be stored in GridFS with each attachment for instruments.

   :author: René Reitmann

Fields
------
dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private String dataAcquisitionProjectId
   :outertype: InstrumentAttachmentMetadata

description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringNotEmpty private I18nString description
   :outertype: InstrumentAttachmentMetadata

fileName
^^^^^^^^

.. java:field:: @NotEmpty @Pattern private String fileName
   :outertype: InstrumentAttachmentMetadata

id
^^

.. java:field:: @Id private String id
   :outertype: InstrumentAttachmentMetadata

indexInInstrument
^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer indexInInstrument
   :outertype: InstrumentAttachmentMetadata

instrumentId
^^^^^^^^^^^^

.. java:field:: @NotEmpty private String instrumentId
   :outertype: InstrumentAttachmentMetadata

instrumentNumber
^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer instrumentNumber
   :outertype: InstrumentAttachmentMetadata

language
^^^^^^^^

.. java:field:: @NotNull @ValidIsoLanguage private String language
   :outertype: InstrumentAttachmentMetadata

type
^^^^

.. java:field:: @NotNull @I18nStringSize @ValidInstrumentAttachmentType private I18nString type
   :outertype: InstrumentAttachmentMetadata

