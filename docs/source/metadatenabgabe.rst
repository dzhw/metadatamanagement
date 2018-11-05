    .. _metadatenabgabe-label:

Metadatenabgabe (Data Provider)
===============================
.. index:: data provider, Datenaufnahme

Allgemeines
-----------

Der Datenaufnahmeprozess im FDZ des DZHW
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Das FDZ des DZHW stellt Daten quantitativer und qualitativer Erhebungen
aus dem Feld der Hoch-schul- und Wissenschaftsforschung zur Verfügung.
Ein fester Bestandteil dieser Arbeit ist zunächst der Prozess der
Datenaufnahme, welcher im FDZ des DZHW durch ein selbst entwickeltes
System, das Metadatenmanagementsystem (MDM) unterstützt wird. Das
Besondere am MDM ist, dass Informationen über die eigentlich erhobenen
Forschungsdaten, also Metadaten, erfasst werden. Für die strukturierte
Aufnahme der Metadaten sind sieben unterschiedliche Ebenen im MDM
vorgesehen: *Studie*, *Erhebungen*, *Erhebungsinstrumente*, *Fragen*,
*Datensätze*, *Variablen* und *Publikationen*.

Innerhalb der Aufnahme von Forschungsdaten wird anhand der Metadaten auf
diesen Ebenen erfasst, welcher Studie sowie Erhebung die Daten
angehören, welche Erhebungsinstrumente genutzt wurden, welche Fragen
darin gestellt wurden, welche Datensätze existieren, welche Variablen
sich darin befinden und welche Veröffentlichungen bereits mit den Daten
realisiert worden sind. Dadurch, dass alle Ebenen miteinander verknüpft
sind, wird eine umfassende Durchsuchbarkeit aller verfügbaren Daten, die
vom FDZ des DZHW über das MDM verwaltet werden, ermöglicht. Das System
ist über die Website https://metadata.fdz.dzhw.eu zu erreichen.


.. figure:: ./_static/01_de.png
   :name: mdm-ebenen

   Darstellung der verschiedenen Ebenen im MDM, Eben *Studie* aktiv

Die eigenständige Abgabe von Metadaten
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Wenn Sie Ihre Daten im FDZ des DZHW abgeben möchten, erfassen Sie die
projektbezogenen Metadaten selbst und können diese teilweise
eigenständig in das MDM hochladen.

Die Abgabe der Metadaten ist innerhalb der einzelnen Ebenen
unterschiedlich komplex, sodass die Daten für jede der sieben Ebenen
separat erfasst und hochgeladen werden müssen. Hierfür hat das FDZ des
DZHW feste Strukturen entwickelt, die im weiteren Verlauf dieser
Anleitung für jede Ebene detailliert erläutert werden. Diesen Vorgaben
ist unbedingt Folge zu leisten, damit ein erfolgreicher Upload der
einzelnen Metadaten gewährleistet werden kann.

Aktuell können einige Metadaten bereits über eine Eingabemaske im MDM
direkt eingetragen werden. Für die übrigen Ebenen müssen Excel-Tabellen
ausgefüllt werden. Zusätzlich ist auf manchen Ebenen das Erstellen von
weiteren Anhängen (vgl. Kapitel 2.5) vorgesehen. Die Excel-Tabellen und
eventuelle Anhänge müssen für jede Ebene dann entweder in einer
bestimmten Ordnerstruktur ins MDM hochgeladen werden oder zunächst dem
FDZ zur weiteren Bearbeitung geschickt werden. Tabelle 1 zeigt eine
erste Übersicht über das Vorgehen der Metadateneingabe auf jeder Ebene,
detaillierte Erläuterungen werden in den nachstehenden Kapiteln folgen.

Tabelle 1: Vorgehen bei der Metadateneingabe pro Ebene

+-----------------+-----------------+-----------------+-----------------+
| Ebene           | Metadaten       | Zusätzliche     | Metadaten       |
|                 | eingeben        | Dateien         | abgeben         |
+=================+=================+=================+=================+
| Studie          | Eingabemaske    | Anhänge (PDF)   | Eingabemaske    |
|                 |                 |                 | ausfüllen       |
+-----------------+-----------------+-----------------+-----------------+
| Erhebungen      | Eingabemaske    | Anhänge (PDF)   | Eingabemaske    |
|                 |                 |                 | ausfüllen       |
+-----------------+-----------------+-----------------+-----------------+
| Erhebungs-      | Excel-Tabelle/  | Anhänge (PDF,   | Ordner ins MDM  |
| instrumente     | Eingabemaske    | Excel)          | laden/          |
|                 |                 |                 | Eingabemaske    |
|                 |                 |                 | ausfüllen       |
+-----------------+-----------------+-----------------+-----------------+
| Fragen          | Excel-Tabelle   | Fragebilder     | Im              |
|                 | (oder Zofar)    | (.png)          | Vorlage-Ordner  |
|                 |                 |                 | speichern       |
+-----------------+-----------------+-----------------+-----------------+
| Datensätze      | Excel-Tabelle   | Anhänge (PDF)   | Ordner ins MDM  |
|                 |                 |                 | laden           |
+-----------------+-----------------+-----------------+-----------------+
| Variablen       | mind. 1         | mind. 1         | Im              |
|                 | Excel-Tabelle   | Datensatz       | Vorlage-Ordner  |
|                 |                 | (Stata)         | speichern       |
+-----------------+-----------------+-----------------+-----------------+
| Publikationen   | -               | -               | Informationen   |
|                 |                 |                 | dem FDZ         |
|                 |                 |                 | schicken        |
+-----------------+-----------------+-----------------+-----------------+

Vorbereitende Schritte
----------------------

Registrierung
~~~~~~~~~~~~~

Sie müssen sich zunächst auf https://metadata.fdz.dzhw.eu registrieren,
um die Berechtigung für das Anlegen eines Projektes sowie das Hochladen
von Metadaten zu erhalten. Dies können Sie über die Sidebar links auf
der Website erledigen:


.. figure:: ./_static/02_de.png
   :name: registrierung

   Registrierung im MDM

Da das FDZ Ihre Registrierungsanfrage zunächst bestätigen muss, kann es
etwas dauern, bis Sie freigeschaltet sind. Sie bekommen dann eine
Bestätigung per Email.

Neues Projekt anlegen
~~~~~~~~~~~~~~~~~~~~~
.. index:: data provider, Datenaufnahme, Projekt anlegen

Nach erfolgreicher Registrierung können Sie ihr Projekt im MDM anlegen,
indem Sie sich in der Sidebar links anmelden:


.. figure:: ./_static/03_de.png
   :name: anmelden

   Anmeldung im MDM

Nach erfolgreicher Anmeldung erscheint in der Sidebar das folgende Feld:

.. figure:: ./_static/04_de.png
   :name: projektverwaltung

   Bereich für Verwaltung von Projekten im MDM

Über den Plus-Button können Sie ein neues Projekt anlegen (vgl.
:numref:`neuesprojektanlegen`). Als Projektname müssen Sie eine bestimmte ID angeben,
welche das FDZ zuvor speziell für Ihr Projekt vergeben hat und Ihnen
mitteilen muss (z. B. „gra2005“ für das Absolventenpanel 2005). Das
Projekt ist dadurch im MDM angelegt, aber noch nicht automatisch
freigegeben. Solange das Projekt noch nicht freigegeben wurde, können
Sie es jederzeit über den Mülleimer-Button ganz links (vgl. :numref:`neuesprojektanlegen`)
wieder löschen.

.. figure:: ./_static/05_de.png
   :name: neuesprojektanlegen

   Ein neues Projekt im MDM anlegen

Sie können nun beginnen, Ihr Projekt mit Metadaten zu füllen. Wie genau
dies funktioniert, wird im Folgenden zunächst prinzipiell erläutert, ehe
die konkret geforderten Metadaten in den einzelnen Ebenen in Kapitel 4
detailliert beschrieben werden.

Notwendige Schnittstellen und Dateien
-------------------------------------

Grundsätzlich gibt es zwei verschiedene Möglichkeiten Metadaten
einzutragen bzw. hochzuladen: Eingabemasken und Excel-Tabellen.
Eingabemasken ermöglichen eine komfortable Abgabe der Metadaten direkt
auf der Website, während Excel-Tabellen zunächst ausgefüllt und im
Anschluss daran entweder in einer bestimmten Ordnerstruktur (vgl.
Kapitel 3.4) abgelegt und im MDM hochgeladen oder dem FDZ zur weiteren
Bearbeitung zugeschickt werden müssen.

Eingabemasken
~~~~~~~~~~~~~

Für die Ebenenen Studie, Erhebungen und Instrumente steht bereits die Nutzung
von Eingabemasken im MDM zur Verfügung. Jede Maske umfasst verschiedene
Felder, welche mit den einzutragenden Informationen beschriftet sind.
Einige Felder sind verpflichtend auszufüllen und deshalb mit einem
Sternchen versehen. Sie werden beim Speichern der Eingaben automatisch
darauf hingewiesen, wenn noch Felder offen sind, die nicht leer bleiben
dürfen. Die Bedienung der Eingabemasken ist weitgehend intuitiv und an
vielen Stellen selbsterklärend. Im Rahmen der relevanten Ebenen Studie,
Erhebungen und Instrumente wird die Handhabung der jeweiligen Eingabemasken dann
konkret gezeigt (s. Kapitel 4.1 und 4.2).

Excel-Tabellen
~~~~~~~~~~~~~~

Für die weiteren Ebenen können Sie bereits vorbereitete Excel-Tabellen
verwenden, in denen verschiedene Metadaten spaltenweise erfasst werden.
Diese Excel-Tabellen finden Sie im Vorlage-Ordner der jeweiligen Ebene
(vgl. Kapitel 3.4). Welche Metadaten Sie an welcher Stelle in der
Excel-Tabelle eintragen müssen, erkennen Sie an den bereits vorgegebenen
Spaltenüberschriften in der ersten Zeile der Tabelle. Dort stehen die
Bezeichnungen der jeweiligen Metadaten, welche auf den ersten Blick
etwas kryptisch erscheinen, innerhalb der noch folgenden Beschreibung
der einzelnen Ebenen jedoch näher erläutert werden. Beispiele für
Spaltenüberschriften in :numref:`spalten_excel_instruments`: *number* = Nummer des Instruments
(Spalte A), *surveyNumbers* = Nummern der zugehörigen Erhebungen (Spalte
B).

.. figure:: ./_static/06_de.png
   :name: spalten_excel_instruments

   Spaltenüberschriften, Excel-Vorlage für die Ebene *Erhebungsinstrumente*

Die Suffixe „.de“ und „.en“ sind Teil vieler Spaltenüberschriften und
weisen auf die Sprache des einzutragenden Metadatums hin. Beispiel in
:numref:`sprachsuffixe`: Beschreibung des Instruments auf Deutsch (Spalte C),
Beschreibung des Instruments auf Englisch (Spalte D).

.. figure:: ./_static/07_de.png
   :name: sprachsuffixe

   Sprachensuffixe, Excel-Vorlage für die Ebene *Erhebungsinstrumente*

Ab der zweiten Zeile sind die Tabellenvorlagen leer und hier nur für das
Beispiel bereits gefüllt (vgl. :numref:`beispiele_excelvorlage_instruments`). Dort können Sie Ihre
Inhalte entsprechend der Spaltenüberschriften eintragen. Inhaltliche
Hilfen hierfür finden Sie in den Erklärungen zu den einzelnen Ebenen.


.. figure:: ./_static/08_de.png
   :name: beispiele_excelvorlage_instruments

   Beispiele für den Inhalt der einzelnen Metadaten, Excel-Vorlage für die Ebene *Erhebungsinstrumente*

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
d. h. dort können nur bestimmte Inhalte in die Spalten eingetragen
werden. In diesen Spalten wird Ihnen in jeder Zelle eine Auswahl der
möglichen Antworten anhand eines Drop-Down-Menüs angeboten und Sie
müssen diese nur auswählen.

Häufig ist es der Fall, dass aus dem kontrollierten Vokabular einer
bestimmten Zelle automatisch der Inhalt der nächsten Zelle folgt. Für
dieses Szenario sind die Excel-Vorlagen vorbereitet, d. h. in den
betreffenden Spalten sind über mehrere Zeilen bereits Formeln
hinterlegt, die die nächste Zelle automatisch füllen und Ihnen viel
Tipparbeit ersparen (vgl. :numref:`formel_datensatz`). Die Vorlagen sind für alle Fälle
vorbereitet, so dass die Formeln auch in höher nummerierten Zeilen
stehen werden, die Sie voraussichtlich nicht mehr benötigen (bis
einschließlich Zeile 20 der Excel-Vorlage für die Ebenen Instrumente
sowie Datensätze, bis einschließlich Zeile 2000 der Excel-Vorlage für
die Fragen- und Variablenebene). Zum leichteren Erkennen sind die
betreffenden Zeilen in den Vorlagen bereits grau hinterlegt.


.. figure:: ./_static/09_de.png
   :name: formel_datensatz

   Beispiel für eine Formel in einer befüllten Zelle in der Excel-Vorlage für die Ebene *Datensätze*

.. figure:: ./_static/10_de.png
   :name: formel_leer

   Beispiel für eine Formel in einer leeren Zelle in der Excel-Vorlage für die Ebene *Datensätze*

Da Formeln in solchen Zellen, die nicht mit Inhalt befüllt sind (vgl.
:numref:`formel_leer`), einen fehlerhaften Upload der Excel-Tabelle hervorrufen,
müssen die überflüssigen Formeln aus den nicht benötigten Zeilen
herausgelöscht werden. Dies können Sie erledigen, indem Sie die nicht
benötigten der grau eingefärbten Zeilen bis einschließlich der Zeile 20
(für Instrumente und Datensätze) bzw. der Zeile 2000 (für Fragen- und
Variablenebene) markieren und über das Menü per „Blattzeilen löschen“
komplett entfernen (vgl. :numref:`nicht_benötigte_formel`).

.. figure:: ./_static/11_de.png
   :name: nicht_benötigte_formel

   Beispiel für das Löschen nicht benötigter Formeln aus der Excel-Vorlage für die Ebene *Datensätze*

Anhänge
~~~~~~~

Für einige Ebenen können verschiedene Anhänge entweder direkt über die
Eingabemasken oder innerhalb der festgelegten Ordnerstruktur (vgl.
Kapitel 3.4) im MDM hochgeladen werden. Zu den Anhängen zählen z. B. der
Daten- und Methodenbericht auf der Studienebene sowie Fragebögen oder
Codierlisten auf Instrumentenebene. Diese Dokumente müssen als Dateien
im PDF- oder Excel-Format vorliegen (Details dazu finden Sie innerhalb
der einzelnen Ebenen in Kapitel 4) und zudem nach bestimmten Richtlinien
benannt werden. Für die Anhänge im PDF-Format gilt es darüber hinaus zu
beachten, dass dokumenteigene Metadaten wie Autor und Titel aus der
PDF-Datei gelöscht werden. Dies können Sie im PDF-Dokument über „Datei“
-> „Eigenschaften…“ erledigen.

Für das Hochladen der Metadaten über Excel-Tabellen gilt, dass die
Anhänge im MDM in der Reihenfolge dargestellt werden, in der sie in der
Excel-Tabelle eingetragen wurden. Genaue Informationen dazu finden Sie
in den Erläuterungen für die einzelnen Ebenen.

Die korrekte Anordnung der Dateien im Ordner
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Für einen erfolgreichen Upload der Metadaten über Excel-Tabellen müssen
Sie sämtliche Dateien ihrer zugehörigen Ebene entsprechend in den vom
FDZ vorbereiteten Vorlage-Ordner ablegen, welcher nach der jeweiligen
Ebene benannt ist. Dieser Ordner sowie auch seine Unterordner sind mit
englischen Begriffen betitelt. So heißt z. B. der Ordner der
Instrumentenebene „instruments“ und der Ordner der Datensatzebene
„dataSets“. Der Unterordner, welche alle Anhänge enthält, heißt
unabhängig von der Ebene immer „attachments“. Die Reihenfolge der
Dateien im Ordner sowie in den Unterordnern spielt keine Rolle. Da meist
eine Sortierung nach alphabetischer Reihenfolge voreingestellt ist,
steht der „attachments“-Ordner an erster Stelle, während die
Excel-Tabelle erst danach folgt. Die vorliegende Ordnerstruktur am
Beispiel der Instrumentenebene zeigt :numref:`ordnerstruktur_instruments`.


.. figure:: ./_static/12_de.png
   :name: ordnerstruktur_instruments

   Ordnerstruktur am Beispiel der Instrumentenebene

Die Abgabe von Metadaten für die einzelnen Ebenen
-------------------------------------------------

Studie (study)
~~~~~~~~~~~~~~

**Übersicht**

Anhand der Informationen, die Sie bzgl. Ihrer Studie an das MDM liefern,
wird dort später eine Übersichtsseite erstellt, die im Folgenden am
Beispiel des Absolventenpanels 2005 dargestellt wird:


.. figure:: ./_static/13_de.png
   :name: studienübersicht

   Studienübersicht im MDM am Beispiel des Absolventenpanels 2005

**Eine neue Studie anlegen**

Nachdem Sie ein neues Projekt erstellt haben (vgl. Kapitel 2.2), können
Sie nun innerhalb des Projektes eine Studie anlegen. Dazu finden Sie im
Reiter „Studien“ unten rechts auf der Seite einen orangefarbenen
Plus-Button. Wenn Sie mit dem Mauszeiger über diesen Button fahren,
erscheinen links davon zwei weiße Buttons (vgl. :numref:`studien_optionen`).

.. figure:: ./_static/14_de.png
   :name: studien_optionen

   Optionen für das Anlegen einer Studie

Über den weißen Ordner-Button können Studieninformationen weiterhin
mittels Ordner, der eine Excel-Tabelle und Anhänge enthält, hochgeladen
werden, wobei diese Variante hier nicht weiter erläutert wird, da sie
von der Eingabemaske abgelöst wurde. Mit einem Klick auf den weißen
Plus-Button öffnet sich die Eingabemaske, in der Sie Ihre Informationen
zur Studie ablegen können.

**Eingabemaske**

Die Eingabemaske auf Studienebene besteht aus den vier Abschnitten
„Details“, „Studienbeschreibung“, „Projektmitarbeiter(innen)“ sowie
„Materialien zu der Studie“. Der Abschnitt „Details“ ist der
umfangreichste und wird im Folgenden aufgrund der Veranschaulichung mit
bereits eingetragenen Informationen dargestellt (hier beispielhaft: 21.
Sozialerhebung):


.. figure:: ./_static/15_de.png
   :name: studienebene_eingabemaske

   Eingabemaske auf Studienebene, Abschnitt "Details" am Beispiel der 21. Sozialerhebung

Nach dem Öffnen der Eingabemaske erscheint ganz oben die aus ihrem
Projektnamen automatisch generierte ID für die Studienseite (s. rotes
Kästchen in :numref:`studienebene_eingabemaske`). Einige Felder, die Sie frei ausfüllen können,
verfügen über einen Zeichenzähler, der Sie darüber informiert, wie viele
Zeichen Sie dort insgesamt eintragen dürfen und wie viele Zeichen Sie
bereits eingetragen haben (s. blaues Kästchen in :numref:`studienebene_eingabemaske`). Außerdem
finden Sie teilweise Drop-Down-Menüs vor, in denen Sie aus vorgegebenen
Alternativen auswählen können (s. grünes Kästchen in :numref:`studienebene_eingabemaske`).

Im zweiten Abschnitt der Eingabemaske müssen Sie eine Beschreibung Ihrer
Studie sowohl auf Deutsch als auch auf Englisch eingeben. Für ein
Beispiel ist im Folgenden die Beschreibung der 21. Sozialerhebung
abgebildet:


.. figure:: ./_static/16_de.png
   :name: eingabemaske_studienbeschreibung

   Eingabemaske auf Studienebene, Abschnitt "Studienbeschreibung" am Beispiel der 21. Sozialerhebung

Im dritten Abschnitt der Eingabemaske geben Sie die Mitarbeiter(innen)
Ihres Projekts ein. Für die Eingabe weiterer Personen klicken Sie
einfach auf den blauen Plus-Button (s. :numref:`studienebene_eingabemaske_mitarbeiter`). Wenn mindestens zwei
Personen eingetragen sind, erscheinen die Pfeil-Buttons als aktiv
(Farbwechsel von grau zu blau). Dann können Sie die Reihenfolge der
Personen ändern, indem Sie die Namen nach oben oder unten verschieben.
Links neben den bereits aufgeführten Personen erscheint in jeder Zeile
ein blauer Button mit einem Mülleimer-Symbol, mit dem Sie den jeweiligen
Namen wieder löschen können. Mit dem orangefarbenen Save-Button unten
rechts können Sie Ihre Eingaben jederzeit abspeichern. Dies müssen Sie
spätestens jetzt tun, da Sie ansonsten den letzten Abschnitt der
Eingabemaske („Materialien zu der Studie“) nicht bearbeiten können.

.. figure:: ./_static/17_de.png
   :name: studienebene_eingabemaske_mitarbeiter

   Eingabemaske auf Studienebene, Abschnitt "Projektmitarbeiter(innen)"

Im vierten und letzten Abschnitt der Eingabemaske können Sie Materialien
zur Studie ablegen. Dazu klicken Sie auf den blauen Plus-Button (s.
:numref:`eingabemaske_studie_materialien`), woraufhin sich ein Dialog öffnet,
in dem Sie eine Datei hochladen und diese näher beschreiben können.
Die hier relevanten Materialien sind momentan der deutsch- und
englischsprachige Daten- und Methodenbericht (DMB) sowie eine
englischsprachige *study overview*. [1]_ Die Eingaben müssen Sie
anschließend über den orangefarbenen Save-Button abspeichern.
Mit den Pfeil-Buttons können Sie dann ggf. die Reihenfolge bereits
eingegebener Materialien verändern. Wenn Sie eine geänderte Reihenfolge
beibehalten möchten, müssen Sie erneut speichern.

.. figure:: ./_static/18_de.png
   :name: eingabemaske_studie_materialien

   Eingabemasken auf Studienebene, Abschnitt "Materialien zu der Studie"

**Editieren und historisieren**

Falls Sie Ihre Informationen auf Studienebene nicht in einem Vorgang
eingeben und hochladen können oder möchten, ist es immer möglich, dass
Sie Ihre bisherigen Eingaben abspeichern und zu einem späteren Zeitpunkt
weiter bearbeiten. Hierfür wird Ihnen im Reiter „Studien“ am rechten
Rand neben Ihrer Studie ein Stift-Button angezeigt, über den Sie wieder
in die Eingabemaske gelangen (s. :numref:`bearbeitung_gespeicherte_studie`).

.. figure:: ./_static/19_de.png
   :name: bearbeitung_gespeicherte_studie

   Weitere Bearbeitung einer bereits abgespeicherten Studie

Ebenso können Sie ältere Versionen Ihrer abgespeicherten Eingaben
wiederherstellen, indem Sie im Bearbeitungsmodus den
Historisierungs-Button (blauer Pfeil-Button über dem Save-Button unten
rechts auf der Seite) verwenden (s. :numref:`versionierung`).


.. figure:: ./_static/20_de.png
   :name: versionierung

   Ältere Versionen einer Studie wiederherstellen

Bei einem Klick auf den Historisierungs-Button öffnet sich ein Dialog,
der die verschiedenen Versionen der Studie anzeigt (s. :numref:`historisierung_studie`). Zudem
sind der Name des Nutzers, der die entsprechende Version der Studie
gespeichert hat, sowie das Änderungsdatum sichtbar. Durch Klicken auf
die Version wird diese wiederhergestellt, aber nicht automatisch als
aktuelle Version gespeichert. Dies müsste über einen Klick auf den
Save-Button erfolgen. Zu beachten ist, dass Materialien zur Studie nicht
historisiert werden.


.. figure:: ./_static/21_de.png
   :name: historisierung_studie

   Dialog zur Historisierung innerhalb einer Studie

Erhebungen (surveys)
~~~~~~~~~~~~~~~~~~~~

**Übersicht**

Mit den Informationen über die Erhebung(en), die Sie innerhalb Ihrer
Studie durchgeführt haben, wird im MDM folgende Übersichtsseite
erstellt:

.. figure:: ./_static/22_de.png
   :name: erhebungsübersicht

   Erhebungsübersicht im MDM am Beispiel der ersten Welle (Bachelor) im Absolventenpanel 2005


**Eine neue Erhebung anlegen**

Wenn Sie eine Studie angelegt haben (vgl. Kapitel 4.1), können Sie über
den Reiter „Erhebungen“ eine neue Erhebung innerhalb Ihrer Studie
erstellen. Hierzu finden Sie unten rechts auf der Seite – ebenso wie bei
der Studie – einen orangefarbenen Plus-Button (vgl. :numref:`optionen_studie_anlegen`). Wenn
Sie mit dem Mauszeiger darüberfahren, erscheinen die beiden weißen
Buttons, von denen Sie den Plus-Button anklicken, um die Eingabemaske zu
öffnen. Bitte beachten Sie, dass Sie mehrere Erhebungen über die
Eingabemaske in der richtigen Reihenfolge eingeben müssen, da die IDs
beim Anlegen einer neuen Erhebung automatisch generiert werden und sich
später nicht mehr verändern lassen.

.. figure:: ./_static/23_de.png
   :name: optionen_studie_anlegen

   Optionen für das Anlegen einer Erhebung

**Eingabemaske**

Die Eingabemaske auf Erhebungsebene besteht aus den drei Abschnitten
„Details“, „Weitere Informationen zum Rücklauf“ sowie „Materialien zu
der Erhebung“. Im Folgenden wird der Abschnitt „Details“ – aufgrund der
Länge in zwei Teilen – dargestellt:

.. figure:: ./_static/24_de.png
   :name: eingabemaske_erhebung_details_1

   Eingabemaske der Erhebungsebene, Abschnitt "Details" Teil 1

Beim Anlegen einer Erhebung wird automatisch die ID auf Basis des
Projektnamens generiert (s. rotes Kästchen, :numref:`eingabemaske_erhebung_details_1`,
hier als Beispiel der 21. Sozialerhebung). Neben den bereits aus der Studienebene
bekannten Funktionen gibt es in dieser Eingabemaske zusätzlich eine
Kalenderfunktion (s. blaue Kästchen, :numref:`eingabemaske_erhebung_details_1`),
welche die Feldzeit des
Projekts erfasst und in :numref:`kalender_erhebung` dargestellt ist:


.. figure:: ./_static/25_de.png
   :name: kalender_erhebung

   Kalenderfunktion auf der Erhebungsebene

Im zweiten Teil der Eingabemaske für die Erhebungsebene gibt es die
Besonderheit, dass sich die Rücklaufquote automatisch ermitteln lässt
(s. :numref:`eingabemaske_erhebungsebene_details_2`). Sie können den Rücklauf auch manuell eingeben. Hierbei ist
zu jedoch beachten, dass sich bereits eingegebene Zahlen bei Brutto- und
Netto-Stichprobe bei nicht automatisch anpassen.

.. figure:: ./_static/26_de.png
   :name: eingabemaske_erhebungsebene_details_2

   Eingabemaske der Erhebungsebene, Abschnitt "Details" Teil 2

Um den nächsten Abschnitt in der Eingabemaske („Weitere Informationen
zum Rücklauf“ [2]_) bearbeiten zu können, müssen Sie die bisherigen
Eingaben abspeichern. Dann können Sie deutschsprachige und/oder
englischsprachige Grafiken zum Rücklauf entweder über den blauen
Plus-Button oder per Drag & Drop hochladen und dann mit dem Save-Button
speichern. Diese Grafiken dürfen im svg-, png- oder auch PDF-Format
vorliegen. Über den Button mit dem Mülleimer-Symbol lassen sich
hochgeladene Dateien wieder löschen (s. :numref:`weitere_infos_rücklauf`).


.. figure:: ./_static/27_de.png
   :name: weitere_infos_rücklauf

   Eingabemaske der Erhebungsebene, Abschnitt „Weitere Informationen zum Rücklauf“

Im letzten Abschnitt der Eingabemaske können – wie auch bei der Studie –
Materialien hinzugefügt werden (s. :numref:`eingabemaske_erhebung_materialien`).
Die Funktionsweise ist identisch zu der auf Studienebene. [3]_

.. figure:: ./_static/28_de.png
   :name: eingabemaske_erhebung_materialien

   Eingabemaske der Erhebungsebene, Abschnitt „Materialien zu der Erhebung“

**Editieren und historisieren**

Falls Sie Ihre Informationen auf Erhebungsebene nicht in einem Vorgang
eingeben und hochladen können oder möchten, ist es immer möglich, dass
Sie Ihre bisherigen Eingaben abspeichern und zu einem späteren Zeitpunkt
weiter bearbeiten. Hierfür wird Ihnen im Reiter „Erhebungen“ am rechten
Rand ein Stift-Button angezeigt, über den Sie wieder in die Eingabemaske
gelangen. Außerdem finden Sie dort auch einen Button mit
Mülleimer-Symbol, mit dem Sie die Erhebung komplett löschen können (s.
:numref:`bearbeitung_erhebung`).

.. figure:: ./_static/29_de.png
   :name: bearbeitung_erhebung

   Weitere Bearbeitung einer bereits abgespeicherten Erhebung

Es ist außerdem möglich, ältere Versionen der bereits gespeicherten
Eingaben wiederherzustellen. Im Bearbeitungsmodus gibt es auch auf der
Erhebungsebene einen Historisierungs-Button, den Sie rechts unten über
dem Save-Button betätigen können (s. :numref:`version_erhebung_wiederherstellen`).

.. figure:: ./_static/30_de.png
   :name: version_erhebung_wiederherstellen

   Ältere Versionen einer Erhebung wiederherstellen

Bei einem Klick auf den Historisierungs-Button öffnet sich ein Dialog,
der die verschiedenen Versionen der Erhebung anzeigt (s. :numref:`historisierungsdialog_erhebung`). Zudem
sind der Name des Nutzers, der die entsprechende Version der Studie
gespeichert hat, sowie das Änderungsdatum sichtbar. Durch Klicken auf
die Version wird diese wiederhergestellt, aber nicht automatisch als
aktuelle Version gespeichert. Dies müsste über einen Klick auf den
Save-Button erfolgen. Zu beachten ist, dass Materialien zur Erhebung
nicht historisiert werden.

.. figure:: ./_static/31_de.png
   :name: historisierungsdialog_erhebung

   Dialog zur Historisierung innerhalb einer Erhebung

Erhebungsinstrumente (instruments)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

**Übersicht**

Wenn Sie Informationen über Ihre Erhebungsinstrumente aufnehmen, wird
folgende Übersicht im MDM erstellt:

.. figure:: ./_static/32_0.png
   :name: instrumentenübersicht_fragebogen

   Instrumentenübersicht im MDM am Beispiel des Fragebogens der ersten Welle im Absolventenpanel 2005

**Eingabemaske**

Erhebungsinstrumente lassen sich per Eingabemaske erfassen und editieren.
Dafür darf die Studie aktuell nicht released sein.
Um ein Erhebungsinstrument mittels Eingabemaske anzulegen muss man sich im Datenaufbereitungsprojekt im
Instrumentereiter befinden. Anschließend wird der Plusbutton gedrückt und es öffnet sich
der Dialog um ein neues Instrument anzulegen.

.. figure:: ./_static/add_instrument_de.png
   :scale: 50 %
   :name: instruments_plusbutton

   Plusbutton


.. figure:: ./_static/add_instrument_manually_de.png
   :scale: 50 %
   :name: instruments_manuell_anlegen

   Manuelles Anlegen des Instruments.


Die Eingabemaske besteht
aus den Pflichtfeldern Beschreibung, Titel, Typ und Erhebung, sowie
den nicht verpflichtenden Feldern Untertitel und Anmerkungen.

Des weiteren können weitere Materialien zum Instrument
hochgeladen werden. Um weitere Materialien hochzuladen muss zunächst das Instrument abgespeichert sein.
Im Anschluss muss der Plusbutton gedrückt werden, woraufhin sich ein Dialog öffnet (s.
:numref:`instruments_anhang_dialog`), in welchem der Anhang hochgeladen werden kann und Metadaten zur Datei
eingegeben werden können. Um die Datei hochzuladen wird auf den Büroklammer-Button gedrückt und es öffnet sich ein
weiterer Dialog. Alle Felder dieses Dialogs sind verpflichtend. Anschließend lässt sich der Anhang mit dem
Speichern-Button (Diskettensymbol unten rechts) speichern.


.. figure:: ./_static/instruments_anhang_dialog.png
   :name: instruments_anhang_dialog

   Instrumente Anhang

**Excel-Tabelle**

Sie können für jede Ihrer Erhebungen alle zugehörigen Instrumente inkl.
Anhänge innerhalb einer Tabelle eintragen und hochladen. Dazu müssen Sie
die Excel-Datei *instruments.xlsx* ausfüllen, welche aus zwei
Tabellenblättern besteht: *instruments* und *attachments*. Nachstehend
finden Sie die Erläuterungen zur Tabelle:

Tabelle 2: Ausfüllanweisungen für die Excel-Tabelle "instruments"

+-----------------------+-----------------------+-----------------------+
| **Tabellenblatt 1:                                                    |
| instruments**                                                         |
+=======================+=======================+=======================+
| Es können mehrere                                                     |
| Instrumente                                                           |
| eingetragen werden (=                                                 |
| mehrere Zeilen                                                        |
| möglich, ein                                                          |
| Instrument pro Zeile)                                                 |
+-----------------------+-----------------------+-----------------------+
| **Spaltenüberschrift**| **Muss ich das        | **Was muss ich        |
|                       | ausfüllen?**          | eintragen?**          |
+-----------------------+-----------------------+-----------------------+
| number                | Ja                    | Nummer des            |
|                       |                       | Instrumentes          |
|                       |                       | (fortlaufend)         |
+-----------------------+-----------------------+-----------------------+
| surveyNumbers         | Ja                    | Nummer der            |
|                       |                       | zugehörigen           |
|                       |                       | Erhebungen            |
+-----------------------+-----------------------+-----------------------+
| description.de/en     | Ja                    | Beschreibung des      |
|                       |                       | Instrumentes          |
+-----------------------+-----------------------+-----------------------+
| title.de/en           | Ja                    | Titel des Instruments |
+-----------------------+-----------------------+-----------------------+
| subtitle.de/en        | Nein                  | Untertitel des        |
|                       |                       | Instruments           |
+-----------------------+-----------------------+-----------------------+
| type                  | Ja                    | Instrumenttyp:        |
|                       |                       |                       |
|                       |                       | „PAPI“ (paper and     |
|                       |                       | pencil interview),    |
|                       |                       |                       |
|                       |                       | „CAPI“                |
|                       |                       | (computer-assisted    |
|                       |                       | personal interview),  |
|                       |                       |                       |
|                       |                       | „CATI“                |
|                       |                       | (computer-assisted    |
|                       |                       | telephone interview), |
|                       |                       |                       |
|                       |                       | „CAWI”                |
|                       |                       | (computer-assisted    |
|                       |                       | web interview)        |
+-----------------------+-----------------------+-----------------------+
| annotations.de/en     | Nein                  | Anmerkungen zum       |
|                       |                       | Instrument            |
+-----------------------+-----------------------+-----------------------+

+-----------------------+-----------------------+-----------------------+
| **Tabellenblatt 2:                                                    |
| attachments**                                                         |
+=======================+=======================+=======================+
| Es können mehrere                                                     |
| Anhänge eingetragen                                                   |
| werden (= mehrere                                                     |
| Zeilen möglich, ein                                                   |
| Anhang pro Zeile)                                                     |
+-----------------------+-----------------------+-----------------------+
| **Spaltenüberschrift**| **Muss ich das        | **Was muss ich        |
|                       | ausfüllen?**          | eintragen?**          |
+-----------------------+-----------------------+-----------------------+
| filename              | Ja                    | Name der Datei im     |
|                       |                       | attachments-Ordner    |
|                       |                       | (z. B.                |
|                       |                       | „gra2005_W1_Questionn |
|                       |                       | aire_de.pdf“)         |
+-----------------------+-----------------------+-----------------------+
| type.de/en            | Ja                    | de: „Fragebogen“,     |
|                       |                       | „Filterführungsdiagra |
|                       |                       | mm“,                  |
|                       |                       | „Variablenfragebogen“ |
|                       |                       | ,                     |
|                       |                       | „Sonstige“;           |
|                       |                       |                       |
|                       |                       | en: „Questionnaire“,  |
|                       |                       | „Question Flow“,      |
|                       |                       | „Variable             |
|                       |                       | Questionnaire“,       |
|                       |                       | „Other“               |
+-----------------------+-----------------------+-----------------------+
| description.de/en     | Ja                    | Beschreibung des      |
|                       |                       | Dokuments             |
+-----------------------+-----------------------+-----------------------+
| language              | Ja                    | Sprache des Anhangs   |
|                       |                       |                       |
|                       |                       | *Bitte verwenden Sie  |
|                       |                       | eine Abkürzung nach   |
|                       |                       | ISO 639-1*:           |
|                       |                       |                       |
|                       |                       | z. B. „de“, „en“      |
+-----------------------+-----------------------+-----------------------+
| instrumentNumber      | Ja                    | Nummer des dem Anhang |
|                       |                       | zugehörigen           |
|                       |                       | Instruments           |
+-----------------------+-----------------------+-----------------------+

Zu den möglichen Anhängen zählen z. B. Fragebögen, Variablenfragebögen
sowie Filterführungsdiagramme [4]_. Diese müssen als PDF-Dateien
vorliegen. [5]_ Außerdem können an dieser Stelle Codierlisten, welche
als Excel-Tabelle vorliegen müssen, erfasst werden. Alle Anhänge werden
im Ordner *attachments* abgelegt.

Für den Upload der Dateien ins MDM muss die Ordnerstruktur wie in :numref:`ordnerstruktur_instruments_2` vorliegen.

.. figure:: ./_static/32_1_de.png
   :name: ordnerstruktur_instruments_2

   Ordnerstruktur: Inhalt des Ordners instruments

Hochladen können Sie den Ordner im Reiter Instrumente entweder über den
orangefarbenen Plus-Button (unten rechts) oder per Drag & Drop.

Fragen (questions) [6]_
~~~~~~~~~~~~~~~~~~~~~~~

**Übersicht**

Zu den einzelnen Fragen eines Instruments (sprich: Fragebogen) können
Sie Informationen in das MDM übermitteln, in welchem dann für jede Frage
folgende Übersichtsseite erstellt wird:


.. figure:: ./_static/33_de.png
   :name: fragenübersicht

   Fragenübersicht im MDM am Beispiel der Frage 1.1 des Fragebogens der ersten Welle im Absolventenpanel 2005

Auf dieser Ebene werden Informationen über alle Fragen für jedes
einzelne Erhebungsinstrument einer Studie abgeben. Der
Einspeisungsprozess dieser Informationen hängt vom Typ des
Erhebungsinstrumentes ab. Während Daten aus Onlinebefragungen, die mit
ZOFAR, dem Datenerhebungssystem den DZHW, durchgeführt wurden, direkt
aus dem System heraus extrahiert werden (siehe **Questions (ZOFAR)**),
müssen Daten aus allen anderweitig durchgeführten Befragungen – sowohl
andere Onlinebefragungen als auch PAPI-Befragungen – manuell erfasst
werden (siehe **Questions (manuell)**). Im Folgenden werden beide
Vorgehensweisen schrittweise beschrieben.

Questions (manuell)
~~~~~~~~~~~~~~~~~~~

**Excel-Tabelle**

Um Metadaten auf der Fragenebene in manueller Weise zu erfassen, müssen
Sie die Excel-Datei *questions.xlsx* ausfüllen, welche die beiden
Tabellenblätter *questions* und *images* beinhaltet. Sie können alle
Fragen aus allen Erhebungsinstrumenten in einer einzigen Exceltabelle
erfassen:

Tabelle 3: Ausfüllanweisungen für die Excel-Tabelle "questions"

+-----------------------+-----------------------+-----------------------+
| **Tabellenblatt 1:                                                    |
| questions**                                                           |
+=======================+=======================+=======================+
| Es können mehrere                                                     |
| Fragen eingetragen                                                    |
| werden (= mehrere                                                     |
| Zeilen möglich, eine                                                  |
| Frage pro Zeile)                                                      |
+-----------------------+-----------------------+-----------------------+
| **Spaltenüberschrift**| **Muss ich das        | **Was muss ich        |
|                       | ausfüllen?**          | eintragen?**          |
+-----------------------+-----------------------+-----------------------+
| indexInInstrument     | Ja                    | Nummer der Frage im   |
|                       |                       | Fragebogen, nach der  |
|                       |                       | die Reihenfolge       |
|                       |                       | festgelegt wird       |
|                       |                       | (ganzzahlig)          |
+-----------------------+-----------------------+-----------------------+
| questionNumber        | Ja                    | Fragenummer,          |
|                       |                       | idealerweise          |
|                       |                       | selbsterklärend aus   |
|                       |                       | Instrument (z. B.     |
|                       |                       | 1.1)                  |
+-----------------------+-----------------------+-----------------------+
| instrumentNumber      | Ja                    | Nummer des            |
|                       |                       | Instruments           |
+-----------------------+-----------------------+-----------------------+
| questionsText.de/en   | Ja                    | „Übergreifender“      |
|                       |                       | Fragetext, bei        |
|                       |                       | Itembatterien oder    |
|                       |                       | komplexen Fragen der  |
|                       |                       | einleitende           |
|                       |                       | Fragetext. Bei        |
|                       |                       | „einfachen“           |
|                       |                       | Fragetypen der        |
|                       |                       | komplette Fragetext.  |
+-----------------------+-----------------------+-----------------------+
| instruction.de/en     | Nein                  | wenn vorhanden,       |
|                       |                       | Anweisungstext der    |
|                       |                       | Frage                 |
+-----------------------+-----------------------+-----------------------+
| introduction.de/en    | Nein                  | wenn vorhanden,       |
|                       |                       | Einleitungstext der   |
|                       |                       | Frage                 |
+-----------------------+-----------------------+-----------------------+
| type.de/en            | Ja                    | de: „Einfachnennung“, |
|                       |                       | „Offen“,              |
|                       |                       | „Mehrfachnennung“,    |
|                       |                       | „Itembatterie“ oder   |
|                       |                       | „Matrix“ (eine        |
|                       |                       | Anleitung zur         |
|                       |                       | Einteilung der        |
|                       |                       | verschiedenen         |
|                       |                       | Fragetypen kann unter |
|                       |                       | https://github.com/dz |
|                       |                       | hw/metadatamanagement |
|                       |                       | /files/1421895/Anleit |
|                       |                       | ung_Vergabe_Fragetype |
|                       |                       | n.docx                |
|                       |                       | gefunden werden)      |
|                       |                       |                       |
|                       |                       | en: „Single Choice“,  |
|                       |                       | „Open“, „Multiple     |
|                       |                       | Choice“, „Item Set“   |
|                       |                       | or „Grid“.            |
+-----------------------+-----------------------+-----------------------+
| topic.de/en           | Nein                  | Themenblock, in dem   |
|                       |                       | die Frage im          |
|                       |                       | Instrument            |
|                       |                       | eingeordnet ist       |
|                       |                       | (idealerweise direkt  |
|                       |                       | aus Instrument        |
|                       |                       | entnehmbar)           |
+-----------------------+-----------------------+-----------------------+
| successorNumbers      | Nein                  | Fragenummern der      |
|                       |                       | nachfolgenden         |
|                       |                       | Frage(n) (Angabe in   |
|                       |                       | einer Zeile durch     |
|                       |                       | Komma getrennt)       |
+-----------------------+-----------------------+-----------------------+
| technicalRepresentati | x\*                   | Herkunft des          |
| on.type               |                       | Codeschnipsels (z. B. |
|                       |                       | „ZOFAR-Question       |
|                       |                       | Markup Language“)     |
+-----------------------+-----------------------+-----------------------+
| technicalRepresentati | x\*                   | Technische Sprache    |
| on.language           |                       | des Codeschnipsels    |
|                       |                       | (z. B. XML)           |
+-----------------------+-----------------------+-----------------------+
| technicalRepresentati | x\*                   | Codeschnipsel, um     |
| on.source             |                       | Frage technisch       |
|                       |                       | abbilden zu können    |
|                       |                       | (z. B. QML-Schnipsel) |
+-----------------------+-----------------------+-----------------------+
| additionalQuestionTex | Nein                  | Weitere Ausführungen  |
| t.de/.en              |                       | der Frage, die nicht  |
|                       |                       | im Fragetext stehen,  |
|                       |                       | wie z. B. der         |
|                       |                       | Itemtext (bei         |
|                       |                       | Itembatterien) oder   |
|                       |                       | Antworttext (bei      |
|                       |                       | Mehrfachnennungen).   |
|                       |                       | Aktuell ist diese     |
|                       |                       | Information für den   |
|                       |                       | Nutzenden des MDM     |
|                       |                       | nicht sichtbar,       |
|                       |                       | sondern wird nur bei  |
|                       |                       | einer Volltextsuche   |
|                       |                       | berücksichtigt.       |
+-----------------------+-----------------------+-----------------------+
| annotations.de/en     | Nein                  | Anmerkungen zur Frage |
+-----------------------+-----------------------+-----------------------+

x\* = nur, wenn technicalRepresentation vorhanden (wird dann automatisch
von ZOFAR geliefert)

+-----------------------+-----------------------+-----------------------+
| **Tabellenblatt 2:                                                    |
| images**                                                              |
+=======================+=======================+=======================+
| Es können mehrere                                                     |
| Bilder eingetragen                                                    |
| werden (= mehrere                                                     |
| Zeilen möglich, ein                                                   |
| Bild pro Zeile)                                                       |
+-----------------------+-----------------------+-----------------------+
| **Spaltenüberschrift**| **Muss ich das        | **Was muss ich        |
|                       | ausfüllen?**          | eintragen?**          |
+-----------------------+-----------------------+-----------------------+
| fileName              | Ja                    | Dateiname des Bildes  |
|                       |                       | (z.B. „1.1_1.png“)    |
+-----------------------+-----------------------+-----------------------+
| questionNumber        | Ja                    | Dem Bild zugeordnete  |
|                       |                       | Fragenummer           |
+-----------------------+-----------------------+-----------------------+
| instrumentNumber      | Ja                    | Nummer des zum Bild   |
|                       |                       | gehörenden            |
|                       |                       | Instruments           |
+-----------------------+-----------------------+-----------------------+
| language              | Ja                    | Sprache des Bildes    |
|                       |                       |                       |
|                       |                       | *Bitte verwenden Sie  |
|                       |                       | eine Abkürzung nach   |
|                       |                       | ISO 639-1*:           |
|                       |                       | z. B. „de“, „en“      |
+-----------------------+-----------------------+-----------------------+
| indexInQuestion       | Ja                    | Auf das wievielte     |
|                       |                       | Bild der Frage        |
|                       |                       | bezieht sich die      |
|                       |                       | Zeile? (Liegt pro     |
|                       |                       | Frage nur ein Bild    |
|                       |                       | vor, steht hier immer |
|                       |                       | 1)                    |
+-----------------------+-----------------------+-----------------------+

Mit dem zweiten Tabellenblatt *images* erfassen Sie Informationen zu den
Fragebildern, welche Sie für jede Frage mit hochladen müssen. Zu jeder
Frage muss mindestens ein Bild (es können auch mehrere sein) im
png-Format vorhanden sein. Die Fragebilder können z. B. mit Ragtime
extrahiert werden, sofern der Fragebogen auch mit Ragtime erstellt
wurde. Ansonsten lassen sich die Fragebilder auch aus einer PDF-Datei
erstellen. [7]_ Anleitung für beiden Varianten finden Sie unter
https://github.com/dzhw/metadatamanagement-io/wiki/Bilderfassung-aus-RagTime
und
https://github.com/dzhw/metadatamanagement-io/wiki/Bilderfassung-aus-pdf.

Die fertig ausgefüllte Excel-Datei sowie die Bilder zu den Fragen
speichern Sie dann in dem Ordner, den das FDZ für Sie vorbereitet hat.
Das FDZ greift daraufhin auf die Dateien zu, verarbeitet sie weiter und
lädt die Metadaten für die Fragenebene dann selbst ins MDM.

Questions (Zofar)
~~~~~~~~~~~~~~~~~

Bei Onlinebefragungen mit Zofar können die Metadaten für Fragen
automatisch extrahiert werden (.jsons + .pngs).

Der Prozess befindet sich gerade im Aufbau...

Datensätze (dataSets)
~~~~~~~~~~~~~~~~~~~~~

**Übersicht**

Mit den Informationen über die Datensätze, welche Sie aus den Daten
Ihrer Studie erstellt haben, wird für jeden dieser Datensätze folgende
Übersicht im MDM angezeigt:


.. figure:: ./_static/34_0.png
   :name: datensatzübersicht

   Datensatzübersicht im MDM am Beispiel des Personendatensatzes (Bachelor) im Absolventenpanel 2005

**Excel-Tabelle**

Auf der Datensatzebene werden alle Datensätze und Subdatensätze [8]_,
die einer Studie zugeordnet sind, erfasst. Ihre Informationen zu den
Datensätzen können Sie in die Excel-Tabelle *dataSets.xlsx*, welche aus
drei Tabellenblättern (*dataSets*, *subDataSets* und *attachments*)
besteht, eintragen.

Tabelle 4: Ausfüllanweisungen für die Excel-Tabelle "dataSets"

+-----------------------+-----------------------+-----------------------+
| **Tabellenblatt 1:                                                    |
| dataSets**                                                            |
+=======================+=======================+=======================+
| Es können mehrere                                                     |
| Datensätze                                                            |
| eingetragen werden (=                                                 |
| mehrere Zeilen                                                        |
| möglich, ein                                                          |
| Datensatz pro Zeile)                                                  |
+-----------------------+-----------------------+-----------------------+
| **Spaltenüberschrift**| **Muss ich das        | **Was muss ich        |
|                       | ausfüllen?**          | eintragen?**          |
+-----------------------+-----------------------+-----------------------+
| number                | Ja                    | Nummer des            |
|                       |                       | Datensatzes (laufende |
|                       |                       | Nummer, eindeutig im  |
|                       |                       | Datenaufbereitungspro |
|                       |                       | jekt)                 |
+-----------------------+-----------------------+-----------------------+
| description.de/en     | Nein                  | Beschreibung des      |
|                       |                       | Datensatzes           |
+-----------------------+-----------------------+-----------------------+
| type.de/en            | Ja                    | de:                   |
|                       |                       | „Personendatensatz“,  |
|                       |                       | „Episodendatensatz“;  |
|                       |                       |                       |
|                       |                       | en: „Individual       |
|                       |                       | Data“, „Spell Data“   |
+-----------------------+-----------------------+-----------------------+
| format.de/en          | Nein                  | de: „breit“, „lang“;  |
|                       |                       | en: „wide“, „long“    |
+-----------------------+-----------------------+-----------------------+
| surveyNumbers         | Ja                    | Nummern der zum       |
|                       |                       | Datensatz gehörenden  |
|                       |                       | Erhebungen            |
+-----------------------+-----------------------+-----------------------+
| annotations.de/en     | Nein                  | Anmerkungen zum       |
|                       |                       | Datensatz             |
+-----------------------+-----------------------+-----------------------+

+-----------------------+-----------------------+-----------------------+
| **Tabellenblatt 2:                                                    |
| subDataSets**                                                         |
+=======================+=======================+=======================+
| Es können mehrere                                                     |
| Subdatensätze                                                         |
| eingetragen werden (=                                                 |
| mehrere Zeilen                                                        |
| möglich, ein                                                          |
| Subdatensatz pro                                                      |
| Zeile)                                                                |
+-----------------------+-----------------------+-----------------------+
| **Spaltenüberschrift**| **Muss ich das        | **Was muss ich        |
|                       | ausfüllen?**          | eintragen?**          |
+-----------------------+-----------------------+-----------------------+
| name                  | Ja                    | Dateiname des         |
|                       |                       | „physikalischen“      |
|                       |                       | Datensatzes ohne      |
|                       |                       | Dateikürzel (z. B.    |
|                       |                       | „gra2005_tS_p_c_1-0-0 |
|                       |                       | “)                    |
+-----------------------+-----------------------+-----------------------+
| numberOfObservations  | Ja                    | Anzahl der            |
|                       |                       | Beobachtungseinheiten |
|                       |                       | in einem Datensatz    |
|                       |                       | (z. B. Anzahl der     |
|                       |                       | Befragten)            |
+-----------------------+-----------------------+-----------------------+
| accessWay             | Ja                    | „download-cuf“,       |
|                       |                       | „download-suf“,       |
|                       |                       | „remote-desktop-suf“  |
|                       |                       | oder „onsite-suf“     |
+-----------------------+-----------------------+-----------------------+
| description.de/en     | Ja                    | Beschreibung des      |
|                       |                       | Datensatzes           |
+-----------------------+-----------------------+-----------------------+
| dataSetNumber         | Ja                    | Nummer des            |
|                       |                       | zugehörigen           |
|                       |                       | Datensatzes           |
+-----------------------+-----------------------+-----------------------+
| citationHint.de/en    | Nein                  | Hinweise zur          |
|                       |                       | Zitation:             |
|                       |                       | Primärforscher_1,     |
|                       |                       | Primärforscher_2 &    |
|                       |                       | Primärforscher_3      |
|                       |                       | (Jahr). Studientitel. |
|                       |                       | Aufbereitet durch     |
|                       |                       | FDZMitarbeiter_1,     |
|                       |                       | FDZMitarbeiter_2 &    |
|                       |                       | FDZMitarbeiter_3,     |
|                       |                       | doi:                  |
|                       |                       | 10.21249/DZHW:a-Z0-9: |
|                       |                       | 0-9.0-9.0-9,          |
|                       |                       | released Jahr.        |
|                       |                       | Hannover: FDZ-DZHW.   |
+-----------------------+-----------------------+-----------------------+

+-----------------------+-----------------------+-----------------------+
| **Tabellenblatt 3:                                                    |
| attachments**                                                         |
+=======================+=======================+=======================+
| Es können mehrere                                                     |
| Anhänge eingetragen                                                   |
| werden (= mehrere                                                     |
| Zeilen möglich, ein                                                   |
| Anhang pro Zeile)                                                     |
+-----------------------+-----------------------+-----------------------+
| **Spaltenüberschrift**| **Muss ich das        | **Was muss ich        |
|                       | ausfüllen?**          | eintragen?**          |
+-----------------------+-----------------------+-----------------------+
| fileName              | Ja                    | Name der Datei im     |
|                       |                       | attachments-Ordner    |
|                       |                       | (z. B.                |
|                       |                       | „DataSetReport-abs200 |
|                       |                       | 5-ds1.pdf“)           |
+-----------------------+-----------------------+-----------------------+
| title                 | Ja                    | Titel des Anhangs     |
+-----------------------+-----------------------+-----------------------+
| description.de/en     | Ja                    | Beschreibung des      |
|                       |                       | Anhangs               |
+-----------------------+-----------------------+-----------------------+
| language              | Ja                    | Sprache des Anhangs   |
|                       |                       | *Bitte verwenden Sie  |
|                       |                       | eine Abkürzung nach   |
|                       |                       | ISO 639-1*:           |
|                       |                       | z. B. „de“, „en“      |
+-----------------------+-----------------------+-----------------------+
| dataSetNumber         | Ja                    | Nummer des            |
|                       |                       | zugehörigen           |
|                       |                       | Datensatzes           |
+-----------------------+-----------------------+-----------------------+

Wenn Sie Materialien auf Ebene der Datensätze haben, können Sie diese
auch hier wieder im Ordner *attachments* ablegen. [9]_

Um den erfolgreichen Upload der Informationen ins MDM gewährleisten, ist
auf Datensatzebene folgende Ordnerstruktur vorgegeben:

**Ordnerstruktur: Inhalt des Ordners dataSets**

.. figure:: ./_static/34_1_de.png

Hochladen können Sie den Ordner im Reiter Datensätze entweder über den
orangefarbenen Plus-Button (unten rechts) oder per Drag & Drop.

Variablen (variables) [10]_
~~~~~~~~~~~~~~~~~~~~~~~~~~~

**Übersicht**

Anhand der Informationen, die Sie auf Ebene der Variablen abgeben, wird
für jede Variable eine Übersichtsseite im MDM erstellt:


.. figure:: ./_static/35_de.png
   :name: variablenübersicht

   Variablenübersicht im MDM am Beispiel der Variable "1. Studium: Beginn (Semester)" im Absolventenpanel 2005, erste Welle (BA)

Die Erstellung der Variablenebene beinhaltet einerseits recht viel
Aufwand, da für jeden Datensatz eine eigene Excel-Tabelle mit
Informationen zu allen Variablen geliefert werden muss. Viele
Informationen müssen manuell eingetragen werden, einige können – sofern
die Befragung über Zofar stattgefunden hat – auch direkt aus Zofar
extrahiert werden oder sogar aus der Excel-Tabelle der Frageebene
importiert werden.

Die Variablenebene ist andererseits sehr wertvoll im Hinblick auf die
Nachnutzbarkeit der Forschungsdaten. Wenn Metadaten auf dieser Ebene
vorhanden sind, können die dazugehörigen Daten auch aus inhaltlicher
Sicht umfassend durchsucht werden, sodass Analysepotential auch für sehr
spezielle Fragestellungen direkt sichtbar wird.

Für die Darstellung der Metadatenaufnahme auf Variablenebene gilt es
noch folgende Dinge zu beachten:

-  Wenn Sie mehrere Datensätze liefern: Es darf kein Variablenname
   doppelt vorkommen.

-  Missings müssen global definiert sein, d. h. sie müssen für alle
   Variablen eines Datensatzes gelten.

**Excel-Tabelle**

Ausfüllen müssen Sie je nach Anzahl der Datensätze mindestens eine
Excel-Datei mit dem Namen *vimport_ds\ *\ **Nr.**\ *.xlsx*, wobei die
**„\ Nr.\ “** im Dateinamen der Nummer des dazugehörigen Datensatzes
entsprechen muss, d. h. die Variablen des Datensatzes mit der Nummer 1
muss *vimport_ds1.xlsx* heißen usw. Die Datei enthält die beiden
Tabellenblätter *variables* und *relatedQuestions*.

Tabelle 5: Ausfüllanweisungen für die Excel-Tabelle "vimport_ds*Nr*."

+-----------------------+-----------------------+-----------------------+
| **Tabellenblatt 1:                                                    |
| variables**                                                           |
+=======================+=======================+=======================+
| Es können mehrere                                                     |
| Variablen eingetragen                                                 |
| werden (= mehrere                                                     |
| Zeilen möglich, eine                                                  |
| Variable pro Zeile)                                                   |
+-----------------------+-----------------------+-----------------------+
| **Spaltenüberschrift**| **Muss ich das        | **Was muss ich        |
|                       | ausfüllen?**          | eintragen?**          |
+-----------------------+-----------------------+-----------------------+
| name                  | Ja                    | Variablenname         |
+-----------------------+-----------------------+-----------------------+
| surveyNumbers         | Ja\*                  | Angabe aller der      |
|                       |                       | Variablen zugehörigen |
|                       |                       | Erhebungsnummern (in  |
|                       |                       | einer Zelle durch     |
|                       |                       | Komma getrennt)       |
+-----------------------+-----------------------+-----------------------+
| scaleLevel.de/.en     | Ja                    | de: „nominal“,        |
|                       |                       | „ordinal“,            |
|                       |                       | „intervall“ oder      |
|                       |                       | „verhältnis“          |
|                       |                       | en: „nominal“,        |
|                       |                       | „ordinal“,            |
|                       |                       | „intervall“ or        |
|                       |                       | „ratio“               |
+-----------------------+-----------------------+-----------------------+
| panelIdentifier       | Nein\*                | Identifier zur        |
|                       |                       | eindeutigen Zuordnung |
|                       |                       | von Panelvariablen.   |
|                       |                       | Präfix muss aus der   |
|                       |                       | Projekt-ID + Nummer   |
|                       |                       | des Datensatzes       |
|                       |                       | bestehen (Beispiel:   |
|                       |                       | *gra2005-ds1*), der   |
|                       |                       | hintere Teil des      |
|                       |                       | Identifiers ist       |
|                       |                       | beliebig wählbar,     |
|                       |                       | muss aber eindeutig   |
|                       |                       | sein.                 |
|                       |                       | Beispiel: Sind die    |
|                       |                       | Variablen *astu01a*   |
|                       |                       | und *bstu01a* aus dem |
|                       |                       | 1. Datensatz des      |
|                       |                       | Projekts *gra2005*    |
|                       |                       | Panelvariablen, so    |
|                       |                       | könnte der Identifier |
|                       |                       | *gra2005-ds1-stu01a*  |
|                       |                       | lauten.               |
+-----------------------+-----------------------+-----------------------+
| annotations.de/en     | Nein                  | Anmerkungen zur       |
|                       |                       | Variablen             |
+-----------------------+-----------------------+-----------------------+
| accessWays            | Ja\*                  | Mögliche Zugangswege: |
|                       |                       | Download-CUF,         |
|                       |                       | Download-SUF,         |
|                       |                       | Remote-Desktop-SUF,   |
|                       |                       | On-Site-SUF.          |
|                       |                       | Bei mehreren          |
|                       |                       | Zugangswegen sind den |
|                       |                       | verschiedenen         |
|                       |                       | Zugangswegen          |
|                       |                       | entsprechend Spalten  |
|                       |                       | vorhanden, die mit    |
|                       |                       | „nicht verfügbar im … |
|                       |                       | “ überschrieben sind. |
|                       |                       | Für jede Variable     |
|                       |                       | muss dann ein „x“     |
|                       |                       | gesetzt werden, wenn  |
|                       |                       | ´diese über den       |
|                       |                       | jeweiligen Zugangsweg |
|                       |                       | nicht vorhanden ist.  |
+-----------------------+-----------------------+-----------------------+
| filterDetails.descrip | Nein                  | Verbalisierte         |
| tion.de/.en           |                       | Beschreibung des      |
|                       |                       | Variablenfilters      |
+-----------------------+-----------------------+-----------------------+
| filterDetails.express | Ja, wenn Filter       | Regel, die in der     |
| ion [11]_             | vorhanden             | angegebenen „Sprache“ |
|                       |                       | (.expressionLanguage) |
|                       |                       | beschreibt, welche    |
|                       |                       | Teilpopulation zu     |
|                       |                       | dieser Variable hin   |
|                       |                       | gefiltert wurde (auch |
|                       |                       | verschachtelte        |
|                       |                       | Filterführung wird    |
|                       |                       | beachtet (PAPI))      |
+-----------------------+-----------------------+-----------------------+
| filterDetails.express | Ja, wenn Filter       | Sprache des           |
| ionLanguage [12]_     | vorhanden             | Filterausdrucks:      |
|                       |                       | „Stata“               |
+-----------------------+-----------------------+-----------------------+
| generationDetails.des | Nein                  | Beschreibung, wie die |
| cription.de/.en       |                       | Variable erzeugt      |
|                       |                       | wurde, wenn sie nicht |
|                       |                       | direkt aus dem        |
|                       |                       | Fragebogen abgelesen  |
|                       |                       | werden kann           |
|                       |                       | (`Beispiel <https://m |
|                       |                       | etadata.fdz.dzhw.eu/# |
|                       |                       | !/de/variables/var-gr |
|                       |                       | a2005-ds1-aocc221j_g1 |
|                       |                       | r$?search-result-inde |
|                       |                       | x=1>`__,              |
|                       |                       | siehe Abschnitt       |
|                       |                       | "Generierungsdetails")|
+-----------------------+-----------------------+-----------------------+
| generationDetails.rul | Ja, wenn Variable     | Regel, die in der     |
| e                     | generiert             | angegebenen „Sprache“ |
|                       |                       | (.ruleExpressionLangu |
|                       |                       | age)                  |
|                       |                       | beschreibt, wie die   |
|                       |                       | Variable erzeugt      |
|                       |                       | wurde                 |
|                       |                       | (`Beispiel <https://m |
|                       |                       | etadata.fdz.dzhw.eu/# |
|                       |                       | !/de/variables/var-gr |
|                       |                       | a2005-ds1-afec021k_g2 |
|                       |                       | $?search-result-index |
|                       |                       | =1>`__,               |
|                       |                       | siehe Abschnitt       |
|                       |                       | „Generierungsregel    |
|                       |                       | (Stata)“)             |
+-----------------------+-----------------------+-----------------------+
| generationDetails.rul | Ja, wenn Variable     | Sprache der           |
| eExpressionLanguage   | generiert             | Erzeugungsregel:      |
|                       |                       | „Stata“ oder „R“      |
+-----------------------+-----------------------+-----------------------+
| derivedVariablesIdent | Nein\*                | Identifier zur        |
| ifier                 |                       | eindeutigen Zuordnung |
|                       |                       | von abgeleiteten      |
|                       |                       | Variablen. Präfix     |
|                       |                       | muss aus der          |
|                       |                       | Projekt-ID + Nummer   |
|                       |                       | des Datensatzes       |
|                       |                       | bestehen (Beispiel:   |
|                       |                       | *gra2005-ds1*), der   |
|                       |                       | hintere Teil des      |
|                       |                       | Identifiers ist frei  |
|                       |                       | wählbar, muss aber    |
|                       |                       | eindeutig sein.       |
|                       |                       |                       |
|                       |                       | Beispiel: Wurde die   |
|                       |                       | Variable *astu01a_g1* |
|                       |                       | aus *astu01a*         |
|                       |                       | abgeleitet, so könnte |
|                       |                       | der Identifier        |
|                       |                       | *gra2005-ds1-astu*    |
|                       |                       | lauten.               |
|                       |                       |                       |
|                       |                       | Wichtig: Alle         |
|                       |                       | Variablen, aus denen  |
|                       |                       | die abgeleitete       |
|                       |                       | Variable entstanden   |
|                       |                       | ist, müssen           |
|                       |                       | berücksichtigt werden |
|                       |                       | (sowohl aufwärts als  |
|                       |                       | auch abwärts).        |
|                       |                       |                       |
|                       |                       | Beispiel: Von der     |
|                       |                       | tatsächlichen         |
|                       |                       | Hochschule wird       |
|                       |                       | sowohl der            |
|                       |                       | Hochschulort          |
|                       |                       | (West-/Ostdeutschland |
|                       |                       | )                     |
|                       |                       | als auch der          |
|                       |                       | Hochschulort nach     |
|                       |                       | Bundesländern         |
|                       |                       | abgeleitet.           |
+-----------------------+-----------------------+-----------------------+
| doNotDisplayThousands | Nein                  | Wenn bei der Anzeige  |
| Seperator             |                       | der Werte einer       |
|                       |                       | Variablen *keine*     |
|                       |                       | Tausendertrennzeichen |
|                       |                       | angezeigt werden      |
|                       |                       | sollen, muss hier     |
|                       |                       | "true" angezeigt      |
|                       |                       | werden (z. B.         |
|                       |                       | Jahreszahlen). Bleibt |
|                       |                       | das Feld leer, wird   |
|                       |                       | dies als "false"      |
|                       |                       | interpretiert, d.h.   |
|                       |                       | es werden             |
|                       |                       | Tausendertrennzeichen |
|                       |                       | angezeigt.            |
+-----------------------+-----------------------+-----------------------+

\* Wenn eigene Konventionen verwendet werden, muss das Feld manuell
ausgefüllt werden. Bei Verwendung von FDZ-eigenen Schemata kann dieses
Feld auch leer gelassen werden.

+-----------------------+-----------------------+-----------------------+
| **Tabellenblatt 2:                                                    |
| relatedQuestions**                                                    |
+=======================+=======================+=======================+
| **Variablen, die mit                                                  |
| mehreren Fragen                                                       |
| verbunden sind,                                                       |
| können mehrfach                                                       |
| aufgeführt werden.                                                    |
| Variablen, die keiner                                                 |
| Frage (oder keinem                                                    |
| Instrument)                                                           |
| zugeordnet sind,                                                      |
| müssen nicht                                                          |
| eingetragen werden.**                                                 |
+-----------------------+-----------------------+-----------------------+
| Es können mehrere                                                     |
| verbundene Fragen                                                     |
| eingetragen werden (=                                                 |
| mehrere Zeilen, eine                                                  |
| verbundene Frage pro                                                  |
| Zeile)                                                                |
+-----------------------+-----------------------+-----------------------+
| **Spaltenüberschrift**| **Muss ich das        | **Was muss ich        |
|                       | ausfüllen?**          | eintragen?**          |
+-----------------------+-----------------------+-----------------------+
| name                  | Ja                    | Variablenname         |
+-----------------------+-----------------------+-----------------------+
| relatedQuestionString | Nein                  | Text, der den         |
| s.de/.en              |                       | Frageinhalt der       |
|                       |                       | Variable darstellt.   |
|                       |                       | Also Fragetext der    |
|                       |                       | dazugehörigen Frage   |
|                       |                       | plus evtl. weitere    |
|                       |                       | Ausführungen wie      |
|                       |                       | bspw. der Itemtext    |
|                       |                       | (bei Itembatterien)   |
|                       |                       | oder der Antworttext  |
|                       |                       | (bei Einfach- oder    |
|                       |                       | Mehrfachnennungen)    |
+-----------------------+-----------------------+-----------------------+
| questionNumber        | Ja                    | Nummer der zur        |
|                       |                       | Variablen zugehörigen |
|                       |                       | Frage im Fragebogen   |
+-----------------------+-----------------------+-----------------------+
| instrumentNumber      | Ja                    | Nummer des zur        |
|                       |                       | Variablen zugehörigen |
|                       |                       | Fragebogens           |
+-----------------------+-----------------------+-----------------------+

Dem Namen entsprechend wird aus den Informationen des zweiten
Tabellenblatts die Verknüpfung zwischen einer Variablen und der
dazugehörigen Frage aus dem Erhebungsinstrument erstellt. Für eine
nachvollziehbare Dokumentation dieser Verbindung ist die Erstellung
eines Variablenfragebogens sehr hilfreich. Aus diesem kann die
Verknüpfung aus Variable und Frage problemlos abgelesen werden. :numref:`ausschnitt_variablenfragebogen` zeigt beispielhaft, dass den Variablen *astu08a* bis *astu08e* die Frage
1.8 zugeordnet ist.

.. figure:: ./_static/36_de.png
   :name: ausschnitt_variablenfragebogen

   Ausschnitt aus dem Variablenfragebogen des Absolventenpanels 2005, erste Welle, Frage 1.8


Außer der/den Excel-Tabelle/n müssen Sie für jede Tabelle noch den
zugehörigen Stata-Datensatz liefern, aus dem die Variablen stammen.
Diese Dateien speichern Sie dann in dem Ordner, den das FDZ für Sie
vorbereitet hat. Das FDZ greift daraufhin auf die Dateien zu,
verarbeitet sie weiter und lädt die finalisierten Metadaten für die
Variablenebene dann selbst ins MDM.

Publikationen (relatedPublications)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

**Überblick**

Auf der Ebene der Publikationen werden wissenschaftliche
Veröffentlichungen, welche auf Grundlage von Daten Ihres Projekts
verfasst worden sind, erfasst. Die Informationen, die Sie im Hinblick
auf Ihre Publikationen abgeben, werden im MDM für jede Veröffentlichung
wie folgt dargestellt:


.. figure:: ./_static/37_de.png
   :name: publikationsübersicht

   Publikationsübersicht im MDM am Beispiel einer Veröffentlichung, welche im Rahmen des Absolventenpanels 2005 verfasst wurde

Wenn Sie Publikationen zu Ihren Daten abgeben möchten, senden Sie dem
FDZ per Mail die PDF-Datei Ihrer Publikation sowie den dazugehörigen
Zitationshinweis zu. Die weitere Bearbeitung wie z. B. die Registrierung
für das Erhalten einer DOI und den Upload ins MDM übernimmt das FDZ.

Die Freigabe eines neuen Projekts
---------------------------------

Wenn Sie alle Metadaten ausgefüllt bzw. ans FDZ gesendet haben, melden
Sie sich beim FDZ mit dem Hinweis, dass Sie Ihre Daten nicht weiter
editieren möchten. Das FDZ nimmt ihre Daten dann in die sogenannte
Release-Pipeline auf. Die finale Freigabe erfolgt dann über einen dafür
benannten Mitarbeiter des FDZ, den Release-Manager.

Anhang
------

Checkliste für Abgabe der Metadaten
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Vor Abgabe bzw. dem Hochladen der Daten sind folgende Punkte zu
überprüfen:

☐ Ordnerstruktur und Dateinamen sind unverändert

☐ Excel-Dateien sind richtig und vollständig ausgefüllt

-  verpflichtende Felder sind ausgefüllt (vgl. Kapitel 4)

-  es sind keine Fehlermeldungen vorhanden

-  die Daten wurden auf Richtigkeit überprüft

☐ Metadaten sind aus PDF-Dokumenten entfernt (vgl. Kapitel 3.3)

☐ Nicht benötigte Zeilen entfernt (vgl. Kapitel 3.2)

-  instruments.xlsx: löschen bis Zeile 20

-  dataSets.xlsx: löschen bis Zeile 20

-  questions.xlsx: löschen bis Zeile 2000

-  variables.xlsx: löschen bis Zeile 2000

☐ Dateien sind richtig abgegeben worden

   ☐ In das Metadatensystem eingegeben

-  Studie (study)

-  Erhebungen (surveys)

..

   ☐ In das Metadatensystem hochgeladen

-  instruments.xlsx

-  dataSets.xlsx

..

   ☐ In der Ordnerstruktur abgelegt

-  questions.xlsx

-  variables.xlsx

.. [1]
   Bitte beachten Sie, die dokumenteigenen Metadaten der PDF-Dateien
   vorab zu löschen (vgl. Kapitel 3.3).

.. [2]
   Rücklaufgrafiken sind nur im Dokumentationsstandard der Stufe 3
   gefordert. Die Erläuterungen zu den drei verschiedenen
   Dokumentationsstandards finden Sie in den Dokumenten `„Anforderungen
   an Daten und Dokumentation im FDZ des
   DZHW“ <file:///\\faust\Abtuebergreifend\Projekte\FDZ\Allgemeine%20Materialien\Dokumentation>`__.

.. [3]
   Bitte beachten Sie, die dokumenteigenen Metadaten bei PDF-Dateien
   vorab zu löschen (vgl. Kapitel 3.3).

.. [4]
   Filterführungsdiagramme sind erst ab der 2. Dokumentationsstufe
   gefordert. Die Erläuterungen zu den drei verschiedenen
   Dokumentationsstandards finden Sie in den Dokumenten `„Anforderungen
   an Daten und Dokumentation im FDZ des
   DZHW“ <file:///\\faust\Abtuebergreifend\Projekte\FDZ\Allgemeine%20Materialien\Dokumentation>`__.

.. [5]
   Bitte beachten Sie, die dokumenteigenen Metadaten der PDF-Dateien
   vorab zu löschen (vgl. Kapitel 3.3).

.. [6]
   Metadaten auf Fragenebene sind erst ab der 2. Dokumentationsstufe
   gefordert. Die Erläuterungen zu den drei verschiedenen
   Dokumentationsstandards finden Sie in den Dokumenten `„Anforderungen
   an Daten und Dokumentation im FDZ des
   DZHW“ <file:///\\faust\Abtuebergreifend\Projekte\FDZ\Allgemeine%20Materialien\Dokumentation>`__.

.. [7]
   Bitte beachten Sie, die dokumenteigenen Metadaten der PDF-Dateien
   vorab zu löschen (vgl. Kapitel 3.3).

.. [8]
   Mit Subdatensätzen sind solche gemeint, die Sie nach einer
   Anonymisierung Ihrer Daten erhalten. Sie können mehrere Stufen der
   Anonymisierung verwenden, wobei jede Stufe einen eigenen Zugangsweg
   zu den anonymisierten Daten mit sich bringt. Für jeden Zugangsweg
   wird dann ein eigener Subdatensatz erstellt (vgl. hierzu „accessWay“
   im Tabellenblatt „subDataSets“).

.. [9]
   Bitte beachten Sie, die dokumenteigenen Metadaten bei PDF-Dateien
   vorab zu löschen (vgl. Kapitel 3.3).

.. [10]
   Metadaten auf Variablenebene sind erst ab der 2. Dokumentationsstufe
   gefordert. Die Erläuterungen zu den drei verschiedenen
   Dokumentationsstandards finden Sie in den Dokumenten `„Anforderungen
   an Daten und Dokumentation im FDZ des
   DZHW“ <file:///\\faust\Abtuebergreifend\Projekte\FDZ\Allgemeine%20Materialien\Dokumentation>`__.

.. [11]
   Nur in der Dokumentationsstufe 3 gefordert. Die Erläuterungen zu den
   drei verschiedenen Dokumentationsstandards finden Sie in den
   Dokumenten `„Anforderungen an Daten und Dokumentation im FDZ des
   DZHW“ <file:///\\faust\Abtuebergreifend\Projekte\FDZ\Allgemeine%20Materialien\Dokumentation>`__.

.. [12]
   Nur in der Dokumentationsstufe 3 gefordert. Die Erläuterungen zu den
   drei verschiedenen Dokumentationsstandards finden Sie in den
   Dokumenten `„Anforderungen an Daten und Dokumentation im FDZ des
   DZHW“ <file:///\\faust\Abtuebergreifend\Projekte\FDZ\Allgemeine%20Materialien\Dokumentation>`__.
