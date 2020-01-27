.. java:import:: java.util List

.. java:import:: javax.validation Valid

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

.. java:import:: org.springframework.beans BeanUtils

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.index CompoundIndex

.. java:import:: org.springframework.data.mongodb.core.index CompoundIndexes

.. java:import:: org.springframework.data.mongodb.core.index Indexed

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractShadowableRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidShadowId

.. java:import:: eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain DataSet

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain DataAcquisitionProject

.. java:import:: eu.dzhw.fdz.metadatamanagement.questionmanagement.domain Question

.. java:import:: eu.dzhw.fdz.metadatamanagement.studymanagement.domain Study

.. java:import:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain Survey

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation OnlyOrdinalScaleLevelForDateDataType

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation StatisticsFirstQuartileMustBeANumberOnNumericDataType

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation StatisticsFirstQuartileMustBeAnIsoDateOnDateDataType

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation StatisticsMaximumMustBeANumberOnNumericDataType

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation StatisticsMaximumMustBeAnIsoDateOnDateDataType

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation StatisticsMedianMustBeANumberOnNumericDataType

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation StatisticsMedianMustBeAnIsoDateOnDateDataType

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation StatisticsMinimumMustBeANumberOnNumericDataType

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation StatisticsMinimumMustBeAnIsoDateOnDateDataType

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation StatisticsThirdQuartileMustBeANumberOnNumericDataType

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation StatisticsThirdQuartileMustBeAnIsoDateOnDateDataType

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation UniqueVariableNameInDataSet

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation ValidAccessWays

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation ValidDataType

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation ValidDerivedVariablesIdentifier

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation ValidPanelIdentifier

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation ValidResponseValueMustBeANumberOnNumericDataType

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation ValidResponseValueMustBeAnIsoDateOnDateDataType

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation ValidScaleLevel

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation ValidStorageType

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation ValidVariableIdName

.. java:import:: lombok AccessLevel

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok Setter

.. java:import:: lombok ToString

Variable
========

.. java:package:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain
   :noindex:

.. java:type:: @Document @CompoundIndexes @ValidShadowId @ValidVariableIdName @ValidPanelIdentifier @ValidDerivedVariablesIdentifier @UniqueVariableNameInDataSet @OnlyOrdinalScaleLevelForDateDataType @ValidResponseValueMustBeAnIsoDateOnDateDataType @StatisticsMinimumMustBeAnIsoDateOnDateDataType @StatisticsMaximumMustBeAnIsoDateOnDateDataType @StatisticsMedianMustBeAnIsoDateOnDateDataType @StatisticsFirstQuartileMustBeAnIsoDateOnDateDataType @StatisticsThirdQuartileMustBeAnIsoDateOnDateDataType @ValidResponseValueMustBeANumberOnNumericDataType @StatisticsMinimumMustBeANumberOnNumericDataType @StatisticsMaximumMustBeANumberOnNumericDataType @StatisticsMedianMustBeANumberOnNumericDataType @StatisticsFirstQuartileMustBeANumberOnNumericDataType @StatisticsThirdQuartileMustBeANumberOnNumericDataType @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Variable extends AbstractShadowableRdcDomainObject

   A variable contains the results from at least one \ :java:ref:`Survey`\ . These results can be the responses from participants of an online survey, hence a variable can result from \ :java:ref:`RelatedQuestion`\ s. A variable is part of exactly one \ :java:ref:`DataSet`\ .

Fields
------
accessWays
^^^^^^^^^^

.. java:field:: @NotEmpty @ValidAccessWays private List<String> accessWays
   :outertype: Variable

   The access way of this variable. Depends on the sensitivity of the data and describes how the data user will be able to work with the data. Must not be empty and be one of \ :java:ref:`AccessWays`\ .

annotations
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString annotations
   :outertype: Variable

   Arbitrary additional text for this variable. Must not contain more than 2048 characters.

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Indexed @NotEmpty private String dataAcquisitionProjectId
   :outertype: Variable

   The id of the \ :java:ref:`DataAcquisitionProject`\  to which this variable belongs. The dataAcquisitionProjectId must not be empty.

dataSetId
^^^^^^^^^

.. java:field:: @Indexed @NotEmpty private String dataSetId
   :outertype: Variable

   The id of the \ :java:ref:`DataSet`\  to which this variable belongs. Must not be empty.

dataSetNumber
^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer dataSetNumber
   :outertype: Variable

   The number of the \ :java:ref:`DataSet`\  to which this variable belongs. Must not be empty.

dataType
^^^^^^^^

.. java:field:: @NotNull @ValidDataType private I18nString dataType
   :outertype: Variable

   The technical type which the \ :java:ref:`ValidResponse`\ s have. Must be one of \ :java:ref:`DataTypes`\  and must not be empty.

derivedVariablesIdentifier
^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Size @Pattern private String derivedVariablesIdentifier
   :outertype: Variable

   Identifier used to group variables within this \ :java:ref:`DataSet`\  which have been derived from each other. For instance one variable might be an aggregated version of the other. Must be of the form {{dataAcquisitionProjectId}}-ds{{dataSetNumber}}-{{string}}$. Must not contain more than 512 characters and must contain only (german) alphanumeric characters and "_" and "-".

distribution
^^^^^^^^^^^^

.. java:field:: @Valid private Distribution distribution
   :outertype: Variable

   The \ :java:ref:`Distribution`\  contains the descriptives of this variable meaning \ :java:ref:`ValidResponse`\ s, \ :java:ref:`Missing`\ s and \ :java:ref:`Statistics`\ .

doNotDisplayThousandsSeparator
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Builder.Default private Boolean doNotDisplayThousandsSeparator
   :outertype: Variable

   Flag indicating whether the \ :java:ref:`ValidResponse`\ s should be displayed with a thousands separator or not. For instance years (1970) are numeric but should not be displayed with a thousands separator. Default value is false indicating that the \ :java:ref:`ValidResponse`\ s are displayed with thousands separator.

filterDetails
^^^^^^^^^^^^^

.. java:field:: @Valid private FilterDetails filterDetails
   :outertype: Variable

   \ :java:ref:`FilterDetails`\  of a variable describe the condition which must have evaluated to true before a participant was asked a \ :java:ref:`Question`\  resulting in this variable.

generationDetails
^^^^^^^^^^^^^^^^^

.. java:field:: @Valid private GenerationDetails generationDetails
   :outertype: Variable

   \ :java:ref:`GenerationDetails`\  describe how this variable was generated from one or more input variables.

id
^^

.. java:field:: @Id @Setter private String id
   :outertype: Variable

   The id of the variable which uniquely identifies the variable in this application. The id must not be empty and must be of the form var-{{dataAcquisitionProjectId}}-ds{{dataSetNumber}}-{{name}}$. The id must not contain more than 512 characters.

indexInDataSet
^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer indexInDataSet
   :outertype: Variable

   The index in the \ :java:ref:`DataSet`\  of this variable. Used for sorting the variables of this \ :java:ref:`DataSet`\  and for displaying successors and predecessors of this variable. Must not be empty and the successor of this variable must have indexInDataSet incremented by one.

label
^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringNotEmpty private I18nString label
   :outertype: Variable

   The label of the variable should describe its content. It must be specified in at least one language and it must not contain more than 512 characters.

masterId
^^^^^^^^

.. java:field:: @NotEmpty @Size @Pattern @Setter @Indexed private String masterId
   :outertype: Variable

name
^^^^

.. java:field:: @NotEmpty @Size @Pattern private String name
   :outertype: Variable

   The name of the variable as it is used in the \ :java:ref:`DataSet`\ . It must not be empty and must be unique in the \ :java:ref:`DataSet`\ . It must contain only alphanumeric (english) characters and "_". The first character must not be a number. It must not contain more than 32 characters.

panelIdentifier
^^^^^^^^^^^^^^^

.. java:field:: @Size @Pattern private String panelIdentifier
   :outertype: Variable

   Identifier used to group variables within this \ :java:ref:`DataSet`\  which measure the same across multiple waves. Must be of the form {{dataAcquisitionProjectId}}-ds{{dataSetNumber}}-{{string}}$. Must not contain more than 512 characters and must contain only (german) alphanumeric characters and "_" and "-".

relatedQuestions
^^^^^^^^^^^^^^^^

.. java:field:: @Valid private List<RelatedQuestion> relatedQuestions
   :outertype: Variable

   List of \ :java:ref:`RelatedQuestion`\ s which have been asked to generate the values of this variable.

relatedVariables
^^^^^^^^^^^^^^^^

.. java:field:: private List<String> relatedVariables
   :outertype: Variable

   List of ids of variables which are "related" to this variable. The type of relation is arbitrary.

scaleLevel
^^^^^^^^^^

.. java:field:: @NotNull @ValidScaleLevel private I18nString scaleLevel
   :outertype: Variable

   The scale level (or level of measurement) classifies the nature of information within the values assigned to this variable (\ :java:ref:`ValidResponse`\ s). It determines which mathematical operations can be performed with the values. It must be one of \ :java:ref:`ScaleLevels`\  and must not be empty. If the data type of this variable is \ :java:ref:`DataTypes.DATE`\  then the ScaleLevel must be \ :java:ref:`ScaleLevels.ORDINAL`\ .

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: Variable

storageType
^^^^^^^^^^^

.. java:field:: @NotNull @ValidStorageType private String storageType
   :outertype: Variable

   Associated with each data type is a storage type. For instance numerics can be stored as integer or double. Must be one of \ :java:ref:`StorageTypes`\  and must not be empty.

studyId
^^^^^^^

.. java:field:: @Indexed @NotEmpty private String studyId
   :outertype: Variable

   Id of the \ :java:ref:`Study`\  to which this variable belongs.

surveyIds
^^^^^^^^^

.. java:field:: @Indexed private List<String> surveyIds
   :outertype: Variable

   List of ids of \ :java:ref:`Survey`\ s which have been conducted to create this variable. Must not be empty.

surveyNumbers
^^^^^^^^^^^^^

.. java:field:: @NotEmpty private List<Integer> surveyNumbers
   :outertype: Variable

   List of numbers of \ :java:ref:`Survey`\ s which have been conducted to create this variable. Must not be empty.

