[WARNING] Reference not found for 'intervall' at line 4 column 278
[WARNING] Reference not found for 'verhältnisskala' at line 4 column 300
[WARNING] Reference not found for 'nominalskala' at line 8 column 17
[WARNING] Reference not found for 'jedes' at line 8 column 111
[WARNING] Reference not found for 'einer' at line 8 column 132
[WARNING] Reference not found for 'Key "logische / mathematische operationen"' at line 10 column 40
[WARNING] Reference not found for 'Key "stata-beispiel syntax"' at line 10 column 85
[WARNING] Reference not found for 'Key ":---"' at line 11 column 13
[WARNING] Reference not found for 'sysuse' at line 12 column 64
[WARNING] Reference not found for 'fre' at line 12 column 86
[WARNING] Reference not found for 'Key "_sysuse_[2] auto <br /> _fre_ make"' at line 12 column 92
[WARNING] Reference not found for 'ordinalskala' at line 15 column 17
[WARNING] Reference not found for 'Key "logische / mathematische operationen"' at line 17 column 40
[WARNING] Reference not found for 'Key "stata-beispiel syntax"' at line 17 column 85
[WARNING] Reference not found for 'Key ":---"' at line 18 column 7
[WARNING] Reference not found for 'Key ":---"' at line 18 column 17
[WARNING] Reference not found for 'sysuse' at line 19 column 89
[WARNING] Reference not found for 'fre' at line 19 column 110
[WARNING] Reference not found for 'Key "_sysuse_ bplong <br /> _fre_ agegrp"' at line 19 column 118
[WARNING] Reference not found for 'intervallskala' at line 21 column 19
[WARNING] Reference not found for 'Key "logische / mathematische operationen"' at line 23 column 40
[WARNING] Reference not found for 'Key "stata-beispiel syntax"' at line 23 column 85
[WARNING] Reference not found for 'Key ":---"' at line 24 column 13
[WARNING] Reference not found for 'sysuse' at line 25 column 84
[WARNING] Reference not found for 'fre' at line 25 column 105
[WARNING] Reference not found for 'Key "_sysuse_ surface <br /> _fre_ temperature"' at line 25 column 118
[WARNING] Reference not found for 'verhältnisskala' at line 27 column 20
[WARNING] Reference not found for 'Key ":---"' at chunk line 1 column 11
[WARNING] Reference not found for 'sysuse' at chunk line 1 column 50
[WARNING] Reference not found for 'fre' at chunk line 1 column 68
[WARNING] Reference not found for 'Key "_sysuse_ auto <br /> _fre_ ???"' at chunk line 1 column 73
[WARNING] Reference not found for 'Key ":---"' at line 38 column 7
[WARNING] Reference not found for 'Key ":---"' at line 38 column 17
[WARNING] Reference not found for 'Key ":---"' at line 38 column 27
[WARNING] Reference not found for 'Key "**nominalskala**"' at line 39 column 19
[WARNING] Reference not found for 'Key "**ordinalskala**"' at line 40 column 19
[WARNING] Reference not found for 'Key "**intervallskala**"' at line 41 column 21
[WARNING] Reference not found for 'Key "**verh\228ltnisskala**"' at line 42 column 22
[WARNING] Reference not found for 'sysuse' at line 54 column 105
[WARNING] Reference not found for 'fre' at line 54 column 132
# Skalenniveau prüfen

# Skalenniveau Diese Informationen dienen dazu die verschiedenen
Skalentypen voneinander unterscheiden zu können und Variablen
selbständig einordnen zu können. Das Forschungsdatenzentrum (**FDZ**)
hat sich dazu entschieden vier Skalentypen zu unterscheiden \_Nominal-_,
\_Ordinal-_, \_\ `Intervall <>`__ und \_\ `Verhältnisskala <>`__. Siehe
hierzu [Wikipedia-Eintrag](https://de.wikipedia.org/wiki/Skalenniveau)

## Skalentypen **\_\ **\ `Nominalskala <>`__ - Nominale Messung besteht
in der Erstellung einer einfachen Klasseneinteilung, die
\_\ `jedes <>`__ Objekt genau \_\ `einer <>`__ Klasse zuordnet: Weder
darf ein bestimmtes Objekt überhaupt nicht zugeordnet werden, noch darf
ein Objekt mehreren Klassen zugeordnet werden.

Beispiele (Variable) \| :---:---\| \| (=/≠) \| Geschlecht,
Universitätsnamen, Studienfächer

**\_\ **\ `Ordinalskala <>`__ - Sie besitzt eine Rangordnung der Objekte
in Bezug auf die interessierende Dimension. Die entsprechend
zugeordneten Zahlen müssen diese Rangordnung wiedergeben.

Beispiele (Variable) :--- \| (=/≠ ; </>) \| Schulnoten, höchster
Bildungsabschluss, Zufriedenheitensskalen

**\_\ **\ `Intervallskala <>`__ - ist ein Skalenniveau in der Statistik.
Sie zählt zum metrischen Messniveau, da sich die Ausprägungen dieses
Skalenniveaus quantitativ mittels Zahlen darstellen lassen. Insbesondere
bedeutet das auch, dass Rangunterschiede und Abstand zwischen Werten
gemessen werden können; das heißt, quantitative Merkmale gehen in ihren
Anforderungen über ordinale oder gar nominale Eigenschaften hinaus.

Beispiele (Variable) \| :---:---\| \| =/≠ ; </> ; −; + \| Temperatur
(Celcius, Fahrenheit), Zeitskala (Datum)

**\_\ **\ `Verhältnisskala <>`__ - ist das höchste Skalenniveau in der
Statistik. Bei ihr handelt es sich um eine metrische Skala, im
Unterschied zur Intervallskala existiert jedoch ein absoluter Nullpunk

| logische / mathematische Operationen \| Beispiele (Variable) \|
  Stata-Beispiel Syntax\|
| :---:---\|
| =/≠ ; </> ; +/− ; ÷ \| Alter, Einkommen

### Übersicht

| \| Verschiedenartigkeit \| natürl. Reihenfolge \| Interpretierbarkeit
  der Verhältnisse der Differenzen \| natürl. Nullpunkt \| natürl.
  Einheit \|

:---:---:---\| ja \| nein \| nein \| nein \| nein \| ja \| ja \| nein \|
nein \| nein \| ja \| ja \| ja \| nein \| nein \| ja \| ja \| ja \| ja
\| ja \|

### FAQ (ausfüllen ): - Was ist mit "Sonstige"-Kategorien - Was mit
fehlenden Werten machen? - Prinzipiell immer konservativ (niedriges
Niveau) Skalenniveau vergeben

### Typische Vergaben -

[1]: R. Schnell, P. Hill, and E. Esser. Oldenbourg, München u.a., 6.,
völlig überarb. und erw. Aufl. edition, (1999); S.134ff. [2]: Stata
commands sind kursiv. Vor einem kursiven Ausdruck muss ein Zeilenumbruch
sein; Bsp.: \_\ `sysuse <>`__ auto (Zeilenumbruch) \_\ `fre <>`__ make
