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

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

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

.. java:import:: io.searchbox.annotations JestId

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

Variable
========

.. java:package:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain
   :noindex:

.. java:type:: @Document @CompoundIndexes @ValidVariableIdName @ValidPanelIdentifier @ValidDerivedVariablesIdentifier @UniqueVariableNameInDataSet @OnlyOrdinalScaleLevelForDateDataType @ValidResponseValueMustBeAnIsoDateOnDateDataType @StatisticsMinimumMustBeAnIsoDateOnDateDataType @StatisticsMaximumMustBeAnIsoDateOnDateDataType @StatisticsMedianMustBeAnIsoDateOnDateDataType @StatisticsFirstQuartileMustBeAnIsoDateOnDateDataType @StatisticsThirdQuartileMustBeAnIsoDateOnDateDataType @ValidResponseValueMustBeANumberOnNumericDataType @StatisticsMinimumMustBeANumberOnNumericDataType @StatisticsMaximumMustBeANumberOnNumericDataType @StatisticsMedianMustBeANumberOnNumericDataType @StatisticsFirstQuartileMustBeANumberOnNumericDataType @StatisticsThirdQuartileMustBeANumberOnNumericDataType @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Variable extends AbstractRdcDomainObject

   A Variable.

   :author: René Reitmann, Daniel Katzberg

Fields
------
accessWays
^^^^^^^^^^

.. java:field:: @NotEmpty @ValidAccessWays private List<String> accessWays
   :outertype: Variable

annotations
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString annotations
   :outertype: Variable

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Indexed @NotEmpty private String dataAcquisitionProjectId
   :outertype: Variable

dataSetId
^^^^^^^^^

.. java:field:: @Indexed @NotEmpty private String dataSetId
   :outertype: Variable

dataSetNumber
^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer dataSetNumber
   :outertype: Variable

dataType
^^^^^^^^

.. java:field:: @NotNull @ValidDataType private I18nString dataType
   :outertype: Variable

derivedVariablesIdentifier
^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Size @Pattern private String derivedVariablesIdentifier
   :outertype: Variable

distribution
^^^^^^^^^^^^

.. java:field:: @Valid private Distribution distribution
   :outertype: Variable

doNotDisplayThousandsSeparator
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Builder.Default private Boolean doNotDisplayThousandsSeparator
   :outertype: Variable

filterDetails
^^^^^^^^^^^^^

.. java:field:: @Valid private FilterDetails filterDetails
   :outertype: Variable

generationDetails
^^^^^^^^^^^^^^^^^

.. java:field:: @Valid private GenerationDetails generationDetails
   :outertype: Variable

id
^^

.. java:field:: @Id @JestId @NotEmpty @Size @Pattern private String id
   :outertype: Variable

indexInDataSet
^^^^^^^^^^^^^^

.. java:field:: @NotNull private Integer indexInDataSet
   :outertype: Variable

label
^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringNotEmpty private I18nString label
   :outertype: Variable

name
^^^^

.. java:field:: @NotEmpty @Size @Pattern private String name
   :outertype: Variable

panelIdentifier
^^^^^^^^^^^^^^^

.. java:field:: @Size @Pattern private String panelIdentifier
   :outertype: Variable

relatedQuestions
^^^^^^^^^^^^^^^^

.. java:field:: @Valid private List<RelatedQuestion> relatedQuestions
   :outertype: Variable

relatedVariables
^^^^^^^^^^^^^^^^

.. java:field:: private List<String> relatedVariables
   :outertype: Variable

scaleLevel
^^^^^^^^^^

.. java:field:: @NotNull @ValidScaleLevel private I18nString scaleLevel
   :outertype: Variable

storageType
^^^^^^^^^^^

.. java:field:: @NotNull @ValidStorageType private String storageType
   :outertype: Variable

studyId
^^^^^^^

.. java:field:: @Indexed @NotEmpty private String studyId
   :outertype: Variable

surveyIds
^^^^^^^^^

.. java:field:: @Indexed private List<String> surveyIds
   :outertype: Variable

surveyNumbers
^^^^^^^^^^^^^

.. java:field:: @NotEmpty private List<Integer> surveyNumbers
   :outertype: Variable

