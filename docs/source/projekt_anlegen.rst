Neues Projekt anlegen
=====================
Im Folgenden wird zunächst der Prozess zur Erstellung eines Projektes und dann
die Vergabelogik der DataAcquisitionProject-ID erklärt.

Prozess
~~~~~~~

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

.. figure:: ./_static/05_de.png
   :name: neuesprojektanlegen

   Ein neues Projekt im MDM anlegen


Über den Plus-Button können *Publisher* ein neues Projekt anlegen (vgl. :numref:`neuesprojektanlegen`).
Als Projektname müssen diese eine bestimmte ID
angeben, welche das FDZ zuvor speziell für Ihr Projekt vergeben hat und Ihnen
mitteilen muss (z. B. „gra2005“ für das Absolventenpanel 2005).

DataAcquisitionProject-ID
~~~~~~~~~~~~~~~~~~~~~~~~~

- **drei Kleinbuchstaben**: Um die internationale Nutzbarkeit der Daten zu
  erleichtern, ergeben sich die drei Kleinbuchstaben aus der englischsprachigen
  Abkürzung des Projektes. Es kann Ausnahmen geben, wenn z.B. bestimmte Projekte
  ein besonders griffiges Kürzel haben wie z.B. bei Libertas 2016 - lib2016.
- **(zwei bis) vier Ziffern**: Die Ziffern sind in der Regel die vier Ziffern
  des Jahres, das die Zugehörigkeit zur Stichprobe definiert, z. B. der
  Abschluss des Studiums (z. B. im (Prüfungs-)Jahr 2005 oder der Erwerb der
  Hochschulzugangsberechtigung im Jahr 2008). Davon kann in begründeten
  Ausnahmefällen abgewichen werden.
- Beispielsweise ist die Sozialerhebung deutlich stärker unter der
  jeweiligen Nummer der Studie als unter dem Jahr, das die Zugehörigkeit
  zur Stichprobe definiert, bekannt, so dass beispielsweise für die 19.
  Sozialerhebung aus dem Jahr 2009 die Ziffern 19 (und nicht 2009) vergeben
  werden.
- Bei Befragungen, die sich auf mehrere Jahreszahlen beziehen, kann eine
  andere eindeutige Jahreszahl verwendet werden. Beispielweise gehören in
  der KomPaed-Befragung die zuletzt aktiven Panelteilnehmer mehrerer
  Kohorten zur Stichprobe. Hier werden die vier Ziffern des Erhebungsjahres
  (2014) genutzt.

Für jedes weitere Objekt wird später ebenfalls eine ID generiert, die die DAP-id
enthält und sich nach der folgenden Logik zusammensetzt:

+--------------------+-----------------------------------------------------------------------------------+
| Metadaten          | Id-Generierung                                                                    |
+====================+===================================================================================+
| Study              | "stu-" + DAP-id + "$"                                                             |
+--------------------+-----------------------------------------------------------------------------------+
| Survey             | "sur-" + DAP-id + "-" + "sy" + survey.number + "$"                                |
+--------------------+-----------------------------------------------------------------------------------+
| DataSet            | "dat-" + DAP-id + "-" + "ds" + dataSet.number + "$"                               |
+--------------------+-----------------------------------------------------------------------------------+
| Variable           | "var-" + DAP-id + "-" + "ds" + variable.dataSetNumber + "-" + variable name + "$" |
+--------------------+-----------------------------------------------------------------------------------+
| Instrument         | "ins-" + DAP-id + "-" + "ins" + number + "$"                                      |
+--------------------+-----------------------------------------------------------------------------------+
| Question           | "que-" + DAP-id + "-ins" + instrumentNumber + "-" + number + "$"                  |
+--------------------+-----------------------------------------------------------------------------------+
| relatedPublication | "pub-" + citaviId + "$"                                                           |
+--------------------+-----------------------------------------------------------------------------------+

Das Projekt ist dadurch im MDM angelegt, aber noch nicht automatisch
freigegeben. Solange das Projekt noch nicht freigegeben wurde, können
Nutzer der Gruppe *publisher* es jederzeit über den Mülleimer-Button ganz links (vgl.
:numref:`neuesprojektanlegen`)
wieder löschen.
