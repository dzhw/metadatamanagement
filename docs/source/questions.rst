.. _questions:

Fragen (questions)
==================

**Übersicht**

Zu den einzelnen Fragen eines Instruments (sprich: Fragebogen) können
Sie Informationen in das MDM übermitteln, in welchem dann für jede Frage
folgende Übersichtsseite erstellt wird:


.. figure:: ./_static/33_de.png
   :name: fragenübersicht

   Fragenübersicht im MDM am Beispiel der Frage 1.1 des Fragebogens der ersten
   Welle im Absolventenpanel 2005

Auf dieser Ebene werden Informationen über alle Fragen für jedes
einzelne Erhebungsinstrument einer Studie abgeben. Der
Einspeisungsprozess dieser Informationen hängt vom Typ des
Erhebungsinstrumentes ab. Während Daten aus Onlinebefragungen, die mit
ZOFAR, dem Datenerhebungssystem den DZHW, durchgeführt wurden, direkt
aus dem System heraus extrahiert werden (siehe **Questions (ZOFAR)**),
müssen Daten aus allen anderweitig durchgeführten Befragungen – sowohl
andere Onlinebefragungen als auch PAPI-Befragungen – manuell erfasst
werden (siehe **Questions (manuell bzw. handcrafted)**). Im Folgenden werden beide
Vorgehensweisen schrittweise beschrieben.

Fragestruktur
~~~~~~~~~~~~~

Fragen sind gekennzeichnet durch einen einleitenden/übergreifenden Fragetext,
sowie eine "natürliche" sichtbare Abgrenzung gegenüber anderer Fragen und eine
meist "erkennbare" Nummerierung. Es wird zwischen fünf Fragetypen differenziert:

- Single Choice: Auf die Frage kann nur mit einer Antwortmöglichkeit geantwortet
  werden (z.B. Einfachauswahl aus mehren Antwortmöglichkeiten oder Angabe eines
  numerischen Wertes).
- Mehrfachnennung: Für die Frage gibt es eine Auswahl an Antwortmöglichkeiten
  bei denen eine oder mehre ausgewählt werden können.
- Itembatterie: Besitzt überleitenden Fragetext, welche jeweils weitere Items
  mit den gleichen Antwortmöglichkeiten besitzen.
- Matrix: Ist ein komplexer Fragetyp in dem viele Unterfragen geschachtelt
  werden können und die nicht durch die anderen Fragetypen abgedeckt werden
  (z.B. Tableaufragen des Absolventenpanels ).
- Undocumented: Die "Restkategorie", sollte die Frage nicht mit einem der oben
  genannten Fragetypen abzubilden sein.


Die technische Dokumentation zum erstellen der Frage-Metadaten finden Sie hier:
https://dzhw.github.io/questionMetadataPreparation/index.html

Die Anleitung zur Zusammenstellung der Metadaten (inkl. Beschreibung der Attribute) finden Sie hier:
https://dzhw.github.io/questionMetadataPreparation/articles/question_metadata_preparation_introduction.html
