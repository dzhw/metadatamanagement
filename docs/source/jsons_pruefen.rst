Prüfung der Jsons nach Umstellung der Generierungsskripte
=========================================================

Es ist mögliche, zwei jsons online miteinander zu vergleiche, z.B.
`hier <http://www.jsondiff.com/>`__ . Dazu einfach die Texte aus den zu
vergleichenden jsons kopieren und in die jeweiligen Felder einfügen.

Struktur der Jsons
------------------

Die vorgegebene Struktur der json Dateien ist hier zu finden:

-  `Link to Variable Import
   File <https://github.com/dzhw/metadatamanagement/wiki/Interfaces/#variable-import>`__
-  `Link to Question Import
   File <https://github.com/dzhw/metadatamanagement/wiki/Interfaces/#question-import>`__

Variablenprüfung
----------------

Beim Prüfen ist besonders drauf zu achten, Variablen mit:

-  verschiedenen Skalenniveaus (besonders **intervall** und
   **verhältnis**, da viele Maßzahlen)
-  verschiedenen Datentypen
-  einer oder mehreren zugeordneten Fragen

auszuwählen.

Allgemeine Prüfung
------------------

-  ist ein Attribut leer muss der Wert null sein (alternativ kann das
   Attribut auch aus dem json herausgenommen werden)

   -  richtig: "panelIdentifier": null
   -  falsch: "panelIdentifier": ""
   -  falsch: "validResponses": []

-  der Datentyp muss richtig angegeben werden (wie im Beispiel Import
   File) -> die häufigsten Datentypen sind:

   +-----------------------------------+-----------------------------------+
   | Datentyp                          | Beispiel                          |
   +===================================+===================================+
   | string                            | "storageType": "integer"          |
   +-----------------------------------+-----------------------------------+
   | I18nString                        | "scaleLevel": {"en": "interval",  |
   |                                   | "de": "intervall"}                |
   +-----------------------------------+-----------------------------------+
   | integer                           | "indexInDataSet": 642             |
   +-----------------------------------+-----------------------------------+
   | double                            | "standardDeviation": 2.7881       |
   +-----------------------------------+-----------------------------------+
   | list                              | "accessWays": ["download-cuf",    |
   |                                   | "download-suf",                   |
   |                                   | "remote-desktop-suf",             |
   |                                   | "onsite-suf"]                     |
   +-----------------------------------+-----------------------------------+
   | boolean                           | "containsAnnotations": false      |
   +-----------------------------------+-----------------------------------+
