'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'shopping-cart': {
        'title': 'Einkaufswagen',
        'detail': {
          'table-title': 'Ihre ausgewählten Datenpakete',
          'hint': 'Hinweis',
          'label': {
            'title': 'Studientitel',
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
            'study-series': 'aus der Studienreihe "{{series}}"',
            'contains': 'enthält',
            'variables': '{variables, plural, =0{unbekannt viele Variablen} =1{eine Variable} other{{formattedVariables} Variablen}}',
            'in': 'in',
            'data-sets': '{dataSets, plural, =0{unbekannt vielen Datensätze.} =1{einem Datensatz.} other{{formattedDataSets} Datensätzen.}}',
            'study': 'Studie',
            'customer-name': 'Ihr Name',
            'customer-email': 'Ihre E-Mailadresse',
            'data-formats': 'Die Datensätze enthalten Daten in den folgenden Formaten:'
          },
          'hints': {
            'name': 'Bitte teilen Sie uns Ihren vollständigen Namen mit.',
            'email': 'Bitte teilen Sie uns Ihre E-Mailadresse mit, damit wir Sie kontaktieren können.',
            'accessWay': 'Benötigen Sie einen CUF oder SUF und wie möchten Sie mit den Daten arbeiten?',
            'version': 'Welche Version der Datensätze benötigen Sie?'
          },
          'thank-you': 'Vielen Dank für Ihr Interesse an unseren Datenpaketen!',
          'dlp-redirect': 'Sie werden in {{seconds}} Sekunden zu unserem Dienstleistungsportal weitergeleitet...',
          'empty-cart-text': 'Ihr Einkaufswagen ist aktuell leer. Sie können <a href="#!/de/search?type=studies"><strong>hier</strong></a> nach Datenpaketen suchen und diese Ihrem Einkaufswagen hinzufügen.',
          'warn-not-current-versions': 'Da Sie sich nicht für die aktuelle Version dieses Datenpakets entschieden haben, kann Ihnen dieses System keine genaue Auskunft über die Anzahl an Variablen und Datensätzen des Produktes anzeigen.',
          'explain-data-product': 'Ein Datenpaket beinhaltet immer alle Datensätze einer Studie, die für den gewählten Zugangsweg (download, on-site, remote,...) aufbereitet wurden. Sie können mehrere Datenpakete einer Studie mit unterschiedlichen Zugangswegen beantragen.',
          'no-final-release': 'Die Datenpakete wurden noch nicht erstellt. Sobald diese fertig sind können Sie sie an dieser Stelle in den Einkaufswagen legen.',
          'variable-not-accessible': 'Diese Variable wurde zwar erhoben, ist aber aus datenschutzrechtlichen Gründen in keinem Datenpaket verfügbar.',
          'data-not-available': 'Dieses Datenpaket ist aktuell nicht verfügbar.',
          'study-tooltip': 'Klicken, um die Studie anzuzeigen.',
          'study-series-tooltip': 'Klicken, um alle Studien der Studienreihe anzuzeigen.',
          'data-sets-tooltip': 'Klicken, um alle Datensätze dieses Datenpaketes anzuzeigen.',
          'variables-tooltip': 'Klicken, um alle Variablen dieses Datenpaketes anzuzeigen.',
          'citation': 'Datenpaket zitieren',
          'citation-success-copy-to-clipboard': 'Der Zitationshinweis wurde erfolgreich in die Zwischenablage kopiert.',
          'copy-citation-tooltip': 'Klicken, um den Zitationshinweis in die Zwischenablage zu kopieren.'
        },
        'error': {
          'synchronize': 'Der Einkaufswagen konnte nicht mit dem Server synchronisiert werden.',
          'already-completed': 'Die Bestellung wurde bereits abgeschlossen. Ihr Einkaufswagen wurde geleert.',
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
          'study-added': 'Das Datenpaket wurde in den Einkaufswagen gelegt.',
          'study-already-in-cart': 'Das Datenpaket ist bereits im Einkaufswagen.',
          'order-has-validation-errors-toast': 'Ihr Formular enthält ungültige Angaben.',
          'error-on-saving-order': 'Beim Senden Ihrer Bestellung trat ein Fehler auf.'
        },
        'buttons': {
          'checkout': 'Beantragen',
          'add-study': 'In den Einkaufswagen',
          'add-study-tooltip': 'Klicken, um das Datenpaket mit den ausgewählten Optionen in den Einkaufswagen zu legen.',
          'open-cart': 'Zum Einkaufswagen',
          'checkout-tooltip': 'Klicken, um die Datenpakete zu bestellen.',
          'remove-all': 'Einkaufswagen leeren',
          'remove-all-tooltip': 'Klicken, um alle Datenpakete aus dem Einkaufswagen zu entfernen.',
          'delete-product-tooltip': 'Klicken, um das Datenpaket aus dem Einkaufswagen zu entfernen.',
          'open-cart-tooltip': 'Klicken, um die Inhalte des Einkaufswagens anzuzeigen.',
          'open-citation-tooltip': 'Klicken, um Zitationsinformationen zu erhalten und zu kopieren.',
          'open-citation': 'Datenpaket zitieren',
          'close-tooltip': 'Klicken, um die Produktauswahl zu verlassen.',
          'data-package-version-tooltip': 'Klicken, um weitere Informationen zur Version von Datenpaketen zu erhalten.',
          'data-package-access-way-tooltip': 'Klicken, um weitere Informationen zu Zugangswegen zu erhalten.'
        },
        'version-info': {
          'title': 'Eine Version auswählen',
          'content': '<p style="margin-bottom: 0px;">Unsere Datenpakete liegen dreistellig versioniert vor. Die Stellen der Versionsnummer legen nahe, wie groß die Änderungen an den Daten sind. Bei Änderungen an den ersten beiden Stellen werden Sie benachrichtigt.</p><ul style="list-style-type: disc; margin-inline-start: 16px; margin-bottom: 0px;"><li>Erste Stelle (Major): Änderungen am Datensatz (abgesehen von Änderungen der Variablenlabels)</li><li>Zweite Stelle (Minor): Label ändern sich, Metadatenänderungen wie Hinzufügen weiterer Fragen oder Änderungen der Metadaten/Dokumentation, die Auswirkungen auf die Analyse haben.</li><li>Dritte Stelle (Patch): Zusätzliche Datensatzformate werden bereitgestellt, Hinzufügen/Löschen von Sprachversionen.</li></ul><strong>Für die meisten Datennutzer/-innen ist die aktuellste Version der Daten relevant.</strong>',
          'close-tooltip': 'Klicken, um diesen Dialog zu schließen.'
        },
        'access-way-info': {
          'title': 'Einen Zugangsweg auswählen',
          'content': '<p style="margin-bottom: 0px;">Für unsere Datenpakete gibt es vier Zugangswege, die zum einen den Grad der Anonymisierung bestimmen und zum anderen bestimmen auf welchem Weg die Daten verarbeitet werden können und ob sie zur Verwendung in der Lehre zugelassen sind. Scientific Use Files (SUF) sind ausschließlich zum Zweck der Forschung zu verwenden.</p><p style="margin-bottom: 0px; padding-bottom: 0px;">Unsere Datenpaketkonfigurationen sind gegliedert in:</p><ul style="list-style-type: disc; margin-inline-start: 16px; margin-bottom: 0px; padding-top: 0px;"><li><strong>download-cuf</strong> (Zulassung für Lehrzwecke; starke Anonymisierung; zum Download)</li><li><strong>download-suf</strong> (stark anonymisiert; zum Download)</li><li><strong>remote-desktop-suf</strong> (mittlerer Anonymisierungsgrad; Zugang über virtuelle Desktops per Internet)</li><li><strong>onsite-suf</strong> (geringer Anonymisierungsgrad; Zugang nur vor Ort im fdz.DZHW in Hannover)</li></ul><p style="margin-bottom: 0px;">Weitere Informationen finden Sie <a href="https://fdz.dzhw.eu/datennutzung/zugang">hier</a>.</p>'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
