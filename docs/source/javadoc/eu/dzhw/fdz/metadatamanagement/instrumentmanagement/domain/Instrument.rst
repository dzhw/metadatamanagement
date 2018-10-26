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

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation ValidInstrumentIdPattern

.. java:import:: eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation ValidInstrumentType

.. java:import:: eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation ValidUniqueInstrumentNumber

.. java:import:: io.searchbox.annotations JestId

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

Instrument
==========

.. java:package:: eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain
   :noindex:

.. java:type:: @Entity @Document @ValidInstrumentIdPattern @ValidUniqueInstrumentNumber @CompoundIndex @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Instrument extends AbstractRdcDomainObject

   A Instrument.

   :author: Daniel Katzberg

Fields
------
annotations
^^^^^^^^^^^

.. java:field:: @I18nStringSize private I18nString annotations
   :outertype: Instrument

dataAcquisitionProjectId
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Indexed @NotEmpty private String dataAcquisitionProjectId
   :outertype: Instrument

description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringNotEmpty private I18nString description
   :outertype: Instrument

id
^^

.. java:field:: @Id @JestId @NotEmpty @Pattern @Size private String id
   :outertype: Instrument

number
^^^^^^

.. java:field:: @NotNull private Integer number
   :outertype: Instrument

studyId
^^^^^^^

.. java:field:: @Indexed @NotEmpty private String studyId
   :outertype: Instrument

subtitle
^^^^^^^^

.. java:field:: @I18nStringSize private I18nString subtitle
   :outertype: Instrument

surveyIds
^^^^^^^^^

.. java:field:: @Indexed private List<String> surveyIds
   :outertype: Instrument

surveyNumbers
^^^^^^^^^^^^^

.. java:field:: @NotEmpty private List<Integer> surveyNumbers
   :outertype: Instrument

title
^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringNotEmpty private I18nString title
   :outertype: Instrument

type
^^^^

.. java:field:: @NotEmpty @ValidInstrumentType private String type
   :outertype: Instrument

