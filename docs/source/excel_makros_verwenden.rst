Excel Makros verwenden
======================

Um Makros in Excel zu nutzen, wird der VBA Editor benötigt. Dieser wird
mit Alt+F11 geöffnet. Über ``Einfügen`` > ``Modul`` wird ein neues Modul
angelegt, in das die fertigen Skripte einfach reinkopiert werden können.

In der Excel Tabelle können die Makros über ``Ansicht`` > ``Makros`` >
``Makros anzeigen`` > ``Ausführen`` gestartet werden.

+---------------------------------+-------------------------------+-------------------------------+
|    Name                         | Was kann es?                  | Wo wird es genutzt?           |
+=================================+===============================+===============================+
| repeatedMeasurementIdentifier_  | Generiert den                 | repeatedMeasurementIdentifier |
|                                 | RepeatedMeasurementIdentifier | in variables.xlsx             |
|                                 | durch den Vergleich von       |                               |
|                                 | Variablennamen.               |                               |
|                                 |                               |                               |
|                                 | Dabei                         |                               |
|                                 | dürfen Variablennamen         |                               |
|                                 | verschiedene                  |                               |
|                                 | Versionierungen               |                               |
|                                 | und/oder Zugangswege          |                               |
|                                 | haben.                        |                               |
|                                 | Die Eingabe des               |                               |
|                                 | Projektnamens, der            |                               |
|                                 | Datensatznummer, der          |                               |
|                                 | Spalte in die der             |                               |
|                                 | repeatedMeasurementIdentifier |                               |
|                                 | eingefügt werden soll         |                               |
|                                 | und die Auswahl der           |                               |
|                                 | Variablen erfolgt             |                               |
|                                 | über eine                     |                               |
|                                 | Inputbox                      |                               |
|                                 |                               |                               |
|                                 | Beispiel: der                 |                               |
|                                 | repeatedMeasurementIdentifier |                               |
|                                 | zu adem01_g1v1r und           |                               |
|                                 | bdem01_g1v3r lautet           |                               |
|                                 | z.B.                          |                               |
|                                 | abs2005-ds1-dem01_g1          |                               |
|                                 |                               |                               |
+---------------------------------+-------------------------------+-------------------------------+
| addPrefix_                      | Fügt einer oder               | Panelvariablen in             |
|                                 | mehreren Variablen in         | manMetadaten.xlsx             |
|                                 | einer Zelle und durch         |                               |
|                                 | Komma getrennt ein            |                               |
|                                 | Präfix hinzu (z.B.            |                               |
|                                 | abs2005).                     |                               |
|                                 | Der Bereich                   |                               |
|                                 | in der die Variablen          |                               |
|                                 | stehen und das                |                               |
|                                 | Projektkürzel werden          |                               |
|                                 | über eine InputBox            |                               |
|                                 | eingegeben                    |                               |
|                                 | Beispiel:                     |                               |
|                                 | bski01d_v1,bski01a_v1         |                               |
|                                 | bski01u_v1 wird zu            |                               |
|                                 | abs2005-bski01d_v1,ab         |                               |
|                                 | s2005-bski01a_v1,abs2         |                               |
|                                 | 005-bski01u_v1                |                               |
+---------------------------------+-------------------------------+-------------------------------+

.. _repeatedMeasurementIdentifier: https://github.com/dzhw/metadatamanagement-io/blob/master/generation/example/Hilfsskripte/repeatedMeasurementIdentifier.txt
.. _addPrefix: https://github.com/dzhw/metadatamanagement-io/blob/master/generation/example/Hilfsskripte/addPrefix.txt
