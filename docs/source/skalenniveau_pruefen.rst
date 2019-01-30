.. _skalenniveau-pruefen-label:

Skalenniveau prüfen
===================

Skalenniveau Diese Informationen dienen dazu die verschiedenen
==============================================================

Skalentypen voneinander unterscheiden zu können und Variablen
selbständig einordnen zu können. Das Forschungsdatenzentrum (**FDZ**)
hat sich dazu entschieden vier Skalentypen zu unterscheiden
\_Nominal-\ *, \_Ordinal-*, \_\\ ``Intervall <>``\ \_\_ und \_\\
``Verhältnisskala <>``\ \__. Siehe hierzu
`Wikipedia-Eintrag <https://de.wikipedia.org/wiki/Skalenniveau>`__

.. _skalentypen-_--nominalskala-__---nominale-messung-besteht:

Skalentypen \**_\\ \**\\ ``Nominalskala <>``\ \_\_ - Nominale Messung besteht
-----------------------------------------------------------------------------

in der Erstellung einer einfachen Klasseneinteilung, die \_\\
``jedes <>``\ \_\_ Objekt genau \_\\ ``einer <>``\ \_\_ Klasse zuordnet:
Weder darf ein bestimmtes Objekt überhaupt nicht zugeordnet werden, noch
darf ein Objekt mehreren Klassen zugeordnet werden.

Beispiele (Variable) \| :---:---\| \| (=/≠) \| Geschlecht,
Universitätsnamen, Studienfächer

\**_\\ \**\\ ``Ordinalskala <>``\ \_\_ - Sie besitzt eine Rangordnung
der Objekte in Bezug auf die interessierende Dimension. Die entsprechend
zugeordneten Zahlen müssen diese Rangordnung wiedergeben.

Beispiele (Variable) :--- \| (=/≠ ; </>) \| Schulnoten, höchster
Bildungsabschluss, Zufriedenheitensskalen

\**_\\ \**\\ ``Intervallskala <>``\ \_\_ - ist ein Skalenniveau in der
Statistik. Sie zählt zum metrischen Messniveau, da sich die Ausprägungen
dieses Skalenniveaus quantitativ mittels Zahlen darstellen lassen.
Insbesondere bedeutet das auch, dass Rangunterschiede und Abstand
zwischen Werten gemessen werden können; das heißt, quantitative Merkmale
gehen in ihren Anforderungen über ordinale oder gar nominale
Eigenschaften hinaus.

Beispiele (Variable) \| :---:---\| \| =/≠ ; </> ; −; + \| Temperatur
(Celcius, Fahrenheit), Zeitskala (Datum)

\**_\\ \**\\ ``Verhältnisskala <>``\ \_\_ - ist das höchste Skalenniveau
in der Statistik. Bei ihr handelt es sich um eine metrische Skala, im
Unterschied zur Intervallskala existiert jedoch ein absoluter Nullpunk

\| logische / mathematische Operationen \| Beispiele (Variable) \|
Stata-Beispiel Syntax\| \| :---:---\| \| =/≠ ; </> ; +/− ; ÷ \| Alter,
Einkommen

Übersicht
~~~~~~~~~

\| \| Verschiedenartigkeit \| natürl. Reihenfolge \| Interpretierbarkeit
der Verhältnisse der Differenzen \| natürl. Nullpunkt \| natürl. Einheit
\|

:---:---:---\| ja \| nein \| nein \| nein \| nein \| ja \| ja \| nein \|
nein \| nein \| ja \| ja \| ja \| nein \| nein \| ja \| ja \| ja \| ja
\| ja \|

.. _faq-ausfüllen----was-ist-mit-sonstige-kategorien---was-mit:

FAQ (ausfüllen ): - Was ist mit "Sonstige"-Kategorien - Was mit
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

fehlenden Werten machen? - Prinzipiell immer konservativ (niedriges
Niveau) Skalenniveau vergeben

Typische Vergaben -
~~~~~~~~~~~~~~~~~~~

[1]: R. Schnell, P. Hill, and E. Esser. Oldenbourg, München u.a., 6.,
völlig überarb. und erw. Aufl. edition, (1999); S.134ff. [2]: Stata
commands sind kursiv. Vor einem kursiven Ausdruck muss ein Zeilenumbruch
sein; Bsp.: \_\\ ``sysuse <>``\ \_\_ auto (Zeilenumbruch) \_\\
``fre <>``\ \_\_ make
