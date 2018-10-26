.. java:import:: java.time LocalDateTime

.. java:import:: javax.validation.constraints NotEmpty

.. java:import:: javax.validation.constraints NotNull

.. java:import:: javax.validation.constraints Pattern

.. java:import:: javax.validation.constraints Size

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.util Patterns

.. java:import:: eu.dzhw.fdz.metadatamanagement.common.domain.validation StringLengths

.. java:import:: lombok AllArgsConstructor

.. java:import:: lombok Builder

.. java:import:: lombok Data

.. java:import:: lombok NoArgsConstructor

Release
=======

.. java:package:: eu.dzhw.fdz.metadatamanagement.projectmanagement.domain
   :noindex:

.. java:type:: @NoArgsConstructor @Data @AllArgsConstructor @Builder public class Release

   The release includes all additional information for a release of a data acquisition project. It is not a regular domain object with a own id, because it is additional for the Data Aquisition Project.

   :author: Daniel Katzberg

Fields
------
date
^^^^

.. java:field:: @NotNull private LocalDateTime date
   :outertype: Release

version
^^^^^^^

.. java:field:: @NotEmpty @Size @Pattern private String version
   :outertype: Release

