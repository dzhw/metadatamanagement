.. java:import:: javax.validation Valid

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

.. java:import:: org.javers.core.metamodel.annotation Entity

.. java:import:: org.springframework.data.annotation Id

.. java:import:: org.springframework.data.mongodb.core.mapping Document

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain AbstractRdcDomainObject

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation SetHasBeenReleasedBeforeOnlyOnce

.. java:import:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation ValidSemanticVersion

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok EqualsAndHashCode

.. java:import:: lombok NoArgsConstructor

.. java:import:: lombok ToString

DataAcquisitionProject
======================

.. java:package:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain
   :noindex:

.. java:type:: @Entity @Document @SetHasBeenReleasedBeforeOnlyOnce @ValidSemanticVersion @EqualsAndHashCode @ToString @NoArgsConstructor @Data @AllArgsConstructor @Builder public class DataAcquisitionProject extends AbstractRdcDomainObject

   The Data Acquisition Project collects all data which are going to be published by our RDC.

   :author: Daniel Katzberg

Fields
------
hasBeenReleasedBefore
^^^^^^^^^^^^^^^^^^^^^

.. java:field:: @NotNull private Boolean hasBeenReleasedBefore
   :outertype: DataAcquisitionProject

id
^^

.. java:field:: @Id @NotEmpty @Pattern @Size private String id
   :outertype: DataAcquisitionProject

release
^^^^^^^

.. java:field:: @Valid private Release release
   :outertype: DataAcquisitionProject

