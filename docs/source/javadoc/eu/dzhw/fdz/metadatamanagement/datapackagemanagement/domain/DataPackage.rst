.. java:import:: java.util List

.. java:import:: javax.validation Valid

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

.. java:import:: org.javers.core.metamodel.annotation Entity

.. java:import:: org.javers.core.metamodel.annotation TypeName

.. java:import:: org.springframework.beans BeanUtils

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.index Indexed

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractShadowableRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain Person

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringEntireNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringEntireNotEmptyOptional

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringMustNotContainComma

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidShadowId

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain DataAcquisitionProject

.. java:import:: eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.projection DataPackageSubDocumentProjection

.. java:import:: eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.validation ValidDataAvailability

.. java:import:: eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.validation ValidDataPackageId

.. java:import:: eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.validation ValidSurveyDesign

.. java:import:: io.swagger.v3.oas.annotations.media Schema

.. java:import:: lombok AccessLevel

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok Setter

.. java:import:: lombok ToString

DataPackage
===========

.. java:package:: eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain
   :noindex:

.. java:type:: @Entity @Document @TypeName @ValidDataPackageId @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder @Schema @ValidShadowId public class DataPackage extends AbstractShadowableRdcDomainObject implements DataPackageSubDocumentProjection

   A data package contains all metadata of a \ :java:ref:`DataAcquisitionProject`\ . It will get a DOI (Digital Object Identifier) when the \ :java:ref:`DataAcquisitionProject`\  is released.

Fields
------
annotations
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString annotations
   :outertype: DataPackage

   Arbitrary additional text for this dataPackage. Markdown is supported. Must not contain more than 2048 characters.

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Indexed @NotEmpty private String dataAcquisitionProjectId
   :outertype: DataPackage

   The id of the \ :java:ref:`DataAcquisitionProject`\  to which this dataPackage belongs. The dataAcquisitionProjectId must not be empty.

dataAvailability
^^^^^^^^^^^^^^^^

.. java:field:: @NotNull @ValidDataAvailability private I18nString dataAvailability
   :outertype: DataPackage

   The current state of the data's availability. Must be one of \ :java:ref:`DataAvailabilities`\  and must not be empty.

dataCurators
^^^^^^^^^^^^

.. java:field:: @Valid @NotEmpty private List<Person> dataCurators
   :outertype: DataPackage

   List of \ :java:ref:`Person`\ s which have curated this data package. Must not be empty.

description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringEntireNotEmpty private I18nString description
   :outertype: DataPackage

   A description of the dataPackage. Markdown is supported. It must be specified in German and English and it must not contain more than 2048 characters.

id
^^

.. java:field:: @Id @Setter @NotEmpty private String id
   :outertype: DataPackage

   The id of the dataPackage which uniquely identifies the dataPackage in this application.

institutions
^^^^^^^^^^^^

.. java:field:: @NotEmpty private List<I18nString> institutions
   :outertype: DataPackage

   The names of the institutions which have performed this dataPackage. It must be specified in German and English and it must not contain more than 512 characters.

masterId
^^^^^^^^

.. java:field:: @NotEmpty @Size @Pattern @Setter @Indexed private String masterId
   :outertype: DataPackage

   The master id of the dataPackage. The master id must not be empty, must be of the form \ ``stu-{{dataAcquisitionProjectId}}$``\  and the master id must not contain more than 512 characters.

projectContributors
^^^^^^^^^^^^^^^^^^^

.. java:field:: @Valid @NotEmpty private List<Person> projectContributors
   :outertype: DataPackage

   List of \ :java:ref:`Person`\ s which have performed this dataPackage. Must not be empty.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: DataPackage

sponsor
^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringEntireNotEmpty private I18nString sponsor
   :outertype: DataPackage

   The name of the sponsor who which has sponsored this dataPackage. It must be specified in German and English and it must not contain more than 512 characters.

studySeries
^^^^^^^^^^^

.. java:field:: @I18nStringSize @I18nStringEntireNotEmptyOptional @I18nStringMustNotContainComma private I18nString studySeries
   :outertype: DataPackage

   The name of the series of dataPackages to which this dataPackage belongs. If specified it must be specified in German and English. It must not contain more than 512 characters and must not contain ",".

surveyDesign
^^^^^^^^^^^^

.. java:field:: @NotNull @ValidSurveyDesign private I18nString surveyDesign
   :outertype: DataPackage

   The survey design of this \ :java:ref:`DataPackage`\ . Must be one of \ :java:ref:`SurveyDesigns`\  and must not be empty.

tags
^^^^

.. java:field:: @Valid private Tags tags
   :outertype: DataPackage

   Keywords for the dataPackage.

title
^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringEntireNotEmpty private I18nString title
   :outertype: DataPackage

   The title of the dataPackage. It must be specified in German and English and it must not contain more than 2048 characters.

