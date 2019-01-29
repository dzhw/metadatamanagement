Datensatzreport (Lektorat)
==========================

Diese Seite ist aktuell nicht relevant, da im Moment der redaktionelle
Teil des Datensatzreports nicht verwendet wird. Eventuell wird es in
Zukunft wieder relevant und wird daher hier dokumentiert.

Diese Seite dokumentiert das geplante Aussehen des Datensatzreport und
die dafür notwendigen Änderungen bei Inhalt, Struktur und Layout sowie
den Stand der vorgenommenen Anpassungen.

1 geplanter Aufbau des DSR
--------------------------

.. _1-titelei:

Titelseite
^^^^^^^^^^

::

   1.1 Titelblatt
   1.2 Zweite Seite
   1.3 Inhaltsverzeichnis
   1.4 Verzeichnis der Variablenseiten (+ Verlinkung auf entsprechende Seite)

.. _2-redaktioneller-teil:

Redaktioneller Teil
^^^^^^^^^^^^^^^^^^^

::

   2.1 Einleitung
   2.2 Informationen zum Datensatz / Datensatzstruktur
   2.3 Variablenbenennung, Vergabe von Labels
   2.4 Codierung fehlender Werte
   2.5 Lesehilfe / Legende zu Variablendetailseiten

.. _3-variablendetailseiten:

Variablendetailseiten
^^^^^^^^^^^^^^^^^^^^^

.. _4-anhang:

Anhang
^^^^^^

::

   4.1 Tabelle Übersicht über Panelvariablen
   4.2 Tabelle Übersicht über vercodete ursprünglich offen erfragte Variablen
   4.3 Tabelle Übersicht über anonymisierte Variablen
   4.4 Tabelle Übersicht über generierte Variablen (andere Gründen als Anonymisierung)

bisherige Festlegungen
~~~~~~~~~~~~~~~~~~~~~~

-  Redaktioneller Teil und Anhang werden jeweils eigene LaTeX-Dateien,
   die in die main.tex eingebunden werden
-  Variablennamen im Anhang werden auf entsprechende Variablenseite im
   DSR verlinkt

noch offene Aspekte
~~~~~~~~~~~~~~~~~~~

-  Existenz des Anhangs ist abhängig von vorhandenen Personal- und
   Zeitressourcen; Inhalt des Anhangs muss noch endgültig festgelegt
   werden
-  enthält der Anhang irgendeine Übersicht/Text etc. zu Gewichten?
-  soll es ein Verzeichnis der eingesetzten Codierlisten geben (mit oder
   ohne Links auf die Listen)?
-  wird Titelei eigene LaTeX-Datei oder Bestandteil der main.tex

aktuelle ToDos
~~~~~~~~~~~~~~

-  Inhalt Titelblatt und zweite Seite festlegen
-  Redaktionellen Teil schreiben (2.1 bis 2.4)
-  Inhalt und Darstellung Lesehilfe (2.5) festlegen
-  Abbildungs- und Tabellenverzeichnis rausnehmen (klären mit
   LaTeX-Firma, ob wir grundlegender Typ des Dokumentes ändern, kein
   *Buch* mit linker/rechter Seite etc.)

.. _2-variablendetailseiten:

2 Variablendetailseiten
-----------------------

.. _bisherige-festlegungen-1:

bisherige Festlegungen
~~~~~~~~~~~~~~~~~~~~~~

+--------------------+---------+---------+-----------+------------+
| Skalenniveau/Block | nominal | ordinal | intervall | verhältnis |
+====================+=========+=========+===========+============+
| Variablendetails   | ja      | ja      | ja        | ja         |
+--------------------+---------+---------+-----------+------------+
| Fragedetails       | ja      | ja      | ja        | ja         |
+--------------------+---------+---------+-----------+------------+
| Maßzahlen          | wie MDM | wie MDM | wie MDM   | wie MDM    |
+--------------------+---------+---------+-----------+------------+
| Boxplot            | nein    | ja      | ja        | ja         |
+--------------------+---------+---------+-----------+------------+
| Häufigkeitstabelle | wie MDM | wie MDM | wie MDM   | wie MDM    |
+--------------------+---------+---------+-----------+------------+
| Histogramm         | nein    | nein    | nein      | ja         |
+--------------------+---------+---------+-----------+------------+


-  *Eingangsfilter* und *Generierungsregel* werden nur angezeigt, wenn
   sie eine noch zu definierende String-Länge nicht überschreiten. Bei
   längeren Strings in diesen Atrributen wird aufs MDM verlinkt
   (teilweise Darstellung im DSR oder dort nur Standardtext?)

.. _noch-offene-aspekte-1:

noch offene Aspekte
~~~~~~~~~~~~~~~~~~~

-  Block Variablendetails: Sichtbarkeitsbedingung für Attribute
   *Beschreibung*, *Panelvariablen* und *Eingangsfilter*
-  Block Variablendetails: Schreibweise der Skalenniveaus im DSR
   festlegen (Groß-oder Kleinschreibung von *intervall* und *verhältnis*
   oder *intervall skaliert*)
-  Block Fragendetails: Sichtbarkeitsbedingung für Attribute *Einleitung
   der Frage* und *Ausfüllanweisung*
-  Block Maßzahlen: entscheiden, ob *Devianz* bleibt oder raus soll
-  lange *Eingangsfilter* und *Generierungsregel*: festlegen, was im DSR
   angezeigt wird (s.o.)

.. _aktuelle-todos-1:

aktuelle ToDos
~~~~~~~~~~~~~~

-  [[Anpassungen LaTeX-Template \| Anpassungen LateX-Template DSR]], um
   Anforderungen (vgl. Tabelle) zu erfüllen
-  Sonderfall-Variablen identifizieren, bei denen abweichend von den
   Vorgaben der Tabelle bestimmte Elemente (z.B. Boxplot bei aocc12)
   nicht angezeigt werden soll. Sonderfallklassen bilden und im
   LaTeX-Template behandeln.
-  Block Fragendetails: festlegen, welcher Text erscheinen soll, wenn es
   keine zugehörige Frage gibt
-  LaTeX-Template: Fallunterscheidungen für Attribute *Eingangsfilter*
   und *Generierungsregel* bezüglich String-Länge ergänzen
-  LaTeX-Template: Tausendertrennzeichen einbauen
-  LaTeX-Template: Darstellung des Histogramms anpassen, so dass die
   Balken verbunden sind
-  Daten: ungewünschte Zeilenumbrüche entfernen

.. _3-layout-anpassungen:

3 Layout-Anpassungen
--------------------

.. _aktuelle-todos-2:

aktuelle ToDos
~~~~~~~~~~~~~~

-  festlegen, an welchen Stellen das Layout angepasst werden soll und
   rausfinden, ob das noch im Rahmen des externen LaTeX-Auftrags möglich
   ist

.. _4-verschiedenes:

4 Verschiedenes
---------------

.. _bisherige-festlegungen-2:

bisherige Festlegungen
~~~~~~~~~~~~~~~~~~~~~~

-  der Datensatzreport wird zunächst nicht übersetzt. Falls die neuen
   Kolleginnen (Übersetzerinnen) in den nächsten Wochen noch Ressourcen
   frei haben, wird dieser Aspekt neu diskutiert.

.. _noch-offene-aspekte-2:

noch offene Aspekte
~~~~~~~~~~~~~~~~~~~

-  wie verfahren wir mit Episodendatensätzen? Datensatzreport macht
   wenig Sinn, evtl. stattdessen nur ein PDF mit einer Beschreibung, was
   ein Episodendatensatz ist, wie die Struktur des konkreten Datensatzes
   aussieht (Anzahl Fälle, Anzahl Variablen etc.) und wie man die
   Episodendaten an den Personendatensatz anspielt, praktisch eine
   Miniversion des redaktionellen Teils des normalen Datensatzreports
