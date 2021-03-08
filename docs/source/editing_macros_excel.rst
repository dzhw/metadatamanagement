Editing Macros Excel
====================
Die folgenden Makros können unterstützend nach nach dem manuellen
Ausfüllen der Exceltabelle vimport_ds\ **Nr**.xlsx genutzt werden. Vor
dem Ausführen der Makros sollte sichergestellt werden, dass die
Tabellenblätter der Exceltabelle richtig benannt wurden (variables und
relatedQuestions) und die für das jeweilige Makro notwendigen Spalten
vorhanden sind.

Wie werden Makros ausgeführt?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Um Makros in Excel zu nutzen, wird der VBA Editor benötigt. Dieser wird
mit Alt+F11 geöffnet. Über ``Einfügen > Modul`` wird ein neues Modul
angelegt, in das die fertigen Skripte einfach reinkopiert werden können.
In der Excel Tabelle können die Makros über
``Ansicht > Makros > Makros anzeigen > Ausführen`` ausgeführt werden.

Makro-Übersicht
~~~~~~~~~~~~~~~

repeatedMeasurementIdentifier
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Wo wird es genutzt? repeatedMeasurementIdentifier_ in vimport.xlsx


 - generiert den repeatedMeasurementIdentifier durch den Vergleich vom Variablenstamm
   (ohne-Präfix)
 - dabei dürfen Variablennamen verschiedene Versionierungen und/oder
   Zugangswege-haben
 - das FDZ Variablenschema muss verwendet worden sein (Variablenname
   z.B.-astu01_g1v1r mit den möglichen Zugangswegen c,d,o,r,a)
 - im Tabellenblatt variables müssen die beiden Spaltennamen name
   und-repeatedMeasurementIdentifier vorhanden sein
 - der Projektnamen und die Datensatznummer werden über eine Inputbox eingegeben
 - Beispiel: der repeatedMeasurementIdentifier zu adem01_g1v1r und bdem01_g1v3r lautet
   z.B.-abs2005-ds1-dem01_g1

.. _repeatedMeasurementIdentifier: https://github.com/dzhw/metadatamanagement-io/blob/master/generation/example/Hilfsskripte/repeatedMeasurementIdentifier.txt

derivedVariablesIdentifier
^^^^^^^^^^^^^^^^^^^^^^^^^^

Wo wird es genutzt? derivedVariablesIdentifier_ in vimport.xlsx

- generiert den derivedVariablesIdentifier durch den Vergleich vom
  Variablenstamm (mit Präfix)
- das FDZ Variablenschema muss verwendet worden sein (Variablenname z.B.
  astu01_g1v1r mit den möglichen Zugangswegen c,d,o,r,a)
- im Tabellenblatt variables müssen die beiden Spaltennamen name und
  derivedVariablesIdentifier vorhanden sein
- der Projektname und die Datensatznummer werden über eine Inputbox eingegeben
- Beispiel: der derivedVariablesIdentifier zu adem01 und adem01_g1r des
  Projektes gra2005-ds1 lautet z.B. gra2005-ds1-adem01

 .. _derivedVariablesIdentifier: https://github.com/dzhw/metadatamanagement-io/blob/master/generation/example/Hilfsskripte/derivedVariablesIdentifier.txt

accessWaysInOneColumn
^^^^^^^^^^^^^^^^^^^^^

accessWaysInOneColumn-Skript_


Wo wird es genutzt?  accessWays in vimport.xlsx - wenn die vier Spalten nicht
verfügbar im Download-CUF, nicht verfügbar im Download-SUF, nicht verfügbar im
Remote-Desktop-SUF und nicht verfügbar im On-Site-SUF ausgefüllt wurden

- wurden in der Excel Tabelle die vier Spalten nicht verfügbar im Download-CUF,
  nicht verfügbar im Download-SUF, nicht verfügbar im Remote-Desktop-SUF und
  nicht verfügbar im On-Site-SUF durch ankreuzen mit "x" ausgefüllt, können
  daraus die Zugangswege in einer Spalte generiert werden
- dafür müssen die Spaltennamen nicht verfügbar im Download-CUF, nicht
  verfügbar im Download-SUF, nicht verfügbar im Remote-Desktop-SUF, nicht
  verfügbar im On-Site-SUF, accessWays und name im Tabellenblatt variables
  vorhanden sein
- Beispiel: wurden alle vier Spalten einer Variablen mit "x" ausgefüllt, wird
  der zugehörige Zugangsweg "not-accessible" ermittelt

.. _accessWaysInOneColumn-Skript:  https://github.com/dzhw/metadatamanagement-io/blob/master/generation/example/Hilfsskripte/accessWaysInOneColumn.txt


matchSurveyNumbers
^^^^^^^^^^^^^^^^^^

`matchSurveyNumbers <https://github.com/dzhw/metadatamanagement-io/blob/master/generation/example/Hilfsskripte/matchSurveyNumbers.txt>`__

Wo wird es genutzt? surveyNumbers in vimport.xlsx - wenn das Präfix im
Variablennamen in Abhängigkeit von der surveyNumber vergeben wurde

- wurden das Präfix des Variablennamens in Abhängigkeit von der surveyNumber
  vergeben, kann die surveyNumber automatisch generiert werden
- es muss ein zusätzlichen Tabellenblatt mit dem Namen optionalEntries eingefügt
  werden
- dieses Tabellenblatt hat 2 Spalten mit den Namen prefix (hier steht z.B. a, b
  oder c) und surveyNumber (hier wird die zum Präfix zugehörige Nummer der
  Erhebung eingetragen)
- das Tabellenblatt variables muss die beiden Spalten name und surveyNumbers
  enthalten
- Beispiel: alle Variablen mit dem Präfix "a" (z.B. astu01, adem05) bekommen die
  surveyNumber 1, alle Variablen mit dem Präfix "b" (z.B. bstu02, bdem03)
  bekommen die surveyNumber 2, alle Variablen mit abweichendem (bzw. ohne)
  Präfix (z.B. pid, wave, wgt-Variablen) bekommen die surveyNumbers 1,2




addRelatedQuestionsForGeneratedVariables
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

`addRelatedQuestionsForGeneratedVariables <https://github.com/dzhw/metadatamanagement-io/blob/master/generation/example/Hilfsskripte/addRelatedQuestionsForGeneratedVariables.txt>`__

Wo wird es genutzt?  vimport.xlsx > relatedQuestions - das Makro ergänzt im
Tabellenblatt relatedQuestions generierte Variablen von Fragebogenvariabeln.
Das Makro funktioniert nur, wenn die generierten, wenn die generierte Variablen
denselben Stamm haben, wie ihre Ausgangsvariablen (z.B. astu01_g1 von astu01),
d.h. wenn das FDZ-Variablennamenschema (mindestens Silber) verwendet wurde!

- Es müssen alle Variablen aus dem Datensatz im Tabellenblatt variables
  eingetragen sein.
- Es müssen alle Fragebogenvariablen (d. h. "Originalvariablen") inkl.
  questionNumber und instrumentNumber im Tabellenblatt relatedQuestions
  eingetragen sein.
- Das Makro gleicht die Variablenliste vom Tabellenblatt relatedQuestions mit
  der aus dem Tabellenblatt variables ab und ergänzt die generierten Variablen
  der eingetragenen Fragebongenvariablen im Tabellenblatt relatedQuestions.
- Die generierten Variablen werden inklusive questionNumber und instrumentNumber
  am Ende des Tabellenblatts eingefügt
- zur Kontrolle werden die neuen Zellen blau gefüllt
- relatedQuestionString.de/.en muss nicht ausgefüllt werden. name im
  Tabellenblatt variables und name, questionNumber und instrumentNumber im
  Tabellenblatt relatedQuestions müssen gefüllt sein- name, questionNumber,
  instrumentNumber im Tabellenblatt relatedQuestions
