.. java:import:: java.util List

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

.. java:import:: org.javers.core.metamodel.annotation Entity

.. java:import:: org.springframework.beans BeanUtils

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.index CompoundIndex

.. java:import:: org.springframework.data.mongodb.core.index Indexed

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractShadowableRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidIsoLanguage

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidShadowId

.. java:import:: eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain Concept

.. java:import:: eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.validation ConceptExists

.. java:import:: eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation ValidInstrumentIdPattern

.. java:import:: eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation ValidInstrumentType

.. java:import:: eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation ValidUniqueInstrumentNumber

.. java:import:: eu.dzhw.fdz.metadatamanagement.ordermanagement.domain OrderedStudy

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain DataAcquisitionProject

.. java:import:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain Survey

.. java:import:: lombok AccessLevel

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok Setter

.. java:import:: lombok ToString

Instrument
==========

.. java:package:: eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain
   :noindex:

.. java:type:: @Entity @Document @ValidInstrumentIdPattern @ValidUniqueInstrumentNumber @CompoundIndex @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder @ValidShadowId public class Instrument extends AbstractShadowableRdcDomainObject

   An instrument (e.g. a questionnaire) which was used in at least one \ :java:ref:`Survey`\ .

Fields
------
annotations
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString annotations
   :outertype: Instrument

   Arbitrary additional text for this instrument. Markdown is supported. Must not contain more than 2048 characters.

conceptIds
^^^^^^^^^^

.. java:field:: @Indexed private List<String> conceptIds
   :outertype: Instrument

   List of ids of \ :java:ref:`Concept`\ s to which are covered by this instrument.

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Indexed @NotEmpty private String dataAcquisitionProjectId
   :outertype: Instrument

   The id of the \ :java:ref:`DataAcquisitionProject`\  to which this instrument belongs. The dataAcquisitionProjectId must not be empty.

description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringNotEmpty private I18nString description
   :outertype: Instrument

   A short description of the instrument. It must be specified in at least one language and it must not contain more than 512 characters.

id
^^

.. java:field:: @Id @NotEmpty @Setter private String id
   :outertype: Instrument

   The id of the instrument which uniquely identifies the instrument in this application.

masterId
^^^^^^^^

.. java:field:: @NotEmpty @Size @Pattern @Setter @Indexed private String masterId
   :outertype: Instrument

   The instrument's master id. It must not be empty, must be of the form \ ``ins-{{dataAcquisitionProjectId}}-ins{{number}}$``\  and must not contain more than 512 characters.

number
^^^^^^

.. java:field:: @NotNull private Integer number
   :outertype: Instrument

   The number of the instrument. Must not be empty and must be unique within the \ :java:ref:`DataAcquisitionProject`\ .

originalLanguages
^^^^^^^^^^^^^^^^^

.. java:field:: private List<String> originalLanguages
   :outertype: Instrument

   The languages of the instrument during the data collection. Must be specified as ISO 639 language codes.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: Instrument

studyId
^^^^^^^

.. java:field:: @Indexed @NotEmpty private String studyId
   :outertype: Instrument

   The id of the \ :java:ref:`OrderedStudy`\  to which this instrument belongs. Must not be empty.

subtitle
^^^^^^^^

.. java:field:: @I18nStringSize private I18nString subtitle
   :outertype: Instrument

   An optional subtitle of the instrument. It must not contain more than 2048 characters.

surveyIds
^^^^^^^^^

.. java:field:: @Indexed @NotEmpty private List<String> surveyIds
   :outertype: Instrument

   List of ids of \ :java:ref:`Survey`\ s of this \ :java:ref:`DataAcquisitionProject`\ . The instrument has been used in these \ :java:ref:`Survey`\ s. Must contain at least one element.

surveyNumbers
^^^^^^^^^^^^^

.. java:field:: @NotEmpty private List<Integer> surveyNumbers
   :outertype: Instrument

   List of numbers of \ :java:ref:`Survey`\ s of this \ :java:ref:`DataAcquisitionProject`\ . The instrument has been used in these \ :java:ref:`Survey`\ s. Must contain at least one element.

title
^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringNotEmpty private I18nString title
   :outertype: Instrument

   The title of the instrument. It must be specified in at least one language and it must not contain more than 2048 characters.

type
^^^^

.. java:field:: @NotEmpty @ValidInstrumentType private String type
   :outertype: Instrument

   The type of this instrument. Must be one of \ :java:ref:`InstrumentTypes`\  and must not be empty.

