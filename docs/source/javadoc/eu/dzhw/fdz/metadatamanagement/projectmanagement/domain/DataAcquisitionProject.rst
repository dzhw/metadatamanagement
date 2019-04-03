.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractShadowableRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation ValidShadowId

.. java:import:: eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain DataSet

.. java:import:: eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain Instrument

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation SetHasBeenReleasedBeforeOnlyOnce

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation ValidSemanticVersion

.. java:import:: eu.dzhw.fdz.metadatamanagement.questionmanagement.domain Question

.. java:import:: eu.dzhw.fdz.metadatamanagement.studymanagement.domain Study

.. java:import:: eu.dzhw.fdz.metadatamanagement.surveymanagement.domain Survey

.. java:import:: eu.dzhw.fdz.metadatamanagement.variablemanagement.domain Variable

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

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: javax.validation Valid

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

.. java:import:: java.io Serializable

DataAcquisitionProject
======================

.. java:package:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain
   :noindex:

.. java:type:: @Entity @Document @SetHasBeenReleasedBeforeOnlyOnce @ValidSemanticVersion @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder @ValidShadowId public class DataAcquisitionProject extends AbstractShadowableRdcDomainObject implements Serializable

   The data acquisition project collects the metadata for the data products which are published by our RDC. One project can contain one \ :java:ref:`Study`\ , many \ :java:ref:`Survey`\ s, many \ :java:ref:`Instrument`\ s and \ :java:ref:`Question`\ s, and many \ :java:ref:`DataSet`\ s and \ :java:ref:`Variable`\ s. A project can be currently released (visible to public users) or not. When a publisher releases a project and its version is greater than or equal to 1.0.0 then the metadata is published to \ `da|ra <https://www.da-ra.de/home/>`_\ .

Fields
------
assigneeGroup
^^^^^^^^^^^^^

.. java:field:: @NotNull private AssigneeGroup assigneeGroup
   :outertype: DataAcquisitionProject

   Determines which assignee group is able to edit data on the project.

configuration
^^^^^^^^^^^^^

.. java:field:: @Valid @NotNull @Builder.Default private Configuration configuration
   :outertype: DataAcquisitionProject

   Contains the project configuration.

hasBeenReleasedBefore
^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Boolean hasBeenReleasedBefore
   :outertype: DataAcquisitionProject

   Flag indicating whether this project has ever been released in its life. It is used to ensure that project cannot be deleted once they have been released.

id
^^

.. java:field:: @Id @NotEmpty @Setter private String id
   :outertype: DataAcquisitionProject

   The id of this project. Must not be empty

lastAssigneeGroupMessage
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @Size private String lastAssigneeGroupMessage
   :outertype: DataAcquisitionProject

   The last message provided by an assignee group user before \ :java:ref:`DataAcquisitionProject.assigneeGroup`\  value changed.

masterId
^^^^^^^^

.. java:field:: @NotEmpty @Size @Pattern @Setter private String masterId
   :outertype: DataAcquisitionProject

   The master id of this project. Must not be empty, must only contain lower cased (english) letters and numbers and must not contain more than 32 characters.

release
^^^^^^^

.. java:field:: @Valid private Release release
   :outertype: DataAcquisitionProject

   A valid \ :java:ref:`Release`\  object. Null if the project is currently not released. The version of a \ :java:ref:`Release`\  must be a syntactically correct according to semver (major.minor.patch) and must not be decreased.

serialVersionUID
^^^^^^^^^^^^^^^^

.. java:field:: private static final long serialVersionUID
   :outertype: DataAcquisitionProject

