Testen von MDM-Issues
=====================

Issues des metadatamanagement-Repos werden im Testsystem getestet:
https://test.metadata.fdz.dzhw.eu Für einige Issues ist eine
Anmeldung notwendig.

Wie genau beim Testen vorzugehen ist, hängt stark vom jeweiligen Issue
ab.

Generell sind die folgenden Punkte zu beachten:

-  ist alles so umgesetzt, wie erwartet?
-  verschiedene Browser benutzen (Firefox, Chrome, Internet Explorer 11
   und - wenn möglich - Edge)
-  sowohl deutsche als auch englische Seiten testen
-  Issues, die den Upload betreffen, können nur über Chrome und Firefox
   getestet werden, da der Upload über IE nicht funktioniert
-  bei Issues, die den Upload betreffen, auch prüfen, ob das
   Fehlerprotokoll nachvollziehbar ist
-  nicht nur prüfen, ob etwas funktioniert, sondern auch, ob es nicht
   funktioniert, wenn es nicht funktionieren soll
-  um den Upload mit gültigen und nicht-gültigen Daten zu prüfen: Daten
   im io-Testprojekt manipulieren
-  überprüfen, ob Funktionen auch auf verkleinertem Bildschirm
   funktionieren (responsives Design)
-  überprüfen, ob Funktionen auch bei Verwendung eines Handys
   funktionieren (auch hier versch. Browser testen)
-  prüfen, ob die wiki-Dokumentation im metadatamanagement-Repo richtig
   angelegt wurde

Wenn **keine Fehler gefunden** wurden: Label "status: testing"
entfernen, unassignen und Issue schließen. Wenn **Fehler gefunden**
wurden: Fehler im Issue möglichst genau dokumentieren (gerne auch
Screenshots), label "status: testing" entfernen und "status:
development" hinzufügen, sich selbst unassignen und den Developer
assignen.
