'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var searchTranslations = {
      //jscs:disable
      'search-management': {
        'delete-messages': {
          'delete-variables-title': 'Alle Variablen ersetzen?',
          'delete-variables': 'Sind Sie sicher, dass Sie alle Variablen innerhalb des Datenaufbereitungsprojekts "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
          'delete-surveys-title': 'Alle Erhebungen ersetzen?',
          'delete-surveys': 'Sind Sie sicher, dass Sie alle Erhebungen innerhalb des Datenaufbereitungsprojekts "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
          'delete-questions-title': 'Alle Fragen ersetzen?',
          'delete-questions': 'Sind Sie sicher, dass Sie alle Fragen innerhalb des Datenaufbereitungsprojekts mit der FDZ-ID "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
          'delete-data-sets-title': 'Alle Datensätze ersetzen?',
          'delete-data-sets': 'Sind Sie sicher, dass Sie alle Datensätze innerhalb des Datenaufbereitungsprojekts mit der FDZ-ID "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
          'delete-studies-title': 'Studie ersetzen?',
          'delete-studies': 'Sind Sie sicher, dass Sie die Studie des Datenaufbereitungsprojekts mit der FDZ-ID "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
          'delete-related-publications-title': 'Alle Publikationen ersetzen?',
          'delete-related-publications': 'Sind Sie sicher, dass Sie die Publikationen mit den übergebenen Daten ersetzen möchten?',
          'delete-instruments-title': 'Alle Instrumente ersetzen?',
          'delete-instruments': 'Sind Sie sicher, dass Sie alle Instrumente innerhalb des Datenaufbereitungsprojekts mit der FDZ-ID "{{ id }}" mit den übergebenen Daten ersetzen möchten?'
        },
        'detail': {
          'search': 'Suche'
        },
        'buttons': {
          'refresh-tooltip': 'Klicken, um die Suche zu aktualisieren',
          'upload-variables-tooltip': 'Klicken, um Variablen für das ausgewählte Datenaufbereitungsprojekt hochzuladen',
          'upload-surveys-tooltip': 'Klicken, um Erhebungen für das ausgewählte Datenaufbereitungsprojekt hochzuladen',
          'upload-data-sets-tooltip': 'Klicken, um Datensätze für das ausgewählte Datenaufbereitungsprojekt hochzuladen',
          'upload-questions-tooltip': 'Klicken, um Fragen für das ausgewählte Datenaufbereitungsprojekt hochzuladen',
          'upload-studies-tooltip': 'Klicken, um Studie für das ausgewählte Datenaufbereitungsprojekt hochzuladen',
          'upload-related-publications-tooltip': 'Klicken, um Publikationen hochzuladen',
          'post-validate-related-publications-tooltip': 'Klicken, um Publikationen zu validieren',
          'upload-instruments-tooltip': 'Klicken, um Instrumente für das ausgewählte Datenaufbereitungsprojekt hochzuladen'
        },
        'input-label': {
          'all': 'Suchen Sie Studien, Variablen, Fragen, Erhebungen, Datensätze, Instrumente oder Publikationen...',
          'variables': 'Suchen Sie nach Variablen...',
          'questions': 'Suchen Sie nach Fragen...',
          'surveys': 'Suchen Sie nach Erhebungen...',
          'data-sets': 'Suchen Sie nach Datensätzen...',
          'studies': 'Suchen Sie nach Studien...',
          'related-publications': 'Suchen Sie nach Publikationen...',
          'instruments': 'Suchen Sie nach Instrumenten...'
        },
        'no-results-text': {
          'all': 'Keine Ergebnisse zu Ihrer Suchanfrage gefunden.',
          'variables': 'Keine Variablen zu Ihrer Suchanfrage gefunden.',
          'questions': 'Keine Fragen zu Ihrer Suchanfrage gefunden.',
          'surveys': 'Keine Erhebungen zu Ihrer Suchanfrage gefunden.',
          'data-sets': 'Keine Datensätze zu Ihrer Suchanfrage gefunden.',
          'studies': 'Keine Studien zu Ihrer Suchanfrage gefunden.',
          'related-publications': 'Keine Publikationen zu Ihrer Suchanfrage gefunden.',
          'instruments': 'Keine Instrumente zu Ihrer Suchanfrage gefunden.'
        },
        'tabs': {
          'variables': 'Variablen',
          'variables-tooltip': 'Klicken, um nach Variablen zu suchen',
          'questions': 'Fragen',
          'questions-tooltip': 'Klicken, um nach Fragen zu suchen',
          'surveys': 'Erhebungen',
          'surveys-tooltip': 'Klicken, um nach Erhebungen zu suchen',
          'data_sets': 'Datensätze',
          'data_sets-tooltip': 'Klicken, um nach Datensätzen zu suchen',
          'studies': 'Studien',
          'studies-tooltip': 'Klicken, um nach Studien zu suchen',
          'all': 'Alle',
          'all-tooltip': 'Klicken, um nach allen Objekten zu suchen',
          'related_publications': 'Publikationen',
          'related_publications-tooltip': 'Klicken, um nach Publikationen zu suchen',
          'instruments': 'Instrumente',
          'instruments-tooltip': 'Klicken, um nach Instrumenten zu suchen'
        },
        'cards': {
          'question-tooltip': 'Klicken, um die Frage "{{id}}" anzuzeigen',
          'variable-tooltip': 'Klicken, um die Variable "{{id}}" anzuzeigen',
          'data-set-tooltip': 'Klicken, um den Datensatz "{{id}}" anzuzeigen',
          'instrument-tooltip': 'Klicken, um das Instrument "{{id}}" anzuzeigen',
          'survey-tooltip': 'Klicken, um die Erhebung "{{id}}" anzuzeigen',
          'study-tooltip': 'Klicken, um die Studie "{{id}}" anzuzeigen',
          'publication-tooltip': 'Klicken, um die Publikation "{{id}}" anzuzeigen'
        },
        'filter': {
          'study': 'Studie',
          'data-set': 'Datensatz',
          'question': 'Frage',
          'related-publication': 'Publikation',
          'panel-identifier': 'Panelkennung',
          'derived-variables-identifier': 'Abgeleiteten Variablen',
          'access-way': 'Zugangsweg',
          'instrument': 'Instrument',
          'variable': 'Variable',
          'survey': 'Erhebung',
          'survey-series': 'Erhebungsreihe',
          'survey-series-de': 'Erhebungsreihe',
          'survey-series-en': 'Erhebungsreihe',
          'floating-label': {
            'survey': 'In welcher Erhebung wollen Sie suchen?',
            'instrument': 'In welchem Instrument wollen Sie suchen?',
            'study': 'In welcher Studie wollen Sie suchen?',
            'data-set': 'In welchem Datensatzes wollen Sie suchen?',
            'related-publication': 'In welcher Publikation sollen die Daten verwendet worden sein?',
            'panel-identifier': 'Welche Panelkennung sollen die Variablen haben?',
            'derived-variables-identifier': 'Welche abgeleiteten Variablen wollen Sie suchen?',
            'access-way': 'Welcher Zugangsweg soll zur Verfügung stehen?',
            'variable': 'Welche Variable soll vorkommen?',
            'question': 'Welche Frage soll vorkommen?',
            'survey-series': 'In welcher Erhebungsreihe wollen Sie suchen?'
          },
          'input-label': {
            'studies': 'Filter für die Studiensuche...',
            'surveys': 'Filter für die Erhebungssuche...',
            'instruments': 'Filter für die Instrumentensuche...',
            'questions': 'Filter für die Fragensuche...',
            'data_sets': 'Filter für die Datensatzsuche...',
            'variables': 'Filter für die Variablensuche...',
            'related_publications': 'Filter für die Publikationssuche...'
          },
          'uncollapse-filters-tooltip': {
            'true': 'Klicken, um die ausgewählten Filter einzublenden',
            'false': 'Klicken, um die ausgewählten Filter auszublenden'
          },
          'filter-search-input-label': 'Filtern nach...',
          'study-filter': {
            'not-found': 'Keine Studie gefunden!',
            'no-valid-selected': 'Keine gültige Studie ausgewählt!'
          },
          'survey-filter': {
            'not-found': 'Keine Erhebung gefunden!',
            'no-valid-selected': 'Keine gültige Erhebung ausgewählt!'
          },
          'instrument-filter': {
            'not-found': 'Kein Instrument gefunden!',
            'no-valid-selected': 'Kein gültiges Instrument ausgewählt!'
          },
          'question-filter': {
            'not-found': 'Keine Frage gefunden!',
            'no-valid-selected': 'Keine gültige Frage ausgewählt!'
          },
          'data-set-filter': {
            'not-found': 'Kein Datensatz gefunden!',
            'no-valid-selected': 'Kein gültiger Datensatz ausgewählt!'
          },
          'variable-filter': {
            'not-found': 'Keine Variable gefunden!',
            'no-valid-selected': 'Keine gültige Variable ausgewählt!'
          },
          'related-publication-filter': {
            'not-found': 'Keine Publikation gefunden!',
            'no-valid-selected': 'Keine gültige Publikation ausgewählt!'
          },
          'panel-identifier-filter': {
            'not-found': 'Keine Panelkennung gefunden!',
            'no-valid-selected': 'Keine gültige Panelkennung ausgewählt!'
          },
          'derived-variables-identifier-filter': {
            'not-found': 'Keine abgeleitete Variablenkennung gefunden!',
            'no-valid-selected': 'Keine gültige abgeleitete Variablenkennung ausgewählt!'
          },
          'access-way-filter': {
            'not-found': 'Kein Zugangsweg gefunden!',
            'no-valid-selected': 'Kein gültiger Zugangsweg ausgewählt!'
          },
          'survey-series-filter': {
            'not-found': 'Keine Erhebungsreihen gefunden!',
            'no-valid-selected': 'Keine gültige Erhebungsreihe ausgewählt!'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', searchTranslations);
  });
