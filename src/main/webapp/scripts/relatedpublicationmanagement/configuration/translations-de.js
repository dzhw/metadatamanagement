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
            'duplicate-id': 'Die FDZ-ID ({{ id }}) der {{ index }}. Publikation wurde bereits verwendet.',
            'upload-terminated': 'Upload von {{ total }} Publikationen mit {{warnings}} Warnungen und {{ errors }} Fehlern beendet!',
            'unable-to-load-study-serieses': 'Die verfügbaren Studienreihen konnten nicht geladen werden!',
            'unable-to-delete': 'Die Publikationen konnten nicht gelöscht werden!',
            'cancelled': 'Upload von Publikationen abgebrochen'
          }
        },
        'detail': {
          'label': {
            'publication': 'Publikation',
            'publications': 'Publikationen',
            'doi': 'DOI',
            'sourceReference': 'Quellenangabe',
            'sourceLink': 'URL',
            'authors': 'Autor:innen',
            'year': 'Erscheinungsjahr',
            'source-reference': 'Referenz',
            'abstract-source': 'Quelle',
            'studySerieses': 'Studienreihen',
            'annotations': 'Anmerkungen'
          },
          'abstract': 'Abstract',
          'title': '{{ title }} ({{publicationId}})',
          'doi-tooltip': 'Klicken, um die DOI in einem neuen Tab zu öffnen',
          'sourceLink-tooltip': 'Klicken, um die Quelle dieser Publikation in einem neuen Tab zu öffnen ',
          'tooltips': {
            'surveys': {
              'one': 'Klicken, um die Erhebung anzuzeigen, zu der diese Publikation verfasst wurde',
              'many': 'Klicken, um alle Erhebungen anzuzeigen, zu denen diese Publikation verfasst wurde'
            },
            'data-sets': {
              'one': 'Klicken, um den Datensatz anzuzeigen, zu dem diese Publikation verfasst wurde',
              'many': 'Klicken, um alle Datensätze anzuzeigen, zu denen diese Publikation verfasst wurde'
            },
            'questions': {
              'one': 'Klicken, um die Frage anzuzeigen, zu der diese Publikation verfasst wurde',
              'many': 'Klicken, um alle Fragen anzuzeigen, zu denen diese Publikation verfasst wurde'
            },
            'instruments': {
              'one': 'Klicken, um das Instrument anzuzeigen, zu dem diese Publikation verfasst wurde',
              'many': 'Klicken, um alle Instrumente anzuzeigen, zu denen diese Publikation verfasst wurde'
            },
            'variables': {
              'one': 'Klicken, um die Variable anzuzeigen, zu der diese Publikation verfasst wurde',
              'many': 'Klicken, um alle Variablen anzuzeigen, zu denen diese Publikation verfasst wurde'
            },
            'data-packages':{
              'one': 'Klicken, um das Datenpaket anzuzeigen, zu der diese Publikation verfasst wurde',
              'many': 'Klicken, um alle Datenpakete anzuzeigen, zu denen diese Publikation verfasst wurde'
            },
            'studies-series' : 'Klicken, um alle Datenpakete dieser Studienreihe anzuzeigen'
          }
        },
        'assign': {
          'page-title': 'Publikationen zum Datenpaket des Projektes "{{projectId}}" erfassen',
          'header': 'Publikationenzuweisung',
          'all-publications-removed-toast': 'Alle Publikationen wurden von dem Datenpaket des Projektes "{{id}}" entfernt!',
          'empty-publications': 'Es wurden noch keine Publikationen zum Datenpaket erfasst.',
          'choose-unreleased-project-toast': 'Publikationen dürfen nur hinzugefügt bzw. entfernt werden, wenn das Projekt aktuell nicht freigegeben ist!',
          'search': {
            'placeholder': 'Publikationen suchen und dem Datenpaket zuordnen',
            'no-publications-found': 'Keine Publikation gefunden'
          },
          'button': {
            'remove-publication': 'Publikation von Datenpaket entfernen'
          }
        },
        'error': {
          'related-publication': {
            'one-foreign-key-is-used': 'Die Publikation hat keine Verknüpfung zu einem anderen Objekt.',
            'one-data-package-or-study-series-is-used': 'Die Publikation hat keine Verknüpfung zu einem Datenpaket oder einer Studienreihe',
            'valid-related-publication-id': 'Die Id einer Publikation muss dem Muster "pub-" + {IdAusCitavi} + "$".',
            'data-package-exists': 'Es gibt kein Datenpaket mit der FDZ-ID "{{invalidValue}}"!',
            'survey-exists': 'Es gibt keine Erhebung mit der FDZ-ID "{{invalidValue}}"!',
            'dataset-exists': 'Es gibt keinen Datensatz mit der FDZ-ID "{{invalidValue}}"!',
            'variable-exists': 'Es gibt keine Variable mit der FDZ-ID "{{invalidValue}}"!',
            'instrument-exists': 'Es gibt kein Instrument mit der FDZ-ID "{{invalidValue}}"!',
            'question-exists': 'Es gibt keine Frage mit der FDZ-ID "{{invalidValue}}"!',
            'study-series-exists': 'Es gibt kein Datenpaket mit der Studienreihe "{{invalidValue.de}}"!',
            'id': {
              'not-empty': 'Die FDZ-ID der Publikation darf nicht leer sein!',
              'size': 'Die Maximallänge der FDZ-ID ist 512 Zeichen.',
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
              'size': 'Die Maximallänge der DOI der Publikation ist 512 Zeichen.'
            },
            'source-link': {
              'pattern': 'Der Link der Quelle der Publikation ist keine gültige URL'
            },
            'title': {
              'not-empty': 'Der Titel der Publikation darf nicht leer sein!',
              'size': 'Die Maximallänge des Titels der Publikation ist 2048 Zeichen.'
            },
            'authors': {
              'size': 'Die Maximallänge des Feldes Autor:innen der Publikation ist 2048 Zeichen.',
              'not-empty': 'Es muss mindestens eine Autor:in angegeben werden!'
            },
            'year': {
              'not-null': 'Erscheinungsjahr darf nicht leer sein!',
              'valid': 'Erscheinungsjahr muss zwischen 1960 und {{currentDate | date :"yyyy"}} sein!.'
            },
            'abstract-source': {
              'i18n-string-size': 'Die Maximallänge der Quelle der Publication ist 2048 Zeichen.'
            },
            'language': {
              'not-null': 'Die Sprache der Publikation darf nicht leer sein!',
              'not-supported': 'Die Sprache muss eine gültige zweibuchstabige Abkürzung gemäß ISO 639-1 sein.'
            },
            'annotations': {
              'size': 'Die Maximallänge der Anmerkungen zur Publikation ist 2048 Zeichen.'
            }
          },
          'post-validation': {
            'variable-has-not-a-referenced-data-package': 'Die Variable "{{invalidValue}}" gehört zu einem Datenpaket, das nicht mit der Publikation verknüpft ist.',
            'survey-has-not-a-referenced-data-package': 'Die Erhebung "{{invalidValue}}" gehört zu einem Datenpaket, das nicht mit der Publikation verknüpft ist.',
            'data-set-has-not-a-referenced-data-package': 'Der Datensatz "{{invalidValue}}" gehört zu einem Datenpaket, das nicht mit der Publikation verknüpft ist.',
            'instrument-has-not-a-referenced-data-package': 'Das Instrument "{{invalidValue}}" gehört zu einem Datenpaket, das nicht mit der Publikation verknüpft ist.',
            'question-has-not-a-referenced-data-package': 'Die Frage "{{invalidValue}}" gehört zu einem Datenpaket, das nicht mit der Publikation verknüpft ist.'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
