+-----------------+-----------------+-----------------+-----------------+
| Name            | Was kann es?    | Wo wird es      | Was muss        |
|                 |                 | genutzt?        | manuell gemacht |
|                 |                 |                 | werden?         |
+=================+=================+=================+=================+
| `csv2json.R <ht |  erstellt aus   | Fragenexport im |  die Tabelle    |
| tps://github.co | der             | öffentlichen    | ``ProjID.csv``  |
| m/dzhw/metadata | ``ProjID.csv``  | Bereich         | muss ferig      |
| management-io/b | Tabelle und den |                 | aufbereitet     |
| lob/master/gene | ``FrageNr.pdf`` |                 | sein und im     |
| ration/example/ | für jede Frage  |                 | Ordner          |
| questions/Skrip | eine json Datei |                 | ``\questions``  |
| te/csv2json.R>` | die json        |                 | liegen zu jeder |
| __              | Dateien werden  |                 | Frage muss eine |
|                 | im Ordner       |                 | ``FrageNr.pdf`` |
|                 | ``\questions\js |                 | Datei im Ordner |
|                 | on\insNr``      |                 | ``questions\Bil |
|                 | gespeichert     |                 | der\insNr\pdf`` |
|                 |                 |                 | vorliegen       |
|                 |                 |                 | Alternativ kann |
|                 |                 |                 | auch ocr für    |
|                 |                 |                 | Bilder genutzt  |
|                 |                 |                 | werden, dies    |
|                 |                 |                 | muss jedoch im  |
|                 |                 |                 | Skript          |
|                 |                 |                 | auskommentiert  |
|                 |                 |                 | werden Angabe   |
|                 |                 |                 | des             |
|                 |                 |                 | Projektnames im |
|                 |                 |                 | Skript          |
|                 |                 |                 | Ausführen (Strg |
|                 |                 |                 | + r)            |
+-----------------+-----------------+-----------------+-----------------+
| `variablesToJso |  erstellt für   | Variablenexport |  die Datei      |
| n-ProjID.R <htt | jede Variable   | im öffentlichen | ``summary.csv`` |
| ps://github.com | eine json Datei | Bereich         | muss aus dem    |
| /dzhw/metadatam | die json        |                 | geschützen      |
| anagement-io/bl | Dateien werden  |                 | Bereich         |
| ob/master/gener | im Ordner       |                 | transferiert    |
| ation/example/v | ``variables\Upl |                 | worden sein und |
| ariables/variab | oad\variables\d |                 | im Ordner       |
| lesToJson-abs20 | sNr\variables`` |                 | ``variables\Sta |
| 05.R>`__        | gespeichert     |                 | tistn\von\csv\v |
|                 | stellt eine     |                 | ariable``       |
|                 | Verknüpfung     |                 | liegen die      |
|                 | zwischen        |                 | Dateien         |
|                 | Variablen und   |                 | ``dsNr-Varname. |
|                 | Fragen her      |                 | csv``           |
|                 |                 |                 | müssen aus dem  |
|                 |                 |                 | geschützen      |
|                 |                 |                 | Bereich         |
|                 |                 |                 | transferiert    |
|                 |                 |                 | worden sein und |
|                 |                 |                 | im Ordner       |
|                 |                 |                 | ``variables\Sta |
|                 |                 |                 | tistn\von\csv\v |
|                 |                 |                 | alues``         |
|                 |                 |                 | liegen die      |
|                 |                 |                 | Tabelle         |
|                 |                 |                 | ``fragenVariabe |
|                 |                 |                 | lnverkn.xlsx``  |
|                 |                 |                 | muss fertig     |
|                 |                 |                 | aufbereitet im  |
|                 |                 |                 | Ordner          |
|                 |                 |                 | ``variables\man |
|                 |                 |                 | uelleListen``   |
|                 |                 |                 | vorliegen       |
|                 |                 |                 | Angabe des      |
|                 |                 |                 | Dateipfades im  |
|                 |                 |                 | Skript:         |
|                 |                 |                 | ``...\Metadaten |
|                 |                 |                 | \Erzeugen\Proje |
|                 |                 |                 | kte\...\variabl |
|                 |                 |                 | es``            |
|                 |                 |                 | Ausführen (Strg |
|                 |                 |                 | + r)            |
+-----------------+-----------------+-----------------+-----------------+
| `responserateSV |  erstellt für   | Responserate    |  für eine       |
| G <https://gith | alle csv        | Diagramme für   | Survey muss     |
| ub.com/dzhw/met | Tabellen im     | den             | eine csv        |
| adatamanagement | Ordner \\csv    | Surveyexport    | Tabelle         |
| -io/blob/master | jeweils ein     |                 | erstellt werden |
| /generation/exa | deutsches und   |                 | die Angaben     |
| mple/survey/Res | ein englisches  |                 | ``einghauf, ein |
| ponserate/respo | Diagramm die    |                 | ghaufcum, datum |
| nserateSVG.R>`_ | Diagramme       |                 | w``             |
| _               | werden als      |                 | müssen in der   |
|                 | ``responserate_ |                 | csv Tabelle     |
|                 | de.svg und resp |                 | stehen          |
|                 | onserate_en.svg |                 | Ausführen (Strg |
|                 | ``              |                 | + r)            |
|                 | im Ordner       |                 |                 |
|                 | \\images        |                 |                 |
|                 | gespeichert     |                 |                 |
+-----------------+-----------------+-----------------+-----------------+

.. _-die-struktur-der-dateipfade-muss-immer-eingehalten-werden-nach-dem-beispiel-test2017-sonst-funktioniert-es-nicht:

⚠ Die Struktur der Dateipfade muss immer eingehalten werden (nach dem Beispiel test2017), sonst funktioniert es nicht!
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Response Rate Diagram
~~~~~~~~~~~~~~~~~~~~~
Das Skript
[responserateSVG](http://github.com/dzhw/metadatamanagement-io/blob/master/generation/example/survey/Responserate/responserateSVG.R)
erzeugt Responserate Diagramme als SVG in deutsch und englisch für die
verschiedenen Surveys. Benötigt wird dafür je eine csv Tabelle pro
Survey, in der folgende Spalten enthalten sind: - einghauf = Rücklauf
pro Woche - einghaufcum = Rücklauf kummuliert - datumw = Wochendatum im
Format Jahr_w_Kalenderwoche (z.B. 2006w52)
