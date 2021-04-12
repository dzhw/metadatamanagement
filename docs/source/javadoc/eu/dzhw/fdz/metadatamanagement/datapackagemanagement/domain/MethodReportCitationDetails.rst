.. java:import:: java.io Serializable

.. java:import:: java.util List

.. java:import:: javax.validation Valid

.. java:import:: javax.validation.constraints Max

.. java:import:: javax.validation.constraints Min

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Size

.. java:import:: org.javers.core.metamodel.annotation ValueObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain Person

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

MethodReportCitationDetails
===========================

.. java:package:: eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder @ValueObject public class MethodReportCitationDetails implements Serializable

   Additional details required by \ :java:ref:`DataPackageAttachmentMetadata`\ s of type "Method Report".

   :author: René Reitmann

Fields
------
authors
^^^^^^^

.. java:field:: @Valid @NotEmpty private List<Person> authors
   :outertype: MethodReportCitationDetails

   List of \ :java:ref:`Person`\ s which have authored this report. Must not be empty.

institution
^^^^^^^^^^^

.. java:field:: @NotEmpty @Size private String institution
   :outertype: MethodReportCitationDetails

   The institution which created the method report. Must not be empty and not more than 512 characters.

location
^^^^^^^^

.. java:field:: @NotEmpty @Size private String location
   :outertype: MethodReportCitationDetails

   The location of the institution which created the method report. Must not be empty and not more than 512 characters.

publicationYear
^^^^^^^^^^^^^^^

.. java:field:: @NotNull @Min @Max private Integer publicationYear
   :outertype: MethodReportCitationDetails

   The year in which the method report was published. Must not be empty and not before 1990.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: MethodReportCitationDetails

