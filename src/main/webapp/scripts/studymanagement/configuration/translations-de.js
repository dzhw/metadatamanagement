'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'study-management': {
        'detail': {
          'title': '{{ title }} ({{ studyId }})',
          'study': 'Studie',
          'studies': 'Studien',
          'study-informations': 'Details der Studie',
          'related-information': 'Zugehörige Informationen',
          'not-found': 'Die id {{id}} referenziert auf eine unbekannte Studie.',
          'not-found-references': 'Die id {{id}} hat keine Referenzen auf Studien.',
          'description': 'Studienbeschreibung',
          'survey-details': 'Erhebungsdetails',
          'data-set-details': 'Datensatzinformationen',
          'instrument-information': 'Instrumenteninformationen',
          'surveySeries': 'Erhebungsreihe',
          'institution': 'Erhebende Institution',
          'authors': 'Projektmitarbeiter(innen)',
          'sponsors': 'Gefördert von',
          'not-yet-released': 'Noch nicht freigegeben',
          'version': 'Version',
          'notes': 'Notizen',
          'surveyDesign': 'Erhebungsdesign',
          'citationHint': 'Zitation',
          'instruments': 'Instrumente',
          'no-related-studies': 'Keine zugehörige Studien.',
          'related-studies': 'Zugehörige Studien',
          'available-data-products': 'Verfügbare Datenprodukte',
          'basic-data-of-surveys': 'Eckdaten der Erhebungen',
          'downloads': 'Materialien zum Download',
          'not-released-toast': 'Die Studie "{{ id }}" wurde noch nicht für alle Benutzer freigegeben!',
          'tooltips': {
            'surveys': {
              'one': 'Klicken, um die Erhebung dieser Studie anzuzeigen',
              'many': 'Klicken, um alle Erhebungen dieser Studie anzuzeigen'
            },
            'data-sets': {
              'one': 'Klicken, um den Datensatz dieser Studie anzuzeigen',
              'many': 'Klicken, um alle Datensätze dieser Studie anzuzeigen',
            },
            'publications': {
              'one': 'Klicken, um die Publikation zu dieser Studie anzuzeigen',
              'many': 'Klicken, um alle Publikationen zu dieser Studie anzuzeigen'
            }
          },
          'data-set': {
            'card-title': 'Verfügbare Datensätze',
            'accessWays': 'Zugangswege',
            'description': 'Beschreibung',
            'description-tooltip': 'Klicken, um den Datensatz "{{id}}" anzuzeigen',
            'maxNumberOfObservations': 'Fälle'
          }
        },
        'log-messages': {
          'study': {
            'saved': 'Studie mit FDZ-ID {{ id }} erfolgreich gespeichert!',
            'not-saved': 'Studie mit FDZ-ID {{ id }} wurde nicht gespeichert:',
            'study-file-not-found': 'In dem ausgewählten Verzeichnis fehlt die folgende Datei: study.xlsx!',
            'releases-file-not-found': 'In dem ausgewählten Verzeichnis fehlt die folgende Datei: releases.xlsx!',
            'unable-to-delete': 'Die Studie konnte nicht gelöscht werden!',
            'missing-id': 'Die {{ index }}. Studie enthält keine FDZ-ID und wurde nicht gespeichert!',
            'upload-terminated': 'Upload von {{ total }} Studie mit {{warnings}} Warnungen und {{ errors }} Fehlern beendet!',
            'cancelled': 'Upload der Studie Abgebrochen!'
          }
        },
        'error': {
          'study': {
            'id': {
              'not-empty': 'Die FDZ-ID der Studie darf nicht leer sein!',
              'size': 'Die Maximallänge der FDZ-ID ist 128 Zeichen.',
              'pattern': 'Es dürfen für die FDZ-ID nur alphanumerische Zeichen, deutsche Umlaute, ß, Minus, Ausrufezeichen und der Unterstrich verwendet werden.',
              'not-valid-id': 'Die FDZ-ID der Studie muss der Form "stu-" + {ProjektID} + "!" entsprechen.'
            },
            'title': {
              'not-null': 'Der Titel einer Studie darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge des Titels einer Studie ist 2048 Zeichen.',
              'i18n-string-not-empty': 'Der Titel einer Studie muss in mindestens einer Sprache vorhanden sein.'
            },
            'description': {
              'not-null': 'Die Beschreibung einer Studie darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge der Beschreibung einer Studie ist 2048 Zeichen.',
              'i18n-string-not-empty': 'Die Beschreibung einer Studie muss in mindestens einer Sprache vorhanden sein.'
            },
            'institution': {
              'not-null': 'Die Institution einer Studie darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge der Institution einer Studie ist 128 Zeichen.',
              'i18n-string-not-empty': 'Die Institution einer Studie muss in mindestens einer Sprache vorhanden sein.'
            },
            'sponsor': {
              'i18n-string-size': 'Die Maximallänge des Sponsors einer Studie ist 128 Zeichen.'
            },
            'citation-hint': {
              'i18n-string-size': 'Die Maximallänge des Zitationshinweises einer Studie ist 2048 Zeichen.'
            },
            'survey-series': {
              'i18n-string-size': 'Die Maximallänge der Erhebungsreihe einer Studie ist 128 Zeichen.'
            },
            'data-availability': {
              'not-null': 'Die Datenerreichbarkeit einer Studie darf nicht leer sein!',
              'valid-data-availability': 'Die erlaubten Werte für Datenverfügbarkeit der Studie sind: Verfügbar, In Aufbereitung, Nicht verfügbar.'
            },
            'survey-design': {
              'not-null': 'Das Erhebungsdesign einer Studie darf nicht leer sein!',
              'valid-survey-design': 'Die erlaubten Werte für das Erhebungsdesign der Studie sind: Querschnitt, Panel.'
            },
            'authors': {
              'not-empty': 'Die Liste der Autoren einer Studie benötigt mindestens ein Element und darf nicht leer sein!',
            },
            'data-acquisition-project': {
              'id': {
                'not-empty': 'Die FDZ - ID des Projektes darf bei der Studie nicht leer sein!'
              }
            }
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
