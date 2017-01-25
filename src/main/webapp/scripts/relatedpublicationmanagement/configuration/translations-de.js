'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'related-publication-management': {
        'home': {
          'title': 'Publikation'
        },
        'log-messages': {
          'related-publication': {
            'saved': 'Die Publikation mit FDZ-ID {{ id }} erfolgreich gespeichert!',
            'not-saved': 'Die Publikation mit FDZ-ID {{ id }} wurde nicht gespeichert!',
            'missing-id': 'Der {{ index }}. Die Publikation enthält keine FDZ-ID und wurde nicht gespeichert!',
            'upload-terminated': 'Upload von {{ total }} Publikationen mit {{ errors }} Fehlern beendet!',
            'unable-to-delete': 'Die Publikationen konnten nicht gelöscht werden!',
            'cancelled': 'Upload von Publikationen abgebrochen'
          }
        },
        'detail': {
          'title': '{{ title }} ({{publicationId}})',
          'publication': 'Publikation',
          'publications': 'Publikationen',
          'related-information': 'Zugehörige Informationen',
          'studies-title': 'Studien',
          'questions-title': 'Fragen',
          'variables-title': 'Variablen',
          'citation-text': 'Zitationstext',
          'abstract': 'Abstract',
          'doi': 'DOI',
          'sourceReference': 'Quellenangabe',
          'sourceLink': 'URL',
          'no-related-publications': 'Keine Publikationen.',
          'related-publications': 'Publikationen'
        },
        'error': {
          'related-publication': {
            'one-foreign-key-is-used': 'Die Publikation hat keine Verknüpfung zu einem anderen Objekt.',
            'one-study-is-used': 'Die Publikation hat keine Verknüpfung zu einer Studie',
            'id': {
              'not-empty': 'Die FDZ-ID der Publikation darf nicht leer sein!',
              'size': 'Die Maximallänge der FDZ-ID ist 128 Zeichen.',
              'pattern': 'Die FDZ-ID darf keine Leerzeichen enthalten.'
            },
            'source-reference': {
              'not-empty': 'Die Source-Referenz der Publikation darf nicht leer sein!',
              'size': 'Die Maximallänge der Source-Referenz der Publikation ist 2048 Zeichen.'
            },
            'publication-abstract': {
              'size': 'Die Maximallänge des Abstrakts der Publikation ist 1048576 Zeichen.'
            },
            'doi': {
              'size': 'Die Maximallänge der DOI der Publikation ist 128 Zeichen.'
            },
            'source-link': {
              'pattern': 'Der Link der Quelle der Publikation ist keine gültige URL'
            },
            'title': {
              'not-empty': 'Der Titel der Publikation darf nicht leer sein!',
              'size': 'Die Maximallänge des Titels der Publikation ist 128 Zeichen.'
            },
            'authors': {
              'size': 'Die Maximallänge der Autoren der Publication ist 2048 Zeichen.',
              'not-empty': 'Die Autoren der Publikation darf nicht leer sein!'
            },
            'year': {
              'not-null': 'Veröffentlichungsjahr darf nicht leer sein!',
              'valid': 'Veröffentlichungsjahr muss zwischen 1960 und {{currentDate | date :"yyyy"}} sein!.'
            },
            'abstract-source': {
              'i18n-string-size': 'Die Maximallänge der #"TODO"# der Publication ist 2048 Zeichen.'
            }
          },
          'post-validation': {
            'study-unknown': 'Die Studie {{id}}, die bei der Publikation ({{toBereferenzedId}}) verlinkt ist, konnte nicht gefunden werden.',
            'variable-unknown': 'Die Variable {{id}}, die bei der Publikation ({{toBereferenzedId}}) verlinkt ist, konnte nicht gefunden werden.',
            'variable-has-not-a-referenced-study': 'Die Variable {{id}} referenziert auf eine Studie ({{additionalId}}), die nicht mit der Publikation ({{toBereferenzedId}}) verknüpft ist.',
            'survey-unknown': 'Die Erhebung {{id}}, die bei der Publikation ({{toBereferenzedId}}) verlinkt ist, konnte nicht gefunden werden.',
            'survey-has-not-a-referenced-study': 'Die Erhebung {{id}} referenziert auf eine Studie ({{additionalId}}), die nicht mit der Publikation ({{toBereferenzedId}}) verknüpft ist.',
            'data-set-unknown': 'Der Datensatz {{id}}, die bei der Publikation ({{toBereferenzedId}}) verlinkt ist, konnte nicht gefunden werden.',
            'data-set-has-not-a-referenced-study': 'Der Datensatz {{id}} referenziert auf eine Studie ({{additionalId}}), die nicht mit der Publikation ({{toBereferenzedId}}) verknüpft ist.',
            'instrument-unknown': 'Das Instrument {{id}}, die bei der Publikation ({{toBereferenzedId}}) verlinkt ist, konnte nicht gefunden werden.',
            'instrument-has-not-a-referenced-study': 'Das Instrument {{id}} referenziert auf eine Studie ({{additionalId}}), die nicht mit der Publikation ({{toBereferenzedId}}) verknüpft ist.',
            'question-unknown': 'Die Frage {{id}}, die bei der Publikation ({{toBereferenzedId}}) verlinkt ist, konnte nicht gefunden werden.',
            'question-has-not-a-referenced-study': 'Die Frage {{id}} referenziert auf eine Studie ({{additionalId}}), die nicht mit der Publikation ({{toBereferenzedId}}) verknüpft ist.'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
