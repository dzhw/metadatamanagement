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

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain DataAcquisitionProject

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

DataSetAttachmentMetadata
=========================

.. java:package:: eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain
   :noindex:

.. java:type:: @Entity @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class DataSetAttachmentMetadata extends AbstractRdcDomainObject

   Metadata which will be stored with each attachment of a \ :java:ref:`DataSet`\ .

Fields
------
dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private String dataAcquisitionProjectId
   :outertype: DataSetAttachmentMetadata

   The id of the \ :java:ref:`DataAcquisitionProject`\  to which the \ :java:ref:`DataSet`\  of this attachment belongs. Must not be empty.

dataSetId
^^^^^^^^^

.. java:field:: @NotEmpty private String dataSetId
   :outertype: DataSetAttachmentMetadata

   The id of the \ :java:ref:`DataSet`\  to which this attachment belongs. Must not be empty.

dataSetNumber
^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer dataSetNumber
   :outertype: DataSetAttachmentMetadata

   The number of the \ :java:ref:`DataSet`\  to which this attachment belongs. Must not be empty.

description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringNotEmpty private I18nString description
   :outertype: DataSetAttachmentMetadata

   A description for this attachment. It must be specified in at least one language and it must not contain more than 512 characters.

fileName
^^^^^^^^

.. java:field:: @NotEmpty @Pattern private String fileName
   :outertype: DataSetAttachmentMetadata

   The filename of the attachment. Must not be empty and must contain only (german) alphanumeric characters and "_" and "-" and ".".

id
^^

.. java:field:: @Id private String id
   :outertype: DataSetAttachmentMetadata

   The id of the attachment. Holds the complete path which can be used to download the file.

indexInDataSet
^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer indexInDataSet
   :outertype: DataSetAttachmentMetadata

   The index in the \ :java:ref:`DataSet`\  of this attachment. Used for sorting the attachments of this \ :java:ref:`DataSet`\ . Must not be empty.

language
^^^^^^^^

.. java:field:: @NotNull @ValidIsoLanguage private String language
   :outertype: DataSetAttachmentMetadata

   The language of the attachments content. Must not be empty and must be specified as ISO 639 language code.

title
^^^^^

.. java:field:: @NotEmpty @Size private String title
   :outertype: DataSetAttachmentMetadata

   The title of the attachment in the language of the attachment. Must not be empty and must not contain more than 2048 characters.

