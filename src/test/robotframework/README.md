# E2E Test mit dem RobotFramework

# Ausführen der Tests

## Tests lokal ausführen

In der Datei `common_variables.yaml` muss der Parameter `website` angepasst werden.
Für eine lokale Ausführung ist `website: http://localhost:8080/de/start` notwendig.
Für die Ausführung auf der Entwicklungsinstanz ist `website: https://dev.metadata.fdz.dzhw.eu/de/start` zu verwenden.

## Ausführen ganzer Test Suites

Es gibt drei Suites, die die unterschiedlichen Rollen des MDM abbilden. Beispielhaft sind hier die notwendigen
Befehle ausgeführt, die die Tests in Chrome für die jeweilige Rolle starten.

- Publisher
  ``` shell
  robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs/chrome -v BROWSER:chrome --include publisherNOTfirefoxonly ./src/test/robotframework
  ```
- Dataprovider
  ``` shell
  robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs/chrome -v BROWSER:chrome --include dataproviderNOTfirefoxonly ./src/test/robotframework
  ```
- Public User
  ``` shell
  robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs/chrome -v BROWSER:chrome --include publicuserNOTfirefoxonly ./src/test/robotframework
  ```

## Einzelne Tests ausführen

Parameter `-t` erlaubt es einen Test Case Namen anzugeben. Der Parameter kann mehrfach verwendet werden.

``` shell
robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs/chrome -v BROWSER:chrome --include publisherNOTfirefoxonly -t CreateAnalysisPackage ./src/test/robotframework
```

Mit dem Parameter `-s` kann eine ganze Test Suite angegeben werden. Es werden alle darin enthaltenen Tests ausgeführt.

``` shell
robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs/chrome -v BROWSER:chrome --include publisherNOTfirefoxonly -s conceptmanagement ./src/test/robotframework
```

Die Namen von Test Cases und Test Suites müssen immer zusammengeschrieben werden und sind case-INsensitiv.

Zu beachten ist auch, dass beim einzelnen Ausführen von Test Cases der Setup/Teardown Prozess der zugehörigen Test Suite
nicht ausgeführt wird.

## Breakpoints setzen

Im Test kann folegen Zeile eingefügt werden, um einen Breakpoint zu setzen.

```robot
Evaluate    pdb.Pdb(stdout=sys.__stdout__).set_trace()    modules=sys, pdb
```

Mit `continue` in der Console kann der Test forgesetzt werden

## Reports

Die Logs und Reporte der ausgeführten Test sind im Projektverzeichnis unter `/target/test/robotframework/logs` zu finden.
Mit dem Parameter `-d` kann ein anderes Verzeichnis für das Ablegen der Reporte angegeben werden.

## Übersicht Testprojekte

### Im Test angelegte Projekte (temporäre Projekte)

Für unterschiedliche Test Suites in den Tests werden Testprojekte angelegt. Gelegentlich wurden diese durch das
Fehlschlagen von Tests nicht gelöscht. Vor dem neuen Ausführen der Tests es es notwendig diese Projekte
zu löschen. Alle zu löschenden Projekte beinhalten den Ausdruck `temprobot`

- `temprobotcheckaccess[BROWSER]`
- `temprobotassignroles[BROWSER]`
- `temprobotdeleteroles[BROWSER]`
- `atemprobotcheckicons[BROWSER]`
- `tempdatapackage[BROWSER]`
- `tempanalysis[BROWSER]`

Der Common-Teardown-Prozess (hinterlegt in `./__init__.robot`) wurde so angepasst, dass die aufgeführten Projekte gelöscht werden, sollten sie noch
vorhanden sein.

### Notwendige Projekte

Projekte und Daten, die zwingend im Datenstand vorhanden sein müssen:

- `robotprojectrelease4[BROWSER]` 
- `robotproject4[BROWSER]` 
- `robotproject`
- `cmp2014` 
- `gra2005` 
- `conceptproject[BROWSER]`
- `fileuploadproject` 
- `testanalysispackage`
- Konzept `Roll Back Concept [BROWSER] De` mit einer Vorgängerversion, die den Titel "Roll Back Concept [BROWSER] De_Rollback"
- Konzept `Referenced Concept [BROWSER] De`, welches in einem Instrument referenziert werden muss (im Dump bspw. `cmp2014`)

Die Projekte müssen nicht nur angelegt sein, sondern auch die entsprechenden Daten enthalten.

Im Common-Setup vor dem Start der Tests wird überprüft, ob die aufgeführten Projekte (nicht die Konzepte) vorhanden sind. Ist das nicht 
der Fall wird der gesamte Testdurchlauf abgebrochen.

### Infos zu verwendeten Daten in Tests

Wo notwendig wurde die Test Suite mit Metadaten unter dem Stichwort "Info on data" versehen. Dort ist der Name des 
verwendeten Testprojektes angegeben und ob es nach Ablauf des Tests gelöscht wird oder nicht. Diese Angaben werden auch 
im Log des Tests hinterlegt.

### MongoDB-Dump

Für das lokale Ausführen der Tests kann der Dump unter `https://metadatamanagement-public.s3.eu-central-1.amazonaws.com/20220926_metadatamanagement_e2e.zip` verwendet werden. Dieser enthält alle
notwendigen Projekte und Daten.
