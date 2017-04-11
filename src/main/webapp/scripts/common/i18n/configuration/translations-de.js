'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'global': {
        'title': 'metadatamanagement',
        'browsehappy': 'Sie benutzen einen <strong>veralteten</strong> Browser. Bitte <a href="http://browsehappy.com/?locale=de/">aktualisieren Sie Ihren Browser</a>, um die Benutzerfreundlichkeit zu erhöhen.',
        'rdc-alt-text': 'Forschungsdatenzentrum, Deutsches Zentrum für Hochschul- und Wissenschaftsforschung',
        'dzhw-alt-text': 'Deutsches Zentrum für Hochschul- und Wissenschaftsforschung DZHW GmbH',
        'bmbf-alt-text': 'Gefördert vom BMBF',
        'search': 'Suche',
        'toolbar': {
          'buttons': {
            'fdz-staff-area-tooltip': 'Klicken, um das Menü "Zugang für FDZ Mitarbeiter" zu öffnen',
            'logout': '{{username}} abmelden',
            'logout-tooltip': 'Klicken, um "{{username}}" abzumelden',
            'login': 'Anmelden',
            'login-tooltip': 'Klicken, um sich anzumelden',
            'change-language': 'Click to view the english version',
            'register': 'Registrieren',
            'register-tooltip': 'Klicken, um sich zu registrieren',
            'open-menu': 'Navigationsmenü öffnen',
            'open-menu-tooltip': 'Klicken, um Navigationsmenü zu öffnen',
            'disclosure-tooltip': 'Klicken, um das Impressum anzuzeigen',
            'administration-tooltip': 'Klicken, um das Menü "Administration" zu öffnen',
            'user-management-tooltip': 'Klicken, um die Benutzerverwaltung zu öffnen',
            'metrics-tooltip': 'Klicken, um die Servermetriken anzuzeigen',
            'health-tooltip': 'Klicken, um die Verfügbarkeit aller externen Dienste zu prüfen',
            'configuration-tooltip': 'Klicken, um die Spring-Konfiguration anzuzeigen',
            'logs-tooltip': 'Klicken, um Loglevel zu ändern',
            'settings-tooltip': 'Klicken, um die Kontoeigenschaften zu bearbeiten',
            'password-tooltip': 'Klicken, um das Passwort zu ändern'
          }
        },
        'dialog': {
          'tooltip': {
            'close': 'Klicken, um den Dialog zu schließen'
          }
        },
        'toast': {
          'tooltip': {
            'close': 'Klicken, um die Benachrichtigung zu schließen'
          }
        },
        'cards': {
          'details': 'Details',
          'related-objects': 'Verbundene Objekte'
        },
        'buttons': {
          'close': 'Schließen',
          'ok': 'OK',
          'cancel': 'Abbrechen'
        },
        'tooltips': {
          'create-project': 'Klicken, um ein neues Datenaufbereitungsprojekt zu erzeugen.',
          'delete-project': 'Klicken, um das ausgewählte Datenaufbereitungsprojekt mit allen verknüpften Daten zu löschen.',
          'release-project': 'Klicken, um das ausgewählte Projekt für alle Benutzer freizugeben.',
          'unrelease-project': 'Klicken, um die Freigabe des ausgewählten Projektes zurückzunehmen.',
          'post-validation': 'Klicken, um das ausgewählte Datenaufbereitungsprojekt zu validieren.',
          'files': {
            'download': 'Klicken, um die Datei "{{filename}}" herunterzuladen'
          },
          'images': 'Klicken, um das Bild in einem neuem Tab zu öffnen',
          'pager': {
            'previous':'Klicken, um die vorherigen Suchergebnisse anzuzeigen',
            'next': 'Klicken, um die nächsten Suchergebnisse anzuzeigen',
            'current': 'Klicken, um die {{number}}. Seite mit Suchergebnissen anzuzeigen'
          },
          'toolbarHeader': {
            'search': 'Klicken, um die letzten Suchergebnisse anzuzeigen',
            'data-set': 'Klicken, um den Datensatz "{{param}}" anzuzeigen',
            'survey': 'Klicken, um die Erhebung "{{param}}" anzuzeigen',
            'surveys': 'Klicken, um die Erhebungen anzuzeigen',
            'question': 'Klicken, um die Frage "{{param}}" anzuzeigen',
            'variable': 'Klicken, um die Variable "{{param}}" anzuzeigen',
            'study': 'Klicken, um die Studie "{{param}}" anzuzeigen',
            'instrument': 'Klicken, um das Instrument "{{param}}" anzuzeigen',
            'publication': 'Klicken, um die Publikation "{{param}}" anzuzeigen'
          }
        },
        'menu': {
          'entities': {
            'main': 'Entitäten',
            'rdcProject': 'Datenaufbereitungsprojekte:',
            'current-project': 'Aktuelles Datenaufbereitungsprojekt',
            'select-project': 'Projekt auswählen',
            'unknown-project': 'Das Datenaufbereitungsprojekt {{projectId}} ist unbekannt.'
          },
          'search': {
            'title': 'Metadatensuche'
          },
          'account': {
            'main': 'Zugang für FDZ Mitarbeiter',
            'settings': 'Konto bearbeiten',
            'password': 'Passwort ändern',
            'sessions': 'Sitzungen'
          },
          'admin': {
            'main': 'Administration',
            'user-management': 'Benutzerverwaltung',
            'metrics': 'Servermetriken',
            'health': 'Verfügbarkeit externer Dienste',
            'configuration': 'Spring-Konfiguration',
            'logs': 'Loglevel',
            'apidocs': 'API',
            'database': 'Database'
          },
          'skip-navigation': 'Zum Inhalt springen',
          'skip-navigation-tooltip': 'Klicken zum Überspringen der Navigationselemente',
          'back-to-search': 'Klicken, um zur Suche zu gelangen',
          'language': 'Sprache',
          'disclosure': 'Impressum',
          'notepad': 'Merkzettel'
        },
        'form': {
          'username': 'Benutzername',
          'username-placeholder': 'Ihr Benutzername',
          'newpassword': 'Neues Passwort',
          'newpassword-placeholder': 'Neues Passwort',
          'confirmpassword': 'Neues Passwort bestätigen',
          'confirmpassword-placeholder': 'Bestätigen Sie ihr neues Passwort',
          'email': 'E-Mail Adresse',
          'email-placeholder': 'Ihre E-Mail Adresse'
        },
        'messages': {
          'info': {
            'register': 'Sie haben noch keinen Zugang? '
          },
          'error': {
            'dontmatch': 'Das bestätigte Passwort entspricht nicht dem neuen Passwort!',
            'unavailable': 'Nicht vorhanden!'
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
            'rdc-id': 'Die FDZ-ID darf nur aus Zahlen, Buchstaben und "_" bestehen.',
            'variable': {
              'name': 'Der Name einer Variablen darf nur aus Zahlen, Buchstaben und "_" bestehen.'
            },
            'survey': {
              'period': 'Der Feldzeitbeginn muss vor dem Ende liegen.'
            },
            'data-acquisition-project': {
              'id': 'Der Name eines Projektes darf nur aus Zahlen und Buchstaben bestehen.',
              'release': {
                'version': 'Die Version darf nur aus Zahlen und Punkten bestehen.'
              }
            }
          }
        },
        'error': {
          'title': 'Fehlerseite!',
          '403': 'Sie haben nicht die nötigen Berechtigungen diese Seite anzuzeigen.',
          'server-not-reachable': 'Der Server ist nicht erreichbar!',
          'not-null': 'Das Feld {{fieldName}} darf nicht leer sein!',
          'entity': {
            'exists': 'Ein Datensatz vom Typ "{{params[0]}}" mit FDZ-ID "{{params[1]}}" existiert bereits!',
            'compoundexists': 'Ein Datensatz vom Typ "{{params[0]}}" und der Felderkombination "{{params[1]}}" existiert bereits!',
            'notfound': 'Ein Datensatz vom Typ "{{params[0]}}" mit FDZ-ID "{{params[1]}}" wurde nicht gefunden!'
          },
          'period': {
            'valid-period': 'Der Start und Endzeitpunkt müssen gesetzt sein und müssen in der richtigen Reihenfolge sein.'
          },
          'import': {
            'json-parsing-error': 'Der Import des Objektes "{{entity}}" schlug fehl, denn das Feld "{{property}}" hat einen ungültigen Wert: {{invalidValue}}',
            'no-json-mapping': 'Ein serverseitiger Fehler trat beim Import eines Objektes auf.',
            'file-size-limit-exceeded': 'Die Datei "{{ entity }}" ist größer 10MB!'
          },
          'server-error': {
            'internal-server-error': 'Sorry, etwas ist schief gelaufen :-( ({{ status }}).'
          },
          'client-error': {
            'unauthorized-error': 'Sie sind nicht berechtigt diese Aktion durchzuführen (Status {{ status }}).',
            'forbidden-error': 'Sie sind nicht berechtigt diese Aktion durchzuführen (Status {{ status }}).',
            'not-found-error': 'Sorry, etwas ist schief gelaufen :-( ({{ status }}).'
          }
        },
        'logos': {
          'fdz': 'Forschungsdatenzentrum, Deutsches Zentrum für Hochschul- und Wissenschaftsforschung',
          'bmbf-tooltip': 'Klicken, um die Internetseite des Bundesministeriums für Bildung und Forschung zu öffnen',
          'bmbf-alt-text': 'Das Logo des Bundesministeriums für Bildung und Forschung',
          'dzhw-tooltip': 'Klicken, um die Internetseite des deutschen Zentrums für Hochschul- und Wissenschaftsforschung zu öffnen',
          'dzhw-alt-text': 'Das Logo des deutschen Zentrums für Hochschul- und Wissenschaftsforschung'
        },
        'main': {
          'title': 'Willkommen beim FDZ des DZHW. Sie suchen ...'
        },
        'pagination': {
          'next': 'Weiter',
          'previous': 'Zurück'
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
          'block-ui-message': '{{warnings}} Warnungen und {{ errors }} Fehler bei {{ total }} Objekten'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
