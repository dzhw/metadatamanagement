'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'global': {
        'title': 'Metadatamanagement (MDM)',
        'browsehappy': 'Sie benutzen einen <strong>veralteten</strong> Browser. Bitte <a href="http://browsehappy.com/?locale=de/">aktualisieren Sie Ihren Browser</a>, um die Benutzer:innenfreundlichkeit zu erhöhen.',
        'rdc-alt-text': 'Logo des Forschungsdatenzentrums des Deutschen Zentrum für Hochschul- und Wissenschaftsforschung',
        'dzhw-alt-text': 'Deutsches Zentrum für Hochschul- und Wissenschaftsforschung DZHW GmbH',
        'bmbf-alt-text': 'Gefördert vom BMBF',
        'rdc': 'Forschungsdatenzentrum für die Hochschul- und Wissenschaftsforschung',
        'rdc-abbreviation': 'FDZ-DZHW',
        'search': 'Datensuche',
        'in-german': 'auf Deutsch',
        'in': 'auf',
        'of': 'von',
        'in-english': 'auf Englisch',
        'more': 'mehr',
        'less': 'weniger',
        'collapsed': 'Eingeklappt',
        'uncollapsed': 'Ausgeklappt',
        'search-component': {
          'search': 'Suchen',
          'delete': 'Löschen'
        },
        'filter': {
          'clear-filter': 'Filter löschen',
          'sponsors': 'Sponsoren',
          'concepts': 'Konzepte',
          'institutions': 'Institutionen',
          'tags': 'Schlagwörter',
          'study-series': 'Studienreihen',
          'survey-data-types': 'Erhebungsdatentyp',
          'access-ways': 'Zugangswege',
          'unavailable': 'Nicht vorhanden'
        },
        'toolbar': {
          'buttons': {
            'fdz-staff-area-tooltip': {
              'false': 'Klicken, um das Menü "Zugang für Datengeber:innen" zu öffnen',
              'true': 'Klicken, um das Menü "Zugang für Datengeber:innen" zu schließen'
            },
            'logout': '{{username}} abmelden',
            'logout-tooltip': 'Klicken, um "{{username}}" abzumelden',
            'login': 'Anmelden',
            'login-tooltip': 'Klicken, um sich anzumelden',
            'change-language': 'Click to view the english version',
            'register': 'Registrieren',
            'register-tooltip': 'Klicken, um sich zu registrieren',
            'open-menu-tooltip': 'Klicken, um das Navigationsmenü zu öffnen',
            'disclosure-tooltip': 'Klicken, um das Impressum anzuzeigen',
            'dataprotection-tooltip': 'Klicken, um die Datenschutzhinweise anzuzeigen',
            'administration-tooltip': {
              'false': 'Klicken, um das Menü "Administration" zu öffnen',
              'true': 'Klicken, um das Menü "Administration" zu schließen'
            },
            'user-management-tooltip': 'Klicken, um die Benutzer:innenverwaltung zu öffnen',
            'health-tooltip': 'Klicken, um die Verfügbarkeit aller externen Dienste zu prüfen',
            'logs-tooltip': 'Klicken, um Loglevel zu ändern',
            'settings-tooltip': 'Klicken, um Ihre Kontoeigenschaften zu bearbeiten',
            'password-tooltip': 'Klicken, um Ihr Passwort zu ändern'
          },
          'released': 'Freigegeben',
          'not-released': 'Nicht freigegeben'
        },
        'dialog': {
          'tooltip': {
            'close': 'Klicken, um den Dialog zu schließen',
            'save': 'Klicken, um das Protokoll zu speichern'
          }
        },
        'toast': {
          'tooltip': {
            'close': 'Klicken, um die Benachrichtigung zu schließen'
          }
        },
        'cards': {
          'metadata': 'Metadaten zu der Datei',
          'file': 'Datei',
          'details': 'Details',
          'related-objects': 'Verbundene Objekte'
        },
        'buttons': {
          'close': 'Schließen',
          'ok': 'OK',
          'save': 'Speichern',
          'cancel': 'Abbrechen',
          'closeDialogTemporarily': 'Jetzt nicht!'
        },
        'tooltips': {
          'create-project': 'Klicken, um ein neues Datenaufbereitungsprojekt zu erzeugen.',
          'delete-project': 'Klicken, um das ausgewählte Datenaufbereitungsprojekt mit allen verknüpften Daten zu löschen.',
          'release-project': 'Klicken, um das ausgewählte Projekt für alle Benutzer:innen freizugeben.',
          'unrelease-project': 'Klicken, um die Freigabe des ausgewählten Projektes zurückzunehmen.',
          'cockpit-project': 'Klicken, um zum Projektcockpit zu gehen.',
          'post-validation': 'Klicken, um das ausgewählte Datenaufbereitungsprojekt zu validieren.',
          'files': {
            'download': 'Klicken, um die Datei "{{filename}}" herunterzuladen'
          },
          'images': 'Klicken, um das Bild in einem neuem Tab zu öffnen',
          'pager': {
            'previous': 'Klicken, um die vorherigen Suchergebnisse anzuzeigen',
            'next': 'Klicken, um die nächsten Suchergebnisse anzuzeigen',
            'current': 'Klicken, um die {{number}}. Seite mit Suchergebnissen anzuzeigen'
          },
          'toolbarHeader': {
            'search': 'Klicken, um die letzten Suchergebnisse anzuzeigen',
            'data-set': 'Klicken, um den Datensatz {{param}} anzuzeigen',
            'survey': 'Klicken, um die Erhebung {{param}} anzuzeigen',
            'surveys': 'Klicken, um die Erhebungen anzuzeigen',
            'question': 'Klicken, um die Frage {{param}} anzuzeigen',
            'variable': 'Klicken, um die Variable {{param}} anzuzeigen',
            'data-package': 'Klicken, um das Datenpaket {{param}} anzuzeigen',
            'analysis-package': 'Klicken, um das Analysepaket {{param}} anzuzeigen',
            'instrument': 'Klicken, um das Instrument {{param}} anzuzeigen',
            'publication': 'Klicken, um die Publikation {{param}} anzuzeigen',
            'concept': 'Klicken, um das Konzept {{param}} anzuzeigen'
          },
          'feedback-dialog': {
            'github': 'Klicken, um auf Github Feedback zu geben oder einen Fehler zu melden',
            'email': 'Klicken, um per E-Mail Feedback zu geben oder einen Fehler zu melden'
          },
          'navbar-feedback': 'Klicken, um Feedback zu geben oder einen Fehler zu melden',
          'navbar-documentation': 'Klicken, um die Benutzer:innendokumentation zu öffnen',
          'navbar-usage-info': 'Klicken, um Hinweise für Datengeber:innen zu erhalten',
          'navbar-project-overview': 'Klicken, um die Projektübersicht zu öffnen'
        },
        'feedback-dialog': {
          'toolbar-head': 'Feedback geben oder Fehler melden',
          'content-body-feedback': 'Sie können Feedback geben oder Fehler melden ...',
          'content-body-via': '... via',
          'content-body-thanks': 'Vielen Dank, dass Sie sich Zeit nehmen, um uns Feedback zu geben!',
          'mail-subject': 'Metadata Management System Feedback'
        },
        'navbar-feedback': {
          'title': 'Feedback geben',
          'source': 'Quelle',
          'category': 'Kategorie'
        },
        'menu': {
          'show-english-pages': 'Show              Pages in English',
          'show-german-pages': 'Seiten              auf Deutsch anzeigen',
          'entities': {
            'main': 'Entitäten',
            'rdcProject': 'Datenaufbereitungsprojekte:',
            'current-project': 'Aktuelles Datenaufbereitungsprojekt',
            'select-project': 'Projekt auswählen',
            'unknown-project': 'Kein Projekt gefunden!'
          },
          'search': {
            'title': 'Datensuche für die Hochschul- und Wissenschaftsforschung',
            'description': 'Mit dieser Datensuche können Sie die Metadaten der beim FDZ-DZHW hinterlegten Datenpakete schnell und einfach durchsuchen. So finden Sie alle Informationen, die Sie brauchen, und können die entsprechenden Datenpakete direkt bestellen.'
          },
          'account': {
            'main': 'Kontodetails ({{username}})',
            'settings': 'Konto bearbeiten',
            'password': 'Passwort ändern',
            'sessions': 'Sitzungen'
          },
          'admin': {
            'main': 'Administration',
            'user-management': 'Benutzer:innenverwaltung',
            'health': 'Verfügbarkeit externer Dienste',
            'logs': 'Loglevel',
            'apidocs': 'API',
            'database': 'Database'
          },
          'skip-navigation': 'Zum Inhalt springen',
          'skip-navigation-tooltip': 'Klicken zum Überspringen des Menüs',
          'back-to-search': 'Klicken, um zur Suche zu gelangen',
          'back-to-start': 'Klicken, um zur Startseite zu gelangen',
          'language': 'Sprache',
          'data-access': 'Datenzugang',
          'disclosure': 'Impressum',
          'dataprotection': 'Datenschutz',
          'notepad': 'Merkzettel',
          'documentation': 'Dokumentation',
          'usage-info': 'Hinweise für Datengeber:innen',
          'project-overview': 'Projektübersicht'
        },
        'form': {
          'username': 'Benutzer:innenname',
          'username-placeholder': 'Ihr Benutzer:innenname',
          'newpassword': 'Neues Passwort',
          'newpassword-placeholder': 'Neues Passwort',
          'confirmpassword': 'Neues Passwort bestätigen',
          'confirmpassword-placeholder': 'Bestätigen Sie ihr neues Passwort',
          'email': 'E-Mail Adresse',
          'email-placeholder': 'Ihre E-Mail Adresse'
        },
        'messages': {
          'info': {
            'data-usage-application': 'Möchten Sie Daten von uns nutzen?',
            'data-usage-application-link': 'Dann klicken Sie hier, um zum Datennutzungsantrag zu kommen!'
          },
          'error': {
            'dontmatch': 'Das bestätigte Passwort entspricht nicht dem neuen Passwort!',
            'unavailable': 'Nicht vorhanden!',
            'undocumented': 'Nicht erfasst.'
          },
          'validate': {
            'newpassword': {
              'required': 'Ein neues Passwort wird benötigt.',
              'minlength': 'Das neue Passwort muss mindestens 5 Zeichen lang sein',
              'maxlength': 'Das neue Passwort darf nicht länger als 50 Zeichen sein',
              'strength': 'Passwortstärke:'
            },
            'confirmpassword': {
              'required': 'Sie müssen das Passwort bestätigen.',
              'minlength': 'Das bestätigte Passwort muss mindestens 5 Zeichen lang sein',
              'maxlength': 'Das bestätigte Passwort darf nicht länger als 50 Zeichen sein'
            },
            'email': {
              'required': 'Ihre E-Mail Adresse wird benötigt.',
              'invalid': 'Ihre E-Mail Adresse ist ungültig.',
              'minlength': 'Ihre E-Mail Adresse muss mindestens 5 Zeichen lang sein',
              'maxlength': 'Ihre E-Mail Adresse darf nicht länger als 50 Zeichen sein'
            }
          }
        },
        'log-messages': {
          'intro': 'Die folgenden Fehler traten beim letzten Upload auf:',
          'internal-server-error': 'Es ist ein interner Server Fehler aufgetreten.',
          'unsupported-excel-file': 'Excel Datei konnte nicht gelesen werden',
          'unsupported-tex-file': 'Tex Datei konnte nicht gelesen werden',
          'unable-to-parse-json-file': 'Die JSON Datei "{{file}}" enthält kein valides JSON!',
          'unable-to-read-file': 'Die Datei "{{file}}" konnte nicht gelesen werden!',
          'unable-to-read-excel-sheet': 'Das Excel Tabellenblatt "{{sheet}}" konnte nicht gelesen werden!',
          'unable-to-read-excel-sheets': 'Mindestens eines der Excel Tabellenblätter "{{sheets}}" konnte nicht gelesen werden!',
          'post-validation-terminated': 'Die Post-Validierung wurde mit {{errors}} Fehlern beendet.'
        },
        'field': {
          'rdc-id': 'FDZ-ID',
          'id': 'ID'
        },
        'entity': {
          'action': {
            'addblob': 'Add blob',
            'addimage': 'Add image',
            'back': 'Zurück',
            'cancel': 'Abbrechen',
            'delete': 'Löschen',
            'edit': 'Bearbeiten',
            'save': 'Speichern',
            'view': 'Details',
            'ok': 'OK'
          },
          'detail': {
            'field': 'Feld',
            'value': 'Wert'
          },
          'delete': {
            'title': 'Löschen bestätigen'
          },
          'validation': {
            'required': 'Dieses Feld wird benötigt.',
            'minlength': 'Dieses Feld muss mind. {{min}} Zeichen lang sein.',
            'maxlength': 'Dieses Feld darf max. {{max}} Zeichen lang sein.',
            'min': 'Dieses Feld muss größer als {{min}} sein.',
            'max': 'Dieses Feld muss kleiner als {{max}} sein.',
            'minbytes': 'This field should be more than {{min}} bytes.',
            'maxbytes': 'This field cannot be more than {{max}} bytes.',
            'pattern': 'Dieses Feld muss das Muster {{pattern}} erfüllen.',
            'number': 'Dieses Feld muss eine Zahl sein.',
            'datetimelocal': 'Dieses Feld muss eine Datums- und Zeitangabe enthalten.',
            'rdc-id': 'Die FDZ-ID darf nur aus Zahlen, Buchstaben und "_" bestehen.'
          }
        },
        'error': {
          'title': 'Fehlerseite!',
          '403': 'Sie haben nicht die nötigen Berechtigungen diese Seite anzuzeigen.',
          'server-not-reachable': 'Der Server ist nicht erreichbar!',
          'not-null': 'Das Feld {{fieldName}} darf nicht leer sein!',
          'browser-not-supported': 'Diese Aktion wird vom verwendeten Browser nicht unterstützt.',
          'entity': {
            'exists': 'Ein Datensatz vom Typ "{{params[0]}}" mit FDZ-ID "{{params[1]}}" existiert bereits!',
            'compoundexists': 'Ein Datensatz vom Typ "{{params[0]}}" und der Felderkombination "{{params[1]}}" existiert bereits!',
            'notfound': 'Ein Datensatz vom Typ "{{params[0]}}" mit FDZ-ID "{{params[1]}}" wurde nicht gefunden!'
          },
          'period': {
            'valid-period': 'Der Start und Endzeitpunkt müssen gesetzt sein und müssen in der richtigen Reihenfolge sein.'
          },
          'import': {
            'json-not-readable': 'Das Json hat im Feld "{{invalidValue}}" einen Fehler.',
            'json-parsing-error': 'Der Import des Objektes "{{entity}}" aus einer JSON Daei schlug fehl, denn das Feld "{{property}}" hat einen ungültigen Wert: {{invalidValue}}',
            'excel-parsing-error': 'Der Import des Objektes "{{entity}}" aus einer Excel Datei in Zeile {{index}} schlug fehl, denn das Feld "{{property}}" hat einen ungültigen Wert: {{invalidValue}}',
            'no-json-mapping': 'Ein serverseitiger Fehler trat beim Import eines Objektes auf.',
            'file-size-limit-exceeded': 'Die angegebene Datei ist größer 15MB!',
            'file-already-exists': 'Das Speichern ist fehlgeschlagen, weil es bereits eine Datei mit dem Namen "{{ filename }}" gibt!'
          },
          'server-error': {
            'internal-server-error': 'Sorry, etwas ist schief gelaufen :( ({{ status }}).',
            'gateway-timeout': 'Die Aktion dauert länger als erwartet. Sie wird im Hintergrund weiter ausgeführt.',
            'freemarker': {
              'parsing-error': 'Bei Freemarker trat ein Parsingproblem in Datei "{{entity}}" auf (Zeile, Zeichen): {{invalidValue}}',
              'invalid-reference-error': 'Bei einem Freemarkerskript liegt eine unbekannte Referenz in der Datei "{{entity}}" vor (Zeile, Zeichen): {{invalidValue}}'
            }
          },
          'client-error': {
            'unauthorized-error': 'Sie sind nicht angemeldet und können daher diese Aktion nicht durchführen (Status {{ status }}).',
            'forbidden-error': 'Sie haben nicht die Berechtigung (Rolle), um diese Aktion durchzuführen (Status {{ status }}).',
            'not-found-error': 'Die angeforderte Seite wurde nicht gefunden ({{ status }}).',
            'not-in-assignee-group': 'Die Bearbeitung ist nicht möglich, weil die Zuständigkeit gerade nicht bei Ihrer Gruppe liegt.'
          },
          'person': {
            'first-name': {
              'not-empty': 'Der Vorname einer Person muss gefüllt sein.'
            },
            'last-name': {
              'not-empty': 'Der Nachname einer Person muss gefüllt sein.'
            }
          },
          'resolution': {
            'width-x': {
              'not-null': 'Die Angabe der Breite eines Bildes (maximaler Wert auf der X-Achse) muss gefüllt sein.'
            },
            'height-y': {
              'not-null': 'Die Angabe der Höhe eines Bildes (maximaler Wert auf der Y-Achse) muss gefüllt sein.'
            }
          }
        },
        'logos': {
          'fdz': 'Forschungsdatenzentrum, Deutsches Zentrum für Hochschul- und Wissenschaftsforschung',
          'bmbf-tooltip': 'Klicken, um die Internetseite des Bundesministeriums für Bildung und Forschung zu öffnen',
          'bmbf-alt-text': 'Logo des Bundesministeriums für Bildung und Forschung',
          'dzhw-tooltip': 'Klicken, um die Internetseite des deutschen Zentrums für Hochschul- und Wissenschaftsforschung zu öffnen',
          'dzhw-alt-text': 'Logo des deutschen Zentrums für Hochschul- und Wissenschaftsforschung'
        },
        'main': {
          'title': 'Willkommen beim FDZ des DZHW. Sie suchen ...'
        },
        'pagination': {
          'next': 'Weiter',
          'previous': 'Zurück',
          'of': 'von',
          'sort': 'Sortiert nach',
          'items': 'Einträge pro Seite'
        },
        'sort':  {
          'relevance': 'Relevanz',
          'alphabetically': 'Alphabet',
          'survey-period': 'Erhebungszeitraum (Ende)',
          'first-release-date': 'Releasedatum'
        },
        'joblogging': {
          'protocol-dialog': {
            'title': 'Protokoll',
            'success': 'Erfolg',
            'warning': 'Warnungen',
            'error': 'Fehler'
          },
          'job-complete-toast': {
            'title': 'Protokoll',
            'show-log': 'Klicken, um das Protokoll zu öffnen.'
          },
          'type': {
            'error': 'Fehler',
            'warning': 'Warnung',
            'info': 'Information'
          },
          'protocol': {
            'created-by': 'Erstellt durch das MDM, am'
          },
          'block-ui-message': '{{warnings}} Warnungen und {{ errors }} Fehler bei {{ total }} Objekten'
        },
        'common-dialogs': {
          'yes': 'Ja',
          'no': 'Nein',
          'confirm-dirty': {
            'title': 'Änderungen verwerfern?',
            'content': 'Sie haben ungespeicherte Änderungen. Wollen Sie diese verwerfen?'
          },
          'confirm-file-delete': {
            'title': 'Datei "{{ filename }}" löschen?',
            'content': 'Wollen Sie die Datei "{{ filename }}" wirklich löschen?'
          },
          'confirm-filename-change': {
            'title': 'Dateinamen ändern?',
            'content': 'Wollen Sie wirklich den Dateinamen von "{{ oldFilename }}" nach "{{ newFilename }}" ändern?\n\nHierdurch geht die Historie der Metadaten der Datei verloren!'
          },
          'confirm-delete-survey': {
            'title': 'Erhebung "{{ id }}" löschen?',
            'content': 'Wollen Sie die Erhebung "{{ id }}" wirklich löschen?'
          },
          'confirm-delete-concept': {
            'title': 'Konzept "{{ id }}" löschen?',
            'content': 'Wollen Sie das Konzept "{{ id }}" wirklich löschen?'
          },
          'confirm-delete-instrument': {
            'title': 'Instrument "{{ id }}" löschen?',
            'content': 'Wollen Sie das Instrument "{{ id }}" wirklich löschen?'
          },
          'confirm-delete-data-set': {
            'title': 'Datensatz "{{ id }}" löschen?',
            'content': 'Wollen Sie den Datensatz "{{ id }}" wirklich löschen?'
          },
          'confirm-delete-all-questions': {
            'title': 'Alle Fragen des Projekts "{{ id }}" löschen?',
            'content': 'Wollen Sie wirklich alle Fragen des Datenaufbereitungsprojekts "{{ id }}" löschen?'
          },
          'confirm-delete-all-data-packages': {
            'title': 'Datenpaket des Projekts "{{ id }}" löschen?',
            'content': 'Wollen Sie wirklich das Datenpaket des Datenaufbereitungsprojekts "{{ id }}" löschen?'
          },
          'confirm-delete-all-analysis-packages': {
            'title': 'Analysepaket des Projekts "{{ id }}" löschen?',
            'content': 'Wollen Sie wirklich das Analysepaket des Datenaufbereitungsprojekts "{{ id }}" löschen?'
          },
          'confirm-delete-all-variables': {
            'title': 'Alle Variablen des Projekts "{{ id }}" löschen?',
            'content': 'Wollen Sie wirklich alle Variablen des Datenaufbereitungsprojekts "{{ id }}" löschen?'
          },
          'confirm-delete-all-instruments': {
            'title': 'Alle Instrumente des Projekts "{{ id }}" löschen?',
            'content': 'Wollen Sie wirklich alle Instrumente des Datenaufbereitungsprojekts "{{ id }}" löschen?'
          },
          'confirm-delete-all-surveys': {
            'title': 'Alle Erhebungen des Projekts "{{ id }}" löschen?',
            'content': 'Wollen Sie wirklich alle Erhebungen des Datenaufbereitungsprojekts "{{ id }}" löschen?'
          },
          'confirm-delete-all-data-sets': {
            'title': 'Alle Datensätze des Projekts "{{ id }}" löschen?',
            'content': 'Wollen Sie wirklich alle Datensätze des Datenaufbereitungsprojekts "{{ id }}" löschen?'
          },
          'confirm-delete-all-publications': {
            'title': 'Alle Publikationen von dem Datenpaket des Projektes "{{ id }}" entfernen?',
            'content': 'Wollen Sie wirklich alle Publikationen von dem Datenpaket des Datenaufbereitungsprojekts "{{ id }}" entfernen?'
          }
        },
        'people': {
          'edit': {
            'label': {
              'first-name': 'Vorname',
              'middle-name': 'Zweiter Vorname',
              'last-name': 'Nachname',
              'search-orcid': 'ORCID suchen',
              'delete-orcid': 'ORCID löschen'
            },
            'tooltip': {
              'search-orcid': 'Klicken, um eine ORCID zu der Person nach Vorname und Nachname zu suchen.',
              'delete-orcid': 'Klicken, um die ORCID zu löschen.'
            },
            'hint': {
              'orcid': 'Sobald Sie den Nachnamen der Person angegeben haben, können Sie eine ORCID nach Vorname und Nachname suchen.'
            },
            'orcid-search': {
              'title': 'ORCID suchen',
              'cancel-tooltip': 'Klicken, um den Dialog, ohne eine ORCID auszuwählen, zu schließen.',
              'results-found-text': 'Die folgenden Einträge wurden bei ORCID.org gefunden (Suchparameter: Vorname={{ firstName }}, Nachname={{ lastName }}):',
              'no-results-found-text': 'Es wurden keine Einträge bei ORCID.org gefunden (Suchparameter: Vorname={{ firstName }}, Nachname={{ lastName }}).',
              'institutions': 'Institutionen',
              'select': 'Übernehmen',
              'select-tooltip': 'Klicken, um diese ORCID zu übernehmen.',
              'select-orcid': 'ORCID übernehmen'
            }
          }
        },
        'user-consent': {
          'text': 'Wir verwenden Cookies zur statistischen Auswertung der Besucherzugriffe. Wenn Sie auf dieser Seite weitersurfen stimmen Sie der Cookie-Nutzung zu. Weitere Informationen zu Cookies erhalten Sie in unserer <a href="https://www.dzhw.eu/gmbh/datenschutz/datenschutzerklaerung" target="_blank">Datenschutzerkärung</a>.',
          'accept': {
            'tooltip': 'Klicken, um das Banner auszublenden.'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
