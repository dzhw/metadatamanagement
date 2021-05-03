.. java:import:: javax.validation Valid

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

.. java:import:: org.javers.core.metamodel.annotation Entity

.. java:import:: org.javers.core.metamodel.annotation TypeName

.. java:import:: org.springframework.data.annotation Id

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractShadowableRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidIsoLanguage

.. java:import:: eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.validation ValidDataPackageAttachmentType

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain DataAcquisitionProject

.. java:import:: lombok AccessLevel

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok Setter

.. java:import:: lombok ToString

DataPackageAttachmentMetadata
=============================

.. java:package:: eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain
   :noindex:

.. java:type:: @Entity @TypeName @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class DataPackageAttachmentMetadata extends AbstractShadowableRdcDomainObject

   Metadata which will be stored with each attachment of a \ :java:ref:`DataPackage`\ .

Fields
------
citationDetails
^^^^^^^^^^^^^^^

.. java:field:: @Valid private MethodReportCitationDetails citationDetails
   :outertype: DataPackageAttachmentMetadata

   Additional details required to generate a citation hint for Method Reports. Can be null for other attachment types than method reports. Can also be null for legacy method reports.

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private String dataAcquisitionProjectId
   :outertype: DataPackageAttachmentMetadata

   The id of the \ :java:ref:`DataAcquisitionProject`\  to which the \ :java:ref:`DataPackage`\  of this attachment belongs. Must not be empty.

dataPackageId
^^^^^^^^^^^^^

.. java:field:: @NotEmpty private String dataPackageId
   :outertype: DataPackageAttachmentMetadata

   The id of the \ :java:ref:`DataPackage`\  to which this attachment belongs. Must not be empty.

description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringNotEmpty private I18nString description
   :outertype: DataPackageAttachmentMetadata

   A description for this attachment. It must be specified in at least one language and it must not contain more than 512 characters.

fileName
^^^^^^^^

.. java:field:: @NotEmpty @Pattern private String fileName
   :outertype: DataPackageAttachmentMetadata

   The filename of the attachment. Must not be empty and must contain only (german) alphanumeric characters and "_" and "-" and ".".

id
^^

.. java:field:: @Id @Setter private String id
   :outertype: DataPackageAttachmentMetadata

   The id of the attachment. Holds the complete path which can be used to download the file.

indexInDataPackage
^^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer indexInDataPackage
   :outertype: DataPackageAttachmentMetadata

   The index in the \ :java:ref:`DataPackage`\  of this attachment. Used for sorting the attachments of this \ :java:ref:`DataPackage`\ . Must not be empty.

language
^^^^^^^^

.. java:field:: @NotNull @ValidIsoLanguage private String language
   :outertype: DataPackageAttachmentMetadata

   The language of the attachments content. Must not be empty and must be specified as ISO 639 language code.

masterId
^^^^^^^^

.. java:field:: @Setter private String masterId
   :outertype: DataPackageAttachmentMetadata

   The master id of the dataPackage attachment.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: DataPackageAttachmentMetadata

title
^^^^^

.. java:field:: @NotEmpty @Size private String title
   :outertype: DataPackageAttachmentMetadata

   An optional title of this attachment in the attachments' language. Must not be empty and it must not contain more than 2048 characters.

type
^^^^

.. java:field:: @NotNull @I18nStringSize @ValidDataPackageAttachmentType private I18nString type
   :outertype: DataPackageAttachmentMetadata

   The type of the attachment. Must be one of \ :java:ref:`DataPackageAttachmentTypes`\  and must not be empty.

