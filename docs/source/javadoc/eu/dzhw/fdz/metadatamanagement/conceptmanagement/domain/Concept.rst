.. java:import:: java.util List

.. java:import:: java.util Set

.. java:import:: javax.validation Valid

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

.. java:import:: org.javers.core.metamodel.annotation Entity

.. java:import:: org.springframework.beans BeanUtils

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain I18nString

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain Person

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringEntireNotEmpty

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation I18nStringSize

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidIsoLanguage

.. java:import:: eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.projections ConceptSubDocumentProjection

.. java:import:: io.searchbox.annotations JestId

.. java:import:: io.swagger.v3.oas.annotations.media Schema

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

Concept
=======

.. java:package:: eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain
   :noindex:

.. java:type:: @Entity @Document @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder @Schema public class Concept extends AbstractRdcDomainObject implements ConceptSubDocumentProjection

   A concept is something which cannot be observed directly but there is a model which helps observing the concept. E.g.: The concept "Personality" can be observed with the help of the five-factor model (Big5).

Fields
------
authors
^^^^^^^

.. java:field:: @Valid @NotEmpty private List<Person> authors
   :outertype: Concept

   List of \ :java:ref:`Person`\ s which have defined this concept. Must not be empty.

citationHint
^^^^^^^^^^^^

.. java:field:: @NotEmpty @Size private String citationHint
   :outertype: Concept

   Hint on how to cite this concept. Must not be empty and must not contain more than 2048 characters.

description
^^^^^^^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringEntireNotEmpty private I18nString description
   :outertype: Concept

   A description of the concept. It must be specified in German and English and it must not contain more than 2048 characters.

doi
^^^

.. java:field:: @Size private String doi
   :outertype: Concept

   The doi of the paper defining the concept. Must not contain more than 512 characters.

id
^^

.. java:field:: @Id @JestId @NotEmpty @Pattern @Size private String id
   :outertype: Concept

   The id of the concept which uniquely identifies the concept in this application. Must not be empty and must not contain more than 512 characters. Must start with "con-" and end with "$" and must not contain any whitespace.

license
^^^^^^^

.. java:field:: @Size private String license
   :outertype: Concept

   The license of this concept. Must not contain more than 1 MB characters.

originalLanguages
^^^^^^^^^^^^^^^^^

.. java:field:: @NotEmpty private Set<String> originalLanguages
   :outertype: Concept

   The original languages of the definition of the concept as ISO 639 code. Must not be empty.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: Concept

tags
^^^^

.. java:field:: @Valid private Tags tags
   :outertype: Concept

   Keywords for the concept. Must not be empty.

title
^^^^^

.. java:field:: @NotNull @I18nStringSize @I18nStringEntireNotEmpty private I18nString title
   :outertype: Concept

   The title of the concept. It must be specified in German and English and it must not contain more than 512 characters.

