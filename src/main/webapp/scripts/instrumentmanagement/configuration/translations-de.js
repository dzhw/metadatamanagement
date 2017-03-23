'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'instrument-management': {
        'log-messages': {
          'instrument': {
            'saved': 'Instrument mit FDZ-ID {{ id }} erfolgreich gespeichert!',
            'not-saved': 'Instrument mit FDZ-ID {{ id }} wurde nicht gespeichert:',
            'missing-number': 'Das {{ index }}. Instrument enthält keine Nummer und wurde nicht gespeichert!',
            'upload-terminated': 'Upload von {{ totalInstruments }} Instrumenten und {{ totalAttachments }} Attachments mit {{totalWarnings}} Warnungen und {{ totalErrors }} Fehlern beendet!',
            'unable-to-delete': 'Die Instrumente konnten nicht gelöscht werden!',
            'cancelled': 'Upload von Instrumenten Abgebrochen!',
            'duplicate-instrument-number': 'Die Nummer ({{ number }}) des {{ index }}. Instrumentes wurde bereits verwendet.'
          },
          'instrument-attachment': {
            'not-saved': 'Attachment "{{ id }}" wurde nicht gespeichert:',
            'missing-instrument-number': 'Das {{ index }}. Attachment hat keine Instrumentnummer und wurde daher nicht gespeichert.',
            'unknown-instrument-number': 'Die Nummer des Instrumentes des {{ index }}. Attachments gibt es nicht. Das Attachment wurde daher nicht gespeichert.',
            'missing-filename': 'Das {{ index }}. Attachment hat keinen Dateinamen und wurde daher nicht gespeichert.',
            'file-not-found': 'Die Datei {{ filename }} wurde nicht gefunden und wurde daher nicht gespeichert!'
          }
        },
        'home': {
          'title': 'Instrumente'
        },
        'detail': {
          'page-title': '{{ description }} ({{ instrumentId }})',
          'instrument': 'Instrument',
          'instruments': 'Instrumente',
          'instrument-informations': 'Details zum Instrument',
          'title': 'Titel',
          'type': 'Typ',
          'related-information': 'Verbundene Objekte',
          'no-related-instruments': 'Keine zugehörige Instrumente.',
          'attachments': {
            'table-title': 'Materialien zum Instrument',
            'type': 'Typ',
            'title': 'Titel',
            'description': 'Beschreibung',
            'language': 'Dokumentensprache',
            'file': 'Datei'
          },
          'not-released-toast': 'Das Instrument "{{ id }}" wurde noch nicht für alle Benutzer freigegeben!'
        },
        'error': {
          'instrument': {
            'id': {
              'not-empty': 'Die FDZ-ID des Instruments darf nicht leer sein!',
              'size': 'Die Maximallänge der FDZ-ID ist 128 Zeichen.',
              'pattern': 'Es dürfen nur alphanumerische Zeichen, deutsche Umlaute, ß, Leerzeichen, Ausrufezeichen und Minus für die FDZ-ID verwendet werden.'
            },
            'title': {
              'not-null': 'Der Titel des Instruments darf nicht leer sein!',
              'i18n-string-size': 'Der Titel muss in beiden Sprachen angegeben werden und darf nicht länger als 128 Zeichen sein.'
            },
            'description': {
              'not-null': 'Die Beschreibung des Instruments darf nicht leer sein!',
              'i18n-string-size': 'Die Beschreibung muss in beiden Sprachen angegeben werden und darf nicht länger als 128 Zeichen sein.',
              'i18n-string-not-empty':'Die Beschreibung darf nicht leer sein.'
            },
            'type': {
              'not-empty': 'Der Typ des Instruments darf nicht leer sein!',
              'valid': 'Der Typ des Instruments muss einer der folgenden sein: PAPI, CAPI, CATI, CAWI'
            },
            'data-acquisition-project-id': {
              'not-empty': 'Die ID des Datenaufbereitungsprojektes darf nicht leer sein!'
            },
            'instrument.unique-instrument-number': 'Die Nummer eines Erhebungsinstruments muss eindeutig innerhalb eines Datenaufbereitungsprojektes sein!',
            'survey-numbers.not-empty': 'Die Liste der Erhebungen darf nicht leer sein!',
            'number': {
              'not-null': 'Die Nummer des Erhebungsinstruments darf nicht leer sein!'
            },
            'survey-id': {
              'not-empty': 'Die ID der zugehörigen Erhebung darf nicht leer sein!'
            },
            'valid-instrument-id-pattern': 'Die FDZ-ID des Instruments hat nicht die folgende Form: "ins-" + {ProjektId} + "-" + "ins" + {Nummer} + "!" .'
          },
          'instrument-attachment-metadata': {
            'instrument-id': {
              'not-empty': 'Die ID des zugehörigen Instrumentes darf nicht leer sein.'
            },
            'instrument-number': {
              'not-null': 'Die Nummer des zugehörigen Instrumentes darf nicht leer sein.'
            },
            'project-id': {
              'not-empty': 'Die ID des zugehörigen Datenaufbereitungsprojektes darf nicht leer sein.'
            },
            'type': {
              'not-null': 'Der Typ des Attachments darf nicht leer sein.',
              'i18n-string-size': 'Der Typ muss in beiden Sprachen angegeben werden und darf nicht länger als 32 Zeichen sein.',
              'valid-type': 'Der Typ muss einer der folgenden Werte sein: Fragebogen, Filterdiagramm, Variablenfragebogen, Sonstige.'
            },
            'description': {
              'not-null': 'Die Beschreibung des Attachments darf nicht leer sein.',
              'i18n-string-size': 'Die Beschreibung muss in mindestens einer Sprache angegeben werden und darf nicht länger als 128 Zeichen sein.',
              'i18n-string-not-empty':'Die Beschreibung darf nicht leer sein.'
            },
            'title': {
                'not-null': 'Der Title des Attachments darf nicht leer sein.',
                'string-size': 'Der Title des Attachments muss angegeben werden und darf nicht länger als 128 Zeichen sein.'
              },
            'language': {
                'not-null': 'Die Sprache des Attachments darf nicht leer sein.',
                'not-supported': 'Die Sprache muss eine gültige zweibuchstabige Abkürzung gemäß ISO 639-1 sein.'
              },
            'filename': {
              'not-empty': 'Der Dateiname des Attachments darf nicht leer sein.'
            }
          },
          'post-validation': {
            'instrument-has-invalid-survey-id': 'Das Instrument {{id}} referenziert eine unbekannte Erhebung ({{toBereferenzedId}}).'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
