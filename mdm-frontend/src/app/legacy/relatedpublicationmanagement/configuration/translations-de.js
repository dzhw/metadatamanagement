'use strict';

angular.module('metadatamanagementApp').config([
  '$translateProvider',

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
            'annotations': 'Anmerkungen',
            'title': 'Titel',
            'issue': 'Ausgabe',
            'journal': 'Fachzeitschrift'
          },
          'abstract': 'Abstract',
          'title': '{{ title }}',
          'description': '{{ sourceReference }}',
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
        'error': {
          'related-publication': {
            'valid-related-publication-id': 'Die Id einer Publikation muss dem Muster "pub-" + {IdAusCitavi} + "$".',
            'dataPackage-exists': 'Es gibt kein Datenpaket mit der FDZ-ID "{{invalidValue}}"!',
            'analysisPackage-exists': 'Es gibt kein Analysepaket mit der FDZ-ID "{{invalidValue}}"!',
            'at-least-one-referenced-id': 'Die Publikation muss mindestens eine Verknüpfung zu einem Datenpaket oder einem Analysepaket haben.',
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
              'not-null': 'Das Erscheinungsjahr darf nicht leer sein!',
              'valid': 'Das Erscheinungsjahr muss nach 1960 sein.'
            },
            'abstract-source': {
              'size': 'Die Maximallänge der Quelle der Publication ist 2048 Zeichen.'
            },
            'language': {
              'not-null': 'Die Sprache der Publikation darf nicht leer sein!',
              'not-supported': 'Die Sprache muss eine gültige zweibuchstabige Abkürzung gemäß ISO 639-1 sein.'
            },
            'annotations': {
              'size': 'Die Maximallänge der Anmerkungen zur Publikation ist 2048 Zeichen.'
            }
          }
        },
        'report-publications': {
          'speech-bubble': {
            'close': 'Schließen',
            'text': 'Kennen Sie Publikationen, die auf Basis unserer Datenpakete entstanden sind? Dann teilen Sie uns diese bitte mit...',
            'analyse-package': {
              'text': 'Kennen Sie Publikationen, die auf Basis unserer Analysepakete entstanden sind? Dann teilen Sie uns diese bitte mit...'
            }
          },
          'button': {
            'text': 'Publikationen melden',
            'tooltip': 'Klicken, um Publikationen zu unseren Datenpaketen zu melden',
            'link': 'mailto:userservice@dzhw.eu?subject=Meldung%20von%20Publikationen%20zu%20Datenpaketen%20vom%20FDZ-DZHW&body=Liebes%20FDZ-DZHW%2C%0D%0A%0D%0Aich%20m%C3%B6chte%20folgende%20Publikation(en)%2C%20welche%20zu%20Datenpaket%20{{ dataPackageId ? "%22" + dataPackageId + "%22" : "X"}}%20geh%C3%B6ren%2C%20melden%3A%0D%0A',
            'analysis-package': {
              'tooltip': 'Klicken, um Publikationen zu unseren Analysepaketen zu melden',
              'link': 'mailto:userservice@dzhw.eu?subject=Meldung%20von%20Publikationen%20zu%20Analysepaketen%20vom%20FDZ-DZHW&body=Liebes%20FDZ-DZHW%2C%0D%0A%0D%0Aich%20m%C3%B6chte%20folgende%20Publikation(en)%2C%20welche%20zu%20Analysepaket%20{{ analysisPackageId ? "%22" + analysisPackageId + "%22" : "X"}}%20geh%C3%B6ren%2C%20melden%3A%0D%0A'
            }
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  }]);
