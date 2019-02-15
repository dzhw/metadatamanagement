Related Publications
====================

**Arbeiten mit der Citavi-Datenbank**

Für dieses Objekt wird eine Citavi-Datenabank angelegt. Diese liegt unter: `\\faust\Abt4\FDZ\Querschnittsaufgaben\Metadaten\Erzeugen\Literaturexport\relatedPublication`.

Die Citavi-Einträge lassen sich exportieren, indem man einen Eintrag in der
Literaturübersicht markiert (linke Seite) und Str+Alt+t drückt. Die
Tabellenansicht öffnet sich und durch klicken auf Spalten (oben links) kann
ausgewählt werden, welche Spalten exportiert werden sollen. Aus der
Tabellenansicht kann die die Datei jetzt nach Excel als relatedPublications.xls
exportiert werden (Datei > nach Microsoft Excel exportieren). Einige
Spaltennamen müssen evtl. später noch manuell umbenannt werden (z.B. BibTeXKey
wird zu id). Die Excel-Tabelle wird
`hier<https://github.com/dzhw/metadatamanagement-io/tree/master/references/relatedPublications>`
gepflegt.

+--------------+----------------------+-------------------------+
| Eigenschaft  | Ausfüllanweisung     | muss ausgefüllt werden? |
+==============+======================+=========================+
| id           | von Citavi           | ja                      |
|              | erzeugter BibTex-Key |                         |
+--------------+----------------------+-------------------------+
| source\      | Quellangabe der      | ja                      |
| Reference    | Publikation          |                         |
|              | (default)            |                         |
+--------------+----------------------+-------------------------+
| publication\ | Zusammenfassung      | nein                    |
| Abstract     |                      |                         |
+--------------+----------------------+-------------------------+
| doi          | doi der Publikation  | nein                    |
+--------------+----------------------+-------------------------+
| sourceLink   | valide URL           | nein                    |
+--------------+----------------------+-------------------------+
| title        | Titel                | ja                      |
+--------------+----------------------+-------------------------+
| authors      | Autoren (Nachname1,  | ja                      |
|              | Vorname1; Nachname2, |                         |
|              | Vorname2)            |                         |
+--------------+----------------------+-------------------------+
| year         | Jahr der Veröffen\   | ja                      |
|              | tlichung (muss klei\ |                         |
|              | ner oder gleich dem  |                         |
|              | aktuellen Jahr sein) |                         |
+--------------+----------------------+-------------------------+
| abstract\    |??                    | nein                    |
| Source.de/\  |                      |                         |
| .en          |                      |                         |
+--------------+----------------------+-------------------------+
| studyIds     | Studien-Ids, der zur | Wenn keine studySeries\ |
|              | Publikation gehören\ | es vorhanden -> ja      |
|              | den Studie           |                         |
+--------------+----------------------+-------------------------+
| dataSetIds   |                      | nein                    |
+--------------+----------------------+-------------------------+
| instrumentIds|                      | nein                    |
+--------------+----------------------+-------------------------+
| surveyIds    |                      | nein                    |
+--------------+----------------------+-------------------------+
| variableIds  |                      | nein                    |
+--------------+----------------------+-------------------------+
| questionIds  |                      | nein                    |
+--------------+----------------------+-------------------------+
| studySeries\ | mindestens eine      | Falls vorhanden ja      |
| es.de        | studyId oder mindes\ |                         |
|              | tens 1 studySeries   |                         |
+--------------+----------------------+-------------------------+
| language     | Sprache der Publika\ | ja                      |
|              | tion                 |                         |
|              | (2-Buchstaben Code   |                         |
|              | nach ISO 639-1_      |                         |
+--------------+----------------------+-------------------------+

.. _639-1: https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
