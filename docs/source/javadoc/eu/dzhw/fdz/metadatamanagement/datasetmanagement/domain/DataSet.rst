.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractShadowableRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidShadowId

.. java:import:: eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation UniqueDatasetNumberInProject

.. java:import:: eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation UniqueSubDatasetAccessWayInDataSet

.. java:import:: eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation ValidDataSetIdName

.. java:import:: eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation ValidDataSetType

.. java:import:: eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation ValidFormat

.. java:import:: eu.dzhw.fdz.metadatamanagement.ordermanagement.domain OrderedStudy

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain DataAcquisitionProject

.. java:import:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain Survey

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain AccessWays

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain Variable

.. java:import:: io.searchbox.annotations JestId

.. java:import:: lombok AccessLevel

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok Setter

.. java:import:: lombok ToString

.. java:import:: org.javers.core.metamodel.annotation Entity

.. java:import:: org.springframework.beans BeanUtils

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.index CompoundIndex

.. java:import:: org.springframework.data.mongodb.core.index Indexed

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: javax.validation Valid

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

.. java:import:: java.util List

DataSet
=======

.. java:package:: eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain
   :noindex:

.. java:type:: @Entity @Document @ValidDataSetIdName @UniqueDatasetNumberInProject @CompoundIndex @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder @ValidShadowId public class DataSet extends AbstractShadowableRdcDomainObject

   A dataset contains \ :java:ref:`Variable`\ s. It results from at least one \ :java:ref:`Survey`\ .

Fields
------
annotations
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString annotations
   :outertype: DataSet

   Arbitrary additional text for the dataset. Must not contain more than 2048 characters.

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Indexed @NotEmpty private String dataAcquisitionProjectId
   :outertype: DataSet

   The id of the \ :java:ref:`DataAcquisitionProject`\  to which this dataset belongs. The dataAcquisitionProjectId must not be empty.

description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringNotEmpty private I18nString description
   :outertype: DataSet

   A short description of the dataset. It must be specified in at least one language and it must not contain more than 2048 characters.

format
^^^^^^

.. java:field:: @ValidFormat private I18nString format
   :outertype: DataSet

   The format of the dataset. Must be one of \ :java:ref:`Format`\ .

id
^^

.. java:field:: @Id @JestId @NotEmpty @Setter private String id
   :outertype: DataSet

   The id of the dataset which uniquely identifies the dataset in this application.

masterId
^^^^^^^^

.. java:field:: @NotEmpty @Size @Pattern @Setter private String masterId
   :outertype: DataSet

   The master id of this dataset. It must not contain more than 512 characters, must not be empty and must be of the form \ ``dat-{{dataAcquisitionProjectId}}-ds{{number}}$``\ .

number
^^^^^^

.. java:field:: @NotNull private Integer number
   :outertype: DataSet

   The number of the dataset. Must not be empty and must be unique within the \ :java:ref:`DataAcquisitionProject`\ .

studyId
^^^^^^^

.. java:field:: @Indexed @NotEmpty private String studyId
   :outertype: DataSet

   The id of the \ :java:ref:`OrderedStudy`\  to which this dataset belongs. Must not be empty.

subDataSets
^^^^^^^^^^^

.. java:field:: @Valid @NotEmpty @UniqueSubDatasetAccessWayInDataSet private List<SubDataSet> subDataSets
   :outertype: DataSet

   List of \ :java:ref:`SubDataSet`\ s (concrete accessible files) within this dataset. Must contain at least one element. There must not be more than one \ :java:ref:`SubDataSet`\  per \ :java:ref:`AccessWays`\ .

surveyIds
^^^^^^^^^

.. java:field:: @Indexed @NotEmpty private List<String> surveyIds
   :outertype: DataSet

   List of ids of \ :java:ref:`Survey`\ s of this \ :java:ref:`DataAcquisitionProject`\ . The dataset contains results from these \ :java:ref:`Survey`\ s. Must contain at least one element.

surveyNumbers
^^^^^^^^^^^^^

.. java:field:: @NotEmpty private List<Integer> surveyNumbers
   :outertype: DataSet

   List of numbers of \ :java:ref:`Survey`\ s of this \ :java:ref:`DataAcquisitionProject`\ . The dataset contains results from these \ :java:ref:`Survey`\ s. Must contain at least one element.

type
^^^^

.. java:field:: @NotNull @ValidDataSetType private I18nString type
   :outertype: DataSet

   The type of the dataset. Must be one of \ :java:ref:`DataSetTypes`\  and must not be empty.

