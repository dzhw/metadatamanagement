.. java:import:: java.util List

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

.. java:import:: org.springframework.beans BeanUtils

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.index Indexed

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidIsoLanguage

.. java:import:: eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation DataSetExists

.. java:import:: eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation InstrumentExists

.. java:import:: eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation QuestionExists

.. java:import:: eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation StudyExists

.. java:import:: eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation StudySeriesExists

.. java:import:: eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation SurveyExists

.. java:import:: eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation ValidPublicationYear

.. java:import:: eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation ValidRelatedPublicationId

.. java:import:: eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation ValidUrl

.. java:import:: eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation VariableExists

.. java:import:: io.searchbox.annotations JestId

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

RelatedPublication
==================

.. java:package:: eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain
   :noindex:

.. java:type:: @Document @ValidPublicationYear @ValidRelatedPublicationId @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class RelatedPublication extends AbstractRdcDomainObject

   Domain Object for the Related Publications.

   :author: Daniel Katzberg

Fields
------
abstractSource
^^^^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString abstractSource
   :outertype: RelatedPublication

authors
^^^^^^^

.. java:field:: @Size @NotEmpty private String authors
   :outertype: RelatedPublication

dataSetIds
^^^^^^^^^^

.. java:field:: @Indexed private List<String> dataSetIds
   :outertype: RelatedPublication

doi
^^^

.. java:field:: @Size private String doi
   :outertype: RelatedPublication

id
^^

.. java:field:: @Id @JestId @NotEmpty @Size @Pattern private String id
   :outertype: RelatedPublication

instrumentIds
^^^^^^^^^^^^^

.. java:field:: @Indexed private List<String> instrumentIds
   :outertype: RelatedPublication

language
^^^^^^^^

.. java:field:: @NotNull @ValidIsoLanguage private String language
   :outertype: RelatedPublication

publicationAbstract
^^^^^^^^^^^^^^^^^^^

.. java:field:: @Size private String publicationAbstract
   :outertype: RelatedPublication

questionIds
^^^^^^^^^^^

.. java:field:: @Indexed private List<String> questionIds
   :outertype: RelatedPublication

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: RelatedPublication

sourceLink
^^^^^^^^^^

.. java:field:: @ValidUrl private String sourceLink
   :outertype: RelatedPublication

sourceReference
^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty @Size private String sourceReference
   :outertype: RelatedPublication

studyIds
^^^^^^^^

.. java:field:: @Indexed private List<String> studyIds
   :outertype: RelatedPublication

studySerieses
^^^^^^^^^^^^^

.. java:field:: @Indexed private List<I18nString> studySerieses
   :outertype: RelatedPublication

surveyIds
^^^^^^^^^

.. java:field:: @Indexed private List<String> surveyIds
   :outertype: RelatedPublication

title
^^^^^

.. java:field:: @Size @NotEmpty private String title
   :outertype: RelatedPublication

variableIds
^^^^^^^^^^^

.. java:field:: @Indexed private List<String> variableIds
   :outertype: RelatedPublication

year
^^^^

.. java:field:: @NotNull private Integer year
   :outertype: RelatedPublication

