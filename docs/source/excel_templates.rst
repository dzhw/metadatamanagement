.. _excel-tabellen-label:

Excel-Tabellen
~~~~~~~~~~~~~~

Für die weiteren Ebenen können Sie bereits vorbereitete Excel-Tabellen
verwenden, in denen verschiedene Metadaten spaltenweise erfasst werden.
Diese Excel-Tabellen finden Sie im Vorlage-Ordner_ der jeweiligen Ebene
(vgl. Kapitel 3.4). Welche Metadaten Sie an welcher Stelle in der
Excel-Tabelle eintragen müssen, erkennen Sie an den bereits vorgegebenen
Spaltenüberschriften in der ersten Zeile der Tabelle. Dort stehen die
Bezeichnungen der jeweiligen Metadaten.

.. _Vorlage-Ordner: https://github.com/dzhw/metadatamanagement-io/wiki/Vorlagen/Vorlagen_fuer_Datengeber.zip

Die Suffixe „.de“ und „.en“ sind Teil vieler Spaltenüberschriften und
weisen auf die Sprache des einzutragenden Metadatums hin.
Ab der zweiten Zeile sind die Tabellenvorlagen leer. Dort können Sie Ihre
Inhalte entsprechend der Spaltenüberschriften eintragen. Inhaltliche
Hilfen hierfür finden Sie in den Erklärungen zu den einzelnen Ebenen.

Bitte beachten Sie außerdem:

-  Die Excel-Tabellen enthalten je nach Ebene unterschiedlich viele
   Tabellenblätter, die Sie bearbeiten müssen.

-  Die Anzahl der Spalten pro Tabellenblatt variiert, sodass ein
   seitliches Scrollen oftmals notwendig ist.

-  Es gibt Metadaten, die Sie ausfüllen *müssen*, und solche, die Sie
   ausfüllen *können*. Die konkreten Ausfüllanweisungen finden Sie in
   Kapitel 4.

   **Kontrolliertes Vokabular**

Für einige Metadaten gibt es ein sogenanntes „kontrolliertes Vokabular“,
d.h. dort können nur bestimmte Inhalte in die Spalten eingetragen
werden. In diesen Spalten wird Ihnen in jeder Zelle eine Auswahl der
möglichen Antworten anhand eines Drop-Down-Menüs angeboten und Sie
müssen diese nur auswählen.

Häufig ist es der Fall, dass aus dem kontrollierten Vokabular einer
bestimmten Zelle automatisch der Inhalt der nächsten Zelle folgt. Für
dieses Szenario sind die Excel-Vorlagen_ vorbereitet, d. h. in den
betreffenden Spalten sind über mehrere Zeilen bereits Formeln
hinterlegt, die die nächste Zelle automatisch füllen und Ihnen viel
Tipparbeit ersparen. Die Vorlagen sind für alle Fälle
vorbereitet, so dass die Formeln auch in höher nummerierten Zeilen
stehen werden, die Sie voraussichtlich nicht mehr benötigen (bis
einschließlich Zeile 20 der Excel-Vorlage für die Ebenen Instrumente
sowie Datensätze, bis einschließlich Zeile 2000 der Excel-Vorlage für
die Fragen- und Variablenebene). Zum leichteren Erkennen sind die
betreffenden Zeilen in den Vorlagen bereits grau hinterlegt.

.. _Excel-Vorlagen: https://github.com/dzhw/metadatamanagement-io/wiki/Vorlagen/Vorlagen_fuer_Datengeber.zip

Da Formeln in solchen Zellen, die nicht mit Inhalt befüllt sind,
einen fehlerhaften Upload der Excel-Tabelle hervorrufen,
müssen die überflüssigen Formeln aus den nicht benötigten Zeilen
herausgelöscht werden. Dies können Sie erledigen, indem Sie die nicht
benötigten der grau eingefärbten Zeilen bis einschließlich der Zeile 2000
(für Fragen- und Variablenebene) markieren und über das Menü per
„Blattzeilen löschen“ komplett entfernen (vgl. dazu analog zur ehemaligen
Exceltabelle für die Datensatzebene -- mittlerweile gibt es dort nur noch
Eingabemasken :numref:`nicht_benötigte_formel`).

.. figure:: ./_static/11_de.png
   :name: nicht_benötigte_formel

   Beispiel für das Löschen nicht benötigter Formeln aus der Excel-Vorlage für
   die Ebene *Datensätze*

Templates
~~~~~~~~~

In dem `Template
<https://github.com/dzhw/metadatamanagement-io/tree/master/datasetreport/template/>`_
befinden sich die relevanten Exceldateien für DatengeberInnen.
Dies ist nur noch relevant für questions/variables und für die related
publications.

Ausfüllhinweis: Die Excel-Interfaces enthalten teilweise Dropdownmenüs und
Formeln als Hilfestellung. Alle Zeilen, die grau eingefärbt sind, enthalten
diese Hilfestellungen. Der Datengeber kann einfach die Felder ausfüllen. Nach
Fertigstellung der Dateien und vor Hochladen der Excel-Interfaces in das MDM
müssen alle grauen Zeilen, die nicht genutzt werden gelöscht werden. Fertig!
Vom FDZ-Team müssen im Anschluss noch folgende Punkte erledigt werden:

+--------------------------+---------------------------------------------------+
| Felder                   | To Do                                             |
+==========================+===================================================+
| Datenaufbereitungsfelder | bei Bedarf für externe Projekte löschen (z.B.     |
|                          | varname_alt, Varlabel_alt)                        |
+--------------------------+---------------------------------------------------+
| alle Felder              | nur einblenden, wenn Datengeber die Informationen |
|                          | liefern (z.B. englische Felder,                   |
|                          | GenerationDetails, …)                             |
+--------------------------+---------------------------------------------------+
| Zugangswege              | - Spalte "accessWays" entfernen, diese wird im    |
|                          |   Nachheinein vom FDZ auf Basis der               |
|			   |   Zugangswegspalten ausgefüllt                    |
|                          |                                                   |
|			   | - entspr. Absprache zu Zugangswegen nur relevante |
|			   |   Zugangsweg-Spalten drin lassen                  |
|                          |                                                   |
|			   | - wenn nur ein Zugangsweg -> alle                 |
|            		   |   Zugangsweg-Spalten rauslassen                   |
+--------------------------+---------------------------------------------------+
| accessWays               | raus                                              |
+--------------------------+---------------------------------------------------+
