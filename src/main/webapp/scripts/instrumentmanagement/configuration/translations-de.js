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
            'missing-id': 'Das {{ index }}. Instrument enthält keine FDZ-ID und wurde nicht gespeichert!',
            'upload-terminated': 'Upload von {{ totalInstruments }} Instrumenten und {{ totalAttachments }} Attachments mit {{ totalErrors }} Fehlern beendet!',
            'unable-to-delete': 'Die Instrumente konnten nicht gelöscht werden!',
            'cancelled': 'Upload von Instrumenten Abgebrochen!'
          },
          'instrument-attachment': {
            'not-saved': 'Attachment "{{ id }}" wurde nicht gespeichert:',
            'missing-instrument-id': 'Das {{ index }}. Attachment hat keine Instrument ID und wurde daher nicht gespeichert,',
            'missing-filename': 'Das {{ index }}. Attachment hat keinen Dateinamen und wurde daher nicht gespeichert.',
            'file-not-found': 'Die Datei {{ filename }} wurde nicht gefunden und wurde daher nicht gespeichert!'
          }
        },
        'home': {
          'title': 'Instrumente'
        },
        'detail': {
          'page-title': 'Erhebungsinstrument {{ surveyTitle }}',
          'instrument': 'Erhebungsinstrument',
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
            'file': 'Datei'
          }
        },
        'error': {
          'instrument': {
            'id': {
              'not-empty': 'Die FDZ-ID des Instruments darf nicht leer sein!',
              'size': 'Die Maximallänge der FDZ-ID ist 128 Zeichen.',
              'pattern': 'Es dürfen nur alphanumerische Zeichen, deutsche Umlaute, ß, Leerzeichen und Minus für die FDZ-ID verwendet werden.'
            },
            'title': {
              'not-null': 'Der Titel des Instruments darf nicht leer sein!',
              'i18n-string-size': 'Der Titel muss in beiden Sprachen angegeben werden und darf nicht länger als 128 Zeichen sein.'
            },
            'type': {
              'not-empty': 'Der Typ des Instruments darf nicht leer sein!',
              'valid': 'Der Typ des Instruments muss einer der folgenden sein: PAPI, CAPI, CATI, CAWI'
            },
            'data-acquisition-project-id': {
              'not-empty': 'Die ID des Datenaufbereitungsprojektes darf nicht leer sein!'
            },
            'survey-id': {
              'not-empty': 'Die ID der zugehörigen Erhebung darf nicht leer sein!'
            },
            'valid-instrument-id-pattern': 'Die FDZ-ID des Instruments hat nicht die folgende Form: ProjektId + "-" + "ins" + Nummer'
          },
          'instrument-attachment-metadata': {
            'instrument-id': {
              'not-empty': 'Die ID des zugehörigen Instrumentes darf nicht leer sein.'
            },
            'project-id': {
              'not-empty': 'Die ID des zugehörigen Datenaufbereitungsprojektes darf nicht leer sein.'
            },
            'type': {
              'not-null': 'Der Typ des Attachments darf nicht leer sein.',
              'i18n-string-size': 'Der Typ muss in beiden Sprachen angegeben werden und darf nicht länger als 32 Zeichen sein.',
              'valid-type': 'Der Typ muss einer der folgenden Werte sein: Fragebogen, Filterdiagramm, Variablenfragebogen, Sonstige.'
            },
            'title': {
              'not-null': 'Der Titel des Attachments darf nicht leer sein.',
              'i18n-string-size': 'Der Titel muss in beiden Sprachen angegeben werden und darf nicht länger als 128 Zeichen sein.'
            },
            'filename': {
              'not-empty': 'Der Dateiname des Attachments darf nicht leer sein.'
            }
          },
          'post-validation': {
            'instrument-has-invalid-survey-id': 'Das Instrument {{id}} referenziert eine unbekannte Erhebung ({{toBereferenzedId}}).',
            'instrument-unknown': 'Das Instrument {{id}}, die bei der Publikation ({{toBereferenzedId}}) verlinkt ist, konnte nicht gefunden werden.',
            'instrument-has-not-a-referenced-study': 'Das Instrument {{id}} referenziert auf eine Studie ({{additionalId}}), die nicht mit der Publikation ({{toBereferenzedId}}) verknüpft ist.'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
