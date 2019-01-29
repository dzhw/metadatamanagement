[WARNING] Reference not found for 'Key "---------------"' at chunk line 1 column 69
# Stata-Skripte

| Name \| Was kann es? \| Wo wird es genutzt? \| Was muss manuell
  gemacht werden? \|
| ------------ \| --------- \| ------------
| [Metadatenexport-master-ProjID.do](https://github.com/dzhw/metadatamanagement-io/blob/master/generation/example/variables/Stata/Metadatenexport-master-abs2005.do)
  \| <ul> <li> definiert die Pfade zu den Import und Export Ordnern
  </li><br><li> wird zum Ausführen der 3 folgenden Skripte genutzt </li>
  <br><li> löscht alle vorhanden Dateien in den Exportordnern </li> \|
  Variablenexport im geschützten Bereich \| <ul> <li> Angabe des Pfades
  zum Metadatenexport Ordner des jeweiligen Projektes:
  /var/proj/fdz/Projekte/.../.../Metadatenexport </li><br><li> Ausführen
  (Strg + d) </li>\|
| [xlsxToDta.do](https://github.com/dzhw/metadatamanagement-io/blob/master/generation/example/variables/Stata/xlsxToDta.do)
  \| <ul> <li> reichert den Datensatz mit Metadaten aus vimport.xlsx an
  </li><br><li> exportiert den angereicherten Datensatz nach
  Export/dta/dsNrexport.dta </li> \| Variablenexport im geschützten
  Bereich \| <ul> <li> für jeden Datensatz muss eine Tabelle
  vimport_dsNr.xlsx fertig ausgefüllt sein </li><br><li> diese müssen im
  Ordner Import/xlsx liegen </li> <br><li> die Missingsystemamtik im
  Do-File muss angepasst werden wenn sie nicht der FDZ.DZHW-Systematik
  entspricht </li> <br><li> die Label-Language in Stata muss **de**
  und/oder **en** sein </li> \|
| [dtaToCsvVar.do](https://github.com/dzhw/metadatamanagement-io/blob/master/generation/example/variables/Stata/dtaToCsvVar.do)
  \| <ul> <li> liest den angereicherten Datensatz ein </li><br><li>
  erstellt für alle Variablen eine Datei summary.csv mit Maßzahlen
  </li><br><li> speichert diese im Ordner Export/csv/variable </li> \|
  Variablenexport im geschützten Bereich \| <ul> <li> die summary.csv
  muss in den öffentliche Bereich transferiert werden </li> \|
| [dataToCsvVal.do](https://github.com/dzhw/metadatamanagement-io/blob/master/generation/example/variables/Stata/dataToCsvVal.do)
  \| <ul> <li> liest den angereicherten Datensatz ein </li><br><li>
  erstellt für jede Variable eine Datei dsNr-Varname.csv mit den
  einzelnen Häufigkeitstabellen </li><br><li> speichert diese im Ordner
  Export/csv/values </li> \| Variablenexport im geschützten Bereich \|
  <ul> <li> alle Dateien dsNr-Varname.csv müssen in den öffentliche
  Bereich transferiert werden </li> <br><li> die Missingsystemamtik im
  Do-File muss angepasst werden wenn sie nicht der FDZ.DZHW-Systematik
  entspricht </li> \|

#### &#9888; Die im Skript Metadatenexport-master-ProjID.do festgelegt
Ordnung der Dateipfade muss eingehalten werden, sonst funktioniert es
nicht!
