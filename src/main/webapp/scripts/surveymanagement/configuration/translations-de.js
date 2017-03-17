'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'survey-management': {
        'log-messages': {
          'survey': {
            'saved': 'Erhebung mit FDZ-ID {{ id }} erfolgreich gespeichert!',
            'not-saved': 'Erhebung mit FDZ-ID {{ id }} wurde nicht gespeichert!',
            'missing-number': 'Die {{ index }}. Erhebung enthält keine Nummer und wurde daher nicht gespeichert!',
            'upload-terminated': 'Upload von {{ totalSurveys }} Erhebungen, {{ totalImages }} Bildern und {{totalAttachments}} Attachments mit {{ totalErrors }} Fehlern beendet!',
            'unable-to-upload-image-file': 'Die Bilddatei "{{ file }}" konnte nicht hochgeladen werden!',
            'unable-to-read-image-file': 'Die Bilddatei "{{ file }}" konnte nicht gelesen werden!',
            'image-file-not-found': 'Die Bilddatei "{{ file }}" konnte nicht gefunden werden!',
            'unable-to-delete': 'Die Erhebungen konnten nicht gelöscht werden!',
            'cancelled': 'Upload von Erhebungen Abgebrochen!',
            'duplicate-survey-number': 'Die Nummer ({{ number }}) der {{ index }}. Erhebung wurde bereits verwendet.'
          },
          'survey-attachment': {
            'not-saved': 'Attachment "{{ id }}" wurde nicht gespeichert.',
            'missing-survey-number': 'Das {{ index }}. Attachment hat keine Erhebungsnummer und wurde daher nicht gespeichert.',
            'missing-filename': 'Das {{ index }}. Attachment hat keinen Dateinamen und wurde daher nicht gespeichert.',
            'file-not-found': 'Die Datei {{ filename }} wurde nicht gefunden und wurde daher nicht gespeichert!'
          }
        },
        'detail': {
          'title': '{{ title }} ({{ surveyId }})',
          'survey': 'Erhebung',
          'surveys': 'Erhebungen',
          'surveys-same-study': 'Alle Erhebungen der Studie',
          'survey-informations': 'Informationen zu der Erhebung',
          'related-information': 'Zugehörige Informationen',
          'related-objects': 'Zugehörige Objekte',
          'field-period': 'Feldzeit',
          'population': 'Grundgesamtheit',
          'survey-method': 'Erhebungsmethode',
          'sample': 'Stichprobe',
          'not-found': 'Die id {{id}} referenziert auf eine unbekannte Erhebung.',
          'not-found-references': 'Die id {{id}} hat keine Referenzen auf Erhebungen.',
          'no-related-surveys': 'Keine zugehörige Erhebungen.',
          'related-surveys': 'Zugehörige Erhebungen',
          'grossSampleSize': 'Bruttostichprobe',
          'sampleSize': 'Nettostichprobe',
          'responseRate': 'Rücklaufquote',
          'response-rate-informations': 'Weitere Informationen zum Rücklauf',
          'attachments': {
            'table-title': 'Materialien zu der Erhebung',
            'title': 'Titel',
            'description': 'Beschreibung',
            'language': 'Dokumentensprache',
            'file': 'Datei'
          }
        },
        'error': {
          'survey': {
            'id': {
              'valid-survey-id-name': 'Die FDZ-ID der Erhebung entspricht nicht dem Muster: "sur-" + {FDZID} + "-sy" + {Number} + "!".',
              'not-empty': 'Die FDZ-ID der Erhebung darf nicht leer sein!',
              'size': 'Die Maximallänge der FDZ-ID der Erhebung ist 128 Zeichen.',
              'pattern': 'Es dürfen für die FDZ-ID nur alphanumerische Zeichen, deutsche Umlaute, ß, Ausrufezeichen, Minus und der Unterstrich verwendet werden.'
            },
            'title': {
              'i18n-string-size': 'Die Maximallänge des Titels der Erhebung ist 128 Zeichen.'
            },
            'field-period': {
              'not-null': 'Der Zeitraum der Erhebung darf nicht leer sein!'
            },
            'data-acquisition-project': {
              'id': {
                'not-empty': 'Die FDZ-ID des Projektes darf bei der Erhebung nicht leer sein!'
              }
            },
            'population': {
              'not-null': 'Die Population der Erhebung darf nicht leer sein!',
              'i18n-string-not-empty': 'Die Population der Erhebung muss mindestens in einer Sprache vorliegen.',
              'i18n-string-size': 'Die Maximallänge der Population der Erhebung ist 2048 Zeichen.'
            },
            'sample': {
              'not-null': 'Die Stichprobe der Erhebung darf nicht leer sein!',
              'i18n-string-not-empty': 'Die Stichprobe der Erhebung muss mindestens in einer Sprache vorliegen.',
              'i18n-string-size': 'Die Maximallänge der Stichprobe der Erhebung ist 2048 Zeichen.'
            },
            'survey-method': {
              'not-null': 'Die Erhebungsmethode darf nicht leer sein!',
              'i18n-string-not-empty': 'Die Erhebungsmethode muss mindestens in einer Sprache vorliegen.',
              'i18n-string-size': 'Die Maximallänge der Erhebungsmethode ist 128 Zeichen.'
            },
            'gross-sample-size': {
              'not-null': 'Die Gross-Sample-Size der Erhebung darf nicht leer sein!'
            },
            'sample-size': {
              'not-null': 'Die Sample-Size der Erhebung darf nicht leer sein!'
            },
            'unique-survey-number': 'Die Nummer einer Erhebung muss eindeutig innerhalb eines Datenaufbereitungsprojektes sein!',
            'number': {
              'not-null': 'Die Nummer der Erhebung darf nicht leer sein!'
            },
            'response-rate': {
              'not-null': 'Die Rücklaufquote der Erhebung darf nicht leer sein!'
            }
          },
          'survey-attachment-metadata':  {
            'survey-id': {
              'not-empty': 'Die ID der zugehörigen Erhebung darf nicht leer sein.'
            },
            'survey-number': {
              'not-null': 'Die Nummer der zugehörigen Erhebung darf nicht leer sein.'
            },
            'project-id': {
              'not-empty': 'Die ID des zugehörigen Datenaufbereitungsprojektes darf nicht leer sein.'
            },
            'description': {
              'not-null': 'Die Beschreibung des Attachments darf nicht leer sein.',
              'i18n-string-size': 'Die Beschreibung muss in beiden Sprachen angegeben werden und darf nicht länger als 128 Zeichen sein.',
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
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
