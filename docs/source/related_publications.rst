Related Publications
====================

Ute hat wegen eines bestimmten Problems ein kurzes Review gemacht. Wenn dies hier überarbeitet wird, wäre es prima, wenn das berücksichtigt werden könnte:
``"\\faust\Abt4\FDZ\7_MitarbeiterInnen\Ute\relatedPublications\Review für Anleitung.docx"``

**Arbeiten mit der Citavi-Datenbank**

Die Related Publications werden in der Citavi-Datenbank unter ``\\faust\Abt4\FDZ\6_Literatur\Literaturexport\relatedPublications\relatedPublications_cit6\relatedPublications.ctv6`` eingepflegt.
Wenn eine neue Publikation eingepflegt wird (grünes Plus-Zeichen), ist darauf zu achten, dass zunächst der richtige Dokumente-Typ gewählt wird. In der Regel wird dies ein Zeitschriftenaufsatz oder graue Literatur sein.  

**Tipp:** Eine Anleitung zum Aufnehmen der Literatur findet man unter https://www1.citavi.com/sub/manual6/de/index.html -> Citavi im Detail -> Titel aufnehmen. Oft kann man mit Hilfe des Citavi-Pickers (Browserplugin) aus Zeitschriften-Datenbanken Einträge importieren. Dies muss aber in jedem Fall überprüft werden, da die Einträge oft Fehler enthalten oder bspw. manche Felder komplett groß geschrieben sind.

Neben den für den jeweiligen Dokumententyp verpflichtenden Feldern, gibt es noch eine Reihe weiterer Felder, welche für das MDM auszufüllen sind.
Diese werden unten bei der Eingabemaske angezeigt und müssen ggfs. noch per Klick auf den Text "weitere Felder" hinzugefügt werden.
Das Feld BibTeX Key wird automatisch von Citavi ausgefüllt.

Durch die Tastenkombination `Str+Alt+t` öffnet sich die Exportfunktion um es als Exceltabelle zu exportieren. Die
Tabellenansicht öffnet sich und durch klicken auf Spalten (oben links) kann
ausgewählt werden, welche Spalten exportiert werden sollen. Aus der
Tabellenansicht kann die die Datei jetzt nach Excel als relatedPublications.xls (**wichtig:** nicht .xlsx)
exportiert werden (Datei > nach Microsoft Excel exportieren). Einige
Spaltennamen müssen noch manuell umbenannt werden:

- `BibTeXKey` -> `id`
- `Jahr ermittelt` -> `year`
- `Autoren` (Frage: `Autor, Herausgeber oder Institution`?) -> `authors`
- `Titel` -> `title`
- `Sprache -> language`
- bei einigen Feldern steht der Zusatz `(=Freitext $ZAHL)`, z. B. sourceReference (= Freitext 1), den Zusatz muss man löschen

Strenggenommen muss das Feld studyIds [Frage: ist das jetzt nicht dataPackageIds?] nicht ausgefüllt werden, da die Verknüpfung `dataPackage <-> relatedPublication` im Project Cockpit erfolgt. 
Es ist aber sinnvoll es weiter zu pflegen, sodass man es auch außerhalb des MDMs vorgehalten hat (falls etwas schiefläuft).

Nachdem die Daten exportiert wurden, sind diese im Reiter Publikationen im MDM über den Plus-Button oder Drag and Drop hochzuladen.
Im Projekt-Cockpit (`Publikationen -> Bearbeiten`) müssen anschließend alle Publikationen, die mit dem Projekt / den Projekten zusammenhängen, verknüpft werden. Die 

+----------------+----------------------------+-------------------------+
| Eigenschaft    | Ausfüllanweisung           | muss ausgefüllt werden? |
+================+============================+=========================+
| id             | von Citavi                 | ja; darf nicht geändert |
|                | erzeugter BibTex-Key       | werden                  |
+----------------+----------------------------+-------------------------+
| source\        | Quellangabe der            | ja                      |
| Reference      | Publikation                |                         |
|                | (default)                  |                         |
+----------------+----------------------------+-------------------------+
| publication\   | Zusammenfassung            | nein                    |
| Abstract       |                            |                         |
+----------------+----------------------------+-------------------------+
| doi            | doi der Publikation        | nein                    |
+----------------+----------------------------+-------------------------+
| sourceLink     | valide URL                 | nein                    |
+----------------+----------------------------+-------------------------+
| title          | Titel                      | ja                      |
+----------------+----------------------------+-------------------------+
| authors        | Autoren (Nachname1,        | ja                      |
|                | Vorname1; Nachname2,       |                         |
|                | Vorname2)                  |                         |
+----------------+----------------------------+-------------------------+
| year           | Jahr der Veröffentlichung  | ja                      |
|                | (muss kleiner oder gleich  |                         |
|                | dem aktuellen Jahr sein)   |                         |
+----------------+----------------------------+-------------------------+
| abstract\      |??                          | nein                    |
| Source.de/\    |                            |                         |
| .en            |                            |                         |
+----------------+----------------------------+-------------------------+
| dataSetIds     |                            | nein                    |
+----------------+----------------------------+-------------------------+
| instrumentIds  |                            | nein                    |
+----------------+----------------------------+-------------------------+
| surveyIds      |                            | nein                    |
+----------------+----------------------------+-------------------------+
| variableIds    |                            | nein                    |
+----------------+----------------------------+-------------------------+
| questionIds    |                            | nein                    |
+----------------+----------------------------+-------------------------+
| studySeries\   |                            | ja, falls vorhanden     |
| es.de          |                            |                         |
+----------------+----------------------------+-------------------------+
| language       | Sprache der Publikation    | ja                      |
|                | (2-Buchstaben Code         |                         |
|                | nach ISO 639-1_            |                         |
+----------------+----------------------------+-------------------------+
| annotations.de | Anmerkungen zur Pub\       | nein                    |
|                | likation auf Deutsch       |                         |
+----------------+----------------------------+-------------------------+
| annotations.en | Anmerkungen zur Pub\       | nein                    |
|                | likation auf Englisch      |                         |
+----------------+----------------------------+-------------------------+

.. _639-1: https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
