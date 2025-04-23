'use strict';

angular.module('metadatamanagementApp').config([
  '$translateProvider',

  function($translateProvider) {
    var translations = {
      //jscs:disable
      'shopping-cart': {
        'title': 'Warenkorb',
        'detail': {
          'table-title': 'Ihre ausgewählten Produkte',
          'hint': 'Hinweis',
          'label': {
            'title': 'Datenpakettitel',
            'access-way': 'Zugangsweg',
            'version': 'Version',
            'annotations': 'Bemerkungen',
            'product-options': 'Optionen des zugehörigen Datenpakets',
            'access-way-of-data-sets': 'Zugangsweg zu den Datensätzen',
            'version-of-data-sets': 'Version der Datensätze',
            'available-versions': 'Verfügbare Versionen',
            'available-access-ways': 'Verfügbare Zugangswege',
            'number-data-sets': 'Datensätze',
            'number-variables': 'Variablen',
            'current': 'aktuell',
            'not-current': 'nicht aktuell',
            'this-data-product': 'Dieses Datenpaket',
            'this-analysis-product': 'Dieses Analysepaket',
            'study-series': 'aus der Studienreihe "{{series}}"',
            'contains': 'enthält',
            'following': '{packages, plural, =0{für folgende} =1{für folgendes} other{für folgende}}',
            'software-packages': '{packages, plural, =0{unbekannt viele Softwarepakete} =1{Softwarepaket} other{Softwarepakete}}',
            'variables': '{variables, plural, =0{unbekannt viele Variablen} =1{eine Variable} other{{formattedVariables} Variablen}}',
            'scripts': '{scripts, plural, =0{unbekannt viele Skripte} =1{ein Skript} other{{formattedScripts} Skripte}}',
            'additionally': 'Außerdem enthält es',
            'custom-data-packages': '{packages, plural, =0{unbekannt viele benutzerdefinierte Datenpakete} =1{ein benutzerdefiniertes Datenpaket} other{{formattedPackages} benutzerdefinierte Datenpakete}}',
            'in': 'in',
            'data-sets': '{dataSets, plural, =0{unbekannt vielen Datensätzen.} =1{einem Datensatz.} other{{formattedDataSets} Datensätzen.}}',
            'qualitative-data-sets': '{dataSets, plural, =0{unbekannt viele Datensätze.} =1{einen Datensatz.} other{{formattedDataSets} Datensätze.}}',
            'data-package': 'Datenpaket',
            'customer-name': 'Ihr Name',
            'customer-email': 'Ihre E-Mailadresse',
            'data-formats': 'Die Datensätze enthalten Daten in den folgenden Formaten:',
            'data-languages': 'Außerdem sind die Datensätze in den folgenden Sprachen verfügbar:',
            'cite-data-package': 'Datenpaket zitieren:',
            'cite-analysis-package': 'Analysepaket zitieren:',
            'cite-method-report': 'Daten- und Methodenbericht zitieren:',
            'cite-questionnaire': 'Fragebogen/Variablenfragebogen zitieren:'
          },
          'hints': {
            'name': 'Bitte teilen Sie uns Ihren vollständigen Namen mit.',
            'email': 'Bitte teilen Sie uns Ihre E-Mailadresse mit, damit wir Sie kontaktieren können.',
            'accessWay': 'Benötigen Sie einen CUF oder SUF und wie möchten Sie mit den Daten arbeiten?',
            'version': 'Welche Version der Datensätze benötigen Sie?'
          },
          'thank-you': 'Vielen Dank für Ihr Interesse an unseren Datenpaketen!',
          'dlp-redirect': 'Sie werden in {{seconds}} Sekunden zu unserem Dienstleistungsportal weitergeleitet...',
          'empty-cart-text': 'Ihr Warenkorb ist aktuell leer. Sie können <a href="/de/search?type=data_packages"><strong>hier</strong></a> nach Datenpaketen suchen und diese Ihrem Warenkorb hinzufügen.',
          'warn-not-current-versions': 'Da Sie sich nicht für die aktuelle Version dieses Datenpakets entschieden haben, kann Ihnen dieses System keine genaue Auskunft über die Anzahl an Variablen und Datensätzen des Produktes anzeigen.',
          'explain-data-product': 'Ein Datenpaket beinhaltet immer alle Datensätze, die für den gewählten Zugangsweg (Download, On-Site oder Remote-Desktop) aufbereitet wurden. Sie können mehrere Datenpakete mit unterschiedlichen Zugangswegen beantragen.',
          'no-final-release': 'Die Datenpakete wurden noch nicht erstellt. Sobald diese fertig sind können Sie sie an dieser Stelle in den Warenkorb legen.',
          'variable-not-accessible': 'Diese Variable wurde zwar erhoben, ist aber aus datenschutzrechtlichen Gründen in keinem Datenpaket verfügbar.',
          'data-package-tooltip': 'Klicken, um das Datenpaket anzuzeigen.',
          'analysis-package-tooltip': 'Klicken, um das Analysepaket anzuzeigen.',
          'study-series-tooltip': 'Klicken, um alle Datenpakete der Studienreihe anzuzeigen.',
          'data-sets-tooltip': 'Klicken, um alle Datensätze dieses Datenpaketes anzuzeigen.',
          'variables-tooltip': 'Klicken, um alle Variablen dieses Datenpaketes anzuzeigen.',
          'scripts-tooltip': 'Klicken, um alle Skripte dieses Analysepaketes anzuzeigen.',
          'custom-data-packages-tooltip': 'Klicken, um alle benutzerdefinierten Datenpakete dieses Analysepaketes anzuzeigen.',
          'software-packages-tooltip': 'Klicken, um alle Softwarepakete dieses Analysepaketes anzuzeigen.',
          'citation': 'Datenpaket, Daten- und Methodenbericht und Fragebogen/Variablenfragenbogen zitieren',
          'citation-success-copy-to-clipboard': 'Der Zitationshinweis wurde erfolgreich in die Zwischenablage kopiert.',
          'copy-citation-tooltip': 'Klicken, um den Zitationshinweis in die Zwischenablage zu kopieren.',
          'download-bibtex-tooltip': 'Klicken, um die Zitationsdetails im BibTex-Format zu speichern.',
          'citation-node': 'Wir bitten Sie, den Daten- und Methodenbericht zu prüfen, ob Angaben zu weiteren Quellen der spezifisch zu zitierenden Frage existieren. Bei Vorliegen entsprechender Angaben schlagen wir vor, im Sinn guter wissenschaftlicher Praxis die im Daten- und Methodenbericht genannte(n) Originalquelle(n) ebenfalls zu zitieren. Bei einer Wiedergabe des vollständigen Texts einer Frage, etwa innerhalb eines Artikels oder bei einer Nachnutzung der Frage für eigene Forschungsvorhaben bzw. Fragebögen, ist zu beachten, dass mglw. weitere Nutzungsbedingungen bestehen. Bitte prüfen Sie die Quellen dahingehend.',
          'select-access-way-title': 'Bitte Zugangsweg auswählen',
          'select-access-way-for-ordering': 'Bitte wählen Sie einen Zugangsweg aus, um das Datenpaket in den Warenkorb legen zu können.',
          'select-access-way-for-citation': 'Bitte wählen Sie einen Zugangsweg aus, um das Datenpaket zitieren zu können.',
          'note': 'Wenn Sie Probleme mit dem Bestellprozess haben, wenden Sie sich bitte an <a href="mailto:userservice@dzhw.eu">userservice@dzhw.eu</a>.',
          'maintenance-hint': 'Am Mittwoch, 25.01 und Donnerstag, 26.01. steht der Bestellprozess aufgrund von Wartungsarbeiten nur eingeschränkt zur Verfügung.',
          'analysis-package': {
            'citation': 'Analysepaket zitieren'
          }
        },
        'error': {
          'synchronize': 'Der Warenkorb konnte nicht mit dem Server synchronisiert werden.',
          'already-completed': 'Die Bestellung wurde bereits abgeschlossen. Ihr Warenkorb wurde geleert.',
          'customer': {
            'name': {
              'empty': 'Ihr Name darf nicht leer sein.',
              'string-size': 'Ihr Name darf nicht länger als 128 Zeichen sein.'
            },
            'email': {
              'empty': 'Ihre E-Mailadresse darf nicht leer sein.',
              'string-size': 'Ihre E-Mailadresse darf nicht länger als 128 Zeichen sein.',
              'invalid': 'Ihre E-Mailadresse ist ungültig.'
            }
          }
        },
        'toasts': {
          'data-package-added': 'Das Datenpaket {{id}} wurde in den Warenkorb gelegt.',
          'data-package-already-in-cart': 'Das Datenpaket {{id}} ist bereits im Warenkorb.',
          'analysis-package-added': 'Das Analysepaket {{id}} wurde in den Warenkorb gelegt.',
          'analysis-package-already-in-cart': 'Das Analysepaket {{id}} ist bereits im Warenkorb.',
          'order-has-validation-errors-toast': 'Ihr Formular enthält ungültige Angaben.',
          'error-on-saving-order': 'Beim Senden Ihrer Bestellung trat ein Fehler auf.'
        },
        'buttons': {
          'checkout': 'Kostenlos bestellen',
          'add-data-package': 'In den Warenkorb',
          'add-data-package-tooltip': 'Klicken, um das Datenpaket mit den ausgewählten Optionen in den Warenkorb zu legen.',
          'add-analysis-package-tooltip': 'Klicken, um das Analysepaket mit den ausgewählten Optionen in den Warenkorb zu legen.',
          'choose-data-package-options': 'Klicken, um eine Variante dieses Datenpakets in den Warenkorb zu legen.',
          'choose-analysis-package-options': 'Klicken, um eine Variante dieses Analysepaketes in den Warenkorb zu legen.',
          'open-cart': 'Zum Warenkorb',
          'checkout-tooltip': 'Klicken, um die Datenpakete zu bestellen.',
          'remove-all': 'Warenkorb leeren',
          'remove-all-tooltip': 'Klicken, um alle Datenpakete aus dem Warenkorb zu entfernen.',
          'delete-product-tooltip': 'Klicken, um das Datenpaket aus dem Warenkorb zu entfernen.',
          'open-cart-tooltip': 'Klicken, um die Inhalte des Warenkorbs anzuzeigen.',
          'open-citation-tooltip': 'Klicken, um Zitationsinformationen zu erhalten und zu kopieren.',
          'open-citation': 'Zitieren...',
          'open-export': 'Metadaten exportieren...',
          'open-export-tooltip': 'Klicken, um die Metadaten dieses Datenpakets zu exportieren.',
          'beta-hint': 'Alle Formate generiert via da|ra (beta-Status)',
          'close-tooltip': 'Klicken, um die Produktauswahl zu verlassen.',
          'data-package-tooltip': 'Klicken, um weitere Informationen zu Datenpaketen zu erhalten.',
          'analysis-package-tooltip': 'Klicken, um weitere Informationen zu Analysepaketen zu erhalten.',
          'related-publications-tooltip': 'Klicken, um weitere Informationen zu Publikationen zu erhalten.',
          'data-package-version-tooltip': 'Klicken, um weitere Informationen zur Version von Datenpaketen zu erhalten.',
          'analysis-package-version-tooltip': 'Klicken, um weitere Informationen zur Version von Analysepaketen zu erhalten.',
          'data-package-access-way-tooltip': 'Klicken, um weitere Informationen zu Zugangswegen zu erhalten.',
          'export-ddi-variables': 'Variablenmetadaten exportieren',
          'export-ddi-variables-tooltip': 'Klicken, um die Variablen-Metadaten als DDI-Codebook-XML herunterzuladen.'
        },
        'version-info': {
          'title': 'Eine Version auswählen',
          'content': '<p style="margin-bottom: 0px;">Unsere Datenpakete liegen dreistellig versioniert vor. Die Stellen der Versionsnummer legen nahe, wie groß die Änderungen an den Daten sind. Bei Änderungen an den ersten beiden Stellen werden Sie benachrichtigt.</p><ul style="list-style-type: disc; margin-inline-start: 16px; margin-bottom: 0px;"><li>Erste Stelle (Major): Änderungen am Datensatz (abgesehen von Änderungen der Variablenlabels)</li><li>Zweite Stelle (Minor): Label ändern sich, Metadatenänderungen wie Hinzufügen weiterer Fragen oder Änderungen der Metadaten/Dokumentation, die Auswirkungen auf die Analyse haben.</li><li>Dritte Stelle (Patch): Zusätzliche Datensatzformate werden bereitgestellt, Hinzufügen/Löschen von Sprachversionen.</li></ul><strong>Für die meisten Datennutzer:innen ist die aktuellste Version der Daten relevant.</strong>',
          'close-tooltip': 'Klicken, um diesen Dialog zu schließen.',
          'analysis-package': {
            'content': '<p style="margin-bottom: 0px;">Unsere Analysepakete liegen dreistellig versioniert vor. Die Stellen der Versionsnummer legen nahe, wie groß die Änderungen an den Daten sind. Bei Änderungen an den ersten beiden Stellen werden Sie benachrichtigt.</p><ul style="list-style-type: disc; margin-inline-start: 16px; margin-bottom: 0px;"><li>Erste Stelle (Major): Änderungen an oder Hinzufügen von einem oder mehreren Analyseskript(en) und/oder benutzerdefinierten Analysedaten.</li><li>Zweite Stelle (Minor): Änderungen der Metadaten/Dokumentation.</li><li>Dritte Stelle (Patch): Hinzufügen/Löschen von Sprachversionen.</li></ul><strong>Für die meisten Datennutzer:innen ist die aktuellste Version der Daten relevant.</strong>',
          }
        },
        'access-way-info': {
          'title': 'Einen Zugangsweg auswählen',
          'content': '<p style="margin-bottom: 0px;">Für unsere Datenpakete gibt es bis zu vier Zugangswege:</p><ul style="list-style-type: disc; margin-inline-start: 16px; margin-bottom: 0px; padding-top: 0px;"><li><strong>CUF: Download</strong> erstellt für die wissenschaftliche Lehre und Übungszwecke; sehr hoher statistischer Anonymisierungsgrad; nach Antragsbewilligung verfügbar per Download</li><li><strong>SUF: Download</strong> erstellt für wissenschaftliche Forschungszwecke; hoher statistischer Anonymisierungsgrad; nach Vertragsabschluss verfügbar per Download</li><li><strong>SUF: Remote-Desktop</strong> erstellt für wissenschaftliche Forschungszwecke; moderater statistischer Anonymisierungsgrad; nach Vertragsabschluss verfügbar per Internet über virtuellen Desktop mit Input-/Output-Prüfungen</li><li><strong>SUF: On-Site</strong> erstellt für wissenschaftliche Forschungszwecke; geringer statistischer Anonymisierungsgrad; nach Vertragsabschluss verfügbar vor Ort im FDZ-DZHW in Hannover mit Input-/Output-Prüfungen</li></ul><p>Mehr Informationen: <a href="https://www.fdz.dzhw.eu/de/datennutzung">https://www.fdz.dzhw.eu/de/datennutzung</a></p>'
        }
      },
      'order-menu': {
        'back-to-project': 'Zurück zum letzten Bearbeitungsstand',
        'back-to-project-tooltip': 'Klicken, um zum letzten Bearbeitungsstand zurückzukehren'
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  }]);
