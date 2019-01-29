# ID-Vergabe
Manuell vergebene ids (DAP-ids) müssen in folgender [Tabelle](https://github.com/dzhw/metadatamanagement-io/wiki/unterlagen/Projektuebersicht.xlsx) festgehalten werden.

## Logik

| Metadaten | Id-Generierung |
| ------- | -------- |
| DataAcquesitionProject (DAP-id) | wird manuell vergeben, siehe Tabell oben <br>Übersicht über alle Projekte des DZHW siehe [hier](https://github.com/dzhw/metadatamanagement-io/wiki/unterlagen/study_ids.xlsx) |
| Study | "stu-" + DAP-id + "$" |
| Survey | "sur-" + DAP-id + "-" + "sy" + survey.number + "$" |
| DataSet | "dat-" + DAP-id + "-" + "ds" + dataSet.number + "$" |
| Variable | "var-" + DAP-id + "-" + "ds" + variable.dataSetNumber + "-" + variable name + "$" |
| Instrument | "ins-" + DAP-id + "-" + "ins" + number + "$" |
| Question | "que-" + DAP-id + "-ins" + instrumentNumber + "-" + number + "$" |
| relatedPublication | "pub-" + citaviId + "$" |

## Beispiele am Absolventenpanel 2005

| Metadaten | Id  |
| --------- | --- |
| DataAcquesitionProject | gra2005 |
| Study | stu-gra2005$ |
| Survey | sur-gra2005-sy1$ |
| DataSet | dat-gra2005-ds1$ |
| Variable | var-gra2005-ds1-stu01$ |
| Instrument | ins-gra2005-ins1$ |
| Question | que-gra2005-ins1-1.1$ |
| BibliographicalReference | pub-Meier.2010$ |
