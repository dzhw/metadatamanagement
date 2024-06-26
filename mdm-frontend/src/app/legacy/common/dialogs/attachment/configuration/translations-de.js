'use strict';

angular.module('metadatamanagementApp').config([
  '$translateProvider',

  function($translateProvider) {
    var translations = {
      //jscs:disable
      'attachment': {
        'create-title': 'Datei anlegen',
        'edit-title': 'Datei bearbeiten',
        'attachment-saved-toast': 'Datei "{{ filename }}" wurde gespeichert.',
        'current-version-restored-toast': 'Die aktuelle Version der Metadaten von Datei "{{ filename }}" wurde wiederhergestellt.',
        'previous-version-restored-toast': 'Die ältere Version der Metadaten von Datei "{{ filename }}" kann jetzt gespeichert werden.',
        'error': {
          'attachment-has-validation-errors-toast': 'Die Datei wurde nicht gespeichert, weil es noch ungültige Felder gibt.',
          'description': {
            'i18n-string-not-empty': 'Die Beschreibung muss in mindestens einer Sprache vorhanden sein.',
            'i18n-string-size': 'Die Beschreibung muss in mindestens einer Sprache angegeben werden und darf nicht länger als 512 Zeichen sein.'
          },
          'filename': {
            'not-empty': 'Es muss eine Datei ausgewählt sein',
            'not-unique': 'Eine Datei mit diesem Namen existiert bereits',
            'not-valid': 'Dieser Dateiname ist ungültig',
            'pattern': 'Es dürfen für die FDZ-ID nur alphanumerische Zeichen, deutsche Umlaute, ß, Minus und der Unterstrich verwendet werden.'
          },
          'file-not-found': 'Die Datei {{ filename }} wurde nicht gefunden und wurde daher nicht gespeichert!',
          'type': {
            'not-null': 'Der Typ des Attachments darf nicht leer sein.'
          },
          'language': {
            'not-found': 'Keine gültige Sprache gefunden!',
            'not-null': 'Die Sprache des Attachments darf nicht leer sein.',
            'not-valid': 'Bitte wählen Sie eine vorgeschlagene Sprache aus.'
          },
          'title': {
            'maxlength': 'Der Titel des Attachments darf nicht länger als 2048 Zeichen sein.',
            'not-empty': 'Der Titel des Attachments darf nicht leer sein.'
          },

          'doi': {
            'size': 'Die DOI muss kurzer als 512 Zeichen sein.',
            'pattern': 'Die DOI-Url sollte nach folgendem Muster eingetragen werden: https://doi.org/<id>'
          }
        },
        'hint': {
          'filename': 'Wählen Sie eine Datei aus, die Sie hinzufügen wollen',
          'language': 'Wählen Sie die Sprache, die in der Datei verwendet wurde, aus.',
          'type': 'Wählen Sie den Typ der Datei aus.',
          'title': 'Geben Sie den Titel der Datei in der Dokumentensprache ein.',
          'description': {
            'de': 'Geben Sie eine Beschreibung dieser Datei auf Deutsch ein.',
            'en': 'Geben Sie eine Beschreibung dieser Datei auf Englisch ein.'
          },
          'doi': 'Bitte tragen Sie die DOI als URL ein (Beispiel: https://doi.org/10.21249/DZHW:gra2009:2.0.0).',
        },
        'label': {
          'description': 'Beschreibung',
          'file': 'Datei',
          'type': 'Typ',
          'language': 'Dokumentensprache',
          'title': 'Titel',
          'doi': 'DOI',
        },
        'tooltip': {
          'cancel': 'Klicken, um den Dialog, ohne zu speichern, zu schließen.',
          'change-file': 'Klicken, um eine Datei auszuwählen.',
          'previous-version': 'Klicken, um eine ältere Version der Metadaten wiederherzustellen.',
          'save': 'Klicken, um die Datei zu speichern.'
        },
        'predefined-content': {
          'description': {
            'method-report': {
              'de': 'Der Bericht umfasst allgemeine Informationen zum Datenpaket.',
              'en': 'The report includes general information on the data package.'
            },
            'release-notes': {
              'de': 'Die Release Notes enthalten Informationen zur aktuellen Version und zu Veränderungen im Vergleich zu vorherigen Versionen.',
              'en': 'The release notes contain information about the current version and changes compared to previous versions.'
            },
            'type': {
              'instrument': {
                'de': 'Erhebungsinstrument zu "#titledatapackage#", #surveys#',
                'en': 'Instrument of "#titledatapackage#", #surveys#'
              },
              'question-flow': {
                'de': 'Filterführungsdiagram zu "#titledatapackage#", #surveys#',
                'en': 'Question Flow of "#titledatapackage#", #surveys#'
              },
              'variable-questionaire': {
                'de': 'Variablenfragebogen zu "#titledatapackage#", #surveys#',
                'en': 'Variable Questionaire of "#titledatapackage#", #surveys#'
              }
            }
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  }]);
