.. java:import:: java.util Set

MetadataExportFormat
====================

.. java:package:: eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain
   :noindex:

.. java:type:: public enum MetadataExportFormat

   Formats to which our metadata can be exported. The actual mapping is either powered by da|ra's OAI-PMH service or data cite.

   :author: René Reitmann

Enum Constants
--------------
dara
^^^^

.. java:field:: public static final MetadataExportFormat dara
   :outertype: MetadataExportFormat

data_cite_json
^^^^^^^^^^^^^^

.. java:field:: public static final MetadataExportFormat data_cite_json
   :outertype: MetadataExportFormat

data_cite_xml
^^^^^^^^^^^^^

.. java:field:: public static final MetadataExportFormat data_cite_xml
   :outertype: MetadataExportFormat

mds
^^^

.. java:field:: public static final MetadataExportFormat mds
   :outertype: MetadataExportFormat

oai_dara
^^^^^^^^

.. java:field:: public static final MetadataExportFormat oai_dara
   :outertype: MetadataExportFormat

oai_dc
^^^^^^

.. java:field:: public static final MetadataExportFormat oai_dc
   :outertype: MetadataExportFormat

oai_ddi31
^^^^^^^^^

.. java:field:: public static final MetadataExportFormat oai_ddi31
   :outertype: MetadataExportFormat

oai_ddi32
^^^^^^^^^

.. java:field:: public static final MetadataExportFormat oai_ddi32
   :outertype: MetadataExportFormat

schema_org_json_ld
^^^^^^^^^^^^^^^^^^

.. java:field:: public static final MetadataExportFormat schema_org_json_ld
   :outertype: MetadataExportFormat

Fields
------
DATACITE_FORMATS
^^^^^^^^^^^^^^^^

.. java:field:: public static final Set<MetadataExportFormat> DATACITE_FORMATS
   :outertype: MetadataExportFormat

   Export formats powered by data cite.

   **See also:** :java:ref:`https://commons.datacite.org/doi.org/10.21249/dzhw:gra2005:1.0.0`

OAI_FORMATS
^^^^^^^^^^^

.. java:field:: public static final Set<MetadataExportFormat> OAI_FORMATS
   :outertype: MetadataExportFormat

   Export formats powered by da|ra's OAI-PMH service.

   **See also:** :java:ref:`https://www.da-ra.de/oaip/`

urlFormat
^^^^^^^^^

.. java:field:: public final String urlFormat
   :outertype: MetadataExportFormat

   The format as it can be used in URLs.

