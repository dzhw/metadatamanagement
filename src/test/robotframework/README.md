# E2E Test mit dem RobotFramework

# TODOS
Kritisch:
- hart kodierte Zugänge
  
- Teardown, das Testprojekte löscht, 
  damit Ausgangslage immer gleich ist
  
- datenstandabhängige Testanforderungen (bspw. Prüfung auf bestimmte Version)

- Gleiche Benennung von Testprojekten über verschiedene Test Suits hinweg (bereits geändert)

Optional:
- doppelte Definition von Keyword bzw. inkonsistente Verwendung (bspw. Switch to Status Tab)

- Untereinander abhängige Test Cases
  Setup: Publisher wird eingeloggt
  Test 1: Publisher ausloggen, Dataprovider einloggen & wieder ausloggen
  Test 2: Publisher einloggen
  beim Nacheinander ausführen kein Problem
  wird nur Test 2 ausgeführt schlägt der Test fehl, weil Publisher bereits eingeloggt
  
- Dokumentation




## Einzelne Tests ausführen

Parameter -t erlaubt es einen Test Case Namen anzugeben

``` shell
robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs/chrome -v BROWSER:chrome --include publisherNOTfirefoxonlyNOTlocalonly -t CreateAnalysisPackage ./src/test/robotframework
```


## Breakpoints setzen

```robot
Evaluate    pdb.Pdb(stdout=sys.__stdout__).set_trace()    modules=sys, pdb
```

Mit `continue` in der Console kann der Test forgesetzt werden

## Übersicht Testprojekte

Für unterschiedliche Test Suites werden Testprojekte angelegt. Gelegentlich werden diese durch das
Fehlschlagen von Tests nicht gelöscht. Vor dem neuen Ausführen der Tests es es notwendig diese Projekte
zu löschen. Alle zu löschenden Projekte beginnen mit dem Ausdruck `temprobot`

- `temprobotcheckaccess`
- `temprobotassignroles`
- `temprobotdeleteroles`
- `temprobotcheckicons`

Projekte die zwingend im Datenstand vorhanden sein müssen:

- robotprojectrelease4[Browser]
- robotproject (wie im WIKI beschrieben)
- cmp2014 Projekt mit allen Inhalten
- gra2005 mit allen Inhalten



