.. _Eingabemasken:

Eingabemasken
=================================

Jede Eingabemaske umfasst verschiedene Felder, welche mit den einzutragenden Informationen beschriftet sind. Die folgenden Aspekte sind dabei zu beachten. Details zur Eingabe der konkreten Metadaten über die Eingabemasken finden sich in den Abschnitten zu den einzelnen Ebenen (:ref:`Datenpaket <DataPackage>`, :ref:`Erhebungen <Surveys>`, :ref:`Erhebungsinstrumente <Instruments>`).

.. _Allgemeines:

Allgemeines
---------------------------------

Pflichtfelder
^^^^^^^^^^^^^^^^^^^^^^^^^^
Einige Felder sind verpflichtend auszufüllen und deshalb mit einem Sternchen versehen. Sie werden beim Speichern der Eingaben automatisch darauf hingewiesen, wenn noch Felder offen sind, die nicht leer bleiben dürfen.

Maximale Zeichenanzahl
^^^^^^^^^^^^^^^^^^^^^^^^^^
Einige Felder, die Sie frei ausfüllen können, verfügen über einen Zeichenzähler, der Sie darüber informiert, wie viele Zeichen dort insgesamt eintragen dürfen und wie viele Zeichen Sie bereits eingetragen haben.

Verwendung von Markdown
^^^^^^^^^^^^^^^^^^^^^^^^^^
Eingabefelder, die mit einem M↓-Symbol gekennzeichnet sind, *können* bei Bedarf mit `Markdown <https://www.markdownguide.org/basic-syntax/>`_ formatiert werden.

Drop-Down-Menüs
^^^^^^^^^^^^^^^^^^^^^^^^^^
Teilweise gibt es Drop-Down-Menüs mit festgelegten Antwortmöglichkeiten, zwischen denen Sie auswählen können (siehe :numref:`dropdownmenue`).

.. figure:: ./_static/dropdownmenü.PNG
   :name: dropdownmenue

   Drop-Down-Menü

Bitte beachten Sie, dass es sich nur um ein Drop-Down-Menü handelt, wenn Sie rechts den kleinen "Pfeil nach unten" sehen.
Bei anderen Feldern werden Ihnen bereits in anderen Projekten gemachte Eingaben ebenfalls (einem Drop-Down-Menü ähnelnd) als Auswahlmöglichkeit angeboten, Sie können aber auch selbst eigene Einträge vornehmen.

.. _Anhaenge:

Anhänge
^^^^^^^^^^^^^^^^^^^^^^^^^^
Über die Eingabemasken können auch zusätzliche Dateien als Anhänge hochgeladen werden. Zu den Anhängen zählen z.B. der Daten- und Methodenbericht auf der Datenpaketebene sowie Fragebögen auf Instrumentenebene.

Um Anhänge hochzuladen muss das jeweilige Element, also z.B. das Datenpaket, erst über die Eingabemaske angelegt und gespeichert werden. Anschließend können Sie über den Bearbeiten-Button wieder in den Bearbeitungs-Modus gehen und sehen dann unter dem Abschnitt "Materialien zu..." in der Eingabemaske einen Plus-Button. Wenn Sie diesen anklicken, öffnet sich ein neues Fenster (s. :numref:`instruments_anhang_dialog`), in dem die jeweilige Datei über den Büroklammer-Button hochgeladen werden kann und Metadaten zur Datei eingegeben werden. Anschließend lässt sich der Anhang mit dem Speichern-Button (Diskettensymbol unten rechts) speichern.

.. figure:: ./_static/instruments_anhang_dialog.png
   :name: instruments_anhang_dialog

   Instrumente Anhang

Die Anhänge sollten i. d. R. in einem der folgenden Formate vorliegen:

- *.pdf (idealerweise PDF/A)
- *.md

Eingaben speichern
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Sie können Ihre Eingaben über den orangenen Speichern-Button (Disketten-Symbol) unten rechts speichern (siehe :numref:`speichernbutton`).

.. figure:: ./_static/diskettensymbol.PNG
   :name: speichernbutton

   Speichern-Button

Ältere Versionen wiederherstellen (Historisierung)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Im Bearbeitungsmodus können Sie ältere Versionen Ihrer abgespeicherten Eingaben wiederherstellen, indem Sie den Historisierungs-Button (blauer Pfeil-Button über dem
Speichern-Button unten rechts auf der Seite) verwenden (s. :numref:`versionierung`).

.. figure:: ./_static/historization_undo.png
   :name: versionierung

   Ältere Versionen eine Datenpakets wiederherstellen

Bei einem Klick auf den Historisierungs-Button öffnet sich ein Dialog, der die verschiedenen Versionen anzeigt (s. :numref:`historisierung_dp`).
Zudem sind der Name der Person, die die entsprechende Version gespeichert hat, sowie das Änderungsdatum sichtbar. Durch Klicken auf die Version wird diese wiederhergestellt, aber nicht automatisch als aktuelle Version gespeichert. Dies müsste über einen Klick auf den Speichern-Button erfolgen. Zu beachten ist, dass :ref:`Anhänge <Anhaenge>` nicht historisiert werden.

.. figure:: ./_static/dp_historization.png
   :name: historisierung_dp

   Dialog zur Historisierung innerhalb eines Datenpakets

.. _DataPackage:

Datenpaket (Data Package)
---------------------------------

Auf der Ebene "Datenpaket" werden verschiedene bibliographische und inhaltsbeschreibende Informationen zu Ihrem Datenpaket erfasst (vgl. z.B.  https://doi.org/10.21249/DZHW:gra2005:2.0.1).

Die Eingabemaske auf Datenpaketebene besteht aus den folgenden Abschnitten:

- Details
- Institution(en)
- Datenpaketbeschreibung
- Projektmitarbeiter:innen
- Datenkuratierung
- Tags (Schlagwörter) zum Datenpaket
- Materialien zu diesem Datenpaket

Allgemeine Informationen zu den Eingabemasken finden Sie :ref:`hier <Eingabemasken>`.

.. _Surveys:

Erhebungen (Surveys)
---------------------------------

Pro Erhebung, die Ihr Datenpaket beinhaltet, wird anhand der erfassten Metadaten eine Übersichtsseite erstellt (vgl. z.B. https://metadata.fdz.dzhw.eu/de/surveys/sur-gra2005-sy1)

Die Eingabemaske auf Erhebungsebene besteht aus den folgenden Abschnitten:

- Details
- Untersuchungsgebiete
- Weitere Informationen zum Rücklauf
- Materialien zu der Erhebung

Im Folgenden wird auf die einzelnen Abschnitte genauer eingegangen. Allgemeine Informationen zu den Eingabemasken finden Sie :ref:`hier <Eingabemasken>`.

Bitte beachten Sie, dass Sie die Ordnungsnummer sich auf die Reihenfolge der Erhebungs im Erhebungsdesign bezieht. So ist die Ordnungsnummer einer dritten Panelwelle 3, auch wenn sie (aus welchen Gründen auch immer) die einzige Erhebung in einem Datenpaket darstellt. Die Sortierung mehrerer Erhebungen in einem Datenpaket orientiert sich an den Ordnungsnummern.

.. _Instruments:

Erhebungsinstrumente (Instruments)
---------------------------------

Pro Instrument, das Ihr Datenpaket beinhaltet, wird anhand der erfassten Metadaten eine Übersichtsseite erstellt (vgl. z.B. https://metadata.fdz.dzhw.eu/de/instruments/ins-gra2005-ins1)

Die Eingabemaske auf Instrumentebene besteht aus den folgenden Abschnitten:

- Details
- Ursprüngliche Sprachen
- Materialien zum Instrument

Im Folgenden wird auf die einzelnen Abschnitte genauer eingegangen. Allgemeine Informationen zu den Eingabemasken finden Sie :ref:`hier <Eingabemasken>`.

**Details**

- Beschreibung: offenen Text eintragen z.B. "Fragebogen des DZHW-Absolventenpanels 2005 - erste Welle"
- Titel (Untertitel optional): offenen Text eintragen z.B. "Zwischen Hochschule und Arbeitsmarkt" (wenn es keinen expliziten Titel gibt, kann der Titel selbst vergeben werden, z.B. "Absolventenbefragung 2005")
- Typ: Auswahl aus den vorgegebenen Kategorien im Drop-Down-Menü
- Erhebungen: Erhebungen zuordnen, bei denen das Instrument zum Einsatz gekommen ist (die Erhebungen müssen entsprechend vorher angelegt worden sein)
- Anmerkungen: offenen Text mit ergänzenden Informationen und/oder Hinweisen eintragen
- Konzepte:

**Ursprüngliche Sprachen**

Auswahl aus den vorgegebenen Kategorien im Drop-Down-Menü

**Materialien zum Instrument**

- Hier soll insbesondere das Erhebungsinstrument selbst hochgeladen werden.
- Ergänzend können weitere Materialien zur Beschreibung des Instruments abgelegt werden, beispielsweise Variablenfragebogen oder Filterführungsdiagramm.
