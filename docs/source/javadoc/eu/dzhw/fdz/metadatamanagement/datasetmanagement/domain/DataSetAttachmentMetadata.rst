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

   Metadata which will be stored in GridFS with each attachment for data sets.

Fields
------
dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private String dataAcquisitionProjectId
   :outertype: DataSetAttachmentMetadata

dataSetId
^^^^^^^^^

.. java:field:: @NotEmpty private String dataSetId
   :outertype: DataSetAttachmentMetadata

dataSetNumber
^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer dataSetNumber
   :outertype: DataSetAttachmentMetadata

description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringNotEmpty private I18nString description
   :outertype: DataSetAttachmentMetadata

fileName
^^^^^^^^

.. java:field:: @NotEmpty @Pattern private String fileName
   :outertype: DataSetAttachmentMetadata

id
^^

.. java:field:: @Id private String id
   :outertype: DataSetAttachmentMetadata

indexInDataSet
^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer indexInDataSet
   :outertype: DataSetAttachmentMetadata

language
^^^^^^^^

.. java:field:: @NotNull @ValidIsoLanguage private String language
   :outertype: DataSetAttachmentMetadata

title
^^^^^

.. java:field:: @NotNull @Size private String title
   :outertype: DataSetAttachmentMetadata

