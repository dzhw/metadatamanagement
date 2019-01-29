Excel Makros verwenden
======================

Um Makros in Excel zu nutzen, wird der VBA Editor benötigt. Dieser wird
mit Alt+F11 geöffnet. Über ``Einfügen`` > ``Modul`` wird ein neues Modul
angelegt, in das die fertigen Skripte einfach reinkopiert werden können.

In der Excel Tabelle können die Makros über ``Ansicht`` > ``Makros`` >
``Makros anzeigen`` > ``Ausführen`` gestartet werden.

+-----------------------+-----------------------+-----------------------+
| Name                  | Was kann es?          | Wo wird es genutzt?   |
+=======================+=======================+=======================+
| `panelIdentifier <htt |  generiert den        | panelIdientifier in   |
| ps://github.com/dzhw/ | panelIdentifier durch | variables.xlsx        |
| metadatamanagement-io | den Vergleich von     |                       |
| /blob/master/generati | Variablennamendabei   |                       |
| on/example/Hilfsskrip | dürfen Variablennamen |                       |
| te/panelIdentifier.tx | verschiedene          |                       |
| t>`__                 | Versionierungen       |                       |
|                       | und/oder Zugangswege  |                       |
|                       | haben die Eingabe des |                       |
|                       | Projektnamens, der    |                       |
|                       | Datensatznummer, der  |                       |
|                       | Spalte in die der     |                       |
|                       | panelIdentifier       |                       |
|                       | eingefügt werden soll |                       |
|                       | und die Auswahl der   |                       |
|                       | Variablen erfolgt     |                       |
|                       | über eine             |                       |
|                       | Inputbox\ **Beispiel: |                       |
|                       | **                    |                       |
|                       | der panelIdentifier   |                       |
|                       | zu adem01_g1v1r und   |                       |
|                       | bdem01_g1v3r lautet   |                       |
|                       | z.B.                  |                       |
|                       | abs2005-ds1-dem01_g1  |                       |
+-----------------------+-----------------------+-----------------------+
| `addPrefix <https://g |  fügt einer oder      | Panelvariablen in     |
| ithub.com/dzhw/metada | mehreren Variablen in | manMetadaten.xlsx     |
| tamanagement-io/blob/ | einer Zelle und durch |                       |
| master/generation/exa | Komma getrennt ein    |                       |
| mple/Hilfsskripte/add | Präfix hinzu (z.B.    |                       |
| Prefix.txt>`__        | abs2005)der Bereich   |                       |
|                       | in der die Variablen  |                       |
|                       | stehen und das        |                       |
|                       | Projektkürzel werden  |                       |
|                       | über eine InputBox    |                       |
|                       | eingegeben            |                       |
|                       | \ **Beispiel:**       |                       |
|                       | bski01d_v1,bski01a_v1 |                       |
|                       | ,                     |                       |
|                       | bski01u_v1 wird zu    |                       |
|                       | abs2005-bski01d_v1,ab |                       |
|                       | s2005-bski01a_v1,abs2 |                       |
|                       | 005-bski01u_v1        |                       |
+-----------------------+-----------------------+-----------------------+
