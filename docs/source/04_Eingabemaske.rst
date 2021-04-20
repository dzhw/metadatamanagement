.. _Eingabemasken:

Eingabemasken
=================================

Jede Eingabemaske umfasst verschiedene Felder, welche mit den einzutragenden Informationen beschriftet sind. Die folgenden Aspekte sind dabei zu beachten. Details zur Eingabe der konkreten Metadaten über die Eingabemasken finden sich in dieser Anleitung in den Abschnitten zu den einzelnen Ebenen (:ref:`Datenpaket <DataPackage>`, :ref:`Erhebungen <Surveys>`, :ref:`Erhebungsinstrumente <Instruments>`). Sie können Ihre Eingaben über den orangenen Speichern-Button (Disketten-Symbol) unten rechts speichern (siehe :numref:`speichernbutton`).

.. figure:: ./_static/diskettensymbol.PNG
   :name: speichernbutton
   
   Speichern-Button

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
