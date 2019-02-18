Übersicht über verschiedene R-Helfer-Skripte
============================================


+------------------+--------------------------+-----------------+------------------------------------+
| Name             | Was kann es?             | Wo wird es      | Was muss                           |
|                  |                          | genutzt?        | manuell gemacht                    |
|                  |                          |                 | werden?                            |
+==================+==========================+=================+====================================+
| csv2json_        | erstellt aus             | Fragenexport im | die Tabelle                        |
|                  | der                      | öffentlichen    | ``ProjID.csv``                     |
|                  | ``ProjID.csv``           | Bereich         | muss ferig                         |
|                  | Tabelle und den          |                 | aufbereitet                        |
|                  | ``FrageNr.pdf``          |                 | sein und im                        |
|                  | für jede Frage           |                 | Ordner                             |
|                  | eine json Datei          |                 | ``\questions``                     |
|                  | die json                 |                 | liegen zu jeder                    |
|                  | Dateien werden           |                 | Frage muss eine                    |
|                  | im Ordner                |                 | ``FrageNr.pdf``                    |
|                  | ``\questions\json\insNr``|                 | Datei im Ordner                    |
|                  | gespeichert              |                 | ``questions\Bilder\insNr\pdf``     |
|                  |                          |                 | vorliegen                          |
|                  |                          |                 | Alternativ kann                    |
|                  |                          |                 | auch ocr für                       |
|                  |                          |                 | Bilder genutzt                     |
|                  |                          |                 | werden, dies                       |
|                  |                          |                 | muss jedoch im                     |
|                  |                          |                 | Skript                             |
|                  |                          |                 | auskommentiert                     |
|                  |                          |                 | werden Angabe                      |
|                  |                          |                 | des                                |
|                  |                          |                 | Projektnames im                    |
|                  |                          |                 | Skript                             |
|                  |                          |                 | Ausführen (Strg                    |
|                  |                          |                 | + r). Bilder sind nicht mehr       |
|                  |                          |                 | verpflichtend.                     |
+------------------+--------------------------+-----------------+------------------------------------+
| responserateSVG_ | Erstellt für             | Responserate    | für eine                           |
|                  | alle csv                 | Diagramme für   | Survey muss                        |
|                  | Tabellen im              | den             | eine csv                           |
|                  | Ordner \\csv             | Surveyexport    | Tabelle                            |
|                  | jeweils ein              |                 | erstellt werden                    |
|                  | deutsches und            |                 | die Angaben                        |
|                  | ein englisches           |                 | ``einghauf, einghaufcum, datumw``  |
|                  | Diagramm die             |                 | müssen in der                      |
|                  | Diagramme                |                 | csv Tabelle                        |
|                  | werden als               |                 | stehen                             |
|                  | responserate_de.svg und  |                 | Ausführen (Strg                    |
|                  | responserate_en.svg      |                 | + r)                               |
|                  | im Ordner                |                 |                                    |
|                  | \\images                 |                 |                                    |
|                  | gespeichert              |                 |                                    |
+------------------+--------------------------+-----------------+------------------------------------+

Unter https://github.com/dzhw/variableMetadataExtractor findet man die Skripte
zur Generierung der Variable-JSONs.

.. _csv2json: https://github.com/dzhw/metadatamanagement-io/blob/master/generation/example/questions/Skripte/csv2json.R
.. _responserateSVG: https://github.com/dzhw/metadatamanagement-io/blob/master/generation/example/survey/Responserate/responserateSVG.R

Die Struktur der Dateipfade muss immer eingehalten werden (nach dem Beispiel
test2017), sonst funktioniert es nicht!


Response Rate Diagram
~~~~~~~~~~~~~~~~~~~~~
Das Skript
[responserateSVG](http://github.com/dzhw/metadatamanagement-io/blob/master/generation/example/survey/Responserate/responserateSVG.R)
erzeugt Responserate Diagramme als SVG in deutsch und englisch für die
verschiedenen Surveys. Benötigt wird dafür je eine csv Tabelle pro
Survey, in der folgende Spalten enthalten sind: - einghauf = Rücklauf
pro Woche - einghaufcum = Rücklauf kummuliert - datumw = Wochendatum im
Format Jahr_w_Kalenderwoche (z.B. 2006w52)
